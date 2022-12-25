package com.eshope.admin.Brand;

import com.eShope.common.entity.Brand;
import com.eShope.common.entity.Category;
import com.eshope.admin.Category.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class BrandController {


    @Autowired
    BrandService brandService;

    @Autowired
    CategoryService categoryService;

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



}
