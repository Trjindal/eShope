package com.eshope.Controller;

import com.eShope.common.entity.CartItem;
import com.eShope.common.entity.Customer;
import com.eshope.Service.CustomerService;
import com.eshope.Service.ShoppingCartService;
import com.eshope.Utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private CustomerService customerService;


    @GetMapping("/cart")
    public String viewCart(Model model, HttpServletRequest request){

        Customer customer=getAuthenticatedCustomer(request);
        List<CartItem> cartItems =shoppingCartService.listCartItems(customer);

        float estimatedTotal=0.0F;
        int totalItems=0;

        for (CartItem item: cartItems){
            totalItems+=item.getQuantity();
            estimatedTotal += item.getSubTotal();
        }

        model.addAttribute("cartItems",cartItems);
        model.addAttribute("estimatedTotal",estimatedTotal);
        model.addAttribute("totalItems",totalItems);
        return "Cart/shoppingCart";
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request){

        String email= Utility.getEmailOfAuthenticatedCustomer(request);
        return customerService.getCustomerByEmail(email);
    }


}
