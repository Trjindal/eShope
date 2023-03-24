package com.eShope.common.entity;

import com.eShope.common.entity.Product.Product;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Getter
@Setter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(length = 128,nullable = false)
    @NotBlank(message="Headline must not be blank")
    @Size(min=3,max = 128,message="Headline must be at least 3 characters long and smaller than 128")
    private String headline;

    @Column(length = 300,nullable = false)
    @NotBlank(message="Comment must not be blank")
    @Size(min=3,max = 128,message="Comment must be at least 3 characters long and smaller than 300")
    private String comment;

    private int rating;

    @Column(nullable = false)
    private Date reviewTime;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;


    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", headline='" + headline + '\'' +
                ", comment='" + comment + '\'' +
                ", rating=" + rating +
                ", reviewTime=" + reviewTime +
                ", product=" + product.getShortName() +
                ", customer=" + customer.getFullName() +
                '}';
    }
}
