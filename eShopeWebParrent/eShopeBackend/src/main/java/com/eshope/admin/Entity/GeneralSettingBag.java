package com.eshope.admin.Entity;

import com.eShope.common.entity.Setting.Setting;
import com.eShope.common.entity.Setting.SettingBag;

import com.eshope.admin.Service.SettingService;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class GeneralSettingBag extends SettingBag {

    @Autowired
    private SettingService settingService;

    public GeneralSettingBag(List<Setting> listSettings) {
        super(listSettings);
    }

    public Setting updateCurrencySymbol(String value){
        Setting setting=super.update("CURRENCY_SYMBOL",value);
        return setting;
    }
    public Setting updateCurrencyId(String value){
        Setting setting=super.update("CURRENCY_ID",value);
        return setting;
    }

    public Setting updateSiteLogo(String value){
        Setting setting=super.update("SITE_LOGO",value);
        return setting;
    }
}
