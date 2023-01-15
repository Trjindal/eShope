package com.eshope.Controller;

import com.eShope.common.entity.Category;
import com.eShope.common.entity.Product;
import com.eshope.Service.CategoryService;
import com.eshope.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Controller
public class ProductController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @GetMapping("c/{category_alias}")
    public String viewCategoryByFirstPage(@PathVariable("category_alias") String alias, Model model,RedirectAttributes redirectAttributes){
        return viewCategoryByPage(alias,model,1,redirectAttributes);
    }


    @GetMapping("c/{category_alias}/page/{pageNum}")
    public String viewCategoryByPage(@PathVariable("category_alias") String alias, Model model, @PathVariable(name="pageNum") int pageNum, RedirectAttributes redirectAttributes){
        try{

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

        model.addAttribute("pageTitle",category.getName());
        model.addAttribute("listCategoryParents",listCategoryParents);
        model.addAttribute("listProducts",listProducts);
        model.addAttribute("category",category);
        return "products_by_category";
        }catch (UsernameNotFoundException ex){
//            redirectAttributes.addFlashAttribute("message",ex.getMessage());
//            return  "redirect:/home";
            return "error/404";
        }
    }

    @GetMapping("p/{product_alias}")
    public String viewProductDetail(@PathVariable("product_alias") String alias, Model model){
        try {
            Product product= productService.getProductByAlias(alias);

            //        FOR BREADCRUMBS FINDING ALL PARENT CATEGORIES
            List<Category> listCategoryParents=categoryService.getCategoryParent(product.getCategory());

            model.addAttribute("pageTitle",product.getShortName());
            model.addAttribute("listCategoryParents",listCategoryParents);
            model.addAttribute("product",product);

            return "product_detail";
        }catch (UsernameNotFoundException ex){
            return "error/404";
        }
    }

    @GetMapping("/search")
    public String search(@Param("keyword") String keyword,Model model){
        return searchByPage(keyword,1,model);
    }

    @GetMapping("/search/page/{pageNum}")
    public String searchByPage(@Param("keyword") String keyword, @PathVariable("pageNum") int pageNum, Model model){
        Page<Product> pageProducts=productService.search(keyword,pageNum);
        List<Product> listResult=pageProducts.getContent();

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

        model.addAttribute("keyword",keyword);
        model.addAttribute("listResult",listResult);

        return "search_result";
    }


}
