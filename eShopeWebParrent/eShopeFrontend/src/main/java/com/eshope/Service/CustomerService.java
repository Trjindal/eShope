package com.eshope.Service;


import com.eShope.common.entity.Country;
import com.eShope.common.entity.Customer;
import com.eShope.common.entity.User;
import com.eshope.Repository.CountryRepository;
import com.eshope.Repository.CustomerRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Country> listAllCountries(){
        return countryRepository.findAllByOrderByNameAsc();
    }

    public boolean isEmailUnique(String email) {
        Customer customerByEmail=customerRepository.getCustomerByEmail(email);
        return customerByEmail==null;
    }

    public void registerCustomer(Customer customer){

//        ENCODING PASSWORD
        String encodedPassword=passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encodedPassword);
        customer.setEnabled(false);
        customer.setCreatedTime(new Date());

        String verifyCode= RandomString.make(64);
        customer.setVerificationCode(verifyCode);

        customerRepository.save(customer);

    }

    public boolean verifyCustomer(String verificationCode){
        Customer customer=customerRepository.findByVerificationCode(verificationCode);

        if(customer==null||customer.isEnabled()){
            return false;
        }else {
            customerRepository.enable(customer.getId());
            return true;
        }
    }
}
