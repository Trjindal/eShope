package com.eshope.admin.user;


import com.eShope.common.entity.Role;
import com.eShope.common.entity.User;
import com.eshope.admin.Repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void testCreateUser(){
        Role roleAdmin=testEntityManager.find(Role.class,1);
        User userTj=new User("tr@eshope.com","12345","Tushar","jindal");
        userTj.addRole(roleAdmin);
        User savedUser=userRepository.save(userTj);
        assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateUserWithTwoRoles(){
        User userRavi=new User("ravi@eshope.com","ravi2022","Ravi","Singla");
        Role roleEditor=new Role(3);
        Role roleAssistant=new Role(5);
        userRavi.addRole(roleAssistant);
        userRavi.addRole(roleEditor);
        User savedUser=userRepository.save(userRavi);
        assertThat(savedUser.getId()).isGreaterThan(0);

    }

    //for this test we created tostring of name in role and in user we created toString of name,email,role
    @Test
    public void testListAllUsers(){
        Iterable<User> listUsers=userRepository.findAll();
        listUsers.forEach(user -> System.out.println(user));
    }

    @Test
    public void testGetUserById(){
        User user=userRepository.findById(1).get();
        System.out.println(user);
        assertThat(user).isNotNull();

    }
    @Test
    public void testUpdateUserDetails(){
        User user=userRepository.findById(1).get();
        user.setEnabled(true);
        userRepository.save(user);

    }

    @Test
    public void testUpdateUserRoles(){
        User user=userRepository.findById(2).get();
        Role roleEditor=new Role(3);
        Role roleSalesPerson =new Role(2);
        user.getRoles().remove(roleEditor);
        user.addRole(roleSalesPerson);
        userRepository.save(user);
    }

    @Test
    public void testDeleteUser(){
        int userId=2;
        userRepository.deleteById(2);
    }

    @Test
    public void testGetUserByEmail(){
        String email="maanik@eshope";
        User user=userRepository.getUserByEmail(email);
        assertThat(user).isNotNull();
    }

    @Test
    public void testCountById(){
        Integer id=1;
        Long countById=userRepository.countById(id);
        assertThat(countById).isNotNull().isGreaterThan(0);
    }

}
