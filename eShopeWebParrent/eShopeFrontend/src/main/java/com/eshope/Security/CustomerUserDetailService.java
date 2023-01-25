package com.eshope.Security;

import com.eShope.common.entity.Customer;
import com.eshope.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomerUserDetailService implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer=customerRepository.getCustomerByEmail(email);
        if(customer==null)
            throw new UsernameNotFoundException("Could not find customer with email : "+email);
        return new CustomerUserDetails(customer);
    }

}
