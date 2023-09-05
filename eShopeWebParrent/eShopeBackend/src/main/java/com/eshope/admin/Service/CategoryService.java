package com.eshope.admin.Service;


import com.eShope.common.entity.Category;
import com.eShope.common.entity.Product.Product;
import com.eshope.admin.Repository.CategoryRepository;
import com.eshope.admin.Repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;


@Service
@Slf4j
public class CategoryService {

    public static final int CATEGORIES_PER_PAGE = 5;

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;

    public Page<Category> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, CATEGORIES_PER_PAGE, sort);

        if (keyword != null) {
            return categoryRepository.findAll(keyword, pageable);
        }

        return categoryRepository.findAllNonZeroCategory(pageable);
    }


    public boolean isNameUnique(String name) {
        Category categoryByName = categoryRepository.getCategoryByName(name);

        return categoryByName == null;
    }

    public List<Category> listAllCategories(){
        return  (List<Category>) categoryRepository.findAllNonZeroCategory();
    }

    public Category saveCategory(Category category) {

        Category parent=category.getParent();
        if(parent!=null){
            String allParents=parent.getAllParentIDs()==null?"-": parent.getAllParentIDs();
            allParents+=String.valueOf(parent.getId())+"-";
            category.setAllParentIDs(allParents);
        }
        return categoryRepository.save(category);
    }

    public List<Category> listCategoriesUsedInForm() {
        List<Category> categoriesUsedInForm = new ArrayList<>();

        Iterable<Category> categoriesInDB = categoryRepository.findAllNonZeroCategory();

        for (Category category : categoriesInDB) {
            if (category.getParent() == null) {
                categoriesUsedInForm.add(new Category(category.getName(),category.getId()));

                Set<Category> children = category.getChildren();

                for (Category subCategory : children) {
                    String name = "--" + subCategory.getName();
                    categoriesUsedInForm.add(new Category(name,subCategory.getId()));
                    listChildren(categoriesUsedInForm, subCategory, 1);
                }
            }
        }
        return categoriesUsedInForm ;
    }

    public void updateCategoryEnabledStatus(Integer id, boolean enabled) {
        categoryRepository.updateEnableStatus(id, enabled);
    }

    public void delete(Integer id) throws UsernameNotFoundException {
        Category category = getCategoryById(id);
        Category deletedCategory=getCategoryById(0);
        if (category == null ) {
            throw new UsernameNotFoundException("Could not found any category with Id " + id);
        }
        List<Product> productList=category.getProducts();
        for(Product product:productList){
            product.setCategory(deletedCategory);
            productRepository.save(product);
        }
        categoryRepository.deleteById(id);
    }


    private void listChildren(List<Category> categoriesUsedInForm, Category parent, int subLevel) {
        int newSubLevel = subLevel + 1;
        Set<Category> children = parent.getChildren();

        for (Category subCategory : children) {
            String name = "";
            for (int i = 0; i < newSubLevel; i++) {
                name += "--";
            }
            name += subCategory.getName();
            categoriesUsedInForm.add(new Category(name,subCategory.getId()));
            listChildren(categoriesUsedInForm, subCategory, newSubLevel);
        }

    }

    public Category getCategoryById(Integer id) {
        try{
            return categoryRepository.findById(id).get();
        }catch (NoSuchElementException ex){
            throw new UsernameNotFoundException("Could not find any category with Id"+ id);
        }
    }

    public boolean isAliasUnique(String alias) {
        Category categoryByAlias = categoryRepository.getCategoryByAlias(alias);

        return categoryByAlias == null;
    }

}


