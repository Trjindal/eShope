package com.eshope.consumer.Controller;

import com.eShope.common.entity.Address;
import com.eShope.common.entity.CartItem;
import com.eShope.common.entity.Customer;
import com.eShope.common.entity.Order.Order;
import com.eShope.common.entity.Order.PaymentMethod;
import com.eShope.common.entity.ShippingRate;
import com.eshope.consumer.Exception.PayPalAPiException;
import com.eshope.consumer.PoJo.CheckoutInfo;
import com.eshope.consumer.Service.*;
import com.eshope.consumer.Service.*;
import com.eshope.consumer.SettingBag.CurrencySettingBag;
import com.eshope.consumer.SettingBag.EmailSettingBag;
import com.eshope.consumer.SettingBag.PaymentSettingBag;
import com.eshope.consumer.Utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

    @Autowired
    private SettingService settingService;

    @Autowired
    private PayPalService payPalService;

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

        String currencyCode= settingService.getCurrencyCode();
        PaymentSettingBag paymentSettings=settingService.getPaymentSettings();
        String paypalClientId=paymentSettings.getClientId();
        System.out.println("mu"+paypalClientId);
        model.addAttribute("paypalClientId",paypalClientId);
        model.addAttribute("currencyCode",currencyCode);
        model.addAttribute("customer",customer);
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

        Order createOrder=orderService.createOrder(customer,defaultAddress,cartItems,paymentMethod,checkoutInfo);
        shoppingCartService.deleteByCustomer(customer);
        try {
            sendOrderConfirmationEmail(request,createOrder);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return "Checkout/order_completed";
    }


    @PostMapping("process_paypal_order")
    public String processPayPalOrder(HttpServletRequest request,Model model){
        String orderId=request.getParameter("orderId");

        String pageTitle="Checkout Failure";
        String message=null;
        try {
            if(payPalService.validateOrder(orderId)){
                return placeOrder(request);
            }else{
                pageTitle="Checkout Failure";
                message="ERROR:Transaction could not be completed because order information is invalid";
            }
        } catch (PayPalAPiException e) {
            message="ERROR: Transaction failed due to error: "+e.getMessage();
        }

        model.addAttribute("pageTitle",pageTitle);
        model.addAttribute("message",message);
        return "message";
    }

    private void sendOrderConfirmationEmail(HttpServletRequest request, Order order) throws MessagingException, UnsupportedEncodingException {

        EmailSettingBag emailSettings=settingService.getEmailSettings();
        JavaMailSenderImpl mailSender=Utility.prepareMailSender(emailSettings);

        mailSender.setDefaultEncoding("utf-8");

        String toAddress=order.getCustomer().getEmail();
        String subject=emailSettings.getOrderConfirmationSubject();
        String content=emailSettings.getOrderConfirmationContent();

        subject=subject.replace("[[orderId]]",String.valueOf(order.getId()));
        subject=subject.replace("[[name]]",String.valueOf(order.getCustomer().getFullName()));
        MimeMessage message=mailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message);

        helper.setFrom(emailSettings.getFromAddress(),emailSettings.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);

        DateFormat dateFormatter=new SimpleDateFormat("HH:mm:ss E, dd MMM yyyy");
        String orderTime=dateFormatter.format(order.getOrderTime());

        CurrencySettingBag currencySettings=settingService.getCurrencySettings();
        String totalAmount=Utility.formatCurrency(order.getTotal(),currencySettings);

        content=content.replace("[[name]]",order.getFullName());
        content=content.replace("[[orderId]]",String.valueOf(order.getId()));
        content=content.replace("[[orderTime]]",orderTime);
        content=content.replace(" [[shippingAddress]]",order.getShippingAddress());
        content=content.replace("[[total]]",totalAmount);
        content=content.replace("[[paymentMethod]]",order.getPaymentMethod().toString());

        helper.setText(content,true);
        mailSender.send(message);

    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request){

        String email= Utility.getEmailOfAuthenticatedCustomer(request);
        return customerService.getCustomerByEmail(email);
    }



}
