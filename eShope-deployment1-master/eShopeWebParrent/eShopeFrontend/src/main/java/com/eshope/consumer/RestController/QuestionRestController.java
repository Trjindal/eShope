package com.eshope.consumer.RestController;

import com.eShope.common.entity.Customer;
import com.eShope.common.entity.Question;
import com.eshope.consumer.Service.CustomerService;
import com.eshope.consumer.Service.QuestionService;
import com.eshope.consumer.Utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class QuestionRestController {


    @Autowired
    private CustomerService customerService;
    @Autowired
    private QuestionService questionService;

    @PostMapping("/post_question/{productId}")
    public ResponseEntity<?> postQuestion(@RequestBody Question question,
                                          @PathVariable(name = "productId") Integer productId,
                                          HttpServletRequest request) throws UsernameNotFoundException, IOException {

        Customer customerUser = getAuthenticatedCustomer(request);
        if (customerUser == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        questionService.saveNewQuestion(question, customerUser, productId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
    private Customer getAuthenticatedCustomer(HttpServletRequest request){

        String email= Utility.getEmailOfAuthenticatedCustomer(request);
        return customerService.getCustomerByEmail(email);
    }

}