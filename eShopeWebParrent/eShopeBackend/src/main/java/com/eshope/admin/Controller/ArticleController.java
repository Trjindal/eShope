package com.eshope.admin.Controller;


import com.eShope.common.entity.Article;
import com.eShope.common.entity.Category;
import com.eshope.admin.Security.EshopeUserDetails;
import com.eshope.admin.Service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@Slf4j
public class ArticleController {
    private String defaultRedirectURL = "redirect:/articles/page/1?sortField=title&sortDir=asc";

    @Autowired
    private ArticleService articleService;

    @GetMapping("/articles")
    public String listFirstPage(Model model) {
        return defaultRedirectURL;
    }

    @GetMapping("/articles/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model, @Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword) {
        Page<Article> page= articleService.listByPage(pageNum, sortField, sortDir, keyword);
        List<Article> listArticles = page.getContent();

        long startCount = (pageNum - 1) * articleService.ARTICLES_PER_PAGE + 1;
        long endCount = startCount + articleService.ARTICLES_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startCount", startCount);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listAllArticles", listArticles);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("keyword", keyword);
        return "Articles/article";
    }

    @GetMapping("/articles/new")
    public String newArticle(Model model) {
        model.addAttribute("article", new Article());
        model.addAttribute("pageTitle", "Create New Article");

        return "Articles/articleForm";
    }

    @PostMapping("/articles/save")
    public String saveArticle(RedirectAttributes redirectAttributes, @Valid @ModelAttribute(value = "article") Article article, Errors
        errors, Model model, @AuthenticationPrincipal EshopeUserDetails userDetails)  {

        if(article.getId()==null){
            //TO CHECK UNIQUE TITLE
            if (article.getTitle() != "" && !articleService.isTitleUnique(article.getTitle())) {
                log.error("Article form validation failed due to name ");
                model.addAttribute("titleNotUnique", "There is another article having same title");
                return "Articles/articleForm";

            }
            //TO CHECK UNIQUE ALIAS
            if (article.getAlias() != "" && !articleService.isAliasUnique(article.getAlias())) {
                log.error("Article form validation failed due to alias ");
                model.addAttribute("AliasNotUnique", "There is another article having same alias");
                return "Articles/articleForm";

            }
            //DISPLAYING ERROR MESSAGES
            if (errors.hasErrors()) {

                log.error("New Article form validation failed due to : " + errors.toString());
                return "Articles/articleForm";
            }
        }else{
            Article previousArticle=articleService.getArticleById(article.getId());
            //TO CHECK UNIQUE TITLE

            if (article.getTitle() != ""&& !article.getTitle().equals(previousArticle.getTitle()) && !articleService.isTitleUnique(article.getTitle())) {
                log.error("Article form validation failed due to name ");
                model.addAttribute("titleNotUnique", "There is another article having same title");
                return "Articles/articleUpdateForm";

            }
            //TO CHECK UNIQUE ALIAS
            if (article.getAlias() != "" &&!article.getAlias().equals(previousArticle.getAlias())&& !articleService.isAliasUnique(article.getAlias())) {
                log.error("Article form validation failed due to alias ");
                model.addAttribute("AliasNotUnique", "There is another article having same alias");
                return "Articles/articleUpdateForm";

            }
            //DISPLAYING ERROR MESSAGES
            if (errors.hasErrors()) {

                log.error("New Article form validation failed due to : " + errors.toString());
                return "Articles/articleUpdateForm";
            }
        }



            articleService.save(article, userDetails.getUser());

        redirectAttributes.addFlashAttribute("message", "The article has been saved successfully.");

        return defaultRedirectURL;
    }

    @GetMapping("/articles/edit/{id}")
    public String editArticle(@PathVariable(name = "id") Integer id, Model model,
                              RedirectAttributes ra) {
        try {
            Article article = articleService.getArticleById(id);
            model.addAttribute("article", article);
            model.addAttribute("pageTitle", "Edit Article (ID: " + id + ")");

            return "Articles/articleUpdateForm";

        } catch (UsernameNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());

            return defaultRedirectURL;
        }
    }

    @GetMapping("/articles/detail/{id}")
    public String viewArticle(@PathVariable(name = "id") Integer id, RedirectAttributes ra,  Model model) {
        try {
            Article article = articleService.getArticleById(id);
            model.addAttribute("article", article);

            return "articles/article_detail_modal";

        } catch (UsernameNotFoundException ex) {
            ra.addFlashAttribute("message", "Could not find any article with ID " + id);
            return defaultRedirectURL;
        }
    }

    @GetMapping("/articles/{id}/enabled/{publishStatus}")
    public String publishArticle(@PathVariable("id") Integer id,
                                 @PathVariable("publishStatus") String publishStatus, RedirectAttributes ra) {
        try {
            boolean published = Boolean.parseBoolean(publishStatus);
            articleService.updateArticlePublishStatusById(id, published);

            String publishResult = published ? "published." : "unpublished.";
            ra.addFlashAttribute("message", "The article ID " + id + " has been " + publishResult);
        } catch (UsernameNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
        }

        return defaultRedirectURL;
    }

    @GetMapping("/articles/delete/{id}")
    public String deleteArticle(@PathVariable(name = "id") Integer id, RedirectAttributes ra) {
        try {
            log.info("HEREEE");
            articleService.deleteArticleById(id);
            ra.addFlashAttribute("message", "The article ID " + id + " has been deleted.");
        } catch (UsernameNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
        }

        return defaultRedirectURL;
    }
}