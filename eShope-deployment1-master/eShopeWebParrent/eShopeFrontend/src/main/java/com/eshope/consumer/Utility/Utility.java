package com.eshope.consumer.Utility;

import com.eshope.consumer.Oauth.CustomerOAuth2User;
import com.eshope.consumer.SettingBag.CurrencySettingBag;
import com.eshope.consumer.SettingBag.EmailSettingBag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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

    public static String formatCurrency(float amount, CurrencySettingBag currencySettings){

        String symbol=currencySettings.getSymbol();
        String symbolPosition=currencySettings.getSymbolPosition();
        String decimalPointType=currencySettings.getDecimalPointType();
        String thousandPointType=currencySettings.getThousandsPointType();
        int decimalDigits= currencySettings.getDecimalDigits();


        String pattern=symbolPosition.equals("Before Price")?symbol:"";
        pattern+="###,###";

        if(decimalDigits>0){
            pattern+=".";
            for (int count=1;count<=decimalDigits;count++)
                pattern+="#";
        }

        pattern+=symbolPosition.equals("After Price")?symbol:"";

        char thousandSeparator=thousandPointType.equals("POINT")?'.':',';
        char decimalSeparator=decimalPointType.equals("POINT")?'.':',';

        DecimalFormatSymbols decimalFormatSymbols=DecimalFormatSymbols.getInstance();
        decimalFormatSymbols.setDecimalSeparator(decimalSeparator);
        decimalFormatSymbols.setGroupingSeparator(thousandSeparator);

        DecimalFormat formatter=new DecimalFormat(pattern,decimalFormatSymbols);

        return formatter.format(amount);
    }

}
