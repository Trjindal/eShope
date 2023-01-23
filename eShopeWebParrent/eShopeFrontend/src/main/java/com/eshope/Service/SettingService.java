package com.eshope.Service;

import com.eShope.common.entity.Currency;
import com.eShope.common.entity.Setting;
import com.eShope.common.entity.SettingCategory;
import com.eshope.Repository.SettingRepository;

import com.eshope.SettingBag.EmailSettingBag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
