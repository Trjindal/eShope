package com.eshope;

import com.eShope.common.entity.Setting;
import com.eShope.common.entity.SettingCategory;
import com.eshope.Repository.SettingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class SettingRepositoryTests {

    @Autowired
    public SettingRepository settingRepository;

    @Test
    public void testFindByTwoCategories(){
        List<Setting> settings =settingRepository.findByTwoCategory(SettingCategory.GENERAL,SettingCategory.CURRENCY);
        settings.forEach(System.out::println);
    }
}
