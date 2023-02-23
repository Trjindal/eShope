package com.eshope.Service;

import com.eShope.common.entity.*;
import com.eShope.common.entity.Order.Order;
import com.eShope.common.entity.Order.OrderDetail;
import com.eShope.common.entity.Order.OrderStatus;
import com.eShope.common.entity.Order.PaymentMethod;
import com.eShope.common.entity.Product.Product;
import com.eshope.PoJo.CheckoutInfo;
import com.eshope.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(Customer customer, Address address, List<CartItem> cartItems, PaymentMethod paymentMethod, CheckoutInfo checkoutInfo){

        Order newOrder=new Order();
        newOrder.setOrderTime(new Date());
        newOrder.setOrderStatus(OrderStatus.NEW);
        newOrder.setCustomer(customer);
        newOrder.setProductCost(checkoutInfo.getProductCost());
        newOrder.setSubTotal(checkoutInfo.getProductTotal());
        newOrder.setShippingCost(checkoutInfo.getShippingCostTotal());
        newOrder.setTax(0.0f);
        newOrder.setTotal(checkoutInfo.getPaymentTotal());
        newOrder.setPaymentMethod(paymentMethod);
        newOrder.setDeliverDays(checkoutInfo.getDeliverDays());
        newOrder.setDeliverDate(checkoutInfo.getDeliverDate());

        if(address==null){
            newOrder.copyAddressFromCustomer();
        }else {
            newOrder.copyShippingAddress(address);
        }

        Set<OrderDetail> orderDetails=newOrder.getOrderDetails();

        for(CartItem item:cartItems){
            Product product=item.getProduct();

            OrderDetail orderDetail=new OrderDetail();
            orderDetail.setOrder(newOrder);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(item.getQuantity());
            orderDetail.setUnitPrice(product.getDiscountPrice());
            orderDetail.setProductCost(product.getCost());
            orderDetail.setSubTotal(item.getSubTotal());
            orderDetail.setShippingCost(item.getShippingCost());

            orderDetails.add(orderDetail);
        }

        return orderRepository.save(newOrder);

    }
}
