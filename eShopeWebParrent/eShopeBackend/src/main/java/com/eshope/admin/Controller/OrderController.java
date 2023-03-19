package com.eshope.admin.Controller;

import com.eShope.common.entity.Country;
import com.eShope.common.entity.Order.Order;
import com.eShope.common.entity.Order.OrderDetail;
import com.eShope.common.entity.Order.OrderStatus;
import com.eShope.common.entity.Order.OrderTrack;
import com.eShope.common.entity.Product.Product;
import com.eShope.common.entity.Setting.Setting;
import com.eshope.admin.Service.OrderService;
import com.eshope.admin.Service.SettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Controller
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private SettingService settingService;

    @GetMapping("/orders")
    public String listAllOrders(Model model,HttpServletRequest request){
        return listByPage(1,model,"orderTime","desc",null,request);
    }

    @GetMapping("/orders/page/{pageNum}")
    public String listByPage(@PathVariable(name="pageNum") int pageNum, Model model, @Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword,HttpServletRequest request){
        Page<Order> page=orderService.listByPage(pageNum,sortField,sortDir,keyword);
        List<Order> listOrders=page.getContent();

        long startCount =(pageNum-1)* orderService.ORDERS_PER_PAGE+1;
        long endCount=startCount+orderService.ORDERS_PER_PAGE-1;
        if(endCount>page.getTotalElements()){
            endCount=page.getTotalElements();
        }

        model.addAttribute("currentPage",pageNum);
        model.addAttribute("startCount",startCount);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("endCount",endCount);
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("listAllOrders",listOrders);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("keyword", keyword );
       loadCurrencySetting(request);
        return "Orders/orders";
    }


    @GetMapping("/orders/detail/{id}")
    public String detailOrder(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model,HttpServletRequest request){
        try{
            Order order=orderService.getOrderById(id);
            loadCurrencySetting(request);
            model.addAttribute("order",order);
            return "Orders/viewOrderModal.html";
        }catch (UsernameNotFoundException ex){
            redirectAttributes.addFlashAttribute("message",ex.getMessage());
            return "redirect:/orders";
        }

    }




    @GetMapping("/orders/edit/{id}")
    public String editOrder(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model,HttpServletRequest request){
        try{
            Order order=orderService.getOrderById(id);
            List<Country> listCountries=orderService.listAllCountries();
            loadCurrencySetting(request);

            model.addAttribute("listCountries",listCountries);
            model.addAttribute("order",order);
            String chk="";
            for(OrderTrack tracks:order.getOrderTracks()){
                chk=tracks.getUpdatedTimeOnForm();
                break;
            }
            model.addAttribute("chk",chk);

            return "Orders/editOrders.html";
        }catch (UsernameNotFoundException ex){
            redirectAttributes.addFlashAttribute("message",ex.getMessage());
            return "redirect:/orders";
        }

    }

    @PostMapping("/orders/save")
    public String saveOrder(@Valid @ModelAttribute(value = "order") Order order, Errors errors,RedirectAttributes redirectAttributes, Model model,HttpServletRequest request){
        log.error(String.valueOf(order.getId()));
        //DISPLAYING ERROR MESSAGES
//        if(errors.hasErrors()){
//            log.error("Order form validation failed due to : " + errors.toString());
//            List<Country> listCountries=orderService.listAllCountries();
//            loadCurrencySetting(request);
//
//            model.addAttribute("listCountries",listCountries);
//            model.addAttribute("order",order);
//            return "Orders/editOrders.html";
//        }
        updateDeliverDate(order,request);
        updateProductDetails(order,request);
        updateOrderTracks(order,request);
        orderService.save(order);
        redirectAttributes.addFlashAttribute("message",order.getId()+" has been updated successfully.");
        return "redirect:/orders ";
    }

    private void updateDeliverDate(Order order, HttpServletRequest request) {
        DateFormat dateFormatter=new SimpleDateFormat("yyyy-MM-dd");
        String[] expectedDeliveryDate=request.getParameterValues("deliverDate");
        System.out.println(expectedDeliveryDate[0]);
        try {
            order.setDeliverDate(dateFormatter.parse(expectedDeliveryDate[0]));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateOrderTracks(Order order, HttpServletRequest request) {

        String[] trackIds= request.getParameterValues("trackId");
        String[] trackStatuses= request.getParameterValues("trackStatus");
        String[] trackDates=request.getParameterValues("trackDate");
        String[] trackNotes= request.getParameterValues("trackNotes");

        List<OrderTrack> orderTracks=order.getOrderTracks();
        DateFormat dateFormatter=new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");

        for(int i=0;i<trackIds.length;i++){
            OrderTrack trackRecord=new OrderTrack();
            Integer trackId=Integer.parseInt(trackIds[i]);

            if(trackId>0){
                trackRecord.setId(trackId);
            }
            trackRecord.setOrder(order);
            trackRecord.setStatus(OrderStatus.valueOf(trackStatuses[i]));
            trackRecord.setNotes(trackNotes[i]);
            try {
                String date=trackDates[i].replace(" ","T");
                trackRecord.setUpdatedTime(dateFormatter.parse(date));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            orderTracks.add(trackRecord);
        }
    }

    private void updateProductDetails(Order order, HttpServletRequest request) {
        String[] detailIds=request.getParameterValues("detailId");
        String[] productIds=request.getParameterValues("productId");
        String[] productCosts=request.getParameterValues("productCosts");
        String[] quantities=request.getParameterValues("quantity");
        String[] productPrices=request.getParameterValues("productPrice");
        String[] productShippingCosts=request.getParameterValues("productShippingCost");
        String[] productSubTotals=request.getParameterValues("productSubTotal");

        Set<OrderDetail> orderDetails=order.getOrderDetails();
        for(int i=0;i<detailIds.length;i++){

            OrderDetail orderDetail=new OrderDetail();
            Integer detailId=Integer.parseInt(detailIds[i]);
            if(detailId>0){
                orderDetail.setId(detailId);
            }
            orderDetail.setOrder(order);
            orderDetail.setProduct(new Product(Integer.parseInt(productIds[i])));
            orderDetail.setProductCost(Float.parseFloat(productCosts[i]));
            orderDetail.setSubTotal(Float.parseFloat(productSubTotals[i]));
            orderDetail.setShippingCost(Float.parseFloat(productShippingCosts[i]));
            orderDetail.setQuantity(Integer.parseInt(quantities[i]));
            orderDetail.setUnitPrice(Float.parseFloat(productPrices[i]));

            orderDetails.add(orderDetail);
        }
    }


//    @PostMapping("/users/editUser")
//    public String saveEditUser(RedirectAttributes redirectAttributes, @Valid @ModelAttribute(value = "user") User user, Errors errors, Model model , @RequestParam("image")MultipartFile multipartFile) throws IOException{
//        log.error(String.valueOf(user.getId()));
//        Integer id= user.getId();
//        User existingUser=userService.getUserById(id);
//        String savedPassword=existingUser.getPassword();
//
//
//        //DISPLAYING ERROR MESSAGES
//        if(errors.hasErrors()){
//            log.error("Contact form validation failed due to : " + errors.toString());
//            List<Role> listAllRoles=userService.listAllRoles();
//            model.addAttribute("listAllRoles",listAllRoles);
//            model.addAttribute("users",existingUser);
//            return "Users/userUpdateForm.html";
//        }
//
//        userService.editUser(existingUser);
//
//        redirectAttributes.addFlashAttribute("message","The user has been edited successfully");
//        return "redirect:/users";
//
//    }

    //DELETE CONTROLLER
    @GetMapping("/orders/delete/{id}")
    public String deleteOrder(@PathVariable(name="id")Integer id,Model model,RedirectAttributes redirectAttributes){
        try{
            orderService.delete(id);
            redirectAttributes.addFlashAttribute("message","The order ID "+id+"has been deleted successfully");
        }catch (UsernameNotFoundException ex){
            redirectAttributes.addFlashAttribute("error",ex.getMessage());
        }
        return "redirect:/orders";
    }


    private void loadCurrencySetting(HttpServletRequest request){
        List<Setting> currencySettings=settingService.getCurrencySettings();

        for(Setting setting:currencySettings){
            request.setAttribute(setting.getKey(), setting.getValue());
        }
    }



}
