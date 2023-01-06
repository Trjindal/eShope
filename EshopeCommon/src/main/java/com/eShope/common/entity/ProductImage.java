package com.eShope.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "product_images")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public ProductImage(String name, Product product) {
        this.name = name;
        this.product = product;
    }

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public ProductImage(Integer id, String name, Product product) {
        this.id=id;
        this.name = name;
        this.product = product;
    }

    @Transient
    public String getImagePath(){
        return "/product-photos/"+product.getId()+"/extras/"+this.name;
    }
}
