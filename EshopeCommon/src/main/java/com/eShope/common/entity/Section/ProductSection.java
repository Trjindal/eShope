package com.eShope.common.entity.Section;

import com.eShope.common.entity.Product.Product;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "sections_products")
public class ProductSection {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "product_order")
    private int productOrder;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public int getProductOrder() {
        return productOrder;
    }

    public void setProductOrder(int productOrder) {
        this.productOrder = productOrder;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }


}