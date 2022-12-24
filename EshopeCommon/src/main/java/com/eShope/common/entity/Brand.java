package com.eShope.common.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name="brands")
public class Brand {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false,length = 45,unique = true)
    private String name;

    @Column(nullable = false,length = 128,unique = true)
    private String logo;


    @ManyToMany
    @JoinTable(
            name="brands_categories",
            joinColumns=@JoinColumn(name="brand_id"),
            inverseJoinColumns = @JoinColumn(name="category_id")
    )
    private Set<Category> ctegories =new HashSet<>();
}
