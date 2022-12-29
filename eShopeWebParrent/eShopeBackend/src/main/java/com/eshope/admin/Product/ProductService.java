package com.eshope.admin.Product;


import com.eShope.common.entity.Product;

import com.eshope.admin.Main.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    public static final int PRODUCTS_PER_PAGE = 5;

    public Page<Product> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE, sort);

        if (keyword != null) {
            return productRepository.findAll(keyword, pageable);
        }

        return productRepository.findAll(pageable);
    }

}
