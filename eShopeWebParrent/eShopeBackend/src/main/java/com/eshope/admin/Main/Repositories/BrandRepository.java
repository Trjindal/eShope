package com.eshope.admin.Main.Repositories;

import com.eShope.common.entity.Brand;
import com.eShope.common.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BrandRepository extends PagingAndSortingRepository<Brand,Integer> {


    @Query("SELECT b FROM Brand b WHERE CONCAT(b.id,' ',b.name) LIKE %?1%")
    public Page<Brand> findAll(String keyword, Pageable pageable);

    Brand getCategoryByName(String name);
}
