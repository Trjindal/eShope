package com.eshope.consumer.RestController;


import com.eShope.common.entity.Customer;
import com.eshope.consumer.PoJo.OrderReturnRequest;
import com.eshope.consumer.PoJo.OrderReturnResponse;
import com.eshope.consumer.Service.CustomerService;
import com.eshope.consumer.Service.OrderService;
import com.eshope.consumer.Utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class OrderRestController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerService customerService;

    @PostMapping("/orders/return")
    public ResponseEntity<?> handleOrderReturnRequest(@RequestBody OrderReturnRequest orderReturnRequest,HttpServletRequest servletRequest){

        System.out.println("Id:"+orderReturnRequest.getOrderId());
        System.out.println(orderReturnRequest.getReason());
        System.out.println(orderReturnRequest.getNote());

        Customer customer=null;
        try {
            customer=getAuthenticatedCustomer(servletRequest);
        }catch (UsernameNotFoundException ex){
            return new ResponseEntity<>("Authentication Required", HttpStatus.BAD_REQUEST);
        }
        try{
            orderService.setOrderReturnRequested(orderReturnRequest, customer);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new OrderReturnResponse(orderReturnRequest.getOrderId()),HttpStatus.OK);
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request){

        String email= Utility.getEmailOfAuthenticatedCustomer(request);
        if(email==null) {
            throw new UsernameNotFoundException("You must login to add this product to cart.");
        }
        return customerService.getCustomerByEmail(email);
    }
}
