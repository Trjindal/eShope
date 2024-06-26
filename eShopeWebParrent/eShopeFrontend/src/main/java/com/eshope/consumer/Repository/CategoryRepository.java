package com.eshope.consumer.Repository;

import com.eShope.common.entity.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface CategoryRepository extends CrudRepository<Category,Integer> {

    @Query("Select c FROM Category c WHERE c.enabled=true ORDER BY c.name ASC")
    public List<Category> findAllEnabled();

    @Query("SELECT c FROM Category c WHERE c.enabled=true AND c.alias=?1")
    public Category findByAliasEnabled(String alias);
}
