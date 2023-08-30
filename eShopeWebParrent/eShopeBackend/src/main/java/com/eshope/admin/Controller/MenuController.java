package com.eshope.admin.Controller;

import com.eShope.common.entity.Article;
import com.eShope.common.entity.Menu;
import com.eshope.admin.Exception.MenuUnmoveableException;
import com.eshope.admin.Service.ArticleService;
import com.eshope.admin.Service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@Slf4j
public class MenuController {
    private final String defaultRedirectURL = "redirect:/menus";

    @Autowired
    private MenuService menuService;
    @Autowired private ArticleService articleService;

    @GetMapping("/menus")
    public String listAll(Model model) {
        List<Menu> listMenuItems = menuService.listAll();
        model.addAttribute("listMenuItems", listMenuItems);

        return "Menu/menu.html";
    }

    @GetMapping("menus/new")
    public String newMenu(Model model) {
        List<Article> listArticles = articleService.listArticlesForMenu();

        model.addAttribute("menu", new Menu());
        model.addAttribute("listArticles", listArticles);
        model.addAttribute("pageTitle", "Create New Menu Item");

        return "Menu/menuForm";
    }

    @PostMapping("/menus/save")
    public String saveMenu(@Valid @ModelAttribute(value = "menu") Menu menu, Errors
            errors, Model model ,RedirectAttributes redirectAttributes) {
        List<Article> listArticles = articleService.listArticlesForMenu();

        if(menu.getId()==null){
            //TO CHECK UNIQUE TITLE
            if (menu.getTitle() != "" && !menuService.isTitleUnique(menu.getTitle())) {
                log.error("Menu form validation failed due to name ");
                model.addAttribute("titleNotUnique", "There is another menu having same title");
                model.addAttribute("listArticles", listArticles);
                return "Menu/menuForm";

            }
            //TO CHECK UNIQUE ALIAS
            if (menu.getAlias() != "" && !menuService.isAliasUnique(menu.getAlias())) {
                log.error("Menu form validation failed due to alias ");
                model.addAttribute("AliasNotUnique", "There is another menu having same alias");
                model.addAttribute("listArticles", listArticles);
                return "Menu/menuForm";
            }
            //DISPLAYING ERROR MESSAGES
            if (errors.hasErrors()) {
                model.addAttribute("listArticles", listArticles);
                log.error("New Menu form validation failed due to : " + errors.toString());
                return "Menu/menuForm";
            }


        }else{
            Menu previousMenu=menuService.getMenuById(menu.getId());
            //TO CHECK UNIQUE TITLE

            if (menu.getTitle() != ""&& !menu.getTitle().equals(previousMenu.getTitle()) && !menuService.isTitleUnique(menu.getTitle())) {
                log.error("Menu form validation failed due to name ");
                model.addAttribute("titleNotUnique", "There is another menu having same title");
                return "Menus/menuUpdateForm";

            }
            //TO CHECK UNIQUE ALIAS
            if (menu.getAlias() != "" &&!menu.getAlias().equals(previousMenu.getAlias())&& !menuService.isAliasUnique(menu.getAlias())) {
                log.error("Menu form validation failed due to alias ");
                model.addAttribute("AliasNotUnique", "There is another menu having same alias");
                return "Menus/menuUpdateForm";

            }

            //DISPLAYING ERROR MESSAGES
            if (errors.hasErrors()) {
                model.addAttribute("listArticles", listArticles);
                log.error("New Menu form validation failed due to : " + errors.toString());
                return "Menu/menuUpdateForm";
            }
        }

        menuService.save(menu);
        redirectAttributes.addFlashAttribute("message", "The menu item has been saved successfully.");

        return defaultRedirectURL;
    }

    @GetMapping("/menus/edit/{id}")
    public String editMenu(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes ra) {
        try {
            Menu menu = menuService.getMenuById(id);
            List<Article> listArticles = articleService.listArticlesForMenu();

            model.addAttribute("menu", menu);
            model.addAttribute("listArticles", listArticles);

            return "Menu/menuUpdateForm";
        } catch (UsernameNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
            return defaultRedirectURL;
        }
    }

    @GetMapping("/menus/{id}/enabled/{enabledStatus}")
    public String updateMenuEnabledStatus(@PathVariable("id") Integer id,
                                          @PathVariable("enabledStatus") String enabledStatus, RedirectAttributes ra) {
        try {
            boolean enabled = Boolean.parseBoolean(enabledStatus);
            menuService.updateEnabledStatus(id, enabled);

            String updateResult = enabled ? "enabled." : "disabled.";
            ra.addFlashAttribute("message", "The menu item ID " + id + " has been " + updateResult);
        } catch (UsernameNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
        }

        return defaultRedirectURL;
    }

    @GetMapping("/menus/delete/{id}")
    public String deleteMenu(@PathVariable(name = "id") Integer id, RedirectAttributes ra) {
        try {
            menuService.delete(id);

            ra.addFlashAttribute("message", "The menu item ID " + id + " has been deleted.");
        } catch (UsernameNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
        }

        return defaultRedirectURL;
    }

    @GetMapping("/menus/{direction}/{id}")
    public String moveMenu(@PathVariable("direction") String direction, @PathVariable("id") Integer id,
                           RedirectAttributes ra) {
        try {
            MenuService.MenuMoveDirection moveDirection = MenuService.MenuMoveDirection.valueOf(direction.toUpperCase());
            menuService.moveMenu(id, moveDirection);

            ra.addFlashAttribute("message", "The menu ID " + id + " has been moved up by one position.");

        } catch (MenuUnmoveableException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
        } catch (UsernameNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
        }

        return defaultRedirectURL;
    }
}
