package com.eshope.consumer.Controller;

import com.eShope.common.entity.Article;
import com.eshope.consumer.Service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@Slf4j
public class MenuController {
    @Autowired
    private MenuService service;

    @GetMapping("/m/{alias}")
    public String viewMenu(@PathVariable(name = "alias") String alias, Model model) {

            log.info("HERE");
            Article article = service.getArticleBoundToMenu(alias);
            model.addAttribute("pageTitle", article.getTitle());
            model.addAttribute("article", article);
            return "myTest";

    }
}
