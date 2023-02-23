package com.eshope.Controller;

import com.eShope.common.entity.Address;
import com.eShope.common.entity.CartItem;
import com.eShope.common.entity.Customer;
import com.eShope.common.entity.Order.Order;
import com.eShope.common.entity.Order.PaymentMethod;
import com.eShope.common.entity.ShippingRate;
import com.eshope.PoJo.CheckoutInfo;
import com.eshope.Service.*;
import com.eshope.SettingBag.CurrencySettingBag;
import com.eshope.SettingBag.EmailSettingBag;
import com.eshope.Utility.Utility;
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

    private void sendOrderConfirmationEmail(HttpServletRequest request, Order order) throws MessagingException, UnsupportedEncodingException {

        EmailSettingBag emailSettings=settingService.getEmailSettings();
        JavaMailSenderImpl mailSender=Utility.prepareMailSender(emailSettings);

        mailSender.setDefaultEncoding("utf-8");

        String toAddress=order.getCustomer().getEmail();
        String subject=emailSettings.getOrderConfirmationSubject();
        String content=emailSettings.getOrderConfirmationContent();

        subject=subject.replace("[[orderId]]",String.valueOf(order.getId()));

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
