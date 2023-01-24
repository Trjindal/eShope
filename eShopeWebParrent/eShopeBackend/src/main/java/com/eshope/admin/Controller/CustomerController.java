package com.eshope.admin.Controller;

import com.eShope.common.entity.*;
import com.eshope.admin.CSVExporter.UserCsvExporter;
import com.eshope.admin.ExcelExporter.UserExcelExporter;
import com.eshope.admin.PDFExporter.UserPdfExporter;
import com.eshope.admin.Service.CustomerService;
import com.eshope.admin.Utility.FileUploadUtil;
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

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.util.List;

@Controller
@Slf4j
public class CustomerController {

    @Autowired
    CustomerService customerService;



    @GetMapping("/customers")
    public String listAllCustomers(Model model){
        return listByPage(1,model,"id","asc",null);
    }

    @GetMapping("/customers/page/{pageNum}")
    public String listByPage(@PathVariable(name="pageNum") int pageNum, Model model, @Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword){
        Page<Customer> page=customerService.listByPage(pageNum,sortField,sortDir,keyword);
        List<Customer> listCustomers=page.getContent();

        long startCount =(pageNum-1)* customerService.CUSTOMERS_PER_PAGE+1;
        long endCount=startCount+customerService.CUSTOMERS_PER_PAGE-1;
        if(endCount>page.getTotalElements()){
            endCount=page.getTotalElements();
        }

        model.addAttribute("currentPage",pageNum);
        model.addAttribute("startCount",startCount);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("endCount",endCount);
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("listAllCustomers",listCustomers);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("keyword", keyword );
        return "Customer/customers";
    }

    @GetMapping("/customers/edit/{id}")
    public String editCustomer(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model,HttpSession session){
        try{

            Customer customer=customerService.getCustomerById(id);
            session.setAttribute("id",id);
            List<Country> countryList=customerService.listAllCountries();
            model.addAttribute("customer",customer);
            model.addAttribute("countryList",countryList);
            return "Customer/customerUpdateForm.html";
        }catch (UsernameNotFoundException ex){
            redirectAttributes.addFlashAttribute("message",ex.getMessage());

            return "redirect:/customers";
        }

    }

    @PostMapping("/customers/editCustomer")
    public String saveEditCustomer(RedirectAttributes redirectAttributes, @Valid @ModelAttribute(value = "customer") Customer customer, Errors errors, Model model, HttpSession session ){

        Integer id= (Integer) session.getAttribute("id");
        Customer existingCustomer=customerService.getCustomerById(id);

        //TO CHECK UNIQUE EMAIL ID
        if(existingCustomer!=null&&!(existingCustomer.getEmail().matches(customer.getEmail()))) {
            if (customer.getEmail() != "" && !customerService.isEmailUnique(customer.getEmail())) {
                log.error("Contact form validation failed due to email ");
                model.addAttribute("emailNotUnique", "There is another customer having same email id");
                List<Country> countryList=customerService.listAllCountries();
                model.addAttribute("customer",customer);
                model.addAttribute("countryList",countryList);
                return "Customer/customerUpdateForm.html";
            }
        }

        //DISPLAYING ERROR MESSAGES
        if(errors.hasErrors()){
            log.error("Contact form validation failed due to : " + errors.toString());
            List<Country> countryList=customerService.listAllCountries();
            model.addAttribute("customer",customer);
            model.addAttribute("countryList",countryList);
            return "Customer/customerUpdateForm.html";
        }

        customer.setPassword(existingCustomer.getPassword());
        customer.setId(existingCustomer.getId());

        //SAVE DETAILS
        customerService.saveCustomer(customer);

        redirectAttributes.addFlashAttribute("message","The Customer has been edited successfully");
        return "redirect:/customers";

    }


    @GetMapping("/customers/detail/{id}")
    public String detailProduct(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model) {
        try {
            Customer customer = customerService.getCustomerById(id);
            model.addAttribute("customer", customer);
            return "Customer/viewCustomerModal.html";
        } catch (UsernameNotFoundException ex) {
            redirectAttributes.addFlashAttribute("message", ex.getMessage());


            return "redirect:/customers";
        }
    }

    //DELETE CONTROLLER
    @GetMapping("/customers/delete/{id}")
    public String deleteCustomer(@PathVariable(name="id")Integer id, Model model, RedirectAttributes redirectAttributes){
        try{
            customerService.delete(id);
            redirectAttributes.addFlashAttribute("message","The Customer ID "+id+"has been deleted successfully");
        }catch (UsernameNotFoundException ex){
            redirectAttributes.addFlashAttribute("message",ex.getMessage());
        }
        return "redirect:/customers";
    }

    @GetMapping("/customers/{id}/enabled/{status}")
    public String updateCustomerEnabledStatus(@PathVariable("id") Integer id,@PathVariable("status") boolean enabled,RedirectAttributes redirectAttributes){
        customerService.updateCustomerEnabledStatus(id,enabled);
        String status=enabled?"enabled":"disabled";
        String message="The customer Id "+id+" has been "+status;
        redirectAttributes.addFlashAttribute("message",message);
        return "redirect:/customers";
    }

//    @GetMapping("/customers/export/csv")
//    public void exportToCSV(HttpServletResponse response) throws IOException {
//        List<Customer> listCustomer=customerService.listAllCustomers();
//        UserCsvExporter exporter=new UserCsvExporter();
//        exporter.export(listCustomer,response);
//    }
//
//
//    @GetMapping("/customers/export/excel")
//    public void exportToExcel(HttpServletResponse response) throws IOException {
//        List<Customer> listCustomer=customerService.listAllCustomers();
//        UserExcelExporter exporter=new UserExcelExporter();
//        exporter.export(listCustomer,response);
//    }
//
//    @GetMapping("/customers/export/pdf")
//    public void exportToPdf(HttpServletResponse response) throws IOException {
//        List<Customer> listCustomer=customerService.listAllCustomers();
//
//        UserPdfExporter exporter=new UserPdfExporter();
//        exporter.export(listCustomer,response);
//    }



}
