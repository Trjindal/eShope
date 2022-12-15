package com.eshope.admin.Security;

import com.eShope.common.entity.User;
import com.eshope.admin.Repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class EshopeUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user=userRepository.getUserByEmail(email);
        if(user!=null){
            return new EshopeUserDetails(user);
        }
        throw new UsernameNotFoundException("Could not find user with email : "+email);
    }
}
