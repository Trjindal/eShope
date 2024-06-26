package com.eshope.consumer.Repository;

import com.eShope.common.entity.Customer;
import com.eShope.common.entity.Order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Integer> {

    @Query("SELECT DISTINCT o FROM Order o JOIN o.orderDetails od JOIN od.product p WHERE o.customer.id=?2 AND (p.name LIKE %?1% OR o.orderStatus LIKE %?1%)")
    Page<Order> findAll(String keyword,Integer customerId,Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.customer.id=?1")
    Page<Order> findAll(Integer customerId,Pageable pageable);

    public Order findByIdAndCustomer(Integer id, Customer customer);
}
