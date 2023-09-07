package com.eshope.consumer.Controller;


import com.eShope.common.entity.Customer;
import com.eShope.common.entity.Product.Product;
import com.eShope.common.entity.Question;

import com.eshope.consumer.Service.CustomerService;
import com.eshope.consumer.Service.ProductService;
import com.eshope.consumer.Service.QuestionService;
import com.eshope.consumer.Utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private ProductService productService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CustomerService customerService;


    @GetMapping("/ask_question/{productAlias}")
    public String askQuestion(@PathVariable(name = "productAlias") String productAlias) {
        return "redirect:/p/" + productAlias + "#qa";
    }
    @GetMapping("questions/{productAlias}")
    public String listQuestionsByFirstProductPage(@PathVariable(name="productAlias") String productAlias,Model model){
        return listQuestionByProductPage(1,productAlias,model,"answerTime","desc");
    }

    @GetMapping("/questions/{productAlias}/page/{pageNum}")
    public String listQuestionByProductPage(@PathVariable(name = "pageNum") int pageNum, @PathVariable(name = "productAlias") String productAlias, Model model, @Param("sortField") String sortField, @Param("sortDir") String sortDir) {

        Product product=null;

        try{
            product=productService.getProductByAlias(productAlias);
        }catch (UsernameNotFoundException e){
            return "error.html";
        }

        //authenticate customer
        Page<Question> page = questionService.listQuestionsByProduct(productAlias,pageNum, sortField, sortDir);
        List<Question> listQuestions = page.getContent();

        long startCount = (pageNum - 1) * questionService.QUESTIONS_PER_PAGE_FOR_PUBLIC_LISTING + 1;
        long endCount = startCount + questionService.QUESTIONS_PER_PAGE_FOR_PUBLIC_LISTING - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        model.addAttribute("product",product);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startCount", startCount);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listAllQuestions", listQuestions);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        return "Questions/questionsByProduct";
    }

    @GetMapping("/customer/questions")
    public String listQuestionsByCustomer(Model model, HttpServletRequest request) {
        return listQuestionsByCustomerByPage(model, request, 1, null, "askTime", "desc");
    }

    @GetMapping("/customer/questions/page/{pageNum}")
    public String listQuestionsByCustomerByPage(
            Model model, HttpServletRequest request,
            @PathVariable(name = "pageNum") int pageNum,
            String keyword, String sortField, String sortDir) {
        Customer customer=getAuthenticatedCustomer(request);
        Page<Question> page = questionService.listQuestionsByCustomer(customer, keyword, pageNum, sortField, sortDir);
        List<Question> listQuestions = page.getContent();

        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("moduleURL", "/customer/questions");

        model.addAttribute("listAllQuestions", listQuestions);

        long startCount = (pageNum - 1) * QuestionService.QUESTIONS_PER_PAGE_FOR_PUBLIC_LISTING + 1;
        model.addAttribute("startCount", startCount);

        long endCount = startCount + QuestionService.QUESTIONS_PER_PAGE_FOR_PUBLIC_LISTING - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        model.addAttribute("endCount", endCount);

        return "Questions/customerQuestions";
    }


    @GetMapping("/customer/questions/detail/{id}")
    public String viewQuestion(@PathVariable("id") Integer id, Model model, RedirectAttributes ra,
                               HttpServletRequest request) {
        Customer customer = getAuthenticatedCustomer(request);
        Question question = questionService.getByCustomerAndId(customer, id);

        if (question != null) {
            model.addAttribute("question", question);
            return "Questions/questionDetailModal";
        } else {
            ra.addFlashAttribute("message", "Could not find any question with ID " + id);
            return "redirect:/customer/questions";
        }
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request){

        String email= Utility.getEmailOfAuthenticatedCustomer(request);
        return customerService.getCustomerByEmail(email);
    }

}
