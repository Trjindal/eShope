package com.eshope.admin.Service;


import com.eShope.common.entity.Brand;
import com.eshope.admin.Repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service

public class BrandService {

    @Autowired
    BrandRepository brandRepository;

    public static final int BRANDS_PER_PAGE = 5;

    public Page<Brand> listByPage(int pageNum, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, BRANDS_PER_PAGE, sort);

        if (keyword != null) {
            return brandRepository.findAll(keyword, pageable);
        }

        return brandRepository.findAll(pageable);
    }

    public boolean isNameUnique(String name) {

        Brand brandByName = brandRepository.getBrandByName(name);
        return brandByName == null;
    }

    public Brand getBrandById(Integer id) {
        try{
            return brandRepository.findById(id).get();
        }catch (NoSuchElementException ex){
            throw new UsernameNotFoundException("Could not find any brand with Id"+ id);
        }
    }

    public void delete(Integer id) throws UsernameNotFoundException {
        Long countById=brandRepository.countById(id);
        if(countById==null||countById==0){
            throw new UsernameNotFoundException("Could not found any brand with Id "+id);
        }
        brandRepository.deleteById(id);
    }

    public List<Brand> listAllBrands(){
        return (List<Brand>) brandRepository.findAll(Sort.by("Name").ascending());
    }

}
