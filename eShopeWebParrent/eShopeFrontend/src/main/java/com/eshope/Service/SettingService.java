package com.eshope.Service;

import com.eShope.common.entity.Setting.Setting;
import com.eShope.common.entity.Setting.SettingCategory;
import com.eshope.Repository.SettingRepository;

import com.eshope.SettingBag.EmailSettingBag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingService {

    @Autowired
    private SettingRepository settingRepository;


    public  List<Setting> getGeneralSettings(){

        return  settingRepository.findByTwoCategory(SettingCategory.GENERAL,SettingCategory.CURRENCY);
    }

    public EmailSettingBag getEmailSettings(){
        List<Setting> settings=settingRepository.findByCategory(SettingCategory.MAIL_SERVER);
        settings.addAll(settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES));

        return new EmailSettingBag(settings);
    }
}
