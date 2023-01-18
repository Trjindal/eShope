package com.eshope.admin.Repository;

import com.eShope.common.entity.Country;
import com.eShope.common.entity.State;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StateRepository extends CrudRepository<State,Integer> {

    public List<State> findByCountryOrderByNameAsc(Country country);

    @Query("SELECT s FROM State s LEFT JOIN s.country ON s.country.id = s.id WHERE s.name = :name")
    State findByName(String name);
}
