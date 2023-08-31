package com.eshope.consumer.Service;

import com.eShope.common.entity.Product.Product;
import com.eshope.consumer.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;


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

    public Page<Product> listByBrand(int pageNum, Integer brandId) {
        Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE);

        return productRepository.listByBrand(brandId, pageable);
    }

    public Product getProductByAlias(String alias){
        Product product=productRepository.findByAlias(alias);
        if(product==null){
            throw new UsernameNotFoundException("Could not found any product with alias " + alias);
        }
        return product;
    }

    public Product getProductById(Integer id){
        Product product=productRepository.findById(id).get();
        if(product==null){
            throw new NoSuchElementException("Could not found any product with Id " + id);
        }
        return product;
    }

    public Page<Product> search(String keyword,int pageNum){
        Pageable pageable=PageRequest.of(pageNum-1,PRODUCTS_PER_PAGE);
        return productRepository.search(keyword,pageable);
    }



}

