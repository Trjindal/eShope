package com.eshope.admin.Repository;

import com.eShope.common.entity.Setting.Setting;
import com.eShope.common.entity.Setting.SettingCategory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface SettingRepository extends CrudRepository<Setting,String> {

    public List<Setting> findByCategory(SettingCategory category);

    @Transactional
    @Modifying
    @Query("Update Setting s SET s.value=?2 where s.key=?1")
   public void updateByKey(String key, String value);
}
