package com.eshope.Service;


import com.eShope.common.entity.AuthenticationType;
import com.eShope.common.entity.Country;
import com.eShope.common.entity.Customer;
import com.eshope.Repository.CountryRepository;
import com.eshope.Repository.CustomerRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
    @Lazy
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
        customer.setAuthenticationType(AuthenticationType.DATABASE);

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

    public void updateAuthenticationType(Customer customer, AuthenticationType authenticationType) {
        if (!customer.getAuthenticationType().equals(authenticationType))
            customerRepository.updateAuthenticationType(customer.getId(), authenticationType);

    }

    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public void addNewCustomerUponOAuthLogin(String name, String email,String countryCode) {

        Customer customer=new Customer();
        customer.setEmail(email);
        setName(name,customer);
        customer.setEnabled(true);
        customer.setCreatedTime(new Date());
        customer.setAuthenticationType(AuthenticationType.GOOGLE);
        customer.setPassword("");
        customer.setAddressLine1("");
        customer.setAddressLine2("");
        customer.setCity("");
        customer.setState("");
        customer.setPostalCode("");
        customer.setPhoneNumber("");
        customer.setCountry(countryRepository.findByCode(countryCode));
        customerRepository.save(customer);
    }

    private void setName(String name,Customer customer){
        String[] nameArray=name.split(" ");
        if(nameArray.length<2){
            customer.setFirstName(name);
            customer.setLastName("");
        }else{
            String firstName=nameArray[0];
            customer.setFirstName(firstName);

            String lastName=name.replace(firstName,"");
            customer.setLastName(lastName);
        }
    }


}
