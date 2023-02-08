package com.eShope.common.entity;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Getter
@Setter
public class ShippingRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @DecimalMin(value = "0.1",inclusive = false,message="The shipping rate cannot be 0. At least 0.01 must be specified.")
    private float rate;

    @Min(value = 1,message="The number of delivery days cannot be 0. You must provide a minimum of 1")
    private int days;


    private boolean codSupported;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @Column(nullable = false,length = 45)
    @NotBlank(message="State must not be blank")
    private String state;

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }


    @Override
    public int hashCode(){
        final int prime=31;
        int result=1;
        result=prime*result+(((id==null))?0:id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj){
        if(this==obj)
            return true;
        if(obj==null)
            return false;
        if(getClass()!=obj.getClass())
            return false;
        ShippingRate other=(ShippingRate) obj;
        if(id==null){
            if(other.id==null)
                return false;
            }else if(!id.equals(other.id))
                return false;
        return true;
    }

}
