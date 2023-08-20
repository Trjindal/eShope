package com.eshope.Controller;


import com.eShope.common.entity.Product.Product;
import com.eShope.common.entity.Question;

import com.eshope.Service.ProductService;
import com.eshope.Service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private ProductService productService;

    @Autowired
    private QuestionService questionService;


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
        Page<Question> page = questionService.listByProduct(productAlias,pageNum, sortField, sortDir);
        List<Question> listQuestions = page.getContent();

        long startCount = (pageNum - 1) * questionService.QUESTIONS_PER_PAGE_FOR_PUBLIC_LISTING + 1;
        long endCount = startCount + questionService.QUESTIONS_PER_PAGE_FOR_PUBLIC_LISTING - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        model.addAttribute("productAlias",productAlias);
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
}
