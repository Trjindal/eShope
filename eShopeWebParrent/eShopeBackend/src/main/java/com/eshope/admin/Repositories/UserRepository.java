package com.eshope.admin.Repositories;


import com.eShope.common.entity.User;
import jakarta.persistence.NamedQuery;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

@EnableJpaRepositories
public interface UserRepository extends JpaRepository<User,Integer> {
    @Query("SELECT u FROM User u WHERE u.email=:email")
    public User getUserByEmail(@Param("email") String email);

    @Transactional
    @Modifying
    @Query("Update User u SET u.email=?1,u.password=?2,u.firstName=?3,u.lastName=?4,u.enabled=?5  WHERE u.id=?6")
    int editUserByMyId(String email,String password,String firstName,String lastName,Boolean enabled,int id);

    public long countById(Integer id);
}
