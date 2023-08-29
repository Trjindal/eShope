package com.eshope.consumer.Service;

import com.eShope.common.entity.Currency;
import com.eShope.common.entity.Setting.Setting;
import com.eShope.common.entity.Setting.SettingCategory;
import com.eshope.consumer.Repository.CurrencyRepository;
import com.eshope.consumer.Repository.SettingRepository;

import com.eshope.consumer.SettingBag.CurrencySettingBag;
import com.eshope.consumer.SettingBag.EmailSettingBag;
import com.eshope.consumer.SettingBag.PaymentSettingBag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingService {

    @Autowired
    private SettingRepository settingRepository;

    @Autowired
    private CurrencyRepository currencyRepository;


    public  List<Setting> getGeneralSettings(){

        return  settingRepository.findByTwoCategory(SettingCategory.GENERAL,SettingCategory.CURRENCY);
    }

    public EmailSettingBag getEmailSettings(){
        List<Setting> settings=settingRepository.findByCategory(SettingCategory.MAIL_SERVER);
        settings.addAll(settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES));


        return new EmailSettingBag(settings);
    }

    public CurrencySettingBag getCurrencySettings(){
        List<Setting> settings=settingRepository.findByCategory(SettingCategory.CURRENCY);
        return new CurrencySettingBag(settings);
    }

    public PaymentSettingBag getPaymentSettings(){
        List<Setting> settings=settingRepository.findByCategory(SettingCategory.PAYMENT);
        return new PaymentSettingBag(settings);
    }

    public String getCurrencyCode(){
        Setting setting=settingRepository.findByKey("CURRENCY_ID");
        Integer currencyId=Integer.parseInt(setting.getValue());
        Currency currency=currencyRepository.findById(currencyId).get();

        return currency.getCode();
    }
}


