package com.eShope.common.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Size;

@MappedSuperclass
@Getter
@Setter
public abstract class AbstractAddress {

    @Column(nullable = false,length = 45)
    @Size(min=3,max=45, message="First name must be at least 3 and at most 45 characters long")
    protected String firstName;

    @Column(nullable = false,length = 45)
    @Size(min=3,max=45, message="Last name must be at least 3 and at most 45 characters long")
    protected String lastName;

    @Column(nullable = false,length = 15)
    @Size(min=10,max=15, message="Phone number must be at least 10 and at most 15 numbers long")
    protected String phoneNumber;

    @Column(name="address_line1",nullable = false,length = 64)
    @Size(min=3,max=45, message="Address Line 1  must be at least 3 and at most 45 characters long")
    protected String addressLine1;

    @Column(name = "address_line2",nullable = true,length = 64)
    @Size(min=0,max=45, message="Address Line 2 must be at most 45 characters long")
    protected String addressLine2;

    @Column(nullable = false,length = 45)
    @Size(min=2,max=45, message="City must be at least 2 and at most 45 characters long")
    protected String city;

    @Column(nullable = false,length = 45)
    @Size(min=2,max=10, message="State must be at least 2 and at most 10 characters long")
    protected String state;


    @Column(nullable = false,length = 10)
    @Size(min=5,max=6, message="Postal Code must be at least 5 and at most 6 number long")
    protected String postalCode;

}
