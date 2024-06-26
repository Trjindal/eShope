package com.eshope.admin.Controller;

import javax.servlet.http.HttpServletRequest;

import com.eShope.common.entity.Product.Product;
import com.eShope.common.entity.Section.ProductSection;
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
public class ProductSectionController {

    @Autowired
    private SectionService service;

    @GetMapping("/sections/new/product")
    public String showForm(Model model) {
        Section section = new Section(true, SectionType.PRODUCT);

        model.addAttribute("section", section);
        model.addAttribute("pageTitle", "Add Product Section");

        return "sections/productSectionForm";
    }


    @PostMapping("/sections/save/product")
    public String saveSection(Section section, HttpServletRequest request, RedirectAttributes ra) {
        addProductsToSection(section, request);
        service.saveSection(section);
        ra.addFlashAttribute("message", "The section of type Product has been saved successfully.");
        return "redirect:/sections";
    }

    private void addProductsToSection(Section section, HttpServletRequest request) {
        String[] productIds = request.getParameterValues("productId");
        String[] productSectionIds = request.getParameterValues("productSectionId");

        if (productIds != null && productIds.length > 0) {
            for (int i = 0; i < productIds.length; i++) {
                ProductSection productSection = new ProductSection();

                if (productSectionIds != null && productSectionIds.length > 0) {
                    if (i < productSectionIds.length) {
                        Integer productSectionId = Integer.valueOf(productSectionIds[i]);
                        productSection.setId(productSectionId);
                    }
                }

                productSection.setProductOrder(i);
                Integer productId = Integer.valueOf(productIds[i]);
                productSection.setProduct(new Product(productId));

                section.addProductSection(productSection);
            }
        }
    }

    @GetMapping("/sections/edit/Product/{id}")
    public String editSection(@PathVariable(name = "id") Integer id, RedirectAttributes ra,
                              Model model) {
        try {
            Section section = service.getSection(id);

            model.addAttribute("section", section);
            model.addAttribute("pageTitle", "Edit Product Section (ID: " + id + ")");

            return "sections/productSectionForm";

        } catch (UsernameNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
            return "redirect:/sections";
        }

    }

}