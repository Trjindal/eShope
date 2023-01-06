package com.eshope.admin.Controller;


import com.eShope.common.entity.*;
import com.eshope.admin.Service.BrandService;
import com.eshope.admin.Service.CategoryService;
import com.eshope.admin.Service.ProductService;
import com.eshope.admin.Utility.FileUploadUtil;
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

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@Slf4j
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    BrandService brandService;

    @Autowired
    CategoryService categoryService;


    @GetMapping("/products")
    public String listAllProducts(Model model){
        return listByPage(1,model,"id","asc",null,0);
    }

    @GetMapping("/products/page/{pageNum}")
    public String listByPage(@PathVariable(name="pageNum") int pageNum, Model model, @Param("sortField") String sortField, @Param("sortDir") String sortDir, @Param("keyword") String keyword,@Param("categoryId") Integer categoryId){
        Page<Product> page=productService.listByPage(pageNum,sortField,sortDir,keyword,categoryId);
        List<Product> listProducts=page.getContent();
        List<Category> listCategories=categoryService.listCategoriesUsedInForm();

        long startCount =(pageNum-1)*productService.PRODUCTS_PER_PAGE+1;
        long endCount=startCount+productService.PRODUCTS_PER_PAGE-1;
        if(endCount>page.getTotalElements()){
            endCount=page.getTotalElements();
        }

        if(categoryId!=null){
            model.addAttribute("categoryId",categoryId);
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
        model.addAttribute("listCategories", listCategories);
        return "Product/product";
    }

    @GetMapping("/products/new")
    public String newProduct( Model model){
        Product product=new Product();
        product.setInStock(true);
        product.setEnabled(true);
        product.setFullDescription("");

        List<Brand> listBrands=brandService.listAllBrands();
        Integer numberOfExistingExtraImages=product.getImages().size();

        model.addAttribute("numberOfExistingExtraImages",numberOfExistingExtraImages);
        model.addAttribute("product",product);
        model.addAttribute("listBrands",listBrands);

        return "Product/productForm.html";
    }

    @PostMapping("/products/saveProduct")
    public String saveProduct(RedirectAttributes redirectAttributes,
                              @Valid @ModelAttribute(value = "product") Product product
            ,Errors errors, Model model,@RequestParam("image") MultipartFile mainImageMultipart
            ,@RequestParam("extraImage") MultipartFile[] extraImageMultiparts,@RequestParam(name = "detailsName",required = false) String[] detailsName
    ,@RequestParam(name="detailsValue",required = false)String[] detailsValue,HttpSession session) throws IOException {

        List<Brand> listBrands = brandService.listAllBrands();
        Integer numberOfExistingExtraImages = product.getImages().size();


        //TO CHECK UNIQUE NAME
        if(product.getId()==null) {
            if (product.getName() != "" && !productService.isNameUnique(product.getName())) {
                log.error("Product form validation failed due to name ");

                model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);
                model.addAttribute("nameNotUnique", "There is another product having same name");
                model.addAttribute("listBrands", listBrands);
                return "Product/productForm.html";
            }
        }else{
            Integer id = (Integer) session.getAttribute("id");
            Product existingProduct=productService.getProductById(id);
            if(existingProduct!=null&&!(existingProduct.getName().matches(product.getName()))) {
                if (product.getName() != "" && !productService.isNameUnique(product.getName())) {
                    log.error("Product form validation failed due to name ");

                    model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);
                    model.addAttribute("nameNotUnique", "There is another product having same name");
                    model.addAttribute("listBrands", listBrands);
                    return "Product/productForm.html";
                }
            }
        }

        //ERROR FOR MAIN IMAGE
        if(mainImageMultipart.getOriginalFilename().isEmpty()){
            log.error("Product form validation failed due to Main Image ");
            model.addAttribute("numberOfExistingExtraImages",numberOfExistingExtraImages);
            model.addAttribute("mainImageNotProvided", "Please upload main image.");
            model.addAttribute("listBrands", listBrands);
            return "Product/productForm.html";
        }

        //ERROR FOR MAIN IMAGE SIZE
        if(mainImageMultipart.getSize()>=512000){
            log.error("Product form validation failed due to Main Image Size");
            model.addAttribute("numberOfExistingExtraImages",numberOfExistingExtraImages);
            model.addAttribute("mainImageNotProvided", "Please upload image size less than 500KB.");
            model.addAttribute("listBrands", listBrands);
            return "Product/productForm.html";
        }

        //ERROR FOR EXTRA IMAGE SIZE
        if(extraImageMultiparts.length>0){
            for(MultipartFile image:extraImageMultiparts){
                if(image.getSize()>=512000){
                    log.error("Product form validation failed due to Extra Image Size");
                    model.addAttribute("numberOfExistingExtraImages",numberOfExistingExtraImages);
                    model.addAttribute("mainImageNotProvided", "Please upload image size less than 500KB.");
                    model.addAttribute("listBrands", listBrands);
                    return "Product/productForm.html";
                }
            }
        }

        //DISPLAYING ERROR MESSAGES
        if (errors.hasErrors()) {
            log.error("New Product form validation failed due to : " + errors.toString());
            model.addAttribute("numberOfExistingExtraImages",numberOfExistingExtraImages);
            model.addAttribute("listBrands", listBrands);
            return "Product/productForm.html";
        }

        //UPLOADING IMAGES
        setMainImageName(mainImageMultipart,product);
        setNewExtraImageNames(extraImageMultiparts,product);
        setProductDetails(detailsName,detailsValue,product);

        Product savedProduct = productService.save(product);

        saveUploadImages(mainImageMultipart,extraImageMultiparts,savedProduct);


        redirectAttributes.addFlashAttribute("message", "The product has been saved successfully.");
        return "redirect:/products";

    }


    //OPENING EDIT_FORM
    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes, Model model, HttpSession session){
        try{
            Product product=productService.getProductById(id);
            Integer numberOfExistingExtraImages=product.getImages().size();

            session.setAttribute("id",id);
            List<Brand> listBrands=brandService.listAllBrands();
            model.addAttribute("product",product);
            model.addAttribute("listBrands",listBrands);
            model.addAttribute("numberOfExistingExtraImages",numberOfExistingExtraImages);

            return "Product/productUpdateForm.html";
        }catch (UsernameNotFoundException ex){
            redirectAttributes.addFlashAttribute("message",ex.getMessage());

            return "redirect:/products";
        }

    }

    @PostMapping("products/editProduct")
    public String saveEditProduct(RedirectAttributes redirectAttributes
            , @Valid @ModelAttribute(value = "product") Product product
            ,Errors errors
            , Model model
            ,@RequestParam("image") MultipartFile mainImageMultipart
            ,@RequestParam("extraImage") MultipartFile[] extraImageMultiparts
            ,@RequestParam("detailsIds") String[] detailIds
            ,@RequestParam(name = "detailsName",required = false) String[] detailsName
            ,@RequestParam(name="detailsValue",required = false)String[] detailsValue
            ,@RequestParam(name="imageIDs",required = false)String[] imageIDs
            ,@RequestParam(name="imageNames",required = false)String[] imageNames
            ,HttpSession session) throws IOException {

        List<Brand> listBrands = brandService.listAllBrands();
        Integer numberOfExistingExtraImages = product.getImages().size();
        Integer id = (Integer) session.getAttribute("id");
        Product existingProduct = productService.getProductById(id);

        //TO CHECK UNIQUE NAME
        if (existingProduct != null && !(existingProduct.getName().matches(product.getName()))) {
            if (product.getName() != "" && !productService.isNameUnique(product.getName())) {
                log.error("Product form validation failed due to name ");

                model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);
                model.addAttribute("nameNotUnique", "There is another product having same name");
                model.addAttribute("listBrands", listBrands);
                return "Product/productEditForm.html";
            }
        }

        //ERROR FOR MAIN IMAGE
        if (mainImageMultipart.getOriginalFilename().isEmpty() && existingProduct.getMainImage() == null) {
            log.error("Product form validation failed due to Main Image ");
            model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);
            model.addAttribute("mainImageNotProvided", "Please upload main image.");
            model.addAttribute("listBrands", listBrands);
            return "Product/productUpdateForm.html";
        }

        //ERROR FOR MAIN IMAGE SIZE
        if (mainImageMultipart.getSize() >= 512000) {
            log.error("Product form validation failed due to Main Image Size");
            model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);
            model.addAttribute("mainImageNotProvided", "Please upload image size less than 500KB.");
            model.addAttribute("listBrands", listBrands);
            return "Product/productUpdateForm.html";
        }

        //ERROR FOR EXTRA IMAGE SIZE
        if (extraImageMultiparts.length > 0) {
            for (MultipartFile image : extraImageMultiparts) {
                if (image.getSize() >= 512000) {
                    log.error("Product form validation failed due to Extra Image Size");
                    model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);
                    model.addAttribute("mainImageNotProvided", "Please upload image size less than 500KB.");
                    model.addAttribute("listBrands", listBrands);
                    return "Product/productUpdateForm.html";
                }
            }
        }

        //DISPLAYING ERROR MESSAGES
        if (errors.hasErrors()) {
            log.error("New Product form validation failed due to : " + errors.toString());
            model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);
            model.addAttribute("listBrands", listBrands);
            return "Product/productUpdateForm.html";
        }

        //UPLOADING IMAGES
        setMainImageName(mainImageMultipart, product);
        setExistingExtraImageNames(imageIDs, imageNames, product);
        setNewExtraImageNames(extraImageMultiparts, product);
        setProductDetails(detailIds,detailsName, detailsValue, product);


        product.setId(existingProduct.getId());
        product.setCreatedTime(existingProduct.getCreatedTime());
        if (product.getMainImage() == null)
            product.setMainImage(existingProduct.getMainImage());
//        if(product.getImages()==null)
//            product.setImages(existingProduct.getImages());
//        if(product.getDetails()==null)
//            product.setDetails(existingProduct.getDetails());

        Product savedProduct = productService.save(product);

        saveUploadImages(mainImageMultipart, extraImageMultiparts, savedProduct);

        deleteExtraImagesRemovedOnForm(product);

        redirectAttributes.addFlashAttribute("message", "The product has been edited successfully.");
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
            String productExtraImagesDir="product-photos/"+id+"/extras";
            String productImageDir="product-photos/"+id;

            FileUploadUtil.removeDir(productExtraImagesDir);
            FileUploadUtil.removeDir(productImageDir);

            redirectAttributes.addFlashAttribute("message","The product ID "+id+" has been deleted successfully");
        }catch (UsernameNotFoundException ex){
            redirectAttributes.addFlashAttribute("message",ex.getMessage());
        }
        return "redirect:/products";
    }


    private void deleteExtraImagesRemovedOnForm(Product product) {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        String extraImageDir = s+"/product-photos/" + product.getId() + "/extras";
        Path dirPath = Paths.get(extraImageDir);

        try {
            Files.list(dirPath).forEach(file -> {
                String fileName = file.toFile().getName();

                if (!product.containsImageName(fileName)) {
                    try {
                        Files.delete(file);
                    } catch (IOException e) {
                        log.error("Could not delete extra image " + fileName);
                    }
                }
            });
        } catch (IOException e) {
            log.error("Could not list directory : "+ dirPath);
        }
    }

    private void setMainImageName(MultipartFile mainImageMultipart,Product product){
        if (!mainImageMultipart.isEmpty())
        {
            String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
            product.setMainImage(fileName);
        }
    }

    private void setExistingExtraImageNames(String[] imageIDs, String[] imageNames, Product product) {

        if(imageIDs==null||imageIDs.length==0) return;

        Set<ProductImage> images=new HashSet<ProductImage>();
        for(int count=0;count<imageIDs.length;count++){
            Integer id=Integer.parseInt(imageIDs[count]);
            String name=imageNames[count];
            if(!(name.isEmpty())){
             images.add(new ProductImage(id,name,product));}
        }


        product.setImages(images);
    }

    private void setNewExtraImageNames(MultipartFile[] extraImageMultiparts, Product product) {
        if(extraImageMultiparts.length>0){
            for(MultipartFile image:extraImageMultiparts){
                if(!image.isEmpty()){
                    String fileName = StringUtils.cleanPath(image.getOriginalFilename());
                    if(!product.containsImageName(fileName)){
                        product.addExtraImage(fileName);
                    }
                }
            }
        }
    }

    private void saveUploadImages(MultipartFile mainImageMultipart, MultipartFile[] extraImageMultiparts, Product savedProduct) throws IOException{
        if (!mainImageMultipart.isEmpty())
        {
            String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
            String uploadDir = "product-photos/" + savedProduct.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultipart);
        }
        if(extraImageMultiparts.length>0){
            String uploadDir = "product-photos/" + savedProduct.getId()+"/extras";
            for(MultipartFile image:extraImageMultiparts){
                if(image.isEmpty())
                    continue;
                String fileName = StringUtils.cleanPath(image.getOriginalFilename());
                FileUploadUtil.saveFile(uploadDir, fileName, image);
                }}

    }

    private void setProductDetails(String[] detailsName, String[] detailsValue, Product product) {
        if(detailsName==null||detailsName.length==0) return;

        for(int count=0;count<detailsName.length;count++){
            String name=detailsName[count];
            String value=detailsValue[count];

            if(!name.isEmpty()&&!value.isEmpty()){
                product.addDetails(name,value);
            }
        }
    }

    private void setProductDetails(String[] detailsIds,String[] detailsName, String[] detailsValue, Product product) {
        if(detailsName==null||detailsName.length==0) return;

        for(int count=0;count<detailsName.length;count++){
            String name=detailsName[count];
            String value=detailsValue[count];
            Integer id=Integer.parseInt(detailsIds[count]);
            if(id!=0){
                product.addDetails(id,name,value);
            }else if(!name.isEmpty()&&!value.isEmpty()){
                product.addDetails(name,value);
            }
        }
    }

}


