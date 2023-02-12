package com.eshope.admin.Repository;

import com.eShope.common.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OrderRepository extends PagingAndSortingRepository<Order,Integer> {

    @Query("SELECT o FROM Order o WHERE CONCAT(o.firstName,' ',o.lastName,' ',o.phoneNumber,' ',o.addressLine1,' ',o.addressLine2,' '," +
            "o.postalCode,' ',o.city,' ',o.state,' ',o.country,' ',o.paymentMethod,' ',o.orderStatus,' ',o.customer.firstName,' ',o.customer.lastName) LIKE %?1%")
    public Page<Order> findAll(String keyword, Pageable pageable);

    public Long countById(Integer id);
}
