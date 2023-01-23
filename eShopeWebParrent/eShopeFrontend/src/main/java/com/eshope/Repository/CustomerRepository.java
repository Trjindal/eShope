package com.eshope.Repository;

import com.eShope.common.entity.Customer;
import com.eShope.common.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends CrudRepository<Customer,Integer> {

    @Query("SELECT c FROM Customer c WHERE c.email=?1")
    public Customer findByEmail(String email);

    @Query("SELECT c FROM Customer c WHERE c.verificationCode=?1")
    public Customer findByVerificationCode(String code);

    @Query("Update Customer c SET c.enabled=true ,  c.verificationCode=null WHERE c.id=?1")
    @Modifying
    public void enable(Integer id);

    @Query("SELECT c FROM Customer c WHERE c.email=:email")
    Customer getCustomerByEmail(String email);


}
