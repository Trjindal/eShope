package com.eshope.Service;

import com.eShope.common.entity.Customer;
import com.eShope.common.entity.Review;
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

}
