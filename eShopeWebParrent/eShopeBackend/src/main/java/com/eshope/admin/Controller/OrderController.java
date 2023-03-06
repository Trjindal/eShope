package com.eshope.admin.Controller;

import com.eShope.common.entity.Country;
import com.eShope.common.entity.Order.Order;
import com.eShope.common.entity.Setting.Setting;
import com.eshope.admin.Service.OrderService;
import com.eshope.admin.Service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
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

            return "Orders/editOrders.html";
        }catch (UsernameNotFoundException ex){
            redirectAttributes.addFlashAttribute("message",ex.getMessage());
            return "redirect:/orders";
        }

    }

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
