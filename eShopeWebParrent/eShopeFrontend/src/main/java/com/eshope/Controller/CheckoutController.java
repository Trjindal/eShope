package com.eshope.Controller;

import com.eShope.common.entity.Address;
import com.eShope.common.entity.CartItem;
import com.eShope.common.entity.Customer;
import com.eShope.common.entity.Order.PaymentMethod;
import com.eShope.common.entity.ShippingRate;
import com.eshope.PoJo.CheckoutInfo;
import com.eshope.Service.*;
import com.eshope.Utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CheckoutController {

    @Autowired
    private CheckoutService checkoutService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private ShippingRateService shippingRateService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/checkout")
    private String showCheckoutPage(Model model, HttpServletRequest request){

        Customer customer=getAuthenticatedCustomer(request);

        Address defaultAddress= addressService.getDefaultAddress(customer);
        ShippingRate shippingRate=null;

        if(defaultAddress!=null){
            model.addAttribute("shippingAddress",defaultAddress.toString());
            shippingRate=shippingRateService.getShippingRateForAddress(defaultAddress);
        }else {
            model.addAttribute("shippingAddress",customer.toString());
            shippingRate=shippingRateService.getShippingRateForCustomer(customer);
        }

        if(shippingRate==null){
            return "redirect:/cart";
        }
        List<CartItem> cartItems =shoppingCartService.listCartItems(customer);
        CheckoutInfo checkoutInfo=checkoutService.prepareCheckout(cartItems,shippingRate);

        model.addAttribute("checkoutInfo",checkoutInfo);
        model.addAttribute("cartItems",cartItems);

        return "Checkout/checkout";
    }

    @PostMapping("/place_order")
    public String placeOrder(HttpServletRequest request){

        String paymentType=request.getParameter("paymentMethod");
        PaymentMethod paymentMethod=PaymentMethod.valueOf(paymentType);


        Customer customer=getAuthenticatedCustomer(request);

        Address defaultAddress= addressService.getDefaultAddress(customer);
        ShippingRate shippingRate=null;

        if(defaultAddress!=null){
            shippingRate=shippingRateService.getShippingRateForAddress(defaultAddress);
        }else {
            shippingRate=shippingRateService.getShippingRateForCustomer(customer);
        }


        List<CartItem> cartItems =shoppingCartService.listCartItems(customer);
        CheckoutInfo checkoutInfo=checkoutService.prepareCheckout(cartItems,shippingRate);

        orderService.createOrder(customer,defaultAddress,cartItems,paymentMethod,checkoutInfo);
        shoppingCartService.deleteByCustomer(customer);

        return "Checkout/order_completed";
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request){

        String email= Utility.getEmailOfAuthenticatedCustomer(request);
        return customerService.getCustomerByEmail(email);
    }

}
