package com.eshope.admin.Category;


import com.eShope.common.entity.Category;
import com.eshope.admin.Main.Repositories.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testCreateParentCategory(){
        Category category=new Category("Electronics");
        Category savedCategory=categoryRepository.save(category);

        assertThat(savedCategory.getCategoryId()).isGreaterThan(0);
    }

    @Test
    public void getCategoryById(){

     Optional<Category> categoryOptional=categoryRepository.findById(1);
       Category category=categoryOptional.get();
        assertThat(category.getName()).isEqualTo("Computer");
    }

}
