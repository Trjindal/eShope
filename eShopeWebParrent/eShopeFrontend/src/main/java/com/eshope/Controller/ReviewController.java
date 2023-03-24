package com.eshope.Controller;

import com.eShope.common.entity.Customer;
import com.eShope.common.entity.Review;
import com.eshope.Service.CustomerService;
import com.eshope.Service.ReviewService;
import com.eshope.Utility.Utility;
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
public class ReviewController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/reviews")
    public String listAllReviews(Model model, HttpServletRequest request) {
        return listByPage(1, model,request, "reviewTime", "desc", null);
    }

    @GetMapping("/reviews/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model, HttpServletRequest request, @Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("reviewKeyword") String reviewKeyword) {
        Customer customer=getAuthenticatedCustomer(request);
        Page<Review> page = reviewService.listByPage(customer,pageNum, sortField, sortDir, reviewKeyword);
        List<Review> listReviews = page.getContent();

        long startCount = (pageNum - 1) * reviewService.REVIEWS_PER_PAGE + 1;
        long endCount = startCount + reviewService.REVIEWS_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }


        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startCount", startCount);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listAllReviews", listReviews);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("reviewKeyword", reviewKeyword);

        return "Reviews/review";
    }

    @GetMapping("/reviews/detail/{id}")
    public String detailReview(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model, HttpServletRequest request){
        try{
            Customer customer=getAuthenticatedCustomer(request);
            Review review=reviewService.getReviewByCustomerAndId(customer,id);
            if(review==null){
                throw new UsernameNotFoundException("Could not find any review with Id "+ id);
            }
            model.addAttribute("review",review);
            return "Reviews/viewReviewModal.html";
        }catch (UsernameNotFoundException ex){
            redirectAttributes.addFlashAttribute("message",ex.getMessage());
            return "redirect:/reviews";
        }

    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request){

        String email= Utility.getEmailOfAuthenticatedCustomer(request);
        return customerService.getCustomerByEmail(email);
    }

}
