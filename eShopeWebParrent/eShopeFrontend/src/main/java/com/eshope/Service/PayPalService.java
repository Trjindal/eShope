package com.eshope.Service;

import com.eshope.Exception.PayPalAPiException;
import com.eshope.PoJo.PayPalOrderResponse;
import com.eshope.SettingBag.PaymentSettingBag;
import org.apache.commons.collections4.MultiValuedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Component
public class PayPalService {

    private static final String GET_ORDER_API="/v2/checkout/orders/";
    @Autowired
    private SettingService settingService;



    public boolean validateOrder(String orderId) throws PayPalAPiException{

        PayPalOrderResponse orderResponse = getOrderDetails(orderId);


        return orderResponse.validate(orderId);
    }

    private PayPalOrderResponse getOrderDetails(String orderId) throws PayPalAPiException {
        ResponseEntity<PayPalOrderResponse> response = makeRequest(orderId);

        HttpStatus statusCode=response.getStatusCode();
        if(!statusCode.equals(HttpStatus.OK)) {
            throwExceptionForNonOkResponse(statusCode);
        }
        PayPalOrderResponse orderResponse=response.getBody();
        return orderResponse;
    }

    private ResponseEntity<PayPalOrderResponse> makeRequest(String orderId) {
        PaymentSettingBag paymentSettings =settingService.getPaymentSettings();
        String baseURL=paymentSettings.getURL();
        String requestURL=baseURL+GET_ORDER_API+ orderId;
        String clientId=paymentSettings.getClientId();
        String clientSecret=paymentSettings.getClientSecret();

        HttpHeaders headers=new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Accept-Language","en_US");
        headers.setBasicAuth(clientId,clientSecret);

        HttpEntity<MultiValuedMap<String,String>> request=new HttpEntity<>(headers);

        RestTemplate restTemplate=new RestTemplate();

        ResponseEntity<PayPalOrderResponse> response=restTemplate.exchange(requestURL, HttpMethod.GET,request, PayPalOrderResponse.class);
        return response;
    }

    private void throwExceptionForNonOkResponse(HttpStatus statusCode) throws PayPalAPiException {
        String message=null;
        switch (statusCode) {
            case NOT_FOUND:
              message="Order Id not Found";
            case BAD_REQUEST:
               message="Bad request to PayPal Checkout Api";
            case INTERNAL_SERVER_ERROR:
                message="PayPal server Error";
            default:
                message="PayPal returned non-OK status code";
        }
        throw new PayPalAPiException(message);
    }
}
