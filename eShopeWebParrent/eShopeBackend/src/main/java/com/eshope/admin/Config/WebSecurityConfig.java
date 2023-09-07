package com.eshope.admin.Config;


import com.eshope.admin.Security.EshopeUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
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

        http.csrf().and()
                .authorizeHttpRequests()
                .antMatchers("/states/listByCountry/**").hasAnyAuthority("Admin","Salesperson")
                .antMatchers("/users/**","/countries/**","/states/**","/settings/**").hasAnyAuthority("Admin","Viewer")
                .antMatchers("/products/new","/products/delete/**").hasAnyAuthority("Admin","Editor")
                .antMatchers("/products/edit/**","/products/save","/products/editProduct").hasAnyAuthority("Admin","Editor","Salesperson")
                .antMatchers("/products","/products/","/products/detail/**","/products/page/**").hasAnyAuthority("Admin","Editor","Salesperson","Shipper")
                .antMatchers("/categories/**","/brands/**","/articles/**","/menus/**","/sections/**","/products/**").hasAnyAuthority("Admin","Editor")
                .antMatchers("/orders","/orders/","/orders/page/**","/orders/detail/**").hasAnyAuthority("Admin","Salesperson","Shipper")
                .antMatchers("/products/detail/**", "/customers/detail/**").hasAnyAuthority("Admin", "Editor", "Salesperson", "Assistant")
                .antMatchers("/customers/**","/orders/**","/get_shipping_cost").hasAnyAuthority("Admin","Salesperson")
                .antMatchers("/orders_shipper/update/**").hasAuthority("Shipper")
                .antMatchers("/reviews/**", "/questions/**").hasAnyAuthority("Admin", "Assistant")


                .antMatchers("/shipping/**").hasAnyAuthority("Admin","Salesperson")


                .antMatchers("/assets/**", "/assets/js/**").permitAll()
                .anyRequest().authenticated()
                .and().formLogin().loginPage("/login")
                .usernameParameter("email").permitAll()
                .defaultSuccessUrl("/", true).failureUrl("/login?error=true").permitAll()
                .and().logout().logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true).permitAll()
                .and().rememberMe().key("Abcdefghijjklmnopqrs_1234567890")
                .tokenValiditySeconds(7*24*60*60)
                .and().httpBasic();

        http.headers().frameOptions().sameOrigin();

        http.authenticationProvider(authenticationProvider());


        return http.build();

    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
    }


    }








