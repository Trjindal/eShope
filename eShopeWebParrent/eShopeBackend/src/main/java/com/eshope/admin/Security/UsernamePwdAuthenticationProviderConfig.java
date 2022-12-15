//package com.eshope.admin.Security;
//
//
//import com.eShope.common.entity.Role;
//import com.eShope.common.entity.User;
//import com.eshope.admin.Repositories.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
//
//@Component
//public class UsernamePwdAuthenticationProviderConfig implements AuthenticationProvider {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        String email = authentication.getName();
//        String pwd = authentication.getCredentials().toString();
//        User user=userRepository.getUserByEmail(email);
//
//        if (user!=null) {
//            if (passwordEncoder.matches(pwd, user.getPassword())) {
//                List<GrantedAuthority> authorities = new ArrayList<>();
//                return new UsernamePasswordAuthenticationToken(email, pwd, getGrantedAuthorities(user));
//            } else {
//                throw new BadCredentialsException("Invalid password!");
//            }
//        }else {
//            throw new BadCredentialsException("No user registered with this details!");
//        }
//    }
//
//    private List<GrantedAuthority> getGrantedAuthorities(User user) {
//        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
//        Set<Role> roles=user.getRoles();
//        for (Role authority : roles) {
//            grantedAuthorities.add(new SimpleGrantedAuthority(authority.getName()));
//        }
//        return grantedAuthorities;
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
//    }
//}
