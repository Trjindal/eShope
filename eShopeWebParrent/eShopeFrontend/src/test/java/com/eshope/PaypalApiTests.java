package com.eshope;

import com.eshope.PoJo.PayPalOrderResponse;
import org.apache.commons.collections4.MultiValuedMap;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class PaypalApiTests {
    private static final String BASE_URL="https://api.sandbox.paypal.com";
    private static final String GET_ORDER_API="/v2/checkout/orders/";
    private static final String CLIENT_ID="AXKvgDsxcnhfg6eESt34yoUjhcZoDlnBztZjlMVKXNAjkbnK3CVKisbPxYJG7xW9Ka_ZQhlZHXD3iMmk";
    private static final String CLIENT_SECRET="EEdb6zvKq3gYB98JjEOVA6bcOw0G0GOA-iDxzWoGb-TBGpG22mRofjrCzJpkxygWgOpMj_PJiMeo0DM7";

    @Test
    public void testGetOrderDetails(){
        String orderId="6KK44567MN328491C";
        String requestURL=BASE_URL+GET_ORDER_API+orderId;

        HttpHeaders headers=new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Accept-Language","en_US");
        headers.setBasicAuth(CLIENT_ID,CLIENT_SECRET);

        HttpEntity<MultiValuedMap<String,String>> request=new HttpEntity<>(headers);
        RestTemplate restTemplate=new RestTemplate();

        ResponseEntity<PayPalOrderResponse> response=restTemplate.exchange(requestURL, HttpMethod.GET,request, PayPalOrderResponse.class);

        PayPalOrderResponse orderResponse=response.getBody();
        System.out.println(orderResponse.getId());

        System.out.println(orderResponse.validate(orderId));

    }

}
