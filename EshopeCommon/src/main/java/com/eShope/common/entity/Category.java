package com.eShope.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@Entity
@Table(name = "categories")
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 128,nullable = false,unique = true)
    private String name;

    @Column(length = 64,nullable = false,unique = true)
    private String alias;

    @Column(length = 128,nullable = false)
    private String image;

    private boolean enabled;

    @OneToOne
    @JoinColumn(name="parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private Set<Category> children=new HashSet<>();


    public Category(String name) {
        this.name=name;
        this.alias=name;
        this.image="default.png";
    }
}
