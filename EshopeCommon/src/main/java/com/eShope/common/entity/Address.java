package com.eShope.common.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@Table(name="addresses")
public class Address {


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

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @Column(nullable = false,length = 10)
    @Size(min=5,max=6, message="Postal Code must be at least 5 and at most 6 number long")
    private String postalCode;

    @Column(name = "default_address")
    private boolean defaultForShipping;

   @ManyToOne
   @JoinColumn(name = "customer_id")
   private Customer customer;

    @Override
    public String toString() {
        return  firstName + ' ' + lastName +", "+addressLine1+", " +addressLine2
                +", "+city+", "+state+", "+country.getName()+". Postal Code : "+postalCode+". Phone Number : "+phoneNumber;
    }

    public String getFullName() {
        return this.firstName+" "+this.lastName;
    }

    @Transient
    public String getDetails(){
        String details="";
        if(addressLine1!=null&&!addressLine1.isEmpty())
            details+=addressLine1;
        if(addressLine2!=null&&!addressLine2.isEmpty())
            details+=", "+addressLine2;
        if(!city.isEmpty())
            details+=", "+city;
        if(state!=null &&!state.isEmpty())
            details+=", "+state;
        details+=", "+country.getName();

        return details;
    }
}
