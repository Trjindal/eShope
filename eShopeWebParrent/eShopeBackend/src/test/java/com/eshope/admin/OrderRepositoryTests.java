package com.eshope.admin;

import com.eShope.common.entity.*;
import com.eShope.common.entity.Order.Order;
import com.eShope.common.entity.Order.OrderDetail;
import com.eShope.common.entity.Order.OrderStatus;
import com.eShope.common.entity.Order.PaymentMethod;
import com.eShope.common.entity.Product.Product;
import com.eshope.admin.Repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class OrderRepositoryTests {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateOrderWithSingleProduct(){
        Customer customer=entityManager.find(Customer.class,44);
        Product product=entityManager.find(Product.class,1);

        Order order=new Order();

        order.setOrderTime(new Date());
        order.setCustomer(customer);
        order.setFirstName(customer.getFirstName());
        order.setLastName(customer.getLastName());
        order.setPhoneNumber(customer.getPhoneNumber());
        order.setAddressLine1(customer.getAddressLine1());
        order.setAddressLine2(customer.getAddressLine2());
        order.setCity(customer.getCity());
        order.setCountry(customer.getCountry().getName());
        order.setPostalCode(customer.getPostalCode());
        order.setState(customer.getState());

        order.setShippingCost(10);
        order.setProductCost(product.getCost());
        order.setTax(0);
        order.setSubTotal(product.getPrice());
        order.setTotal(product.getPrice()+10);

        order.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        order.setOrderStatus(OrderStatus.NEW);
        order.setDeliverDate(new Date());
        order.setDeliverDays(1);

        OrderDetail orderDetail=new OrderDetail();
        orderDetail.setProduct(product);
        orderDetail.setOrder(order);
        orderDetail.setProductCost(product.getCost());
        orderDetail.setShippingCost(10);
        orderDetail.setQuantity(1);
        orderDetail.setSubTotal(product.getPrice());
        orderDetail.setUnitPrice(product.getPrice());

        order.getOrderDetails().add(orderDetail);

        Order savedOrder=orderRepository.save(order);

        assertThat(savedOrder.getId()).isGreaterThan(0);

    }

    @Test
    public void testCreateNewOrderWithMultipleProducts(){

        Customer customer=entityManager.find(Customer.class,44);
        Product product1=entityManager.find(Product.class,3);
        Product product2=entityManager.find(Product.class,5);

        Order order=new Order();
        order.setOrderTime(new Date());
        order.setCustomer(customer);
        order.copyAddressFromCustomer();

        OrderDetail orderDetail1=new OrderDetail();
        orderDetail1.setProduct(product1);
        orderDetail1.setOrder(order);
        orderDetail1.setProductCost(product1.getCost());
        orderDetail1.setShippingCost(10);
        orderDetail1.setQuantity(1);
        orderDetail1.setSubTotal(product1.getPrice());
        orderDetail1.setUnitPrice(product1.getPrice());

        OrderDetail orderDetail2=new OrderDetail();
        orderDetail2.setProduct(product2);
        orderDetail2.setOrder(order);
        orderDetail2.setProductCost(product2.getCost());
        orderDetail2.setShippingCost(20);
        orderDetail2.setQuantity(2);
        orderDetail2.setSubTotal(product2.getPrice()*2);
        orderDetail2.setUnitPrice(product2.getPrice());


        order.getOrderDetails().add(orderDetail1);
        order.getOrderDetails().add(orderDetail2);

        order.setShippingCost(30);
        order.setProductCost(product1.getCost()+product2.getCost());
        order.setTax(0);
        float subTotal=product1.getPrice()+product2.getCost()*2;
        order.setSubTotal(subTotal);
        order.setTotal(subTotal+30);

        order.setPaymentMethod(PaymentMethod.COD);
        order.setOrderStatus(OrderStatus.PROCESSING);
        order.setDeliverDate(new Date());
        order.setDeliverDays(3);

        Order savedOrder=orderRepository.save(order);

        assertThat(savedOrder.getId()).isGreaterThan(0);

    }

    @Test
    public void testListOrders(){
        Iterable<Order> orders=orderRepository.findAll();

        assertThat(orders).hasSizeGreaterThan(0);
    }

    @Test
    public void testUpdateOrder(){
        Integer orderId=2;
        Order order=orderRepository.findById(orderId).get();

        order.setOrderTime(new Date());
        order.setOrderStatus(OrderStatus.SHIPPING);

        Order updatedOrder=orderRepository.save(order);


        assertThat(updatedOrder.getOrderStatus()).isEqualTo(OrderStatus.SHIPPING);
    }

    @Test
    public void testGetOrder(){
        Integer orderId=3;
        Order order=orderRepository.findById(orderId).get();

        assertThat(order).isNotNull();

    }

    @Test
    public void testDeleteOrder(){
        Integer orderId=3;
        orderRepository.deleteById(orderId);

        Optional<Order> result=orderRepository.findById(orderId);
        assertThat(result).isNotPresent();
    }
}
