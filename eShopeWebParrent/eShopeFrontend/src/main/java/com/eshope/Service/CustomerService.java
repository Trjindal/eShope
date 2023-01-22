package com.eshope.Service;


import com.eShope.common.entity.Country;
import com.eShope.common.entity.Customer;
import com.eShope.common.entity.User;
import com.eshope.Repository.CountryRepository;
import com.eshope.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CountryRepository countryRepository;

    public List<Country> listAllCountries(){
        return countryRepository.findAllByOrderByNameAsc();
    }

    public boolean isEmailUnique(String email) {
        Customer customerByEmail=customerRepository.getCustomerByEmail(email);
        return customerByEmail==null;
    }
}
