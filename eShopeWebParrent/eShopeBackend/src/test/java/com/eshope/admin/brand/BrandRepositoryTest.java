package com.eshope.admin.brand;



import com.eShope.common.entity.Brand;
import com.eShope.common.entity.Category;
import com.eshope.admin.Main.Repositories.BrandRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class BrandRepositoryTest {

    @Autowired
    BrandRepository brandRepository;

    @Test
    public void testCreateBrand1(){
        Category laptops=new Category(6);
       Brand acer=new Brand("Acer");
       acer.getCategories().add(laptops);
       Brand savedBrand=brandRepository.save(acer);
        assertThat(savedBrand.getId()).isGreaterThan(0);
    }
    @Test
    public void testCreateBrand2(){
        Category cellphones=new Category(4);
        Category tablets=new Category(7);
        Brand apple=new Brand("Apple");
        apple.getCategories().add(cellphones);
        apple.getCategories().add(tablets);
        Brand savedBrand=brandRepository.save(apple);
        assertThat(savedBrand.getId()).isGreaterThan(0);
    }
    @Test
    public void testCreateBrand3(){
        Category memory=new Category(29);
        Category hardDrive=new Category(24);
        Brand samsung=new Brand("Samsung");
        samsung.getCategories().add(memory);
        samsung.getCategories().add(hardDrive);
        Brand savedBrand=brandRepository.save(samsung);
        assertThat(savedBrand.getId()).isGreaterThan(0);
    }

    @Test
    public void testFindAllBrand(){
        Iterable<Brand> brands=brandRepository.findAll();
        assertThat(brands).isNotEmpty();
    }

    @Test
    public void testGetByBrandId(){
        Brand brand=brandRepository.findById(1).get();
        assertThat(brand.getName()).isEqualTo("Acer");
    }

    @Test
    public void testUpdateBrandName(){
        String newName="Samsung Electronics";
        Brand samsung=brandRepository.findById(5).get();
        samsung.setName(newName);

        Brand saveBrand=brandRepository.save(samsung);
        assertThat(saveBrand.getName()).isEqualTo(newName);
    }

    @Test
    public void testDelete(){
        Integer id=4;
        brandRepository.deleteById(id);

        Optional<Brand> result=brandRepository.findById(id);

        assertThat(result.isEmpty());
    }


}
