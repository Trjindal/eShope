package com.eshope.Service;


import com.eShope.common.entity.Category;
import com.eshope.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> listNoChilderCategories(){
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
}
