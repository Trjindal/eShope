package com.eshope.admin.Repository;

import com.eShope.common.entity.Menu;
import com.eShope.common.entity.MenuType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu,Integer> {

    public List<Menu> findAllByOrderByTypeAscPositionAsc();

    public List<Menu> findByTypeOrderByPositionAsc(MenuType type);

    public Long countByType(MenuType type);

    @Query("UPDATE Menu m SET m.enabled = ?2 WHERE m.id = ?1")
    @Modifying
    public void updateEnabledStatus(Integer id, boolean enabled);

    Menu getArticleByTitle(String title);

    Menu getArticleByAlias(String alias);
}
