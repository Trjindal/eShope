package com.eshope.Service;

import com.eShope.common.entity.Product;
import com.eshope.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    public static final int PRODUCTS_PER_PAGE=12;

    @Autowired
    private ProductRepository productRepository;

    public Page<Product> listByCategory(int pageNum,Integer categoryId){
        String categoryIdMatch="-"+String.valueOf(categoryId)+"-";
        Pageable pageable= PageRequest.of(pageNum-1,PRODUCTS_PER_PAGE);

        return productRepository.listByCategory(categoryId,categoryIdMatch,pageable);
    }


}

