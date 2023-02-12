package com.eshope.Repository;

import com.eShope.common.entity.Country;
import com.eShope.common.entity.ShippingRate;
import org.springframework.data.repository.CrudRepository;

public interface ShippingRateRepository extends CrudRepository<ShippingRate,Integer> {

    public ShippingRate findByCountryAndState(Country country,String state);

}
