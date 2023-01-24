package com.eshope.admin.Repository;

import com.eShope.common.entity.Customer;
import com.eShope.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;

public interface CustomerRepository extends PagingAndSortingRepository<Customer,Integer> {


    @Query("SELECT c FROM Customer c WHERE CONCAT(c.addressLine1,' ',c.addressLine2,' ',c.city,' ',c.state,' ',c.country.name,' ',c.postalCode,' ',c.email,' ',c.firstName,' ',c.lastName) LIKE %?1%")
    public Page<Customer> findAll(String keyword, Pageable pageable);


    Long countById(Integer id);

    @Transactional
    @Modifying
    @Query("Update Customer c SET c.enabled=?2 where c.id=?1")
    public void updateEnableStatus(Integer id,boolean enabled);

    public Customer getCustomerByEmail(String email);
}
