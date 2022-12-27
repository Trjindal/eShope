package com.eshope.admin.Brand;

import com.eShope.common.entity.Brand;
import com.eShope.common.entity.Category;
import com.eShope.common.entity.Role;
import com.eshope.admin.Category.Exporter.CategoryCsvExporter;
import com.eshope.admin.Category.Exporter.CategoryExcelExporter;
import com.eshope.admin.Category.Exporter.CategoryPdfExporter;
import com.eshope.admin.Category.Service.CategoryService;
import com.eshope.admin.Main.Repositories.BrandRepository;
import com.eshope.admin.Main.Utility.FileUploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@Slf4j
public class BrandController {


    @Autowired
    BrandService brandService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    BrandRepository brandRepository;

    @GetMapping("/brands")
    public String listAllBrands(Model model){
        return listByPage(1,model,"id","asc",null);
    }

    @GetMapping("/brands/page/{pageNum}")
    public String listByPage(@PathVariable(name="pageNum") int pageNum, Model model, @Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword){
        Page<Brand> page=brandService.listByPage(pageNum,sortField,sortDir,keyword);
        List<Brand> listBrands=page.getContent();

        long startCount =(pageNum-1)*brandService.BRANDS_PER_PAGE+1;
        long endCount=startCount+brandService.BRANDS_PER_PAGE-1;
        if(endCount>page.getTotalElements()){
            endCount=page.getTotalElements();
        }

        model.addAttribute("currentPage",pageNum);
        model.addAttribute("startCount",startCount);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("endCount",endCount);
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("listAllBrands",listBrands);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("keyword", keyword );
        return "Brand/brand";
    }

    @GetMapping("/brands/new")
    public String newBrand( Model model){
        Brand brand=new Brand();
        List<Category> listCategories =categoryService.listCategoriesUsedInForm();

        model.addAttribute("brand",brand);
        model.addAttribute("listCategories",listCategories);

        return "Brand/brandForm.html";
    }

    @PostMapping("/brands/saveBrand")
    public String saveBrand(RedirectAttributes redirectAttributes, @Valid @ModelAttribute(value = "brand") Brand brand, Errors errors, Model model , @RequestParam("image") MultipartFile multipartFile) throws IOException {

//        log.error(multipartFile.getOriginalFilename());
//        log.error(String.valueOf(multipartFile.isEmpty()));
//        log.error(String.valueOf(errors.hasFieldErrors()));
////        errors.reject("typeMismatch","Please Upload a photo");
//        log.error(String.valueOf(errors.getFieldError()));
//        errors.rejectValue("image","typeMismatch","Please Upload a photo");


        //TO CHECK UNIQUE NAME
        if (brand.getName() != "" && !brandService.isNameUnique(brand.getName())) {
            log.error("Contact form validation failed due to name ");
            model.addAttribute("nameNotUnique", "There is another brand having same name");
            List<Category> listCategories = categoryService.listCategoriesUsedInForm();
            model.addAttribute("listCategories", listCategories);
            return "Brand/brandForm.html";
        }

        //DISPLAY ERROR FOR CATEGORY
        if(brand.getCategories().isEmpty()){
            log.error("Contact form validation failed due to category ");
            model.addAttribute("categoryNotProvided","Please specify at least 1 category");
            List<Category> listCategories = categoryService.listCategoriesUsedInForm();
            model.addAttribute("listCategories", listCategories);
            return "Brand/brandForm.html";
        }



////        DISPLAYING ERROR MESSAGES
//        if (errors.hasErrors()) {
//            for(ObjectError error:errors.getAllErrors()){
////                if(!(error.getCode().matches("typeMismatch"))){
//                    log.error(error.getCode());
//                    log.error(String.valueOf(error));
//                    log.error("New Brand form validation failed due to : " + errors.toString());
//                    List<Category> listCategories = categoryService.listCategoriesUsedInForm();
//                    model.addAttribute("listCategories", listCategories);
//                    return "Brand/brandForm.html";
//                }
//            }
////        }

        //DISPLAYING ERROR MESSAGES
        if(errors.hasErrors()){

            log.error("New Brand form validation failed due to : " + errors.toString());
            List<Category> listCategories = categoryService.listCategoriesUsedInForm();
            model.addAttribute("listCategories", listCategories);
            return "Brand/brandForm.html";
        }

        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            brand.setLogo(fileName);
            log.error(brand.getLogo());
            Brand savedBrand=brandRepository.save(brand);
            String uploadDir = "brand-photos/" + savedBrand.getId();
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            redirectAttributes.addFlashAttribute("message", "The Brand has been saved successfully");
            return "redirect:/brands";
        }
        return "Brand/brandForm.html";
    }


    @GetMapping("/brands/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        List<Brand> listBrands=brandService.listAllBrands();
        BrandCsvExporter exporter=new BrandCsvExporter();
        exporter.export(listBrands,response);
    }


    @GetMapping("/brands/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<Category> listCategories=categoryService.listAllCategories();
        CategoryExcelExporter exporter=new CategoryExcelExporter();
        exporter.export(listCategories,response);
    }

    @GetMapping("/brands/export/pdf")
    public void exportToPdf(HttpServletResponse response) throws IOException {
        log.error("Inside PDFFF");
        List<Category> listCategories=categoryService.listAllCategories();

        CategoryPdfExporter exporter=new CategoryPdfExporter();
        exporter.export(listCategories,response);
    }




}
