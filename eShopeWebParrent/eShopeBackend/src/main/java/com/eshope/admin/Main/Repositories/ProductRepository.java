package com.eshope.admin.Main.Repositories;

import com.eShope.common.entity.Product;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepository extends PagingAndSortingRepository<Product,Integer> {
}
