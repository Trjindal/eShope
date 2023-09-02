package com.eshope.admin.Controller;

import com.eShope.common.entity.Currency;
import com.eShope.common.entity.Setting.Constants;
import com.eShope.common.entity.Setting.Setting;
import com.eshope.admin.Entity.GeneralSettingBag;
import com.eshope.admin.Repository.CurrencyRepository;
import com.eshope.admin.Service.SettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

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
        model.addAttribute("S3_BASE_URI", Constants.S3_BASE_URI);

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



    @PostMapping("/settings/save_mail_server")
    public String saveMailServerSettings( HttpServletRequest request, RedirectAttributes redirectAttributes){
        List<Setting> mailServerSettings=settingService.getMailServerSettings();
        updateSettingValuesFromForm(request,mailServerSettings);
        redirectAttributes.addFlashAttribute("message","Mail server settings have been saved");
        return "redirect:/settings";
    }

    @PostMapping("/settings/save_payment")
    public String savePaymentSettings( HttpServletRequest request, RedirectAttributes redirectAttributes){
        log.error("HERE");
        List<Setting> paymentSettings=settingService.getPaymentSettings();
        updateSettingValuesFromForm(request,paymentSettings);
        redirectAttributes.addFlashAttribute("message","Payment Settings settings have been saved");
        return "redirect:/settings#general";
    }

    @PostMapping("/settings/save_mail_templates")
    public String saveMailTemplateSettings( HttpServletRequest request, RedirectAttributes redirectAttributes){
        List<Setting> mailTemplateSettings=settingService.getMailTemplateSettings();
        updateSettingValuesFromForm(request,mailTemplateSettings);
        redirectAttributes.addFlashAttribute("message","Mail template settings have been saved");
        return "redirect:/settings";
    }

    private void updateSettingValuesFromForm(HttpServletRequest request,List<Setting> listSetting){
        for(Setting setting:listSetting){
            log.error(String.valueOf(setting));
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
