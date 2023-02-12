package com.eShope.common.entity;

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
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(nullable = false,length = 45)
    @Size(min=3,max=45, message="First name must be at least 3 and at most 45 characters long")
    private String firstName;

    @Column(nullable = false,length = 45)
    @Size(min=3,max=45, message="Last name must be at least 3 and at most 45 characters long")
    private String lastName;

    @Column(nullable = false,length = 15)
    @Size(min=10,max=15, message="Phone number must be at least 10 and at most 15 numbers long")
    private String phoneNumber;

    @Column(nullable = false,length = 64)
    @Size(min=3,max=45, message="Address Line 1  must be at least 3 and at most 45 characters long")
    private String addressLine1;

    @Column(nullable = true,length = 64)
    @Size(min=0,max=45, message="Address Line 2 must be at most 45 characters long")
    private String addressLine2;

    @Column(nullable = false,length = 45)
    @Size(min=2,max=45, message="City must be at least 2 and at most 45 characters long")
    private String city;

    @Column(nullable = false,length = 45)
    @Size(min=2,max=10, message="State must be at least 2 and at most 10 characters long")
    private String state;

    @Column(nullable = false,length = 45)
    private String country;

    @Column(nullable = false,length = 10)
    private String postalCode;

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

}
