package com.eshope.admin.Controller;

import com.eShope.common.entity.Section.Section;
import com.eShope.common.entity.Section.SectionType;
import com.eshope.admin.Service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AllCategoriesSectionController {

    @Autowired
    private SectionService sectionService;

    @GetMapping("/sections/new/all_categories")
    public String showForm(Model model) {
        Section section = new Section(true, SectionType.ALL_CATEGORIES);

        model.addAttribute("section", section);
        model.addAttribute("pageTitle", "Add All Categories Section");

        return "Sections/allCategoriesSectionForm";
    }

    @PostMapping("/sections/save/all_categories")
    public String saveSection(Section section, RedirectAttributes ra) {
        sectionService.saveSection(section);
        ra.addFlashAttribute("message", "The section of type All Categories has been saved successfully.");

        return "redirect:/sections";
    }

    @GetMapping("/sections/edit/All_Categories/{id}")
    public String editSection(@PathVariable(name = "id") Integer id, RedirectAttributes ra,
                              Model model) {
        try {
            Section section = sectionService.getSection(id);

            model.addAttribute("section", section);
            model.addAttribute("pageTitle", "Edit All Categories Section (ID: " + id + ")");

            return "Sections/allCategoriesSectionForm";

        } catch (UsernameNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
            return "redirect:/sections";
        }

    }
}