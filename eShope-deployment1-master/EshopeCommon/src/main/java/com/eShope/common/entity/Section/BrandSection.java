package com.eShope.common.entity.Section;

import com.eShope.common.entity.Brand;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "sections_brands")
public class BrandSection  {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "brand_order")
    private int brandOrder;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    public int getBrandOrder() {
        return brandOrder;
    }

    public void setBrandOrder(int brandOrder) {
        this.brandOrder = brandOrder;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

}