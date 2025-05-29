package com.eshope.consumer.Controller;

import com.eShope.common.entity.Customer;
import com.eShope.common.entity.Product.Product;
import com.eShope.common.entity.Review;
import com.eshope.consumer.Service.CustomerService;
import com.eshope.consumer.Service.ProductService;
import com.eshope.consumer.Service.ReviewService;
import com.eshope.consumer.Utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ReviewController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

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


    @GetMapping("ratings/{productAlias}")
    public String listReviewByFirstProductPage(@PathVariable(name="productAlias") String productAlias,Model model){
        return listReviewByProductPage(1,productAlias,model,"reviewTime","desc");
    }

    @GetMapping("/ratings/{productAlias}/page/{pageNum}")
    public String listReviewByProductPage(@PathVariable(name = "pageNum") int pageNum,@PathVariable(name = "productAlias") String productAlias, Model model, @Param("sortField") String sortField, @Param("sortDir") String sortDir) {

        Product product=null;

        try{
            product=productService.getProductByAlias(productAlias);
        }catch (UsernameNotFoundException e){
            return "error.html";
        }
        Page<Review> page = reviewService.listByProduct(product,pageNum, sortField, sortDir);
        List<Review> listReviews = page.getContent();

        long startCount = (pageNum - 1) * reviewService.REVIEWS_PER_PAGE + 1;
        long endCount = startCount + reviewService.REVIEWS_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        model.addAttribute("productAlias",productAlias);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startCount", startCount);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listAllReviews", listReviews);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        return "Reviews/reviewsByProduct";
    }

    @GetMapping("/write_review/product/{productId}")
    public String showReviewForm(@PathVariable("productId") Integer productId,Model model,HttpServletRequest request){

        Review review=new Review();

        Product product =null;

        try{
            product=productService.getProductById(productId);
        }catch (UsernameNotFoundException ex){
            return "error/404";
        }
        model.addAttribute("product",product);
        model.addAttribute("review",review);
//        TO CHECK IF CUSTOMER HAS REVIEWED THE PRODUCT OR NOT
        Customer customer=getAuthenticatedCustomer(request);
        if(customer!=null) {
            boolean customerReviewed = reviewService.didCustomerReviewProduct(customer, product.getId());
            if(customerReviewed){
                model.addAttribute("customerReviewed",true);
            }else{
                boolean customerCanReview=reviewService.canCustomerReviewProduct(customer,product.getId());
                if(customerCanReview){
                    model.addAttribute("customerCanReview",true);
                }else{
                    model.addAttribute("noReviewPermission",true);
                }

            }
        }
        return "reviews/reviewForm";
    }


    @PostMapping("/post_review")
    public String saveReview(Model model,Review review,Integer productId,HttpServletRequest request){
        Customer customer=getAuthenticatedCustomer(request);
        Product product=null;
        try {
            product=productService.getProductById(productId);
        }catch (UsernameNotFoundException ex){
            return "error/404";
        }
        review.setProduct(product);
        review.setCustomer(customer);
        Review savedReview=reviewService.save(review);

        model.addAttribute("review",savedReview);

        return "reviews/reviewDone";
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
