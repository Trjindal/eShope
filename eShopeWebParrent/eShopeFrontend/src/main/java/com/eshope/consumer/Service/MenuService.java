package com.eshope.consumer.Service;

import com.eShope.common.entity.Article;
import com.eShope.common.entity.Menu;
import com.eShope.common.entity.MenuType;
import com.eshope.consumer.Repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {
    @Autowired
    private MenuRepository repo;

    public List<Menu> getHeaderMenuItems() {
        return repo.findByTypeAndEnabledOrderByPositionAsc(MenuType.HEADER, true);
    }

    public List<Menu> getFooterMenuItems() {
        return repo.findByTypeAndEnabledOrderByPositionAsc(MenuType.FOOTER, true);
    }

    public Article getArticleBoundToMenu(String menuAlias)  {
        Menu menu = repo.findByAlias(menuAlias);
        if (menu == null) {
            throw new UsernameNotFoundException("No menu found with alias " + menuAlias);
        }

        return menu.getArticle();
    }
}