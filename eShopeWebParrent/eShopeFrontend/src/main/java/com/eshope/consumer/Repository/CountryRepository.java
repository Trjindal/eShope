package com.eshope.consumer.Repository;

import com.eShope.common.entity.Country;
import com.eShope.common.entity.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CountryRepository extends CrudRepository<Country,Integer> {
    public List<Country> findAllByOrderByNameAsc();

    @Query("SELECT c FROM Country c WHERE c.name = :name")
    public Country findByName(String name);

    @Query("Select c FROM Country c where c.code=?1")
    public Country findByCode(String code);


}
