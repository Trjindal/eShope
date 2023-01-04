package com.eshope.admin.Repository;

import com.eShope.common.entity.Brand;
import com.eShope.common.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface ProductRepository extends PagingAndSortingRepository<Product,Integer> {

    @Query("SELECT p FROM Product p WHERE p.name LIKE %?1%"+"OR p.alias LIKE %?1% "
            +"OR p.shortDescription LIKE %?1% "+"OR p.fullDescription LIKE %?1% "
            +"OR p.brand LIKE %?1% "+"OR p.category LIKE %?1% ")
    public Page<Product> findAll(String keyword, Pageable pageable);


    Product getProductByName(String name);

    @Transactional
    @Modifying
    @Query("Update Product p SET p.enabled=?2 where p.id=?1")
    void updateEnableStatus(Integer id, boolean enabled);

    Long countById(Integer id);

    @Transactional
    @Modifying
    @Query("Update Product p SET p.name=p.name,p.alias=p.alias,p.shortDescription=p.shortDescription where p.id=?1")
    Product updateProductById(Product product);
}
