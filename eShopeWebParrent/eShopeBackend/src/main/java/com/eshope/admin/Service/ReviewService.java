package com.eshope.admin.Service;

import com.eShope.common.entity.Order.Order;
import com.eShope.common.entity.Order.OrderTrack;
import com.eShope.common.entity.Review;
import com.eShope.common.entity.User;
import com.eshope.admin.Repository.ReviewRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service


public class ReviewService {

    public static final int REVIEWS_PER_PAGE=5;

    @Autowired
    private ReviewRepository reviewRepository;

    public Page<Review> listByPage(int pageNum, String sortField, String sortDir, String keyword){
        Sort sort= Sort.by(sortField);
        sort=sortDir.equals("asc")?sort.ascending():sort.descending();

        Pageable pageable= PageRequest.of(pageNum-1,REVIEWS_PER_PAGE,sort);

        if(keyword!=null){
            return reviewRepository.findAll(keyword,pageable);
        }

        return reviewRepository.findAll(pageable);
    }

    public Review getReviewById(Integer id) {
        try{
            return reviewRepository.findById(id).get();
        }catch (NoSuchElementException ex){
            throw new UsernameNotFoundException("Could not find any review with Id "+ id);
        }
    }

    public void delete(Integer id) throws UsernameNotFoundException{
        Long countById=reviewRepository.countById(id);
        if(countById==null||countById==0){
            throw new UsernameNotFoundException("Could not found any review with such Id ");
        }
        reviewRepository.deleteById(id);
    }
    public void saveReview(Review existingReview) {
        reviewRepository.save(existingReview);
    }

}
