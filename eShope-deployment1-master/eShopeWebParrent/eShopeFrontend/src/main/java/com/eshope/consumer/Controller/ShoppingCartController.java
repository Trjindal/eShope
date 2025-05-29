package com.eshope.consumer.Controller;

import com.eShope.common.entity.Address;
import com.eShope.common.entity.CartItem;
import com.eShope.common.entity.Customer;
import com.eShope.common.entity.ShippingRate;
import com.eshope.consumer.Service.AddressService;
import com.eshope.consumer.Service.CustomerService;
import com.eshope.consumer.Service.ShippingRateService;
import com.eshope.consumer.Service.ShoppingCartService;
import com.eshope.consumer.Utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private AddressService addressService;

    @Autowired
    private ShippingRateService shippingRateService;


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

        Address defaultAddress= addressService.getDefaultAddress(customer);
        ShippingRate shippingRate=null;
        boolean usePrimaryAddressAsDefault=false;

        if(defaultAddress!=null){
            shippingRate=shippingRateService.getShippingRateForAddress(defaultAddress);
        }else {
            usePrimaryAddressAsDefault=true;
            shippingRate=shippingRateService.getShippingRateForCustomer(customer);
        }

        model.addAttribute("usePrimaryAddressAsDefault",usePrimaryAddressAsDefault);
        model.addAttribute("shippingSupported",shippingRate!=null);
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
