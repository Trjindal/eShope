package com.eshope.admin.Main.Repositories;

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

    @Query("SELECT p FROM Product p WHERE CONCAT(p.id,' ',p.name) LIKE %?1%")
    public Page<Product> findAll(String keyword, Pageable pageable);


    Product getProductByName(String name);

    @Transactional
    @Modifying
    @Query("Update Product p SET p.enabled=?2 where p.id=?1")
    void updateEnableStatus(Integer id, boolean enabled);

    Long countById(Integer id);
}
