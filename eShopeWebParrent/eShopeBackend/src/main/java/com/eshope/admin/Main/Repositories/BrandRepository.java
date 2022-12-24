package com.eshope.admin.Main.Repositories;

import com.eShope.common.entity.Brand;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BrandRepository extends PagingAndSortingRepository<Brand,Integer> {
}
