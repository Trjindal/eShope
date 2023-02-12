package com.eshope.Service;


import com.eShope.common.entity.Address;
import com.eShope.common.entity.Customer;
import com.eShope.common.entity.ShippingRate;
import com.eshope.Repository.AddressRepository;
import com.eshope.Repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public List<Address> listAddressBook(Customer customer) {
        return addressRepository.findByCustomer(customer);
    }

    public void save(Address address) {
        addressRepository.save(address);
    }
    public void delete(Integer id) throws UsernameNotFoundException {
        Long countById=addressRepository.countById(id);
        if(countById==null||countById==0){
            throw new UsernameNotFoundException("Could not found any Shipping Address with Id "+id);
        }
        addressRepository.deleteById(id);
    }

    public Address getAddressById(Integer id) {
        try{
            return addressRepository.findById(id).get();
        }catch (NoSuchElementException ex){
            throw new UsernameNotFoundException("Could not find any Shipping Address with Id"+ id);
        }
    }


    public Integer countAddress(Integer id) {
        Integer count=addressRepository.countNumberOfAddressByCustomer(id);
        return count!=null?count+1:0;
    }

    public void savePrimaryAddress(Customer customer, Customer existingCustomer) {
        existingCustomer.setAddressLine1(customer.getAddressLine1());
        existingCustomer.setAddressLine2(customer.getAddressLine2());
        existingCustomer.setCountry(customer.getCountry());
        existingCustomer.setState(customer.getState());
        existingCustomer.setCity(customer.getCity());
        existingCustomer.setPostalCode(customer.getPostalCode());

        customerRepository.save(existingCustomer);
    }

    public void setDefaultAddress(Integer defaultAddressId,Integer customerId){
        try{
            if(defaultAddressId>0){
                addressRepository.setDefaultAddress(defaultAddressId);
            }
            addressRepository.setNonDefaultForOthers(defaultAddressId,customerId);
        }catch (NoSuchElementException ex){
            throw new UsernameNotFoundException("Could not find any Shipping Address with Id"+ defaultAddressId);
        }
    }

    public Address getDefaultAddress(Customer customer){
        return addressRepository.findDefaultAddressByCustomer(customer.getId());
    }


}
