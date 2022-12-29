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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
        log.error(String.valueOf(brand.getLogoPath()==""));
        return "Brand/brandForm.html";
    }

    @PostMapping("/brands/saveBrand")
    public String saveBrand(RedirectAttributes redirectAttributes, @Valid @ModelAttribute(value = "brand") Brand brand, Errors errors, Model model , @RequestParam("image") MultipartFile multipartFile) throws IOException {

        //TO CHECK UNIQUE NAME
        if (brand.getName() != "" && !brandService.isNameUnique(brand.getName())) {
            log.error("Contact form validation failed due to name ");
            model.addAttribute("brand",brand);
            model.addAttribute("nameNotUnique", "There is another brand having same name");
            List<Category> listCategories = categoryService.listCategoriesUsedInForm();
            model.addAttribute("listCategories", listCategories);
            return "Brand/brandForm.html";
        }

        //DISPLAY ERROR FOR CATEGORY
        if(brand.getCategories().isEmpty()){
            log.error("Contact form validation failed due to category ");
            model.addAttribute("brand",brand);
            model.addAttribute("categoryNotProvided","Please specify at least 1 category");
            List<Category> listCategories = categoryService.listCategoriesUsedInForm();
            model.addAttribute("listCategories", listCategories);
            return "Brand/brandForm.html";
        }




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


    @GetMapping("/brands/edit/{id}")
    public String editBrand(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model, HttpSession session){
        try{
            Brand brand=brandService.getBrandById(id);
            log.error(brand.getName());
            List<Category> listCategories =categoryService.listCategoriesUsedInForm();
            model.addAttribute("brand",brand);
            model.addAttribute("brands",brand);
            session.setAttribute("id",id);
            model.addAttribute("listCategories",listCategories);
            return "Brand/brandUpdateForm.html";
        }catch (UsernameNotFoundException ex){
            redirectAttributes.addFlashAttribute("message",ex.getMessage());

            return "redirect:/brands";
        }

    }

    //
    @PostMapping("/brands/editBrand")
    public String editBrandSave(RedirectAttributes redirectAttributes, @Valid @ModelAttribute(value = "brand") Brand brand, Errors errors, Model model, HttpSession session , @RequestParam("image")MultipartFile multipartFile) throws IOException{

        Integer id= (Integer) session.getAttribute("id");
        log.error(String.valueOf(id));
        Brand existingBrand=brandService.getBrandById(id);


        //TO CHECK UNIQUE NAME
        if (existingBrand!=null && !(existingBrand.getName().matches(brand.getName()))) {
        if (brand.getName() != "" && !brandService.isNameUnique(brand.getName())) {
            log.error("Contact form validation failed due to name ");
            model.addAttribute("brand",brand);
            model.addAttribute("brands",brand);
            model.addAttribute("nameNotUnique", "There is another brand having same name");
            List<Category> listCategories = categoryService.listCategoriesUsedInForm();
            model.addAttribute("listCategories", listCategories);
            return "Brand/brandUpdateForm.html";
        }
        }


        //DISPLAY ERROR FOR CATEGORY
        if(brand.getCategories().isEmpty()){
            log.error("Brand form validation failed due to category ");
            model.addAttribute("brand",brand);
            model.addAttribute("brands",brand);
            model.addAttribute("categoryNotProvided","Please specify at least 1 category");
            List<Category> listCategories = categoryService.listCategoriesUsedInForm();
            model.addAttribute("listCategories", listCategories);
            return "Brand/brandUpdateForm.html";
        }


//        DISPLAYING ERROR MESSAGES
        if (errors.hasErrors()) {
            for(ObjectError error:errors.getAllErrors()){
                if(!(error.getCode().matches("typeMismatch"))){
                    log.error(error.getCode());
                    log.error(String.valueOf(error));
                    log.error("New Brand form validation failed due to : " + errors.toString());
                    List<Category> listCategories = categoryService.listCategoriesUsedInForm();
                    model.addAttribute("listCategories", listCategories);
                    return "Brand/brandUpdateForm.html";
                }
            }
        }


        //        PHOTOS SAVE

        if(!multipartFile.isEmpty()){
            String fileName= StringUtils.cleanPath(multipartFile.getOriginalFilename());
            existingBrand.setLogo(fileName);
            Brand savedBrand=brandRepository.save(existingBrand);
            String uploadDir="brand-photos/"+savedBrand.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
        }


        existingBrand.setName(brand.getName());
        existingBrand.getCategories().clear();
        existingBrand.setCategories(brand.getCategories());



        //SAVE DETAILS
        brandRepository.save(existingBrand);


        redirectAttributes.addFlashAttribute("message","The Brand has been edited successfully");
        return "redirect:/brands";

    }


    @GetMapping("/brands/delete/{id}")
    public String deleteBrand(@PathVariable(name="id")Integer id,Model model,RedirectAttributes redirectAttributes){
        try{
            brandService.delete(id);
            redirectAttributes.addFlashAttribute("message","The brand ID "+id+" has been deleted successfully");
        }catch (UsernameNotFoundException ex){
            redirectAttributes.addFlashAttribute("message",ex.getMessage());
        }
        return "redirect:/brands";
    }



    @GetMapping("/brands/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        List<Brand> listBrands=brandService.listAllBrands();
        BrandCsvExporter exporter=new BrandCsvExporter();
        exporter.export(listBrands,response);
    }


    @GetMapping("/brands/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<Brand> listBrands=brandService.listAllBrands();
        BrandExcelExporter exporter=new BrandExcelExporter();
        exporter.export(listBrands,response);
    }

    @GetMapping("/brands/export/pdf")
    public void exportToPdf(HttpServletResponse response) throws IOException {
        List<Brand> listBrands=brandService.listAllBrands();
        BrandPdfExporter exporter=new BrandPdfExporter();
        exporter.export(listBrands,response);
    }




}
