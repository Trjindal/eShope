package com.eshope.Utility;

import com.eshope.SettingBag.EmailSettingBag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;

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
}
