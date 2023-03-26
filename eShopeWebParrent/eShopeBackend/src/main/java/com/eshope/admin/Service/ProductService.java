package com.eshope.admin.Service;


import com.eShope.common.entity.Brand;
import com.eShope.common.entity.Product.Product;

import com.eshope.admin.Repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.NoSuchElementException;


@Service
@Slf4j
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    public static final int PRODUCTS_PER_PAGE = 8;

    public Page<Product> listByPage(int pageNum, String sortField, String sortDir, String keyword,Integer categoryId) {

        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE, sort);

        if (keyword != null&&!keyword.isEmpty()) {
            //        SEARCHING FILTER AND CATEGORY TOGETHER
            if(categoryId!=null & categoryId>0){
                String categoryIdMatch="-"+String.valueOf(categoryId)+"-";
                return productRepository.searchInCategory(categoryId,categoryIdMatch,keyword,pageable);
            }
//            ONLY KEYWORD SEARCH
            return productRepository.findAll(keyword, pageable);
        }

        //            ONLY CATEGORY SEARCH
        if(categoryId!=null & categoryId>0){
            String categoryIdMatch="-"+String.valueOf(categoryId)+"-";
            return productRepository.findAllCategory(categoryId,categoryIdMatch,pageable);
        }



        return productRepository.findAll(pageable);
    }


    public Page<Product> searchProducts(int pageNum, String sortField, String sortDir, String keyword){
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE, sort);

        if (keyword != null&&!keyword.isEmpty()) {
            return productRepository.searchProductsByName(keyword, pageable);
        }

        return productRepository.findAll(pageable);
    }



    public Product save(Product product){


        //SETTING CREATED TIME
        if(product.getId()==null){
            product.setCreatedTime(new Date());
        }

        //SETTING ALIAS WITH HYPHEN
        if(product.getAlias()==null || product.getAlias().isEmpty()){
            String defaultAlias=product.getName().replaceAll(" ","-");
            product.setAlias(defaultAlias);
        }else {
            product.setAlias(product.getAlias().replaceAll(" ","-"));
        }

        product.setUpdateTime(new Date());
        Product updatedProduct= productRepository.save(product);
        productRepository.updateReviewCountAndAverageRating(updatedProduct.getId());
        return updatedProduct;
    }



    public boolean isNameUnique(String name) {
        Product productByName = productRepository.getProductByName(name);
        return productByName == null;
    }

    public void updateProductEnabledStatus(Integer id, boolean enabled) {
        productRepository.updateEnableStatus(id,enabled);
    }

    public void delete(Integer id) throws UsernameNotFoundException {
        Long countById=productRepository.countById(id);
        if(countById==null||countById==0){
            throw new UsernameNotFoundException("Could not found any product with Id "+id);
        }
        productRepository.deleteById(id);
    }

    public Product getProductById(Integer id) {
        try{
            return productRepository.findById(id).get();
        }catch (NoSuchElementException ex){
            throw new UsernameNotFoundException("Could not find any product with Id "+ id);
        }
    }


}
