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
public class Address extends AbstractAddress{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;


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
