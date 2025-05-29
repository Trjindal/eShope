package com.eShope.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "countries")
@NoArgsConstructor
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false,length = 45)
    private String name;

    @Column(nullable = false,length=5)
    private String code;

    @OneToMany(mappedBy ="country")
    private Set<State> states;

    public Country(String name,String code){
        this.name=name;
        this.code=code;
    }

    public Country(String name) {
        this.name=name;
    }

    public Country(Integer countryId, String name, String code) {
        this.name=name;
        this.id=countryId;
        this.code=code;
    }

    public Country(Integer id) {
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString(){
        return "Country [id=" +id+", name="+name+", code="+code+"]";
    }
}
