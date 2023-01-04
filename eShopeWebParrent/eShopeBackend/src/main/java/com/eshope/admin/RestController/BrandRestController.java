package com.eshope.admin.RestController;

import com.eShope.common.entity.Brand;
import com.eShope.common.entity.Category;
import com.eshope.admin.Exception.BrandNotFoundRestException;
import com.eshope.admin.DTO.CategoryDTO;
import com.eshope.admin.Service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
public class BrandRestController {

    @Autowired
    private BrandService brandService;


    @GetMapping("/brands/{id}/categories")
    public List<CategoryDTO> listCategoriesByBrand(@PathVariable(name = "id") Integer brandId) throws BrandNotFoundRestException {
        List<CategoryDTO> listCategories = new ArrayList<>();

        try {
            Brand brand = brandService.getBrandById(brandId);
            Set<Category> categories = brand.getCategories();

            for (Category category : categories) {
                CategoryDTO dto = new CategoryDTO(category.getId(), category.getName());
                listCategories.add(dto);
            }

            return listCategories;
        } catch (UsernameNotFoundException e) {
            throw new BrandNotFoundRestException();
        }
    }

    }

