package com.eshope.admin.Category.Controller;

import com.eShope.common.entity.Category;


import com.eShope.common.entity.User;
import com.eshope.admin.Category.Exporter.CategoryCsvExporter;
import com.eshope.admin.Category.Exporter.CategoryExcelExporter;
import com.eshope.admin.Category.Exporter.CategoryPdfExporter;
import com.eshope.admin.Category.Service.CategoryService;

import com.eshope.admin.Main.Utility.FileUploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
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


@Slf4j
@Controller
public class CategoryController {


   @Autowired
   CategoryService categoryService;

    @GetMapping("/categories")
    public String listAllCategories(Model model){
            return listByPage(1,model,"id","asc",null);
    }


    @GetMapping("/categories/page/{pageNum}")
    public String listByPage(@PathVariable(name="pageNum") int pageNum, Model model, @Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword){
        Page<Category> page=categoryService.listByPage(pageNum,sortField,sortDir,keyword);
        List<Category> listCategories=page.getContent();

        long startCount =(pageNum-1)*categoryService.CATEGORIES_PER_PAGE+1;
        long endCount=startCount+categoryService.CATEGORIES_PER_PAGE-1;
        if(endCount>page.getTotalElements()){
            endCount=page.getTotalElements();
        }

        model.addAttribute("currentPage",pageNum);
        model.addAttribute("startCount",startCount);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("endCount",endCount);
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("listAllCategories",listCategories);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("keyword", keyword );
        return "Category/category";
    }

    @GetMapping("/categories/new")
    public String newCategory( Model model){


        Category category=new Category();
        category.setEnabled(true);

        List<Category> listCategories =categoryService.listCategoriesUsedInForm();

        model.addAttribute("category",category);
        model.addAttribute("listCategories",listCategories);

        return "Category/categoryForm.html";
//        return "Category/c.html";

    }


//    ,@RequestParam("image") MultipartFile multipartFile) throws IOException {
    @PostMapping("/categories/saveCategory")
    public String saveCategory(RedirectAttributes redirectAttributes, @Valid @ModelAttribute(value = "category") Category category, Errors errors, Model model ,@RequestParam("image") MultipartFile multipartFile,BindingResult binder) throws IOException {

        log.error(multipartFile.getOriginalFilename());
        log.error(String.valueOf(multipartFile.isEmpty()));
        log.error(String.valueOf(errors.hasFieldErrors()));
//        errors.reject("typeMismatch","Please Upload a photo");
        log.error(String.valueOf(errors.getFieldError()));
        errors.rejectValue("image","typeMismatch","Please Upload a photo");


        //TO CHECK UNIQUE NAME
        if (category.getName() != "" && !categoryService.isNameUnique(category.getName())) {
            log.error("Contact form validation failed due to name ");
            model.addAttribute("nameNotUnique", "There is another category having same name");
            List<Category> listCategories = categoryService.listCategoriesUsedInForm();
            model.addAttribute("listCategories", listCategories);
            return "Category/categoryForm.html";
        }



//        DISPLAYING ERROR MESSAGES
        if (errors.hasErrors()) {
            for(ObjectError error:errors.getAllErrors()){
                if(!(error.getCode().matches("typeMismatch"))){
                    log.error(error.getCode());
                    log.error(String.valueOf(error));
                    log.error("New Category form validation failed due to : " + errors.toString());
                    List<Category> listCategories = categoryService.listCategoriesUsedInForm();
                    model.addAttribute("listCategories", listCategories);
                    return "Category/categoryForm.html";
                }
            }
        }

        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            category.setImage(fileName);
            log.error(category.getImage());
            Category savedCategory = categoryService.saveCategory(category);
            String uploadDir = "category-photos/" + savedCategory.getId();
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            redirectAttributes.addFlashAttribute("message", "The category has been saved successfully");
            return "redirect:/categories";
        }
        return "Category/categoryForm.html";
    }




    @GetMapping("/categories/{id}/enabled/{status}")
    public String updateCategoryEnabledStatus(@PathVariable("id") Integer id,@PathVariable("status") boolean enabled,RedirectAttributes redirectAttributes){
        categoryService.updateCategoryEnabledStatus(id,enabled);
        String status=enabled?"enabled":"disabled";
        String message="The Category Id "+id+" has been "+status;
        redirectAttributes.addFlashAttribute("message",message);
        return "redirect:/categories";
    }


    @GetMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model,HttpSession session){
        try{
            Category category=categoryService.getCategoryById(id);
            log.error(category.getName());
            List<Category> listCategories =categoryService.listCategoriesUsedInForm();
            model.addAttribute("category",category);
            session.setAttribute("id",id);
            model.addAttribute("listCategories",listCategories);
            return "Category/categoryUpdateForm.html";
        }catch (UsernameNotFoundException ex){
            redirectAttributes.addFlashAttribute("message",ex.getMessage());

            return "redirect:/categories";
        }

    }

//
    @PostMapping("/categories/editCategory")
    public String editCategorySave(RedirectAttributes redirectAttributes, @Valid @ModelAttribute(value = "category") Category category, Errors errors, Model model, HttpSession session , @RequestParam("image")MultipartFile multipartFile) throws IOException{

        Integer id= (Integer) session.getAttribute("id");
        log.error(String.valueOf(id));
        Category existingCategory=categoryService.getCategoryById(id);


        //TO CHECK UNIQUE NAME
        if (existingCategory!=null && !(existingCategory.getName().matches(category.getName()))) {
            if (category.getName() != "" && !categoryService.isNameUnique(category.getName())) {
                log.error("Contact form validation failed due to name ");
                model.addAttribute("nameNotUnique", "There is another category having same name");
                List<Category> listCategories = categoryService.listCategoriesUsedInForm();
                model.addAttribute("listCategories", listCategories);
                return "Category/categoryUpdateForm.html";
            }
        }


//        DISPLAYING ERROR MESSAGES
        if (errors.hasErrors()) {
            for(ObjectError error:errors.getAllErrors()){
                if(!(error.getCode().matches("typeMismatch"))){
                    log.error(error.getCode());
                    log.error(String.valueOf(error));
                    log.error("New Category form validation failed due to : " + errors.toString());
                    List<Category> listCategories = categoryService.listCategoriesUsedInForm();
                    model.addAttribute("listCategories", listCategories);
                    return "Category/categoryUpdateForm.html";
                }
            }
        }
        existingCategory.setName(category.getName());
        existingCategory.setAlias(category.getAlias());
        existingCategory.setParent(category.getParent());
        existingCategory.setEnabled(category.isEnabled());


        //SAVE DETAILS
        categoryService.saveCategory(existingCategory);


        redirectAttributes.addFlashAttribute("message","The Category has been edited successfully");
        return "redirect:/categories";

    }



    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable(name="id")Integer id,Model model,RedirectAttributes redirectAttributes){
        try{
            if(categoryService.getCategoryById(id).getChildren().isEmpty()){
                categoryService.delete(id);
                redirectAttributes.addFlashAttribute("message","The Category ID "+id+" has been deleted successfully");
            }
            else{
                redirectAttributes.addFlashAttribute("message","The Category ID "+id+" can't be deleted because it has further subcategories");
            }
        }catch (UsernameNotFoundException ex){
            redirectAttributes.addFlashAttribute("message",ex.getMessage());
        }
        return "redirect:/categories";
    }


    @GetMapping("/categories/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        List<Category> listCategories=categoryService.listAllCategories();
        CategoryCsvExporter exporter=new CategoryCsvExporter();
        exporter.export(listCategories,response);
    }


    @GetMapping("/categories/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<Category> listCategories=categoryService.listAllCategories();
        CategoryExcelExporter exporter=new CategoryExcelExporter();
        exporter.export(listCategories,response);
    }

    @GetMapping("/categories/export/pdf")
    public void exportToPdf(HttpServletResponse response) throws IOException {
        log.error("Inside PDFFF");
         List<Category> listCategories=categoryService.listAllCategories();

        CategoryPdfExporter exporter=new CategoryPdfExporter();
        exporter.export(listCategories,response);
    }



}



