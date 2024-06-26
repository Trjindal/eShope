package com.eshope.consumer.Repository;


import com.eShope.common.entity.Product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;

public interface ProductRepository extends PagingAndSortingRepository<Product,Integer> {

    @Query("SELECT p FROM Product p WHERE p.enabled=true "+"AND (p.category.id=?1 OR p.category.allParentIDs LIKE %?2%)"+"ORDER BY p.name ASC")
    public Page<Product> listByCategory(Integer categoryId, String categoryIDMatch, Pageable pageable);


    @Query("SELECT p FROM Product p WHERE p.enabled=true AND p.brand.id=?1")
    public Page<Product> listByBrand(Integer brandId, Pageable pageable);

    @Query(value = "SELECT * FROM products WHERE enabled=true AND "
        +"MATCH(name,short_description,full_description) AGAINST (?1)",
            nativeQuery = true
    )
    public Page<Product> search(String keyboard,Pageable pageable);

    public Product findByAlias(String alias);

    @Transactional
    @Modifying
    @Query("UPDATE Product p SET p.averageRating = COALESCE((SELECT AVG(r.rating) FROM Review r WHERE r.product.id= ?1 ),0), "
            +" p.reviewCount = (SELECT COUNT(r.id) FROM Review r WHERE r.product.id = ?1) WHERE p.id = ?1 ")
    public void updateReviewCountAndAverageRating(Integer productId);

}
