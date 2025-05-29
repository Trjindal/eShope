package com.eshope.admin;

import com.eShope.common.entity.Customer;
import com.eShope.common.entity.Product.Product;
import com.eShope.common.entity.Review;
import com.eshope.admin.Repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    public void testCreateReview() {

        Integer productId = 23;
        Product product = new Product(productId);

        Integer customerId = 44;
        Customer customer = new Customer(customerId);

        Review review = new Review();
        review.setHeadline("Perfect for my needs. Loving it!");
        review.setComment("Nice to have: wireless remote, iOS app, GPS...");
        review.setReviewTime(new Date());
        review.setRating(2);
        review.setCustomer(customer);
        review.setProduct(product);

        Review savedReview = reviewRepository.save(review);

        assertThat(savedReview).isNotNull();
        assertThat(savedReview.getId()).isGreaterThan(0);
    }

    @Test
    public void testListReviews() {
        List<Review> listReviews = reviewRepository.findAll();
        listReviews.forEach(System.out::println);

        assertThat(listReviews.size()).isGreaterThan(0);
    }

    @Test
    public void testGetReviewById() {
        Integer id = 1;
        Review review = reviewRepository.findById(id).get();

        assertThat(review).isNotNull();

        System.out.println(review);
    }

    @Test
    public void testUpdateReviewById() {
        Integer id = 1;
        Review review = reviewRepository.findById(id).get();

        String headline = "An awesome camera at an awesome price";
        String comment = "Overall great camera and is highly capable...";

        review.setHeadline(headline);
        review.setComment(comment);

        Review updatedReview = reviewRepository.save(review);

        assertThat(updatedReview.getHeadline()).isEqualTo(headline);
        assertThat(updatedReview.getComment()).isEqualTo(comment);
    }

    @Test
    public void testDeleteReviewById() {
        Integer id = 1;
        reviewRepository.deleteById(id);

        Optional<Review> findById = reviewRepository.findById(id);

        assertThat(findById).isNotPresent();
    }
}