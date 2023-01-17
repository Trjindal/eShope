package com.eshope.admin.Repository;

import com.eShope.common.entity.Country;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CountryRepository extends CrudRepository<Country,Integer> {
    public List<Country> findAllByOrderByNameAsc();
}
