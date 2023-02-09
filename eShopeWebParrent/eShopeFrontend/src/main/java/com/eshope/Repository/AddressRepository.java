package com.eshope.Repository;

import com.eShope.common.entity.Address;
import com.eShope.common.entity.Customer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface AddressRepository extends CrudRepository<Address,Integer> {

    public List<Address> findByCustomer(Customer customer);

    @Query("SELECT a FROM  Address a WHERE a.id=?1 AND a.customer.id=?2")
    public Address findByIdAndCustomer(Integer addressId,Integer customerId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Address a WHERE a.id=?1 AND a.customer.id=?2")
    public void deleteByIdAndCustomer(Integer addressId,Integer customerId);

    @Modifying
    @Transactional
    @Query("UPDATE Address a SET a.defaultForShipping=true WHERE a.id=?1")
    public void setDefaultAddress(Integer id);

    Long countById(Integer id);

    @Query("SELECT COUNT(*) FROM Address a WHERE a.customer.id=?1")
    public Integer countNumberOfAddressByCustomer(Integer customerId);

    @Modifying
    @Transactional
    @Query("UPDATE Address a SET a.defaultForShipping= false "+"WHERE a.id!= ?1 AND a.customer.id= ?2")
    public void setNonDefaultForOthers(Integer defaultAddressId,Integer customerId);
}
