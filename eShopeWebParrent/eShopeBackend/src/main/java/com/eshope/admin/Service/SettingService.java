package com.eshope.admin.Service;

import com.eShope.common.entity.Currency;
import com.eShope.common.entity.Setting;
import com.eShope.common.entity.SettingCategory;
import com.eshope.admin.Entity.GeneralSettingBag;
import com.eshope.admin.Repository.CurrencyRepository;
import com.eshope.admin.Repository.SettingRepository;
import com.eshope.admin.Utility.FileUploadUtil;

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

    @Autowired
    private CurrencyRepository currencyRepository;

    public List<Setting> listAllSettings(){
        return (List<Setting>) settingRepository.findAll();
    }

    public GeneralSettingBag getGeneralSettings(){
        List<Setting> settings=new ArrayList<>();

        List<Setting> generalSettings=settingRepository.findByCategory(SettingCategory.GENERAL);
        List<Setting> currencySettings=settingRepository.findByCategory(SettingCategory.CURRENCY);

        settings.addAll(generalSettings);
        settings.addAll(currencySettings);

        return new GeneralSettingBag(settings);
    }

    public void saveSiteLogo(MultipartFile multipartFile, GeneralSettingBag settingBag) throws IOException {
        if(!multipartFile.isEmpty()){
            String fileName= StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String value="/site-logo/"+fileName;
            Setting setting=settingBag.updateSiteLogo(value);
            settingRepository.save(setting);
            String uploadDir="./site-logo/";
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);

        }
    }
    public void saveCurrencySymbol(HttpServletRequest request, GeneralSettingBag settingBag){
        Integer currencyId=Integer.parseInt(request.getParameter("CURRENCY_ID"));
        Optional<Currency> findByIdResult=currencyRepository.findById(currencyId);

        if(findByIdResult.isPresent()){
            Currency currency=findByIdResult.get();
            Setting settingForCurrencySymbol=settingBag.updateCurrencySymbol(currency.getSymbol());
            Setting settingForCurrencyId=settingBag.updateCurrencyId(String.valueOf(currencyId));
            settingRepository.save(settingForCurrencySymbol);
        }
    }

    public void saveAll(Iterable<Setting> settings){
        settingRepository.saveAll(settings);
    }


    public List<Setting> getMailServerSettings(){
        return settingRepository.findByCategory(SettingCategory.MAIL_SERVER);
    }

    public List<Setting> getMailTemplateSettings(){
        return settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES);
    }
    public List<Setting> getCurrencySettings() {
        return settingRepository.findByCategory(SettingCategory.CURRENCY);

    }

    public List<Setting> getPaymentSettings(){
        return settingRepository.findByCategory(SettingCategory.PAYMENT);
    }



}
