package com.eshope.consumer.Service;

import com.eShope.common.entity.Address;
import com.eShope.common.entity.Customer;
import com.eShope.common.entity.ShippingRate;
import com.eshope.consumer.Repository.ShippingRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShippingRateService {

    @Autowired
    private ShippingRateRepository shippingRateRepository;

    public ShippingRate getShippingRateForCustomer(Customer customer){
        String state= customer.getState();
        if(state==null||state.isEmpty()){
            state= customer.getState();
        }
        return shippingRateRepository.findByCountryAndState(customer.getCountry(),state);
    }

    public ShippingRate getShippingRateForAddress(Address address){
        String state=address.getState();
        if(state==null||state.isEmpty()){
            state= address.getCity();
        }
        return shippingRateRepository.findByCountryAndState(address.getCountry(),state);
    }
}
