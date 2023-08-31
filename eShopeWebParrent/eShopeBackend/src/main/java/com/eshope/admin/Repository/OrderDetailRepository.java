package com.eshope.admin.Repository;

import com.eShope.common.entity.Order.OrderDetail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface OrderDetailRepository extends CrudRepository<OrderDetail,Integer> {

    @Query("SELECT NEW com.eShope.common.entity.Order.OrderDetail(d.product.category.name, d.quantity,"
            + " d.productCost, d.shippingCost, d.subTotal)"
            + " FROM OrderDetail d WHERE d.order.orderTime BETWEEN ?1 AND ?2")
    public List<OrderDetail> findWithCategoryAndTimeBetween(Date startTime, Date endTime);

    @Query("SELECT NEW  com.eShope.common.entity.Order.OrderDetail(d.quantity, d.product.name,"
            + " d.productCost, d.shippingCost, d.subTotal)"
            + " FROM OrderDetail d WHERE d.order.orderTime BETWEEN ?1 AND ?2")
    public List<OrderDetail> findWithProductAndTimeBetween(Date startTime, Date endTime);
}
