package com.eShope.common.entity.Product;

import com.eShope.common.entity.Brand;
import com.eShope.common.entity.CartItem;
import com.eShope.common.entity.Category;
import com.eShope.common.entity.Order.OrderDetail;
import com.eShope.common.entity.Review;
import com.eShope.common.entity.Section.ProductSection;
import com.eShope.common.entity.Setting.Constants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Digits;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;


@Getter
@Setter
@Entity
@Table(name="products")
@NoArgsConstructor
public class Product {

    public Product(Integer id) {
        this.id = id;
    }

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
    @Size(min=10,max = 512,message="Short Description must be at least 10 and at most 511 characters long")
    private String shortDescription;

    @Column(length = 4096,nullable = false)
    @NotBlank(message="Full Description must not be blank")
    @Size(min=10,max =4096, message="Full Description must be at least 10 and at most 4095 characters long")
    private String fullDescription;

    @Column(name = "created_time", nullable = false, updatable = false)
    private Date createdTime;
    @Column(name = "updated_time")
    private Date updateTime;

    private boolean enabled;
    @Column(name="in_stock")
    private boolean inStock;

    private float cost;

    private float price;
    @Transient
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

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Review> reviews=new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<OrderDetail> orderDetailList =new ArrayList<>();

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ProductSection> productSections=new ArrayList<>();

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL)
    private List<CartItem> cartItems=new ArrayList<>();



    private int reviewCount;
    private float averageRating;

    public Product(String name) {
        this.name=name;
    }


    @Transient
    public String getMainImagePath(){

        if(id==null||mainImage==null||mainImage.isEmpty()) return "/images/image-thumbnail.png";
        String imgUrl=Constants.changeName(this.mainImage);
        return Constants.S3_BASE_URI +"product-photos/"+this.id+"/"+imgUrl;
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

    @Transient
    public float getDiscountPrice(){
        if(discountPercentage>0){
            return this.price*((100-discountPercentage)/100);
        }
        return this.price;
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

    @Transient
    public String getURI(){
        return "/p/"+this.alias+"/";
    }

    @Transient
    private boolean customerCanReview;

    @Transient
    private boolean reviewedByCustomer;

}
