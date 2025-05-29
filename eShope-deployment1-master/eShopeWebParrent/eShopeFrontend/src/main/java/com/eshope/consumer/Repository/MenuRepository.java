package com.eshope.consumer.Repository;

import com.eShope.common.entity.Menu;
import com.eShope.common.entity.MenuType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Integer> {

    public List<Menu> findByTypeAndEnabledOrderByPositionAsc(MenuType type, boolean enabled);

    @Query("Select m FROM Menu m WHERE m.alias = ?1 AND m.enabled = true")
    public Menu findByAlias(String alias);
}