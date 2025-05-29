package com.eshope.consumer.RestController;

import com.eShope.common.entity.Customer;
import com.eshope.consumer.Service.CustomerService;
import com.eshope.consumer.Service.ShoppingCartService;
import com.eshope.consumer.Utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ShoppingCartRestController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private CustomerService customerService;


    @PostMapping("/cart/add/{productId}/{quantity}")
    public String addProductToCart(@PathVariable("productId") Integer productId,@PathVariable("quantity") Integer quantity,HttpServletRequest request){

        try {
            Customer customer = getAuthenticatedCustomer(request);
            Integer updatedQuantity=shoppingCartService.addProduct(productId,quantity,customer);
            return updatedQuantity+" item(s) of this product were added to your shopping cart";
        }catch (UsernameNotFoundException e){
            return e.getMessage();
        }

    }

    @PostMapping("/cart/update/{productId}/{quantity}")
    public String updateProductToCart(@PathVariable("productId") Integer productId,@PathVariable("quantity") Integer quantity,HttpServletRequest request){

        try {
            Customer customer = getAuthenticatedCustomer(request);
            float subTotal=shoppingCartService.updateQuantity(productId,quantity,customer);
            return String.valueOf(subTotal);
        }catch (UsernameNotFoundException e){
            return "You must login to change quantity of product.";
        }

    }


    @DeleteMapping("/cart/delete/{productId}")
    public String removeProduct(@PathVariable("productId") Integer productId,HttpServletRequest request){
        try {
            Customer customer=getAuthenticatedCustomer(request);
            shoppingCartService.removeProduct(productId,customer);
            return "The product has been removed from your shopping cart.";
        }catch (UsernameNotFoundException e){
            return "You must login to remove product.";
        }
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request){

        String email= Utility.getEmailOfAuthenticatedCustomer(request);
        if(email==null) {
            throw new UsernameNotFoundException("You must login to add this product to cart.");
        }
        return customerService.getCustomerByEmail(email);
    }

}
