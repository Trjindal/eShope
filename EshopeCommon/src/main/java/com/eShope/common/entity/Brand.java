package com.eShope.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
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


    @ManyToMany
    @JoinTable(
            name="brands_categories",
            joinColumns=@JoinColumn(name="brand_id"),
            inverseJoinColumns = @JoinColumn(name="category_id")
    )
    private Set<Category> categories =new HashSet<>();

    public Brand(String name) {
        this.name=name;
        this.logo="brand-logo";
    }

    @Transient
    public String getLogoPath(){
        if(this.id==null)
            return "";
        return "/brand-photos/"+this.id+"/"+this.logo;
    }


}
