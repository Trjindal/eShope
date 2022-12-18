package com.eshope.admin.User.Service;


import com.eShope.common.entity.Role;
import com.eShope.common.entity.User;
import com.eshope.admin.Main.Repositories.RoleRepository;
import com.eshope.admin.Main.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {

    public static final int USERS_PER_PAGE=5;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> listAllUsers(){
        return (List<User>) userRepository.findAll(Sort.by("firstName").ascending());
    }

    public Page<User> listByPage(int pageNum,String sortField,String sortDir,String keyword){
       Sort sort= Sort.by(sortField);
       sort=sortDir.equals("asc")?sort.ascending():sort.descending();

        Pageable pageable=PageRequest.of(pageNum-1,USERS_PER_PAGE,sort);

        if(keyword!=null){
            return userRepository.findAll(keyword,pageable);
        }

        return userRepository.findAll(pageable);
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

    public User editUser(User user) {
        encodePasswordEdit(user);

        userRepository.editUserByMyId(user.getEmail(),user.getPassword(),user.getFirstName(),user.getLastName(),user.isEnabled(),user.getId());
        return userRepository.save(user);
    }

    private void encodePassword(User user){
        String encodePassword=passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
    }

    private void encodePasswordEdit(User user){
        if(!(user.getChangePassword().isEmpty())){
            String encodePassword=passwordEncoder.encode(user.getChangePassword());
            user.setPassword(encodePassword);
            user.setChangePassword("");
        }
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

    public User getUserByEmail(String email) {
       return userRepository.getUserByEmail(email);
    }
}
