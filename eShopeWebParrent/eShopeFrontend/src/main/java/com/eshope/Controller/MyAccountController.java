package com.eshope.Controller;

import com.eShope.common.entity.Country;
import com.eShope.common.entity.Customer;
import com.eshope.Oauth.CustomerOAuth2User;
import com.eshope.Service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
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

       String email= getEmailOfAuthenticatedCustomer(request);
        Customer customer=customerService.getCustomerByEmail(email);
        List<Country> countryList=customerService.listAllCountries();


        model.addAttribute("countryList",countryList);
        model.addAttribute("customer",customer);


        return "Customer/accountForm";
    }

    @PostMapping("/register/update")
    public String updateAccountDetails(RedirectAttributes redirectAttributes, @Valid @ModelAttribute(value = "customer") Customer customer, Errors errors, Model model, HttpServletRequest request){

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

    private String getEmailOfAuthenticatedCustomer(HttpServletRequest request){
        Object principal=request.getUserPrincipal();
        String customerEmail=null;
        if(principal instanceof UsernamePasswordAuthenticationToken||principal instanceof RememberMeAuthenticationToken){
            customerEmail=request.getUserPrincipal().getName();
        }else if(principal instanceof OAuth2AuthenticationToken){
            OAuth2AuthenticationToken auth2AuthenticationToken=(OAuth2AuthenticationToken) principal;
            CustomerOAuth2User oAuth2User=(CustomerOAuth2User) auth2AuthenticationToken.getPrincipal();
            customerEmail=oAuth2User.getEmail();
        }
        return customerEmail;
    }

}
