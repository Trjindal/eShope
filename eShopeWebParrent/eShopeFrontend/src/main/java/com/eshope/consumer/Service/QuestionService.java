package com.eshope.consumer.Service;

import com.eShope.common.entity.Customer;
import com.eShope.common.entity.Product.Product;
import com.eShope.common.entity.Question;
import com.eshope.consumer.Repository.ProductRepository;
import com.eshope.consumer.Repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class QuestionService {
    public static final int QUESTIONS_PER_PAGE_FOR_PUBLIC_LISTING = 10;
    public static final int QUESTIONS_PER_PAGE_FOR_CUSTOMER = 4;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ProductRepository productRepository;


    public void saveNewQuestion(Question question, Customer asker,
                                Integer productId) throws UsernameNotFoundException {
        Optional<Product> productById = productRepository.findById(productId);
        if (!productById.isPresent()) {
            throw new UsernameNotFoundException("Could not find product with ID " + productId);
        }
        question.setAskTime(new Date());
        question.setProduct(productById.get());
        question.setAsker(asker);

        questionRepository.save(question);
    }
    public Page<Question> list3MostRecentQuestionsByProduct(Product product){
        Sort sort=Sort.by("answerTime").descending();
        Pageable pageable=PageRequest.of(0,3,sort);

        return questionRepository.findByProduct(product.getId(),pageable);
    }
    public int getNumberOfAnsweredQuestions(Integer productId) {
        return questionRepository.countAnsweredQuestions(productId);
    }

    public int getNumberOfQuestions(Integer productId) {
        return questionRepository.countApprovedQuestions(productId);
    }

    public Page<Question> listQuestionsByProduct(String productAlias, int pageNum, String sortField, String sortDir){
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, QUESTIONS_PER_PAGE_FOR_PUBLIC_LISTING, sort);

        return questionRepository.findQuestionByProductAlias(productAlias,pageable);
    }

    public Page<Question> listQuestionsByCustomer(Customer customer, String keyword, int pageNum,
                                                  String sortField, String sortDir) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, QUESTIONS_PER_PAGE_FOR_CUSTOMER, sort);

        if (keyword != null) {
            return questionRepository.findByCustomer(customer.getId(), keyword, pageable);
        }

        return questionRepository.findByCustomer(customer.getId(), pageable);
    }

    public Question getByCustomerAndId(Customer customer, Integer questionId) {
        return questionRepository.findByCustomerAndId(customer.getId(), questionId);
    }
}
