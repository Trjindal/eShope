package com.eShope.common.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.constraints.Digits;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;


@Getter
@Setter
@Entity
@Table(name="products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = true)
    private Integer id;
    @Column(unique = true,length = 256,nullable = false)
    @NotBlank(message="Name must not be blank")
    @Size(min=3, message="Name must be at least 3 characters long")
    private String name;

    @Column(unique = true,length = 256,nullable = false)
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

    @Column(name ="main_image",nullable = false)
    private String mainImage;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<ProductImage> images=new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ProductDetails> details=new ArrayList<>();



    @Transient
    public String getMainImagePath(){

        if(id==null||mainImage==null||mainImage.isEmpty()) return "/images/image-thumbnail.png";
        return "/product-photos/"+this.id+"/"+this.mainImage;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public void addExtraImage(String imageName){
        this.images.add(new ProductImage(imageName,this));
    }

    public void addDetails(String name,String value){
        this.details.add(new ProductDetails(name,value,this));
    }

    public void addDetails(Integer id, String name, String value) {
        this.details.add(new ProductDetails(id, name, value, this));
    }

    @Transient
    public String getShortName(){
        if(name.length()>70){
            return name.substring(0,70).concat("...");
        }
        return name;
    }


    public boolean containsImageName(String imageName) {
        Iterator<ProductImage> iterator=images.iterator();
        while(iterator.hasNext()){
            ProductImage image=iterator.next();
            if(image.getName().equals(imageName)){
                return true;
            }
        }
        return false;
    }
}
