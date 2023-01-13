package com.eshope.admin.Controller;

import com.eShope.common.entity.Currency;
import com.eShope.common.entity.Setting;
import com.eshope.admin.Entity.GeneralSettingBag;
import com.eshope.admin.Repository.CurrencyRepository;
import com.eshope.admin.Service.SettingService;
import com.eshope.admin.Utility.FileUploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
public class SettingController {

    @Autowired
    private SettingService settingService;

    @Autowired
    private CurrencyRepository currencyRepository;

    @GetMapping("/settings")
    public String listAll(Model model){

        List<Setting> listSettings=settingService.listAllSettings();
        List<Currency> listCurrencies=currencyRepository.findAllByOrderByNameAsc();

        model.addAttribute("listCurrencies",listCurrencies);

        for(Setting setting:listSettings){
            model.addAttribute(setting.getKey(),setting.getValue());
        }

        return "Settings/settings";
    }

    @PostMapping("/settings/save_general")
    public String saveGeneralSettings(@RequestParam("image")MultipartFile multipartFile, HttpServletRequest request, RedirectAttributes redirectAttributes) throws IOException {

        GeneralSettingBag settingBag=settingService.getGeneralSettings();

        settingService.saveSiteLogo(multipartFile,settingBag);
        settingService.saveCurrencySymbol(request,settingBag);

        updateSettingValuesFromForm(request,settingBag.list());
        redirectAttributes.addFlashAttribute("message","General settings have been saved");
        return "redirect:/settings";
    }

    private void updateSettingValuesFromForm(HttpServletRequest request,List<Setting> listSetting){
        for(Setting setting:listSetting){
//            log.error(String.valueOf(setting));
            String value=request.getParameter(setting.getKey());
            String key=request.getParameter(setting.getValue());
//            log.error("KEY "+key+" VALUE "+value);
            if(value!=null){
                setting.setValue(value);
                log.error("Updated "+String.valueOf(setting));
            }
        }
        settingService.saveAll(listSetting);
    }



}
