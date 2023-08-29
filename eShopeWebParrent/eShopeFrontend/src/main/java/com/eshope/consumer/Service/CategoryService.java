package com.eshope.consumer.Service;


import com.eShope.common.entity.Category;
import com.eshope.consumer.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;



    public List<Category> listNoChildrenCategories(){
        List<Category> categoryListHavingNoChildren=new ArrayList<>();
        List<Category> listEnabledCategories=categoryRepository.findAllEnabled();
        listEnabledCategories.forEach(category -> {
            Set<Category> children=category.getChildren();
            if(children==null||children.size()==0){
                categoryListHavingNoChildren.add(category);
            }
        });
        return categoryListHavingNoChildren;
    }

    public Category getCategory(String alias){
       Category category= categoryRepository.findByAliasEnabled(alias);
        if(category==null){
            throw new UsernameNotFoundException("Could not found any category with alias "+ alias);
        }
        return categoryRepository.findByAliasEnabled(alias);
    }

    public List<Category> getCategoryParent(Category child){

        List<Category> listParents=new ArrayList<>();

        Category parent=child.getParent();
        while(parent!=null){
            listParents.add(0,parent);
            parent=parent.getParent();
        }

        return listParents;
    }

}
