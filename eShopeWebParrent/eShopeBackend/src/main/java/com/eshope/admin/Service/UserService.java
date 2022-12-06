package com.eshope.admin.Service;


import com.eShope.common.entity.Role;
import com.eShope.common.entity.User;
import com.eshope.admin.Repositories.RoleRepository;
import com.eshope.admin.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public List<User> listAllUsers(){
        return (List<User>) userRepository.findAll();
    }

    public List<Role> listAllRoles(){
        return (List<Role>) roleRepository.findAll();
    }

    public void saveUser(User user){
        userRepository.save(user);
    }
}
