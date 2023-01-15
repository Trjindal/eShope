package com.eshope.Service;

import com.eShope.common.entity.Currency;
import com.eShope.common.entity.Setting;
import com.eShope.common.entity.SettingCategory;
import com.eshope.Repository.SettingRepository;

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



//    public void saveSiteLogo(MultipartFile multipartFile, GeneralSettingBag settingBag) throws IOException {
//        if(!multipartFile.isEmpty()){
//            String fileName= StringUtils.cleanPath(multipartFile.getOriginalFilename());
//            String value="./site-logo/"+fileName;
//            Setting setting=settingBag.updateSiteLogo(value);
//            settingRepository.save(setting);
//            String uploadDir="./site-logo/";
//            FileUploadUtil.cleanDir(uploadDir);
//            FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
//
//        }
//    }
//    public void saveCurrencySymbol(HttpServletRequest request, GeneralSettingBag settingBag){
//        Integer currencyId=Integer.parseInt(request.getParameter("CURRENCY_ID"));
//        Optional<Currency> findByIdResult=currencyRepository.findById(currencyId);
//
//        if(findByIdResult.isPresent()){
//            Currency currency=findByIdResult.get();
//            Setting settingForCurrencySymbol=settingBag.updateCurrencySymbol(currency.getSymbol());
//            Setting settingForCurrencyId=settingBag.updateCurrencyId(String.valueOf(currencyId));
//            settingRepository.save(settingForCurrencySymbol);
//
//        }
//    }
}
