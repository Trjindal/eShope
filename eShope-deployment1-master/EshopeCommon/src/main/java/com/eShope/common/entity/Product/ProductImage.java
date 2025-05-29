package com.eShope.common.entity.Product;

import com.eShope.common.entity.Setting.Constants;
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
        String imgUrl= Constants.changeName(this.name);
        return Constants.S3_BASE_URI +"product-photos/"+product.getId()+"/extras/"+imgUrl;
    }
}
