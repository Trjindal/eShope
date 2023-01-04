package com.eshope.admin.Repository;

import com.eShope.common.entity.Brand;
import com.eShope.common.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BrandRepository extends PagingAndSortingRepository<Brand,Integer> {


    @Query("SELECT b FROM Brand b WHERE CONCAT(b.id,' ',b.name) LIKE %?1%")
    public Page<Brand> findAll(String keyword, Pageable pageable);


    @Query("Select NEW Brand(b.id,b.name) FROM Brand b ORDER BY b.name ASC")
    public List<Brand> findAll();

    Long countById(Integer id);


    @Query("SELECT b FROM Brand b WHERE b.name=:name")
    public Brand getBrandByName(@Param("name") String name);


}
