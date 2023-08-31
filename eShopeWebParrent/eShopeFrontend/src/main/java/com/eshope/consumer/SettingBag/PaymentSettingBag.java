package com.eshope.consumer.SettingBag;

import com.eShope.common.entity.Setting.Setting;
import com.eShope.common.entity.Setting.SettingBag;

import java.util.List;

public class PaymentSettingBag extends SettingBag {

    public PaymentSettingBag(List<Setting> listSettings) {
        super(listSettings);
    }

    public String getURL(){
        return super.getValue("PAYPAL_API_BASE_URL");
    }

    public String getClientId(){
        return super.getValue("PAYPAL_API_CLIENT_ID");
    }

    public String getClientSecret(){
        return super.getValue("PAYPAL_API_CLIENT_SECRET");
    }
}