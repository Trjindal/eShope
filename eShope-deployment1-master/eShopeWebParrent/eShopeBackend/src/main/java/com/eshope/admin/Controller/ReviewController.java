package com.eshope.admin.Controller;


import com.eShope.common.entity.Review;
import com.eshope.admin.Service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/reviews")
    public String listAllReviews(Model model){
        return listByPage(1,model,"id","asc",null);
    }

    @GetMapping("/reviews/page/{pageNum}")
    public String listByPage(@PathVariable(name="pageNum") int pageNum, Model model, @Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword){
        Page<Review> page=reviewService.listByPage(pageNum,sortField,sortDir,keyword);
        List<Review> listReviews=page.getContent();

        long startCount =(pageNum-1)* reviewService.REVIEWS_PER_PAGE+1;
        long endCount=startCount+reviewService.REVIEWS_PER_PAGE-1;
        if(endCount>page.getTotalElements()){
            endCount=page.getTotalElements();
        }

        model.addAttribute("currentPage",pageNum);
        model.addAttribute("startCount",startCount);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("endCount",endCount);
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("listAllReviews",listReviews);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("keyword", keyword );
        return "Reviews/reviews";
    }

    @GetMapping("/reviews/detail/{id}")
    public String detailReview(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model, HttpServletRequest request){
        try{
            Review review=reviewService.getReviewById(id);
            model.addAttribute("review",review);
            return "Reviews/viewReviewModal.html";
        }catch (UsernameNotFoundException ex){
            redirectAttributes.addFlashAttribute("message",ex.getMessage());
            return "redirect:/reviews";
        }

    }

    @GetMapping("/reviews/edit/{id}")
    public String editReview(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model){
        try{

            Review review=reviewService.getReviewById(id);
            model.addAttribute("review",review);
            model.addAttribute("reviews",review);

            return "Reviews/reviewUpdateForm.html";
        }catch (UsernameNotFoundException ex){
            redirectAttributes.addFlashAttribute("message",ex.getMessage());

            return "redirect:/reviews";
        }

    }


    @PostMapping("/reviews/save")
    public String saveReview(RedirectAttributes redirectAttributes, @Valid @ModelAttribute(value = "review") Review review, Errors errors, Model model){


        Integer id= review.getId();
        log.error(String.valueOf(id));
        Review existingReview=reviewService.getReviewById(id);
        review.setReviewTime(existingReview.getReviewTime());
        review.setProduct(existingReview.getProduct());
        review.setCustomer(existingReview.getCustomer());

        //DISPLAYING ERROR MESSAGES
        if(errors.hasErrors()){
            log.error("New Review form validation failed due to : " + errors.toString());
            model.addAttribute("review",review);
            return "Reviews/reviewUpdateForm.html";
        }

        //SAVE DETAILS
        reviewService.saveReview(review);
        redirectAttributes.addFlashAttribute("message","The Review has been edited successfully");
        return "redirect:/reviews";
    }



    @GetMapping("/reviews/delete/{id}")
    public String deleteReview(@PathVariable(name="id")Integer id,Model model,RedirectAttributes redirectAttributes){
        try{
                reviewService.delete(id);
                redirectAttributes.addFlashAttribute("message","The Review ID "+id+" has been deleted successfully");
        }catch (UsernameNotFoundException ex){
            redirectAttributes.addFlashAttribute("message",ex.getMessage());
        }
        return "redirect:/reviews";
    }


}
