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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class OrderService {

    public static final int ORDERS_PER_PAGE = 5;
    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(Customer customer, Address address, List<CartItem> cartItems, PaymentMethod paymentMethod, CheckoutInfo checkoutInfo){

        Order newOrder=new Order();
        newOrder.setOrderTime(new Date());
        if(paymentMethod.equals(PaymentMethod.PAYPAL)){
            newOrder.setOrderStatus(OrderStatus.PAID);
        }else {
            newOrder.setOrderStatus(OrderStatus.NEW);
        }
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


    public Page<Order> listByPage(Customer customer,int pageNum, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, ORDERS_PER_PAGE, sort);

        if (keyword != null) {
            Integer id= customer.getId();
            return orderRepository.findAll(keyword,id, pageable);
        }

        return orderRepository.findAll(customer.getId(),pageable);
    }

}
