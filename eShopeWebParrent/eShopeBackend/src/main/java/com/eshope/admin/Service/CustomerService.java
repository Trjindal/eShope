package com.eshope.admin.Service;


import com.eShope.common.entity.Country;
import com.eShope.common.entity.Customer;
import com.eShope.common.entity.User;
import com.eshope.admin.Repository.CountryRepository;
import com.eshope.admin.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CustomerService {

    public static final int CUSTOMERS_PER_PAGE=10;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CountryRepository countryRepository;

    public Page<Customer> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
        Sort sort= Sort.by(sortField);
        sort=sortDir.equals("asc")?sort.ascending():sort.descending();

        Pageable pageable= PageRequest.of(pageNum-1,CUSTOMERS_PER_PAGE,sort);

        if(keyword!=null){
            return customerRepository.findAll(keyword,pageable);
        }

        return customerRepository.findAll(pageable);
    }

    public void delete(Integer id) throws UsernameNotFoundException {
        Long countById=customerRepository.countById(id);
        if(countById==null||countById==0){
            throw new UsernameNotFoundException("Could not found any Customer with Id "+id);
        }
        customerRepository.deleteById(id);
    }

    public List<Customer> listAllCustomers(){
        return (List<Customer>) customerRepository.findAll(Sort.by("firstName").ascending());
    }


    public void updateCustomerEnabledStatus(Integer id, boolean enabled) {
        customerRepository.updateEnableStatus(id,enabled);
    }

    public boolean isEmailUnique(String email){
        Customer emailOfCustomer=customerRepository.getCustomerByEmail(email);
        return emailOfCustomer==null;
    }

    public Customer saveCustomer(Customer customer){

        return customerRepository.save(customer);
    }

    public Customer getCustomerById(Integer id) {
        try{
            return customerRepository.findById(id).get();
        }catch (NoSuchElementException ex){
            throw new UsernameNotFoundException("Could not find any customer with Id "+ id);
        }
    }

    public List<Country> listAllCountries(){
        return countryRepository.findAllByOrderByNameAsc();
    }
}
