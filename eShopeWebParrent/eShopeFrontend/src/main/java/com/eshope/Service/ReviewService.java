package com.eshope.Service;

import com.eShope.common.entity.Customer;
import com.eShope.common.entity.Order.OrderStatus;
import com.eShope.common.entity.Product.Product;
import com.eShope.common.entity.Review;
import com.eshope.Repository.OrderDetailRepository;
import com.eshope.Repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    public static final int REVIEWS_PER_PAGE = 5;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public Page<Review> listByPage(Customer customer, int pageNum, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, REVIEWS_PER_PAGE, sort);

        if (keyword != null) {
            Integer id= customer.getId();
            return reviewRepository.findByCustomer(id,keyword, pageable);
        }

        return reviewRepository.findByCustomer(customer.getId(),pageable);
    }

    public Review getReviewByCustomerAndId(Customer customer, Integer reviewId){
        Review review=reviewRepository.findByCustomerAndId(customer.getId(),reviewId);
        if(review==null)
            throw new UsernameNotFoundException("Customer does not have any reviews with ID "+reviewId);
        return review;
    }

    public Page<Review> list3MostRecentReviewsByProduct(Product product){
        Sort sort=Sort.by("reviewTime").descending();
        Pageable pageable=PageRequest.of(0,3,sort);

        return reviewRepository.findByProduct(product,pageable);
    }


    public Page<Review> listByProduct(Product product, int pageNum, String sortField, String sortDir){
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, REVIEWS_PER_PAGE, sort);

        return reviewRepository.findByProduct(product,pageable);
    }

    public boolean didCustomerReviewProduct(Customer customer,Integer productId){
        Long count=reviewRepository.countByCustomerAndProduct(customer.getId(),productId);
        return count>0;
    }

    public boolean canCustomerReviewProduct(Customer customer,Integer productId){
       Long count = orderDetailRepository.countByProductAndCustomerAndOrderStatus(productId,customer.getId(), OrderStatus.DELIVERED);
        return count>0;
    }

}
