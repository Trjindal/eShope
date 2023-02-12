package com.eshope.Controller;

import com.eShope.common.entity.Address;
import com.eShope.common.entity.Country;
import com.eShope.common.entity.Customer;
import com.eShope.common.entity.ShippingRate;
import com.eshope.Service.AddressService;
import com.eshope.Service.CustomerService;
import com.eshope.Utility.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@Slf4j
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private CustomerService customerService;

    @GetMapping("/address_book")
    public String showAddressBook(Model model, HttpServletRequest request){
        Customer customer=getAuthenticatedCustomer(request);
        List<Address> listAddresses=addressService.listAddressBook(customer);

        boolean usePrimaryAddressAsDefault=true;
        for (Address address:listAddresses) {
            if(address.isDefaultForShipping()){
                usePrimaryAddressAsDefault=false;
                break;
            }
        }


        model.addAttribute("customer",customer);
        model.addAttribute("listAddresses",listAddresses);
        model.addAttribute("usePrimaryAddressAsDefault",usePrimaryAddressAsDefault);
//        model.addAttribute("message","New Address has been added.");


        return "AddressBook/addresses";
    }

    @GetMapping("/address_book/new")
    public String newAddress(Model model,HttpServletRequest request,RedirectAttributes redirectAttributes){

        Customer customer=getAuthenticatedCustomer(request);
        if(addressService.countAddress(customer.getId())<10){
            Address address=new Address();

            List<Country> countryList=customerService.listAllCountries();

            model.addAttribute("countryList",countryList);
            model.addAttribute("address",address);

            return "AddressBook/addressForm";
        }else{
            redirectAttributes.addFlashAttribute("error","Limit of Adding different address is 10 only.");
            return "redirect:/address_book";
        }


    }

    @GetMapping("/address_book/edit/{id}")
    public String editShippingAddress(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model, HttpSession session){
        try{
            Address address=addressService.getAddressById(id);
            List<Country> countryList=customerService.listAllCountries();
            model.addAttribute("countryList",countryList);
            model.addAttribute("address",address);
            model.addAttribute("addresses",address);
            session.setAttribute("id",id);
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
    public String saveAddress(RedirectAttributes redirectAttributes, @Valid @ModelAttribute(value = "address") Address address, Errors errors, Model model,HttpServletRequest request,HttpSession session){

        Customer customer=getAuthenticatedCustomer(request);
        Integer id= (Integer) session.getAttribute("id");
        if(id==null){
            if(addressService.countAddress(customer.getId())>=10){
                redirectAttributes.addFlashAttribute("error","Limit of Adding different address is 10 only.");
                return "redirect:/address_book";
            }
        }

        //DISPLAYING ERROR MESSAGES
        if(errors.hasErrors()){
            log.error("New Address form validation failed due to : " + errors.toString());
            List<Country> countryList=customerService.listAllCountries();
            model.addAttribute("countryList",countryList);

            return "AddressBook/addressForm";
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
        return "redirect:/address_book";
    }


    @PostMapping("/address_book/save/primaryAddress")
    public String savePrimaryAddress(RedirectAttributes redirectAttributes, @Valid @ModelAttribute(value = "customer") Customer customer, Errors errors, Model model,HttpServletRequest request,HttpSession session){
        //DISPLAYING ERROR MESSAGES
        if(errors.hasErrors()){
            log.error("New Address form validation failed due to : " + errors.toString());
            List<Country> countryList=customerService.listAllCountries();
            model.addAttribute("countryList",countryList);

            return "AddressBook/editPrimaryAddress";
        }

        Customer existingCustomer=(Customer)session.getAttribute("customer");
        addressService.savePrimaryAddress(customer,existingCustomer);

        redirectAttributes.addFlashAttribute("message","Address has been edited successfully.");

        return "redirect:/address_book";
    }


        //DELETE CONTROLLER
    @GetMapping("/address_book/delete/{id}")
    public String deleteShippingAddress(@PathVariable(name="id")Integer id, Model model, RedirectAttributes redirectAttributes){
        try{
            addressService.delete(id);
            redirectAttributes.addFlashAttribute("message","The Shipping Address has been deleted successfully");
        }catch (UsernameNotFoundException ex){
            redirectAttributes.addFlashAttribute("error",ex.getMessage());
        }
        return "redirect:/address_book";
    }


    @GetMapping("/address_book/default/{id}")
    public String setDefaultAddress(@PathVariable("id") Integer addressId,HttpServletRequest request,RedirectAttributes redirectAttributes){
        try {
            Customer customer = getAuthenticatedCustomer(request);
            addressService.setDefaultAddress(addressId, customer.getId());

            String redirectOption=request.getParameter("redirect");
            if(redirectOption!=null&&redirectOption.equals("cart")){
                return "redirect:/cart";
            }

            return "redirect:/address_book";
        }catch (UsernameNotFoundException ex){
            redirectAttributes.addFlashAttribute("error",ex.getMessage());

            return "redirect:/address_book";
        }
    }



    private Customer getAuthenticatedCustomer(HttpServletRequest request){

        String email= Utility.getEmailOfAuthenticatedCustomer(request);
        return customerService.getCustomerByEmail(email);
    }

}
