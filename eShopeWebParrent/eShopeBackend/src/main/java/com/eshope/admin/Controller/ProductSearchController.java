package com.eshope.admin.Controller;

import com.eShope.common.entity.Order.Order;
import com.eShope.common.entity.Product.Product;
import com.eShope.common.entity.Setting.Setting;
import com.eshope.admin.Service.ProductService;
import com.eshope.admin.Service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ProductSearchController {

    @Autowired
    private ProductService productService;

    @Autowired
    private SettingService settingService;

    @GetMapping("/orders/search_product")
    public String searchProduct(RedirectAttributes redirectAttributes, Model model, @Param("keyword") String keyword, HttpServletRequest request){
        return listProductByPage(1,model,"name","desc",keyword,request);
    }

    @GetMapping("/orders/search_product/{pageNum}")
    public String listProductByPage(@PathVariable(name="pageNum") int pageNum, Model model, @Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword, HttpServletRequest request){
        Page<Product> page=productService.searchProducts(pageNum,sortField,sortDir,keyword);
        List<Product> listProducts=page.getContent();

        long startCount =(pageNum-1)* productService.PRODUCTS_PER_PAGE+1;
        long endCount=startCount+productService.PRODUCTS_PER_PAGE-1;
        if(endCount>page.getTotalElements()){
            endCount=page.getTotalElements();
        }

        model.addAttribute("currentPage",pageNum);
        model.addAttribute("startCount",startCount);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("endCount",endCount);
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("listProducts",listProducts);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("keyword", keyword );
        loadCurrencySetting(request);
        return "Orders/searchProduct";
    }

    private void loadCurrencySetting(HttpServletRequest request){
        List<Setting> currencySettings=settingService.getCurrencySettings();

        for(Setting setting:currencySettings){
            request.setAttribute(setting.getKey(), setting.getValue());
        }
    }


}
