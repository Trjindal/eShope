package com.eshope.admin.Repository;

import com.eShope.common.entity.Country;
import com.eShope.common.entity.State;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StateRepository extends CrudRepository<State,Integer> {

    public List<State> findByCountryOrderByNameAsc(Country country);
}
