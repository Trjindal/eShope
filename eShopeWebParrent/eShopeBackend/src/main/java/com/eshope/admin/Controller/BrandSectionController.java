package com.eshope.admin.Controller;


import com.eShope.common.entity.Brand;
import com.eShope.common.entity.Section.BrandSection;
import com.eShope.common.entity.Section.Section;
import com.eShope.common.entity.Section.SectionType;
import com.eshope.admin.Service.BrandService;
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
public class BrandSectionController {

    @Autowired
    private SectionService sectionService;

    @Autowired
    private BrandService brandService;

    @GetMapping("/sections/new/brand")
    public String showForm(Model model) {
        Section section = new Section(true, SectionType.BRAND);
        List<Brand> listBrands = brandService.listAllBrands();

        model.addAttribute("listBrands", listBrands);
        model.addAttribute("section", section);
        model.addAttribute("pageTitle", "Add Brand Section");

        return "sections/brandSectionForm";
    }

    @PostMapping("/sections/save/brand")
    public String saveSection(Section section, HttpServletRequest request, RedirectAttributes ra) {
        addBrandsToSection(section, request);
        sectionService.saveSection(section);

        ra.addFlashAttribute("message", "The section of type Brand has been saved successfully.");
        return "redirect:/sections";
    }

    private void addBrandsToSection(Section section, HttpServletRequest request) {
        String[] IDs = request.getParameterValues("chosenBrands");

        if (IDs != null && IDs.length > 0) {
            for (int i = 0; i < IDs.length; i++) {
                String[] arrayIds = IDs[i].split("-");

                BrandSection brandSection = new BrandSection();

                Integer brandSectionId = Integer.valueOf(arrayIds[1]);
                if (brandSectionId > 0) {
                    brandSection.setId(brandSectionId);
                }

                brandSection.setBrandOrder(i);
                Integer brandId = Integer.valueOf(arrayIds[0]);

                brandSection.setBrand(new Brand(brandId));
                section.addBrandSection(brandSection);

            }
        }
    }

    @GetMapping("/sections/edit/Brand/{id}")
    public String editSection(@PathVariable(name = "id") Integer id, RedirectAttributes ra,
                              Model model) {
        try {
            Section section = sectionService.getSection(id);
            List<Brand> listBrands = brandService.listAllBrands();

            model.addAttribute("listBrands", listBrands);
            model.addAttribute("section", section);
            model.addAttribute("pageTitle", "Edit Brand Section (ID: " + id + ")");

            return "sections/brandSectionForm";

        } catch (UsernameNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
            return "redirect:/sections";
        }

    }
}