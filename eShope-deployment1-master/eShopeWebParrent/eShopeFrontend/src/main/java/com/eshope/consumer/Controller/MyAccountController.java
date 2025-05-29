package com.eshope.consumer.Controller;

import com.eShope.common.entity.Country;
import com.eShope.common.entity.Customer;
import com.eshope.consumer.Service.CustomerService;
import com.eshope.consumer.Utility.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@Slf4j
public class MyAccountController {

    @Autowired
    CustomerService customerService;

    @GetMapping("/account")
    public String showMyAccount(Model model,HttpServletRequest request){

       String email= Utility.getEmailOfAuthenticatedCustomer(request);


       Customer customer=customerService.getCustomerByEmail(email);
        List<Country> countryList=customerService.listAllCountries();


        model.addAttribute("countryList",countryList);
        model.addAttribute("customer",customer);


        return "Customer/accountForm";
    }

    @PostMapping("/register/update")
    public String updateAccountDetails(RedirectAttributes redirectAttributes, @Valid @ModelAttribute(value = "customer") Customer customer, Errors errors, Model model){

        //DISPLAYING ERROR MESSAGES
        if(errors.hasErrors()){
            log.error("Update Customer form validation failed due to : " + errors.toString());
            List<Country> countryList=customerService.listAllCountries();
            model.addAttribute("countryList",countryList);
            return "Customer/accountForm";
        }
        customerService.update(customer);
        model.addAttribute("message","Your Account details have been updated");
        log.error("Here");
        List<Country> countryList=customerService.listAllCountries();
        model.addAttribute("countryList",countryList);
        return "Customer/accountForm";
    }



}
