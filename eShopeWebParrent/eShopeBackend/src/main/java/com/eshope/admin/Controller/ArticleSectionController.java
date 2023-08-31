package com.eshope.admin.Controller;

import com.eShope.common.entity.Article;
import com.eShope.common.entity.Section.ArticleSection;
import com.eShope.common.entity.Section.Section;
import com.eShope.common.entity.Section.SectionType;
import com.eshope.admin.Service.ArticleService;
import com.eshope.admin.Service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ArticleSectionController {

    @Autowired
    private SectionService sectionService;

    @Autowired
    private ArticleService articleService;

    @GetMapping("/sections/new/article")
    public String showForm(Model model) {
        Section section = new Section(true, SectionType.ARTICLE);
        List<Article> listArticles = articleService.listAll();

        model.addAttribute("listArticles", listArticles);
        model.addAttribute("section", section);
        model.addAttribute("pageTitle", "Add Article Section");

        return "sections/articleSectionForm";
    }

    @PostMapping("/sections/save/article")
    public String saveSection(Section section, HttpServletRequest request, RedirectAttributes ra) {
        addArticlesToSection(section, request);
        sectionService.saveSection(section);
        ra.addFlashAttribute("message", "The section of type Article has been saved successfully.");
        return "redirect:/sections";
    }

    private void addArticlesToSection(Section section, HttpServletRequest request) {
        String[] IDs = request.getParameterValues("chosenArticles");

        if (IDs != null && IDs.length > 0) {
            for (int i = 0; i < IDs.length; i++) {
                String[] arrayIds = IDs[i].split("-");

                ArticleSection articleSection = new ArticleSection();

                Integer articleSectionId = Integer.valueOf(arrayIds[1]);
                if (articleSectionId > 0) {
                    articleSection.setId(articleSectionId);
                }

                articleSection.setArticleOrder(i);
                Integer articleId = Integer.valueOf(arrayIds[0]);

                articleSection.setArticle(new Article(articleId));
                section.addArticleSection(articleSection);

            }
        }
    }

    @GetMapping("/sections/edit/Article/{id}")
    public String editSection(@PathVariable(name = "id") Integer id, RedirectAttributes ra,
                              Model model) {
        try {
            Section section = sectionService.getSection(id);
            List<Article> listArticles = articleService.listAll();

            model.addAttribute("listArticles", listArticles);
            model.addAttribute("section", section);
            model.addAttribute("pageTitle", "Edit Article Section (ID: " + id + ")");

            return "sections/articleSectionForm";

        } catch (UsernameNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
            return "redirect:/sections";
        }

    }
}