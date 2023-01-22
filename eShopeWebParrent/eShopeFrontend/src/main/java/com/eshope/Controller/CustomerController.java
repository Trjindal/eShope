package com.eshope.Controller;

import com.eShope.common.entity.*;
import com.eshope.Service.CategoryService;
import com.eshope.Service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model){

        List<Country> countryList=customerService.listAllCountries();
        model.addAttribute("countryList",countryList);
        model.addAttribute("customer",new Customer());
        return "Register/registerForm";
    }

    @PostMapping("/register/save")
    public String saveCustomer(RedirectAttributes redirectAttributes, @Valid @ModelAttribute(value = "customer") Customer customer, Errors errors, Model model
    ) throws IOException {


        //TO CHECK UNIQUE EMAIL ID
        if (customer.getEmail() != "" && !customerService.isEmailUnique(customer.getEmail())) {
            log.error("New Customer form validation failed due to email ");
            model.addAttribute("emailNotUnique", "There is another customer having same email id");
            List<Country> countryList=customerService.listAllCountries();
            model.addAttribute("countryList",countryList);
            return "Register/registerForm";
        }

        //DISPLAYING ERROR MESSAGES
        if(errors.hasErrors()){

            log.error("New Customer form validation failed due to : " + errors.toString());
            List<Country> countryList=customerService.listAllCountries();
//            model.addAttribute("customer",customer);
            model.addAttribute("countryList",countryList);
            return "Register/registerForm";
        }
        List<Category> listCategories=categoryService.listNoChildrenCategories();
        model.addAttribute("listCategories",listCategories);
        return "";

    }

}
