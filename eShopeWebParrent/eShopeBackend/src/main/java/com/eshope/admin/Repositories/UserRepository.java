package com.eshope.admin.Repositories;

import com.eShope.common.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User,Integer> {
    @Query("SELECT u FROM User u WHERE u.email=:email")
    public User getUserByEmail(@Param("email") String email);

    @Transactional
    @Modifying
    @Query("Update User u SET u.email=?1,u.password=?2,u.firstName=?3,u.lastName=?4,u.enabled=?5  WHERE u.id=?6")
    int editUserByMyId(String email,String password,String firstName,String lastName,Boolean enabled,int id);

    public long countById(Integer id);

    @Transactional
    @Modifying
    @Query("Update User u SET u.enabled=?2 where u.id=?1")
    public void updateEnableStatus(Integer id,boolean enabled);


    @Query("SELECT u FROM User u WHERE CONCAT(u.id,' ',u.email,' ',u.firstName,' ',u.lastName) LIKE %?1%")
    public Page<User> findAll(String keyword, Pageable pageable);

    @Transactional
    @Modifying
    public User save(User user);

    public Optional<User> findById(int i);

    @Transactional
    @Modifying
    public User deleteById(Integer id);

    public  Iterable<User> findAll();
}
