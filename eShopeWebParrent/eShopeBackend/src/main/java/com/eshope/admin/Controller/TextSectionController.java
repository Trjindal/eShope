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
public class TextSectionController {

    @Autowired
    private SectionService sectionService;

    @GetMapping("/sections/new/text")
    public String showForm(Model model) {
        Section section = new Section(true, SectionType.TEXT);

        model.addAttribute("section", section);
        model.addAttribute("pageTitle", "Add Text Section");

        return "sections/textSectionForm";
    }

    @PostMapping("/sections/save/text")
    public String saveSection(Section section, RedirectAttributes ra) {
        sectionService.saveSection(section);
        ra.addFlashAttribute("message", "The section of type Text has been saved successfully.");
        return "redirect:/sections";
    }

    @GetMapping("/sections/edit/Text/{id}")
    public String editSection(@PathVariable(name = "id") Integer id, RedirectAttributes ra,
                              Model model) {
        try {
            Section section = sectionService.getSection(id);

            model.addAttribute("section", section);
            model.addAttribute("pageTitle", "Edit Text Section (ID: " + id + ")");

            return "sections/textSectionForm";

        } catch (UsernameNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
            return "redirect:/sections";
        }

    }
}