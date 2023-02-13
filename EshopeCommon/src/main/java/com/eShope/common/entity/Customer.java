package com.eShope.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "customers")
public class Customer {


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

    @Column(nullable = false,length = 45)
    @Size(min=3,max=45, message="First name must be at least 3 and at most 45 characters long")
    String firstName;

    @Column(nullable = false,length = 45)
    @Size(min=3,max=45, message="Last name must be at least 3 and at most 45 characters long")
    String lastName;

    @Column(nullable = false,length = 15)
    @Size(min=10,max=15, message="Phone number must be at least 10 and at most 15 numbers long")
    String phoneNumber;

    @Column(nullable = false,length = 64)
    @Size(min=3,max=45, message="Address Line 1  must be at least 3 and at most 45 characters long")
    String addressLine1;

    @Column(nullable = true,length = 64)
    @Size(min=0,max=45, message="Address Line 2 must be at most 45 characters long")
    String addressLine2;

    @Column(nullable = false,length = 45)
    @Size(min=2,max=45, message="City must be at least 2 and at most 45 characters long")
    String city;

    @Column(nullable = false,length = 45)
    @Size(min=2,max=10, message="State must be at least 2 and at most 10 characters long")
    String state;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private AuthenticationType authenticationType;

    @Column(nullable = false,length = 10)
    @Size(min=5,max=6, message="Postal Code must be at least 5 and at most 6 number long")
    String postalCode;

    Date createdTime;

    boolean enabled;

    @Column(nullable = true,length = 64)
    private String verificationCode;

    @Column(nullable = true,length = 30)
    private String resetPasswordToken;

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

