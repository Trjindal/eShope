package com.eshope;


import com.eshope.consumer.PoJo.OrderReturnRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderRestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithUserDetails("trjindal00@gmail.com")
    public void testSendOrderReturnRequestFailed() throws Exception {
        Integer orderId=1111;
        OrderReturnRequest orderReturnRequest=new OrderReturnRequest(orderId,"","");

        String requestUrl="/orders/return";

        mockMvc.perform(post(requestUrl)
                .with(csrf())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(orderReturnRequest)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @WithUserDetails("trjindal00@gmail.com")
    public void testSendOrderReturnRequestSuccessful() throws Exception {
        Integer orderId=5;
        String reason ="I don't like the product.";
        String note="Please refund ASAP";
        OrderReturnRequest orderReturnRequest=new OrderReturnRequest(orderId,reason,note);

        String requestUrl="/orders/return";

        mockMvc.perform(post(requestUrl)
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(orderReturnRequest)))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
