package com.eshope.Controller;


import com.eShope.common.entity.Category;
import com.eshope.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping({"","/","/home"})
    public String viewHomePage(Model model){
        List<Category> listCategories=categoryService.listNoChilderCategories();
        model.addAttribute("listCategories",listCategories);
        return "index";
    }
}