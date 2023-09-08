package com.eShope.common.entity;

import com.eShope.common.entity.Product.Product;
import com.eShope.common.entity.Section.CategorySection;
import com.eShope.common.entity.Setting.Constants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

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
@Table(name = "categories")
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 128,nullable = false,unique = true)
    @NotBlank(message="Name must not be blank")
    @Size(min=3, message="Name must be at least 3 characters long")
    private String name;

    @Column(length = 64,nullable = false,unique = true)
    @NotBlank(message="Alias must not be blank")
    @Size(min=3, message="Alias must be at least 3 characters long")
    private String alias;

    @Column(length = 128)
    private String image;

    private boolean enabled;

    @OneToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    @OrderBy("name asc")
    private Set<Category> children =new HashSet<>();

    //FOR PRODUCT SEARCH PARENT_ID WILL BE LIKE -8-9-
   @Column(name = "all_parent_ids",length = 256,nullable = true)
    private String allParentIDs;

   @OneToMany(mappedBy = "category",cascade = CascadeType.ALL)
   List<CategorySection> categorySectionList=new ArrayList<>();

    @Transient
    @ManyToMany(mappedBy = "categories")
    @Cascade(org.hibernate.annotations.CascadeType.REMOVE)
    private Set<Brand> brands = new HashSet<>();

    @Transient
    @OneToMany(mappedBy = "category")
    private List<Product> products=new ArrayList<>();

    public Category(String name) {
        this.name=name;
        this.alias=name;
    }
    public Category(String name,Category parent) {
       this(name);
       this.parent=parent;
    }
    public Category(String name,int id) {
        this(name);
        this.id=id;
    }

    public Category(Integer id) {
        this.id=id;
    }

    public String getImage() {
        return image;
    }


    public String getPhotosImagePath(){
        if((Object) id==null ||this.image=="default-user.png")
            return "/assets/images/users/default-user.png";

//        return "/assets/images/users/default-user.png";
        String imgUrl= Constants.changeName(this.image);
        return Constants.S3_BASE_URI+"category-photos/"+this.id+"/"+imgUrl;
    }
}
