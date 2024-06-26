package com.eShope.common.entity.Product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="product_details")
public class ProductDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false,length = 255)
    private String name;

    @Column(nullable = false,length = 255)
    private String value;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public ProductDetails(String name, String value,Product product) {
        this.name = name;
        this.value = value;
        this.product = product;
    }

    public ProductDetails(Integer id,String name, String value,Product product) {
        this.id=id;
        this.name = name;
        this.value = value;
        this.product = product;
    }
}
