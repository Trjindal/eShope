package com.eshope.consumer.Controller;

import com.eShope.common.entity.Customer;
import com.eShope.common.entity.Order.Order;
import com.eShope.common.entity.Order.OrderDetail;
import com.eShope.common.entity.Product.Product;
import com.eshope.consumer.Service.CustomerService;
import com.eshope.consumer.Service.OrderService;
import com.eshope.consumer.Service.ReviewService;
import com.eshope.consumer.Utility.Utility;
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
import java.util.Iterator;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private CustomerService customerService;

    @GetMapping("/orders")
    public String listAllOrders(Model model,HttpServletRequest request) {
        return listByPage(1, model,request, "orderTime", "desc", null);
    }

    @GetMapping("/orders/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,HttpServletRequest request, @Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("orderKeyword") String orderKeyword) {
        Customer customer=getAuthenticatedCustomer(request);
        Page<Order> page = orderService.listByPage(customer,pageNum, sortField, sortDir, orderKeyword);
        List<Order> listOrders = page.getContent();

        long startCount = (pageNum - 1) * orderService.ORDERS_PER_PAGE + 1;
        long endCount = startCount + orderService.ORDERS_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

//        Order order=new Order();


        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startCount", startCount);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listAllOrders", listOrders);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("orderKeyword", orderKeyword);

        return "Order/order";
    }

    @GetMapping("/orders/detail/{id}")
    public String detailOrder(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model, HttpServletRequest request){
        try{
            Customer customer=getAuthenticatedCustomer(request);
            Order order=orderService.getOrderByIdAndCustomer(id,customer);
            if(order==null){
                throw new UsernameNotFoundException("Could not find any order with Id "+ id);
            }
            setProductReviewableStatus(customer,order);
//            loadCurrencySetting(request);
            model.addAttribute("order",order);
            return "Order/viewOrderModal.html";
        }catch (UsernameNotFoundException ex){
            redirectAttributes.addFlashAttribute("message",ex.getMessage());
            return "redirect:/orders";
        }

    }

    private void setProductReviewableStatus(Customer customer, Order order) {
       Iterator<OrderDetail> iterator = order.getOrderDetails().iterator();

       while (iterator.hasNext()){
           OrderDetail orderDetail= iterator.next();
           Product product=orderDetail.getProduct();
           Integer productId= product.getId();

           boolean didCustomerReviewProduct=reviewService.didCustomerReviewProduct(customer,productId);
           product.setReviewedByCustomer(didCustomerReviewProduct);

           if(!didCustomerReviewProduct){
               boolean canCustomerReviewProduct=reviewService.canCustomerReviewProduct(customer,productId);
            product.setCustomerCanReview(canCustomerReviewProduct);
           }
       }

    }


    private Customer getAuthenticatedCustomer(HttpServletRequest request){

        String email= Utility.getEmailOfAuthenticatedCustomer(request);
        return customerService.getCustomerByEmail(email);
    }
}
