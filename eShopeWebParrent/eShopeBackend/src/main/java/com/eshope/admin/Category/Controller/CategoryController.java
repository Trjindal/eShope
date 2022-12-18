package com.eshope.admin.Category.Controller;

import com.eShope.common.entity.Category;

import com.eshope.admin.Category.Service.CategoryService;

import com.eshope.admin.Main.Utility.FileUploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;


@Slf4j
@Controller
public class CategoryController {


   @Autowired(required=true)
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
    }

    @PostMapping("/categories/saveCategory")
    public String saveCategory(RedirectAttributes redirectAttributes, @Valid @ModelAttribute(value = "category") Category category, Errors errors, Model model, HttpSession session, @RequestParam("image") MultipartFile multipartFile) throws IOException {



        //TO CHECK UNIQUE NAME
        if (category.getName() != "" && !categoryService.isNameUnique(category.getName())) {
            log.error("Contact form validation failed due to email ");
            model.addAttribute("nameNotUnique", "There is another category having same name");
            return "Category/categoryForm.html";
        }
//        }

        //DISPLAYING ERROR MESSAGES
        if(errors.hasErrors()){
            log.error("New Category form validation failed due to : " + errors.toString());
            return "Category/categoryForm.html";
        }

//       UPLOAD PHOTOS AND SAVE

        if(!multipartFile.isEmpty()){
            String fileName= StringUtils.cleanPath(multipartFile.getOriginalFilename());
            category.setImage(fileName);
            Category savedCategory=categoryService.saveCategory(category);
            String uploadDir="category-photos/"+savedCategory.getId();
            FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
        }

        //SAVE DETAILS
        Category savedCategory=categoryService.saveCategory(category);
        redirectAttributes.addFlashAttribute("message","The category has been saved successfully");
        return "redirect:/Category/category";
    }

    @GetMapping("/categories/{id}/enabled/{status}")
    public String updateCategoryEnabledStatus(@PathVariable("id") Integer id,@PathVariable("status") boolean enabled,RedirectAttributes redirectAttributes){
        categoryService.updateCategoryEnabledStatus(id,enabled);
        String status=enabled?"enabled":"disabled";
        String message="The Category Id "+id+" has been "+status;
        redirectAttributes.addFlashAttribute("message",message);
        return "redirect:/categories";
    }


}



