package com.eshope.admin.Repository;

import com.eShope.common.entity.Category;
import com.eShope.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends PagingAndSortingRepository<Category,Integer> {

    @Query("SELECT c FROM Category c WHERE CONCAT(c.id,' ',c.name,' ',c.alias) LIKE %?1% AND c.id<>0")
    public Page<Category> findAll(String keyword, Pageable pageable);

    @Query("SELECT c FROM Category c WHERE c.name=:name AND c.id <> 0")
    public Category getCategoryByName(@Param("name") String name);

    public long countById(Integer id);

    @Transactional
    @Modifying
    @Query("Update Category c SET c.enabled=?2 where c.id=?1")
    public void updateEnableStatus(Integer id,boolean enabled);

    @Query("SELECT c FROM Category c WHERE c.alias=:alias")
    Category getCategoryByAlias(String alias);

    @Query("SELECT c FROM Category c WHERE c.id<>0")
    Page<Category> findAllNonZeroCategory(Pageable pageable);

    @Query("SELECT c FROM Category c WHERE c.id<>0")
    Iterable<Category> findAllNonZeroCategory();


}
