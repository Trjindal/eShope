package com.eShope.common.entity.Order;

import com.eShope.common.entity.AbstractAddress;
import com.eShope.common.entity.Address;
import com.eShope.common.entity.Customer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order extends AbstractAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(nullable = false,length = 45)
    private String country;


    private Date orderTime;

    private float shippingCost;
    private float productCost;
    private float subTotal;
    private float tax;
    private float total;

    private int deliverDays;
    private Date deliverDate;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    private Set<OrderDetail> orderDetails=new HashSet<>();

    public Customer getCustomer() {
        return customer;
    }

    public String getFullName(){
        return firstName+" "+lastName;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void copyAddressFromCustomer(){
        setFirstName(customer.getFirstName());
        setLastName(customer.getLastName());
        setPhoneNumber(customer.getPhoneNumber());
        setAddressLine1(customer.getAddressLine1());
        setAddressLine2(customer.getAddressLine2());
        setCity(customer.getCity());
        setCountry(customer.getCountry().getName());
        setPostalCode(customer.getPostalCode());
        setState(customer.getState());
    }

    public String destination(){
        return this.country+", "+this.state+", "+this.city;
    }

    public void copyShippingAddress(Address address) {
        setFirstName(address.getFirstName());
        setLastName(address.getLastName());
        setPhoneNumber(address.getPhoneNumber());
        setAddressLine1(address.getAddressLine1());
        setAddressLine2(address.getAddressLine2());
        setCity(address.getCity());
        setCountry(address.getCountry().getName());
        setPostalCode(address.getPostalCode());
        setState(address.getState());
    }

    @Transient
    public String getShippingAddress() {
        return  firstName + ' ' + lastName +", "+addressLine1+", " +addressLine2
                +", "+city+", "+state+", "+country+". Postal Code : "+postalCode+". Phone Number : "+phoneNumber;
    }
}
