package com.eshope.Config;


import com.eshope.Oauth.CustomerOAuth2User;
import com.eshope.Oauth.CustomerOAuth2UserService;
import com.eshope.Oauth.OAuth2LoginSuccessHandler;
import com.eshope.Security.CustomerUserDetailService;
import com.eshope.Security.DatabaseLoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private CustomerOAuth2UserService customerOAuth2User;

    @Autowired
    private OAuth2LoginSuccessHandler auth2LoginSuccessHandler;

    @Autowired
    private DatabaseLoginSuccessHandler databaseLoginSuccessHandler;

//    protected void defaultSecurityFilterChain(AuthenticationManagerBuilder managerBuilder) throws Exception {
//        managerBuilder.authenticationProvider(authenticationProvider());
//    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new CustomerUserDetailService();
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
                .authorizeHttpRequests().antMatchers("/customer").authenticated()
                .antMatchers("/register/update").authenticated()
                .antMatchers("/account","/register/update","/cart","/address_book/**","/checkout","/place_order","/process_paypal_order","/orders/**").authenticated()

                .anyRequest().permitAll()
                .and().formLogin().loginPage("/login")
                .usernameParameter("email")
                .successHandler(databaseLoginSuccessHandler)
                .permitAll()
                .defaultSuccessUrl("/", true).failureUrl("/login?error=true").permitAll()
                .and()
                .oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint()
                .userService(customerOAuth2User)
                .and()
                .successHandler(auth2LoginSuccessHandler)
                .and()
                 .logout().logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true).permitAll()
                .and().rememberMe().key("Abcdefghijjklmnopqrs_1234567890")
                .tokenValiditySeconds(7*24*60*60)
                .and().httpBasic();

        http.headers().frameOptions().sameOrigin();

        return http.build();

    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
    }


    }








