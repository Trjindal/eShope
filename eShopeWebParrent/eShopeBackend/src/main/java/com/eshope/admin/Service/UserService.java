package com.eshope.admin.Service;


import com.eShope.common.entity.Role;
import com.eShope.common.entity.User;
import com.eshope.admin.Repositories.RoleRepository;
import com.eshope.admin.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> listAllUsers(){
        return (List<User>) userRepository.findAll();
    }

    public List<Role> listAllRoles(){
        return (List<Role>) roleRepository.findAll();
    }

    public User saveUser(User user){
        boolean isUpdatingUser = (user.getId() >0);

        if (isUpdatingUser) {
            User existingUser = userRepository.findById(user.getId()).get();

            if (user.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());
            } else {
                encodePassword(user);
            }

        } else {
            encodePassword(user);
        }

        return userRepository.save(user);
    }

    public void editUser(User user) {
        encodePassword(user);
//        userRepository.save(user);
        userRepository.editUserByMyId(user.getEmail(),user.getPassword(),user.getFirstName(),user.getLastName(),user.isEnabled(),user.getId());
    }

    private void encodePassword(User user){
        String encodePassword=passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
    }

    public boolean isEmailUnique(String email){
        User userByEmail=userRepository.getUserByEmail(email);
        return userByEmail==null;
    }

    public User getUserById(Integer id) {
        try{
            return userRepository.findById(id).get();
        }catch (NoSuchElementException ex){
            throw new UsernameNotFoundException("Could not find any user with Id"+ id);
        }

    }

    public void delete(Integer id) throws UsernameNotFoundException{
        Long countById=userRepository.countById(id);
        if(countById==null||countById==0){
            throw new UsernameNotFoundException("Could not found any user with Id "+id);
        }
        userRepository.deleteById(id);
    }

    public void updateUserEnabledStatus(Integer id, boolean enabled) {
        userRepository.updateEnableStatus(id,enabled);
    }
}
