package com.eshope.admin.Config;


import com.eshope.admin.Security.EshopeUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig  {



    protected void defaultSecurityFilterChain(AuthenticationManagerBuilder managerBuilder) throws Exception {
        managerBuilder.authenticationProvider(authenticationProvider());
    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new EshopeUserDetailsService();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }



    @Bean
        SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/eShopeAdmin/**").authenticated()
                .requestMatchers("/assets/**", "/assets/js/**").permitAll()
                .anyRequest().authenticated()
                .and().formLogin().loginPage("/login")
                .usernameParameter("email").permitAll()
                .defaultSuccessUrl("/users/edit/26",true).failureUrl("/login?error=true").permitAll()
                .and().logout().logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true).permitAll()
                .and().httpBasic();
        return http.build();

    }
    }







