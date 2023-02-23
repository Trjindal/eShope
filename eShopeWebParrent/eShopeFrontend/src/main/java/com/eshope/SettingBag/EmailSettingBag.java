package com.eshope.SettingBag;

import com.eShope.common.entity.Setting.Setting;
import com.eShope.common.entity.Setting.SettingBag;

import java.util.List;

public class EmailSettingBag extends SettingBag {
    public EmailSettingBag(List<Setting> listSettings) {
        super(listSettings);
    }

    public String getHost(){
        return super.getValue("MAIL_HOST");
    }
    public String getPort(){
        return super.getValue("MAIL_PORT");
    }
    public String getUserName(){
        return super.getValue("MAIL_USERNAME");
    }
    public String getPassword(){
        return super.getValue("MAIL_PASSWORD");
    }
    public String getSmtpAuth(){
        return super.getValue("SMTP_AUTH");
    }
    public String getSmtpSecured(){
        return super.getValue("SMTP_SECURED");
    }
    public String getFromAddress(){
        return super.getValue("MAIL_FROM");
    }
    public String getSenderName(){
        return super.getValue("MAIL_SENDER_NAME");
    }

    public String getCustomerVerifySubject(){
        return super.getValue("CUSTOMER_VERIFY_SUBJECT");
    }

    public String getCustomerVerifyContent(){
        return super.getValue("CUSTOMER_VERIFY_CONTENT");
    }
}
