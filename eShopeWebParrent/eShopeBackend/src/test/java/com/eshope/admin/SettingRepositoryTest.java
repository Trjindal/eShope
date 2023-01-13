package com.eshope.admin;


import com.eShope.common.entity.Setting;
import com.eShope.common.entity.SettingCategory;
import com.eshope.admin.Repository.SettingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class SettingRepositoryTest {

    @Autowired
    SettingRepository settingRepository;

    @Test
    public void testCreateGeneralSettings(){
//        Setting siteName=new Setting("SITE_NAME","eShope", SettingCategory.GENERAL);
        Setting siteLogo=new Setting("SITE_LOGO","None",SettingCategory.GENERAL);
        Setting copyright=new Setting("COPYRIGHT","© 2022 eShope. All rights reserved.",SettingCategory.GENERAL);

        settingRepository.saveAll(List.of(siteLogo,copyright));

        Iterable<Setting> iterable=settingRepository.findAll();

        assertThat(iterable).size().isGreaterThan(0);
    }

    @Test
    public void testCreateCurrencySettings(){
        Setting currencyId=new Setting("CURRENCY_ID","1",SettingCategory.CURRENCY);
        Setting symbol=new Setting("CURRENCY_SYMBOL","$",SettingCategory.CURRENCY);
        Setting symbolPosition=new Setting("CURRENCY_SYMBOL_POSITION","before",SettingCategory.CURRENCY);
        Setting decimalPointType=new Setting("DECMIAL_POINT_TYPE","POINT",SettingCategory.CURRENCY);
        Setting decimalDigits=new Setting("DECIMAL_DIGITS","2",SettingCategory.CURRENCY);
        Setting thousandPointType=new Setting("THOUSANDS_POINT_TYPE","COMMA",SettingCategory.CURRENCY);

        settingRepository.saveAll(List.of(currencyId,symbol,symbolPosition,decimalPointType,decimalDigits,thousandPointType));
    }

    @Test
    public void testListSettingByCategory(){
        List<Setting> settings=settingRepository.findByCategory(SettingCategory.GENERAL);

        settings.forEach(System.out::println);
    }
}
