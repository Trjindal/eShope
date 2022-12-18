package com.eshope.admin.Main.Repositories;

import com.eShope.common.entity.Category;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends PagingAndSortingRepository<Category,Integer> {


}
