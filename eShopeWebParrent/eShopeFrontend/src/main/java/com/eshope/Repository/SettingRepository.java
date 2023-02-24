package com.eshope.Repository;

import com.eShope.common.entity.Setting.Setting;
import com.eShope.common.entity.Setting.SettingCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SettingRepository extends CrudRepository<Setting,String> {

    public List<Setting> findByCategory(SettingCategory category);

    @Query("SELECT s FROM Setting s WHERE s.category=?1 OR s.category=?2")
    public List<Setting> findByTwoCategory(SettingCategory category1,SettingCategory category2);

    public Setting findByKey(String key);


}
