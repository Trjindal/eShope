package com.eShope.common.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="roles")

public class Role {

    @Id
//    @GenericGenerator(name = "native",strategy = "native")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(length = 40,nullable = false,unique = true)
    private String name;

    @Column(length = 150,nullable = false)
    private String description;


    @Override
    public String toString() {
        return this.name;
    }

    public Role(String name,String description){
        this.name=name;
        this.description=description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return id == role.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Role(int id){
        this.id=id;
    }
}
