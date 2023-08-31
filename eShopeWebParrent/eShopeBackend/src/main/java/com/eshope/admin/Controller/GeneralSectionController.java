package com.eshope.admin.Controller;


import java.util.List;

import com.eShope.common.entity.Section.Section;
import com.eshope.admin.Exception.SectionUnmoveableException;
import com.eshope.admin.Service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class GeneralSectionController {

    @Autowired private SectionService service;

    @GetMapping("/sections")
    public String listAllSections(Model model) {
        List<Section> listSections = service.listSections();
        model.addAttribute("listSections", listSections);

        return "Sections/section";
    }

    @GetMapping("/sections/delete/{id}")
    public String deleteSection(@PathVariable(name = "id") Integer id, RedirectAttributes ra) {
        try {
            service.deleteSection(id);
            ra.addFlashAttribute("message", "The section ID " + id + " has been deleted.");

        } catch (UsernameNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
        }

        return "redirect:/sections";
    }

    @GetMapping("/sections/{id}/enabled/{enabledStatus}")
    public String updateSectionEnabledStatus(@PathVariable(name = "id") Integer id, @PathVariable("enabledStatus") String enabledStatus,
                                             RedirectAttributes ra) {
        try {
            boolean enabled = Boolean.parseBoolean(enabledStatus);
            service.updateSectionEnabledStatus(id, enabled);
            String updateResult = enabled ? "enabled." : "disabled.";
            ra.addFlashAttribute("message", "The section ID " + id + " has been " + updateResult);

        } catch (UsernameNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
        }

        return "redirect:/sections";
    }

    @GetMapping("/sections/moveup/{id}")
    public String moveSectionUp(@PathVariable(name = "id") Integer id, RedirectAttributes ra) {
        try {
            service.moveSectionUp(id);

            ra.addFlashAttribute("message", "The section ID " + id + " has been moved up by one position.");

        } catch (SectionUnmoveableException | UsernameNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
        }

        return "redirect:/sections";
    }

    @GetMapping("/sections/movedown/{id}")
    public String moveSectionDown(@PathVariable(name = "id") Integer id, RedirectAttributes ra) {
        try {
            service.moveSectionDown(id);
            ra.addFlashAttribute("message", "The section ID " + id + " has been moved down by one position.");

        } catch (SectionUnmoveableException | UsernameNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
        }

        return "redirect:/sections";
    }
}