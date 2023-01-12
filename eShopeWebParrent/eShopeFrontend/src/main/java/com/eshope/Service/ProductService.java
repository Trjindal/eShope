package com.eshope.Service;

import com.eShope.common.entity.Product;
import com.eshope.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public Product getProductByAlias(String alias){
        Product product=productRepository.findByAlias(alias);
        if(product==null){
            throw new UsernameNotFoundException("Could not found any product with alias " + alias);
        }
        return product;
    }

    public Page<Product> search(String keyword,int pageNum){
        Pageable pageable=PageRequest.of(pageNum-1,PRODUCTS_PER_PAGE);
        return productRepository.search(keyword,pageable);
    }



}

