package com.eshope.consumer.Controller;

import com.eShope.common.entity.*;
import com.eshope.consumer.Service.CustomerService;
import com.eshope.consumer.Service.SettingService;
import com.eshope.consumer.SettingBag.EmailSettingBag;
import com.eshope.consumer.Utility.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Slf4j
@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SettingService settingService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model){

        List<Country> countryList=customerService.listAllCountries();
        model.addAttribute("countryList",countryList);
        model.addAttribute("customer",new Customer());
        return "Register/registerForm";
    }

    @PostMapping("/register/save")
    public String saveCustomer(RedirectAttributes redirectAttributes, @Valid @ModelAttribute(value = "customer") Customer customer, Errors errors, Model model, HttpServletRequest request
                               ) throws IOException, MessagingException {




        //DISPLAYING ERROR MESSAGES
        if(errors.hasErrors()||!passwordVerification(customer.getPassword())||!uniqueEmailValidation(customer.getEmail())){
            if(passwordVerification(customer.getPassword())==false)
               model.addAttribute("passwordFailed","Password length must be between 6 and 15 and do not contains any white spaces.");
            if(uniqueEmailValidation(customer.getEmail())==false)
                model.addAttribute("emailNotUnique", "There is another customer having same email id");
            log.error("New Customer form validation failed due to : " + errors.toString());
            List<Country> countryList=customerService.listAllCountries();
//            model.addAttribute("customer",customer);
            model.addAttribute("countryList",countryList);
            return "Register/registerForm";
        }

        customerService.registerCustomer(customer);
        sendVerificationEmail(request,customer);

        return "/Register/registerSuccess";

    }


    @GetMapping("/verify")
    public String verifyAccount(@Param("code") String code,Model model){
        boolean verified=customerService.verifyCustomer(code);
        return "Register/"+(verified?"verifySuccess":"verifyFail");
    }



    private void sendVerificationEmail(HttpServletRequest request, Customer customer) throws MessagingException, UnsupportedEncodingException {

        EmailSettingBag emailSettings=settingService.getEmailSettings();
        JavaMailSenderImpl mailSender= Utility.prepareMailSender(emailSettings);

        String toAddress=customer.getEmail();
        String subject=emailSettings.getCustomerVerifySubject();
        String emailContent=emailSettings.getCustomerVerifyContent();

        MimeMessage message=mailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message);

        helper.setFrom(emailSettings.getFromAddress(),emailSettings.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);

        emailContent=emailContent.replace("[[name]]",customer.getFullName());

        String verifyURL=Utility.getSiteURL(request)+"/verify?code="+customer.getVerificationCode();

        emailContent=emailContent.replace("[[URL]]",verifyURL);

        helper.setText(emailContent,true);

        mailSender.send(message);

        log.error("to Address : "+toAddress);
        log.error("verify url : "+verifyURL);
    }


    private boolean passwordVerification(String password){
        if(!password.isEmpty())
            password=password.trim();
        if((password.length()>=6&&password.length()<16)&& !password.contains(" "))
            return true;
        return false;
    }

    private boolean uniqueEmailValidation(String email){
        if (email != "" && !customerService.isEmailUnique(email))
            return false;
        return true;

    }

}


