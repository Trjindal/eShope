package com.eShope.common.entity.Order;

import com.eShope.common.entity.AbstractAddress;
import com.eShope.common.entity.Address;
import com.eShope.common.entity.Customer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Min(value = 1)
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

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<OrderDetail> orderDetails=new HashSet<>();

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL,orphanRemoval = true)
    @OrderBy("updatedTime ASC")
    private List<OrderTrack> orderTracks=new ArrayList<>();

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

    @Transient
    public String getDeliverDateOnForm(){
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(this.deliverDate);
    }

    public void setDeliverDateOnForm(){
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        try {
            this.deliverDate=dateFormat.parse("yyyy-MM-dd");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Transient
    public String getRecipientName(){
        String name=firstName;
        if(lastName!=null&&!lastName.isEmpty())
            name+=" "+lastName;
        return name;
    }

    @Transient
    public String getRecipientAddress() {
        return  addressLine1+", " +addressLine2
                +", "+city+", "+state+", "+country+". Postal Code : "+postalCode+" ." ;
    }

    @Transient
    public boolean isCOD(){
        System.out.println("HERE");
        return paymentMethod.equals(PaymentMethod.COD);
    }

    @Transient
    public boolean isPicked(){
        return hasStatus(OrderStatus.PICKED);
    }

    @Transient
    public boolean isShipped(){
        return hasStatus(OrderStatus.SHIPPING);
    }
    @Transient
    public boolean isDelivered(){
        return hasStatus(OrderStatus.DELIVERED);
    }
    @Transient
    public boolean isReturned(){
        return hasStatus(OrderStatus.RETURNED);
    }

    @Transient
    public boolean isReturnRequested(){
        return hasStatus(OrderStatus.RETURN_REQUESTED);
    }

    public boolean hasStatus(OrderStatus status){
        for(OrderTrack track:orderTracks){
            if(track.getStatus().equals(status)){
                return  true;
            }
        }
        return false;
    }

    @Transient
    public String getProductNames(){
        String productNames="";

        productNames="<ul>";

        for(OrderDetail detail:orderDetails) {
            productNames += "<li>" + detail.getProduct().getShortName() + "</li>";

        }
        productNames+="</ul>";
        return productNames;


    }

}
