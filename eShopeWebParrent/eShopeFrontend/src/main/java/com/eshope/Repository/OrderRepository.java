package com.eshope.Repository;

import com.eShope.common.entity.Order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Integer> {

}
