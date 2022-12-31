package com.eshope.admin.Product;


import com.eShope.common.entity.Brand;
import com.eShope.common.entity.Category;
import com.eShope.common.entity.Product;
import com.eshope.admin.Brand.BrandService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@Slf4j
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    BrandService brandService;


    @GetMapping("/products")
    public String listAllProducts(Model model){
        return listByPage(1,model,"id","asc",null);
    }

    @GetMapping("/products/page/{pageNum}")
    public String listByPage(@PathVariable(name="pageNum") int pageNum, Model model, @Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword){
        Page<Product> page=productService.listByPage(pageNum,sortField,sortDir,keyword);
        List<Product> listProducts=page.getContent();

        long startCount =(pageNum-1)*productService.PRODUCTS_PER_PAGE+1;
        long endCount=startCount+productService.PRODUCTS_PER_PAGE-1;
        if(endCount>page.getTotalElements()){
            endCount=page.getTotalElements();
        }

        model.addAttribute("currentPage",pageNum);
        model.addAttribute("startCount",startCount);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("endCount",endCount);
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("listAllProducts",listProducts);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("keyword", keyword );
        return "Product/product";
    }

    @GetMapping("/products/new")
    public String newProduct( Model model){
        Product product=new Product();
        product.setInStock(true);
        product.setEnabled(true);
        product.setFullDescription("");

        List<Brand> listBrands=brandService.listAllBrands();

        model.addAttribute("product",product);
        model.addAttribute("listBrands",listBrands);
        return "Product/productForm.html";
    }

    @PostMapping("/products/saveProduct")
    public String saveProduct(RedirectAttributes redirectAttributes, @Valid @ModelAttribute(value = "product") Product product, Errors errors, Model model,@RequestParam("image") MultipartFile multipartFile) throws IOException{

        List<Brand> listBrands=brandService.listAllBrands();

        //DISPLAYING ERROR MESSAGES
        if (errors.hasErrors()) {
            log.error("New Product form validation failed due to : " + errors.toString());
            model.addAttribute("listBrands", listBrands);
            return "Product/productForm.html";
        }

        //TO CHECK UNIQUE NAME
        if (product.getName() != "" && !productService.isNameUnique(product.getName())) {
            log.error("Product form validation failed due to name ");
            model.addAttribute("nameNotUnique", "There is another product having same name");
            model.addAttribute("listBrands", listBrands);
            return "Product/productForm.html";
        }

        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            product.setMainImage(fileName);
            Product savedProduct = productService.save(product);
            String uploadDir = "product-photos/" + savedProduct.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            redirectAttributes.addFlashAttribute("message", "The product has been saved successfully.");
            return "redirect:/products";
        }


        redirectAttributes.addFlashAttribute("message","The product could not be saved successfully.");
        return "redirect:/products";
    }


    @GetMapping("/products/{id}/enabled/{status}")
    public String updateProductEnabledStatus(@PathVariable("id") Integer id,@PathVariable("status") boolean enabled,RedirectAttributes redirectAttributes){
        productService.updateProductEnabledStatus(id,enabled);
        String status=enabled?"enabled":"disabled";
        String message="The product Id "+id+" has been "+status;
        redirectAttributes.addFlashAttribute("message",message);
        return "redirect:/products";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable(name="id")Integer id,Model model,RedirectAttributes redirectAttributes){
        try{
            productService.delete(id);
            redirectAttributes.addFlashAttribute("message","The product ID "+id+" has been deleted successfully");
        }catch (UsernameNotFoundException ex){
            redirectAttributes.addFlashAttribute("message",ex.getMessage());
        }
        return "redirect:/products";
    }

}
