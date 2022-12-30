package com.eShope.common.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;


@Getter
@Setter
@Entity
@Table(name="products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true,length = 256,nullable = false)
    @NotBlank(message="Name must not be blank")
    @Size(min=3, message="Name must be at least 3 characters long")
    private String name;

    @Column(unique = true,length = 256,nullable = false)
    @NotBlank(message="Alias must not be blank")
    @Size(min=3, message="Alias must be at least 3 characters long")
    private String alias;

    @Column(length = 512,nullable = false)
    @NotBlank(message="Short Description must not be blank")
    @Size(min=10, message="Short Description must be at least 10 characters long")
    private String shortDescription;

    @Column(length = 4096,nullable = false)
    @NotBlank(message="Full Description must not be blank")
    @Size(min=10, message="Full Description must be at least 10 characters long")
    private String fullDescription;

    @Column(name = "created_time")
    private Date createdTime;
    @Column(name = "updated_time")
    private Date updateTime;

    private boolean enabled;
    @Column(name="in_stock")
    private boolean inStock;

    private float cost;

    private float price;
    private float discountPercentage;

    @Digits(integer = 4,fraction = 2,message = "Length issue")
    private float length;
    private float width;
    private float height;
    private float weight;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
