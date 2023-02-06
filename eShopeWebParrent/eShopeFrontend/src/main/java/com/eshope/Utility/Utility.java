package com.eshope.Utility;

import com.eshope.Oauth.CustomerOAuth2User;
import com.eshope.SettingBag.EmailSettingBag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import java.util.Properties;

@Slf4j
public class Utility {

    public static String getSiteURL(HttpServletRequest request){
        String siteURL=request.getRequestURL().toString();
        log.error(request.getServletPath());
        return siteURL.replace(request.getServletPath(),"");
    }

    public static JavaMailSenderImpl prepareMailSender(EmailSettingBag settings){
        JavaMailSenderImpl mailSender=new JavaMailSenderImpl();

        mailSender.setHost(settings.getHost());
        mailSender.setPort(Integer.parseInt(settings.getPort()));
        mailSender.setUsername(settings.getUserName());
        mailSender.setPassword(settings.getPassword());

        Properties mailProperties=new Properties();
        mailProperties.setProperty("mail.smtp.auth",settings.getSmtpAuth());
        mailProperties.setProperty("mail.smtp.starttls.enable",settings.getSmtpSecured());

        mailSender.setJavaMailProperties(mailProperties);

        return mailSender;
    }

    public static String getEmailOfAuthenticatedCustomer(HttpServletRequest request){
        Object principal=request.getUserPrincipal();
        String customerEmail=null;
        if(principal==null) return null;

        if(principal instanceof UsernamePasswordAuthenticationToken ||principal instanceof RememberMeAuthenticationToken){
            customerEmail=request.getUserPrincipal().getName();
        }else if(principal instanceof OAuth2AuthenticationToken){
            OAuth2AuthenticationToken auth2AuthenticationToken=(OAuth2AuthenticationToken) principal;
            CustomerOAuth2User oAuth2User=(CustomerOAuth2User) auth2AuthenticationToken.getPrincipal();
            customerEmail=oAuth2User.getEmail();
        }
        return customerEmail;
    }
}
