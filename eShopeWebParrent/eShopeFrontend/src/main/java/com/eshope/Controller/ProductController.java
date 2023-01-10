package com.eshope.Controller;

import com.eShope.common.entity.Category;
import com.eShope.common.entity.Product;
import com.eshope.Service.CategoryService;
import com.eshope.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @GetMapping("c/{category_alias}")
    public String viewCategoryByFirstPage(@PathVariable("category_alias") String alias, Model model){
        return viewCategoryByPage(alias,model,1);
    }


        @GetMapping("c/{category_alias}/page/{pageNum}")
    public String viewCategoryByPage(@PathVariable("category_alias") String alias, Model model,@PathVariable(name="pageNum") int pageNum){
        Category category=categoryService.getCategory(alias);

//        WHEN CATEGORY IS NOT THERE
        if(category==null){
            return "error/404";
        }

//        FOR BREADCRUMBS FINDING ALL PARENT CATEGORIES
        List<Category> listCategoryParents=categoryService.getCategoryParent(category);

        Page<Product> pageProducts =productService.listByCategory(pageNum,category.getId());
        List<Product> listProducts=pageProducts.getContent();

        long startCount =(pageNum-1)*productService.PRODUCTS_PER_PAGE+1;
        long endCount=startCount+productService.PRODUCTS_PER_PAGE-1;
        if(endCount>pageProducts.getTotalElements()){
            endCount=pageProducts.getTotalElements();
        }


        model.addAttribute("currentPage",pageNum);
        model.addAttribute("startCount",startCount);
        model.addAttribute("totalPages",pageProducts.getTotalPages());
        model.addAttribute("endCount",endCount);
        model.addAttribute("totalItems",pageProducts.getTotalElements());

        model.addAttribute("pageTitle","eShope - "+category.getName());
        model.addAttribute("listCategoryParents",listCategoryParents);
        model.addAttribute("listProducts",listProducts);
        model.addAttribute("category",category);
        return "products_by_category";
    }
}
