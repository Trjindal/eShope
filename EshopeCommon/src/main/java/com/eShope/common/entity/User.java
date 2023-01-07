package com.eShope.common.entity;

import com.eShope.common.annotation.ChangePasswordValidator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(length = 128,nullable = false,unique = true)
    @NotBlank(message="Email must not be blank")
    @Email(message = "Please provide a valid email address" )
    private String email;
    @Column(length = 64,nullable = false)
    @Size(min=5, message="Password must be at least 5 characters long")
    private String password;

//    @Size(min=5, message="Password must be at least 5 characters long")
    @ChangePasswordValidator
    @Transient
    @Column(length = 64,nullable = true)
    private String changePassword;
    @Column(name = "first_name",length = 45,nullable = false)
    @NotBlank(message="First name must not be blank")
    @Size(min=3, message="First name must be at least 3 characters long")
    private String firstName;
    @Column(name = "last_name",length = 45,nullable = false)
    @NotBlank(message="Last name must not be blank")
    @Size(min=3, message="Last name must be at least 3 characters long")
    private String lastName;
    @Column(length = 64)
    private String photos;
    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="users_roles",joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id")
    )
    private Set<Role> roles=new HashSet<>();

    public User(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", roles=" + roles +
                '}';
    }

    public void addRole(Role role){
        this.roles.add(role);
    }

    public String getPhotosImagePath(){
        if((Object) id==null ||photos==null) return "/assets/images/users/default-user.png";

        return "/user-photos/"+this.id+"/"+this.photos;
    }

    @Transient
    public String getFullName(){
        return firstName+" "+lastName;
    }

    public boolean hasRole(String roleName){
        Iterator<Role> iterator=roles.iterator();

        while(iterator.hasNext()){
            Role role=iterator.next();
            if(role.getName().equals(roleName)){
                return true;
            }
        }
        return false;
    }
}
