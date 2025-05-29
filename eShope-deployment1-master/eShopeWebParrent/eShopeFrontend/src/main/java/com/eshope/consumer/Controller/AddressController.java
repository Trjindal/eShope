package com.eshope.consumer.Controller;

import com.eShope.common.entity.Address;
import com.eShope.common.entity.Country;
import com.eShope.common.entity.Customer;
import com.eshope.consumer.Service.AddressService;
import com.eshope.consumer.Service.CustomerService;
import com.eshope.consumer.Utility.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private CustomerService customerService;

    @GetMapping("/address_book")
    public String showAddressBook(Model model, HttpServletRequest request,HttpSession session){
        Customer customer=getAuthenticatedCustomer(request);
        List<Address> listAddresses=addressService.listAddressBook(customer);

        String redirectOption=request.getParameter("redirect");

        boolean usePrimaryAddressAsDefault=true;
        for (Address address:listAddresses) {
            if(address.isDefaultForShipping()){
                usePrimaryAddressAsDefault=false;
                break;
            }
        }

        if(redirectOption!=null){
            model.addAttribute("redirect",redirectOption);
        }

        model.addAttribute("customer",customer);
        model.addAttribute("listAddresses",listAddresses);
        model.addAttribute("usePrimaryAddressAsDefault",usePrimaryAddressAsDefault);
//        model.addAttribute("message","New Address has been added.");


        return "AddressBook/addresses";
    }

    @GetMapping("/address_book/default/{id}")
    public String setDefaultAddress(@PathVariable("id") Integer addressId, @RequestParam(required = false,name = "redirect") String redirect, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes){
        try {
            Customer customer = getAuthenticatedCustomer(request);
            addressService.setDefaultAddress(addressId, customer.getId());

            log.error(redirect);

            if(redirect!=null){
                return "redirect:/"+redirect;
            }
            return "redirect:/address_book";
        }catch (UsernameNotFoundException ex){
            redirectAttributes.addFlashAttribute("error",ex.getMessage());

            return "redirect:/address_book";
        }
    }




    @GetMapping("/address_book/new")
    public String newAddress(Model model,HttpServletRequest request,RedirectAttributes redirectAttributes){

        Customer customer=getAuthenticatedCustomer(request);

        String redirectOption=request.getParameter("redirect");
        String redirectURL="redirect:/address_book";


        if(redirectOption!=null){
            redirectURL+="?redirect="+redirectOption;
            model.addAttribute("redirect",redirectOption);
        }

        if(addressService.countAddress(customer.getId())<10){
            Address address=new Address();

            List<Country> countryList=customerService.listAllCountries();

            model.addAttribute("countryList",countryList);
            model.addAttribute("address",address);

            return "AddressBook/addressForm";
        }else{
            redirectAttributes.addFlashAttribute("error","Limit of Adding different address is 10 only.");
            return redirectURL;
        }


    }

    @GetMapping("/address_book/edit/{id}")
    public String editShippingAddress(@PathVariable(name = "id") Integer id, HttpServletRequest request,RedirectAttributes redirectAttributes, Model model, HttpSession session){
        try{
            Address address=addressService.getAddressById(id);
            List<Country> countryList=customerService.listAllCountries();
            model.addAttribute("countryList",countryList);
            model.addAttribute("address",address);
            model.addAttribute("addresses",address);
            session.setAttribute("id",id);

            String redirectOption=request.getParameter("redirect");
            String redirectURL="redirect:/address_book";

            if(redirectOption!=null){
                redirectURL+="?redirect="+redirectOption;
                session.setAttribute("redirect",redirectOption);
                model.addAttribute("redirect",redirectOption);
            }

            return "AddressBook/addressUpdateForm.html";
        }catch (UsernameNotFoundException ex){
            redirectAttributes.addFlashAttribute("error",ex.getMessage());

            return "redirect:/address_book";
        }

    }


    @GetMapping("/address_book/edit/primaryAddress")
    public String editPrimaryAddress(Model model,HttpServletRequest request,HttpSession session){
        Customer customer=getAuthenticatedCustomer(request);
        List<Country> countryList=customerService.listAllCountries();

        session.setAttribute("customer",customer);

        model.addAttribute("countryList",countryList);
        model.addAttribute("customer",customer);
        return "AddressBook/editPrimaryAddress";
    }

    @PostMapping("/address_book/save")
    public String saveAddress(RedirectAttributes redirectAttributes,@RequestParam String redirect, @Valid @ModelAttribute(value = "address") Address address, Errors errors, Model model,HttpServletRequest request,@RequestParam(required = false) Integer id){

        Customer customer=getAuthenticatedCustomer(request);
        log.error(String.valueOf(id));
        log.error(redirect);

        String redirectURL="redirect:/address_book";

        if(!redirect.isEmpty())
            redirectURL+="?redirect="+redirect;


        if(id==null){
            if(addressService.countAddress(customer.getId())>=10){
                redirectAttributes.addFlashAttribute("error","Limit of Adding different address is 10 only.");
                return redirectURL;
            }
        }


        //DISPLAYING ERROR MESSAGES
        if(errors.hasErrors()){
            log.error("New Address form validation failed due to : " + errors.toString());
            List<Country> countryList=customerService.listAllCountries();
            model.addAttribute("countryList",countryList);

            return "AddressBook/addressUpdateForm.html";
        }

        if(id!=null){
            address.setId(id);
        }

        address.setCustomer(customer);
        addressService.save(address);
        if(id==null)
            redirectAttributes.addFlashAttribute("message","New Address has been added.");
        else
            redirectAttributes.addFlashAttribute("message","Address has been edited successfully.");
        return redirectURL;
    }


    @PostMapping("/address_book/save/primaryAddress")
    public String savePrimaryAddress(RedirectAttributes redirectAttributes, @Valid @ModelAttribute(value = "customer") Customer customer, Errors errors, Model model,HttpServletRequest request,HttpSession session){

        String redirectOption=request.getParameter("redirect");
        String redirectURL="redirect:/address_book";

        if(!redirectOption.isEmpty())
            redirectURL+="?redirect="+redirectOption;


        //DISPLAYING ERROR MESSAGES
        if(errors.hasErrors()){
            log.error("New Address form validation failed due to : " + errors.toString());
            List<Country> countryList=customerService.listAllCountries();
            model.addAttribute("countryList",countryList);
            List<ObjectError> objectErrorList= errors.getAllErrors();
            List<String> errorsList=new ArrayList<>();
            for (ObjectError error:objectErrorList) {
//                System.out.println(error);
                errorsList.add(error.getDefaultMessage());
            }
            redirectAttributes.addFlashAttribute("errorsList",errorsList);
            if(redirectURL!=null)
                return "redirect:/address_book/edit/primaryAddress?redirect="+redirectOption;
            return "AddressBook/editPrimaryAddress";
        }

        Customer existingCustomer=(Customer)session.getAttribute("customer");
        addressService.savePrimaryAddress(customer,existingCustomer);

        redirectAttributes.addFlashAttribute("message","Address has been edited successfully.");

        return redirectURL;
    }


        //DELETE CONTROLLER
    @RequestMapping("/address_book/delete/{id}")
    public String deleteShippingAddress(@PathVariable(name="id")Integer id, Model model, RedirectAttributes redirectAttributes,@RequestParam(required = false) String redirect){
        String redirectURL="redirect:/address_book";
        try{
            addressService.delete(id);
            if(!redirect.equals("undefined")){
                redirectURL+="?redirect="+redirect;
            }
            redirectAttributes.addFlashAttribute("message","The Shipping Address has been deleted successfully");
        }catch (UsernameNotFoundException ex){
            redirectAttributes.addFlashAttribute("error",ex.getMessage());
        }
        return redirectURL;
    }




    private Customer getAuthenticatedCustomer(HttpServletRequest request){

        String email= Utility.getEmailOfAuthenticatedCustomer(request);
        return customerService.getCustomerByEmail(email);
    }

}
