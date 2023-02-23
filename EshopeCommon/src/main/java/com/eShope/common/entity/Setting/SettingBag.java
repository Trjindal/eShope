package com.eShope.common.entity.Setting;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class SettingBag {


    private List<Setting> listSettings;

    public SettingBag(List<Setting> listSettings){
        this.listSettings=listSettings;
    }

    public Setting get(String key){
        int index=listSettings.indexOf(new Setting(key));
        if(index>=0){
            return listSettings.get(index);
        }
        return null;
    }

    public String getValue(String key){
        Setting setting=get(key);
        if(setting!=null){
            return setting.getValue();
        }
        return null;
    }
    public Setting update(String key,String value){
        Setting setting=get(key);
        if(setting!=null && value!=null){
            setting.setValue(value);
            return setting;
        }
        return null;
    }

    public List<Setting> list(){
        return listSettings;
    }
}
