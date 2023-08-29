package com.eshope.consumer.Service;

import com.eShope.common.entity.Product.Product;
import com.eShope.common.entity.Question;
import com.eshope.consumer.Repository.ProductRepository;
import com.eshope.consumer.Repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class QuestionService {
    public static final int QUESTIONS_PER_PAGE_FOR_PUBLIC_LISTING = 10;
    public static final int QUESTIONS_PER_PAGE_FOR_CUSTOMER = 4;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ProductRepository productRepository;


    public Page<Question> list3MostRecentQuestionsByProduct(Product product){
        Sort sort=Sort.by("answerTime").descending();
        Pageable pageable=PageRequest.of(0,3,sort);

        return questionRepository.findByProduct(product.getId(),pageable);
    }

    public Page<Question> listByProduct(String productAlias, int pageNum, String sortField, String sortDir){
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, QUESTIONS_PER_PAGE_FOR_PUBLIC_LISTING, sort);

        return questionRepository.findQuestionByProductAlias(productAlias,pageable);
    }
}
