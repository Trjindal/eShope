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

///users/{id}/enabled/{status},

    @Bean
        SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http.csrf().and()
                .authorizeHttpRequests()
                .antMatchers("/","/account","/account/**","/home","/states/listByCountry/**","/get_shipping_cost").hasAnyAuthority("Assistant","Admin","Viewer","Salesperson","Shipper","Editor")
                .antMatchers("/users","/users/","/users/page/**","/users/new","/users/edit/**","/settings","/settings/","/countries/list","users/check_email").hasAnyAuthority("Admin","Viewer")
                .antMatchers("/users/saveUser","/users/editUser","/users/delete/**","/users/export/**","/settings/**","countries/save","/countries/delete/**","/countries/check_unique","/users/**/enabled/**").hasAuthority("Admin")
                .antMatchers("/reviews","/reviews/","/reviews/page/**","/reviews/detail/**","/reviews/edit/**","/questions","/questions/page/**","/questions/detail/**","/questions/edit/**").hasAnyAuthority("Assistant","Admin","Viewer")
                .antMatchers(" /questions/**/approve"," /questions/**/disapprove").hasAnyAuthority("Assistant","Admin")
                .antMatchers("/reviews/save","/reviews/delete/**","/questions/save","/questions/delete/").hasAnyAuthority("Assistant","Admin")
                .antMatchers( "/customers/detail/**").hasAnyAuthority("Assistant","Admin","Viewer","Salesperson","Shipper")
                .antMatchers("/products/detail/**").hasAnyAuthority("Assistant","Admin","Viewer","Salesperson","Shipper","Editor")

                .antMatchers("/customers","/customers/","/customers/page/**","/customers/edit/**").hasAnyAuthority("Salesperson","Admin","Viewer")
                .antMatchers("/customers/editCustomer","/customers/delete/**","/customers/**/enabled/**").hasAnyAuthority("Salesperson","Admin")
//            /customers/{id}/enabled/{status} /states/listByCountry/** DONEEE
                .antMatchers("/shipping","/shipping/","/shipping/page/**","/shipping/new","/shipping/edit/**").hasAnyAuthority("Salesperson","Admin","Viewer")
                .antMatchers("/shipping/saveRates","/shipping/delete/**","/shipping/**/enabled/**").hasAnyAuthority("Salesperson","Admin")
//                /shipping/{id}/enabled/{status} DONEEE
                .antMatchers("/orders/","/orders/search_product/**","/orders","/orders/page/**","/orders/detail/**").hasAnyAuthority("Salesperson","Admin","Viewer","Shipper")
                .antMatchers("/orders/edit/**","/products/get/**").hasAnyAuthority("Salesperson","Admin","Viewer")
                .antMatchers("/orders/save","/orders/delete/**").hasAnyAuthority("Salesperson","Admin")

                .antMatchers("/orders_shipper/update/**").hasAnyAuthority("Shipper","Admin")
//                /products/{id}/enabled/{status} DONEE
                .antMatchers("/products","/products/page/**"    ).hasAnyAuthority("Salesperson","Admin","Viewer","Shipper","Editor")
                .antMatchers("/products/edit/**").hasAnyAuthority("Salesperson","Admin","Viewer","Editor")
                .antMatchers("/products/editProduct","/products/**/enabled/**").hasAnyAuthority("Salesperson","Admin","Editor")

//                /brands/{id}/categories DONEE
                .antMatchers("/brands","/brands/","/brands/page/**","/brands/new","/brands/edit/**").hasAnyAuthority("Editor","Admin","Viewer")
                .antMatchers("/brands/saveBrand","/brands/editBrand","/brands/delete/**","/brands/export/**","/brands/**/categories").hasAnyAuthority("Editor","Admin")

//        "/categories/{id}/enabled/{status}" DONEE
                .antMatchers("/categories","/categories/","/categories/page/**","/categories/new","/categories/edit/**").hasAnyAuthority("Editor","Admin","Viewer")
                .antMatchers("/categories/saveCategory","/categories/editCategory","/categories/delete/**","/categories/export/**","/categories/**/enabled/**").hasAnyAuthority("Editor","Admin")

//                "/products/{id}/enabled/{status}" DONEE
                .antMatchers("/products/new").hasAnyAuthority("Editor","Admin","Viewer")
                .antMatchers("/products/saveProduct","/products/delete/**","/products/**/enabled/**").hasAnyAuthority("Editor","Admin")

//                /articles/{id}/enabled/{publishStatus}
                .antMatchers("/articles","/articles/","/articles/page/**","/articles/new","/articles/edit/**","/articles/detail/**").hasAnyAuthority("Editor","Admin","Viewer")
                .antMatchers("/articles/save","/articles/delete/**","/articles/**/enabled/**").hasAnyAuthority("Editor","Admin")

//
                .antMatchers("/menus","/menus/","/menus/new","/menus/edit/**").hasAnyAuthority("Editor","Admin","Viewer")
                .antMatchers("/menus/save","/menus/delete/**","/menus/**/enabled/**","/menus/up/**","/menus/down/**").hasAnyAuthority("Editor","Admin")

                .antMatchers("/sections","/sections/","/sections/new/**","/sections/edit/**").hasAnyAuthority("Editor","Admin","Viewer")
                .antMatchers("/sections/save/**","/sections/**/enabled/**","/sections/moveup/**","/sections/movedown/**").hasAnyAuthority("Editor","Admin")

//                .antMatchers("/users/**","/countries/**","/states/**","/settings/**").hasAnyAuthority("Admin","Viewer")
//                .antMatchers("/products/new","/products/delete/**").hasAnyAuthority("Admin","Editor")
////                .antMatchers("/products/edit/**","/products/save","/products/editProduct").hasAnyAuthority("Admin","Editor","Salesperson")
////                .antMatchers("/products","/products/","/products/detail/**","/products/page/**").hasAnyAuthority("Admin","Editor","Salesperson","Shipper")
//                .antMatchers("/categories/**","/brands/**","/articles/**","/menus/**","/sections/**","/products/**").hasAnyAuthority("Admin","Editor")
////                .antMatchers("/orders","/orders/","/orders/page/**","/orders/detail/**").hasAnyAuthority("Admin","Salesperson","Shipper")
////                .antMatchers("/products/detail/**", "/customers/detail/**").hasAnyAuthority("Admin", "Editor", "Salesperson", "Assistant")
////                .antMatchers("/customers/**","/orders/**","/get_shipping_cost").hasAnyAuthority("Admin","Salesperson")
//
//                .antMatchers("/reviews/**", "/questions/**").hasAnyAuthority("Admin", "Assistant")


//                .antMatchers("/shipping/**").hasAnyAuthority("Admin","Salesperson")


                .antMatchers("/assets/**", "/assets/js/**").permitAll()
                .anyRequest().denyAll()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/access-denied")
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








