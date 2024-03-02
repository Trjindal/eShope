package com.eShope.common.entity;

import com.eShope.common.entity.Product.Product;
import com.eShope.common.entity.Section.BrandSection;
import com.eShope.common.entity.Section.CategorySection;
import com.eShope.common.entity.Setting.Constants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name="brands")
@NoArgsConstructor
public class Brand {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message="Name must not be blank")
    @Size(min=3, message="Name must be at least 3 characters long")
    @Column(nullable = false,length = 45,unique = true)
    private String name;

    @Column(nullable = false,length = 128)
    private String logo;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="brands_categories",
            joinColumns=@JoinColumn(name="brand_id"),
            inverseJoinColumns = @JoinColumn(name="category_id")
    )
    private Set<Category> categories =new HashSet<>();

    @OneToMany(mappedBy = "brand")
    private List<Product> products=new ArrayList<>();


    @OneToMany(mappedBy = "brand",cascade = CascadeType.ALL)
    List<BrandSection> brandSectionList=new ArrayList<>();
    public Brand(String name) {
        this.name=name;
        this.logo="brand-logo";
    }

    public Brand(Integer id){
        this.id=id;
    }

    public Brand(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Brand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Transient
    public String getLogoPath(){
        if(this.id==null)
            return "/assets/images/image-thumbnail.png";
        String imgUrl=Constants.changeName(this.logo);
        return Constants.S3_BASE_URI+"brand-photos/"+this.id+"/"+imgUrl;
    }


}
