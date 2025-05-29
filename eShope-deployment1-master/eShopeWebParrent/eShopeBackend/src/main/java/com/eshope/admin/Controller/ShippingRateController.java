package com.eshope.admin.Controller;


import com.eShope.common.entity.Country;
import com.eShope.common.entity.ShippingRate;
import com.eshope.admin.Service.ShippingRateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@Slf4j
public class ShippingRateController {

    @Autowired
    private ShippingRateService shippingRateService;

    @GetMapping("/shipping")
    public String listAllShippingRate(Model model){
        return listByPage(1,model,"id","asc",null);
    }

    @GetMapping("/shipping/page/{pageNum}")
    public String listByPage(@PathVariable(name="pageNum") int pageNum, Model model, @Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword) {
        Page<ShippingRate> page = shippingRateService.listByPage(pageNum, sortField, sortDir, keyword);
        List<ShippingRate> listShippingRates = page.getContent();

        long startCount = (pageNum - 1) * shippingRateService.SHIPPING_RATES_PER_PAGE + 1;
        long endCount = startCount + shippingRateService.SHIPPING_RATES_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startCount", startCount);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listShippingRates", listShippingRates);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("keyword", keyword);
        return "ShippingRates/shippingRates";
    }
    @GetMapping("/shipping/new")
    public String newShippingRate( Model model){

        ShippingRate shippingRate=new ShippingRate();
        shippingRate.setCodSupported(true);

        List<Country> listCountries=shippingRateService.listAllCountries();

        model.addAttribute("shippingRate",shippingRate);
        model.addAttribute("listCountries",listCountries);
        return "ShippingRates/shippingRatesForm.html";
    }

    @PostMapping("/shipping/saveRates")
    public String saveShippingRate(RedirectAttributes redirectAttributes, @Valid @ModelAttribute(value = "shippingRate") ShippingRate shippingRate, Errors errors, Model model){


        Integer id= shippingRate.getId();
        log.error(String.valueOf(id));

//        //TO CHECK UNIQUE SHIPPING ADDRESS
        if(id==null) {
            if (shippingRate.getState() != "" && !shippingRateService.isUnique(shippingRate.getCountry(), shippingRate.getState())) {
                log.error("New Shipping form validation failed due to not unique shipping address ");
                model.addAttribute("notUnique", "This Shipping address already exists");
                List<Country> listCountries = shippingRateService.listAllCountries();
                model.addAttribute("listCountries", listCountries);
                return "ShippingRates/shippingRatesForm.html";
            }
        }else{

            ShippingRate existingShippingRate=shippingRateService.getShippingById(id);
            shippingRate.setId(id);
            if(!existingShippingRate.getState().equals(shippingRate.getState())||existingShippingRate.getCountry()!=shippingRate.getCountry()){
                if (shippingRate.getState() != "" && !shippingRateService.isUnique(shippingRate.getCountry(), shippingRate.getState())) {
                    log.error("New Shipping form validation failed due to not unique shipping address ");
                    model.addAttribute("notUnique", "This Shipping address already exists");
                    List<Country> listCountries = shippingRateService.listAllCountries();
                    model.addAttribute("listCountries", listCountries);
                    return "ShippingRates/shippingRatesForm.html";
                }
            }
        }
//

        //DISPLAYING ERROR MESSAGES
        if(errors.hasErrors()){

            log.error("New Shipping form validation failed due to : " + errors.toString());
            List<Country> listCountries=shippingRateService.listAllCountries();
            model.addAttribute("listCountries",listCountries);
            return "ShippingRates/shippingRatesForm.html";
        }



        //SAVE DETAILS
        ShippingRate savedShippingRate=shippingRateService.saveShippingRate(shippingRate);
        if(id==null)
            redirectAttributes.addFlashAttribute("message","The Shipping Rate has been saved successfully");
        else
            redirectAttributes.addFlashAttribute("message","The Shipping Rate has been edited successfully");
        return "redirect:/shipping";
    }


    @GetMapping("/shipping/edit/{id}")
    public String editShippingAddress(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model){
        try{
            ShippingRate shippingRate=shippingRateService.getShippingById(id);
            List<Country> listCountries=shippingRateService.listAllCountries();
            model.addAttribute("listCountries",listCountries);
            model.addAttribute("shippingRate",shippingRate);
            model.addAttribute("shippingRates",shippingRate);
            return "ShippingRates/shippingRatesUpdateForm.html";
        }catch (UsernameNotFoundException ex){
            redirectAttributes.addFlashAttribute("error",ex.getMessage());

            return "redirect:/shipping";
        }

    }


    //DELETE CONTROLLER
    @GetMapping("/shipping/delete/{id}")
    public String deleteShippingAddress(@PathVariable(name="id")Integer id,Model model,RedirectAttributes redirectAttributes){
        try{
            shippingRateService.delete(id);
            redirectAttributes.addFlashAttribute("message","The Shipping Rates has been deleted successfully");
        }catch (UsernameNotFoundException ex){
            redirectAttributes.addFlashAttribute("error",ex.getMessage());
        }
        return "redirect:/shipping";
    }

    @GetMapping("/shipping/{id}/enabled/{status}")
    public String updateShippingAddressEnabledStatus(@PathVariable("id") Integer id,@PathVariable("status") boolean enabled,RedirectAttributes redirectAttributes){
        try{
            shippingRateService.updateShippingAddressEnabledStatus(id, enabled);
            String status = enabled ? "enabled" : "disabled";
            String message = "The Shipping Address Id " + id + " has been " + status;
            redirectAttributes.addFlashAttribute("message", message);
        }catch (UsernameNotFoundException ex){
            redirectAttributes.addFlashAttribute("error",ex.getMessage());
        }
        return "redirect:/shipping";
    }


}
