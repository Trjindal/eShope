package com.eShope.common.entity;

import com.eShope.common.entity.Order.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "customers")
public class Customer extends AbstractAddress{


    public Customer(Integer id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(nullable = false,unique = true,length = 45)
    @Email(message = "Please provide a valid email address" )
    @Size(min=8,max=45,message="Email must be at least 8 and at most 45 characters long")
    String email;

    @Column(nullable = false,length = 64)
    String password;

    @OneToMany(mappedBy = "customer")
    List<Review> reviews;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    private List<Address> allAddresses;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private AuthenticationType authenticationType;



    Date createdTime;

    boolean enabled;

    @Column(nullable = true,length = 64)
    private String verificationCode;

    @Column(nullable = true,length = 30)
    private String resetPasswordToken;

    @OneToMany(mappedBy = "customer", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    public String getFullName() {
        return this.firstName+" "+this.lastName;
    }

    @Transient
    public String getAddress(){
        String address="";
        if(!addressLine1.isEmpty())
            address+=addressLine1;
        if(!addressLine2.isEmpty())
            address+=", "+addressLine2;
        if(!city.isEmpty())
            address+=", "+city;
        if(state!=null &&!state.isEmpty())
            address+=", "+state;
        address+=", "+country.getName();

        return address;
    }

}

