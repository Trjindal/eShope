package com.eshope.admin.Category.Service;


import com.eShope.common.entity.Category;
import com.eShope.common.entity.User;
import com.eshope.admin.Main.Repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
public class CategoryService {

    public static final int CATEGORIES_PER_PAGE=5;

    @Autowired
    CategoryRepository categoryRepository;

    public Page<Category> listByPage(int pageNum, String sortField, String sortDir, String keyword){
        Sort sort= Sort.by(sortField);
        sort=sortDir.equals("asc")?sort.ascending():sort.descending();

        Pageable pageable= PageRequest.of(pageNum-1,CATEGORIES_PER_PAGE,sort);

        if(keyword!=null){
            return categoryRepository.findAll(keyword,pageable);
        }

        return categoryRepository.findAll(pageable);
    }

    public boolean isNameUnique(String name){
        Category categoryByName=categoryRepository.getCategoryByName(name);
        return categoryByName==null;
    }

    public Category saveCategory(Category category){
        boolean isUpdatingUser = (category.getId() >0);
        return categoryRepository.save(category);
    }

    public List<Category> listCategoriesUsedInForm() {
        List<Category> categoriesUsedInForm = new ArrayList<>();

        Iterable<Category> categoriesInDB = categoryRepository.findAll();

        for (Category category : categoriesInDB) {
            if (category.getParent() == null) {
                categoriesUsedInForm.add(new Category((category.getName())));

                Set<Category> children = category.getChildren();

                for (Category subCategory : children) {
                    String name="--" + subCategory.getName();
                    categoriesUsedInForm.add(new Category(name));
                    listChildren(categoriesUsedInForm,subCategory, 1);
                }
            }
        }
        return categoriesUsedInForm;
    }

    public void updateCategoryEnabledStatus(Integer id, boolean enabled) {
        categoryRepository.updateEnableStatus(id,enabled);
    }

    private void listChildren(List<Category> categoriesUsedInForm ,Category parent,int subLevel){
        int newSubLevel=subLevel+1;
        Set<Category> children=parent.getChildren();

        for(Category subCategory: children){
            String name="";
            for(int i=0;i<newSubLevel;i++){
                name+="--";
            }
            name+=subCategory.getName();
            categoriesUsedInForm.add(new Category(name));
            listChildren(categoriesUsedInForm,subCategory,newSubLevel);
        }

    }


//    public Page<User> listByPage(int pageNum, String sortField, String sortDir, String keyword){
//        Sort sort= Sort.by(sortField);
//        sort=sortDir.equals("asc")?sort.ascending():sort.descending();
//
//        Pageable pageable=PageRequest.of(pageNum-1,USERS_PER_PAGE,sort);
//
//        if(keyword!=null){
//            return userRepository.findAll(keyword,pageable);
//        }
//
//        return userRepository.findAll(pageable);
//    }
}
