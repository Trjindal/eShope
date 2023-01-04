package com.eshope.admin.product;

import com.eShope.common.entity.Brand;
import com.eShope.common.entity.Category;
import com.eShope.common.entity.Product;
import com.eshope.admin.Repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateProduct(){
        Brand brand=entityManager.find(Brand.class,37);
        Category category=entityManager.find(Category.class,5);

        Product product=new Product();
        product.setName("Acer Aspire Desktop");
        product.setAlias("acer_aspire_desktop");
        product.setShortDescription("Short description for Acer Aspire Desktop");
        product.setFullDescription("Full description for Acer Aspire Desktop");

        product.setBrand(brand);
        product.setCategory(category);

        product.setPrice(678);
        product.setCost(600);
        product.setEnabled(true);
        product.setInStock(true);
        product.setCreatedTime(new Date());
        product.setUpdateTime(new Date());

        Product savedProduct=productRepository.save(product);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isGreaterThan(0);
    }

    @Test
    public void TestListAllProducts(){
        Iterable<Product> productIterable=productRepository.findAll();

        productIterable.forEach(System.out::println);
    }

    @Test
    public void TestGetProduct(){
        Integer id=2;
        Product product=productRepository.findById(id).get();
        assertThat(product).isNotNull();
    }

    @Test
    public void TestUpdateProduct(){
        Integer id=1;
        Product product=productRepository.findById(id).get();
        product.setAlias("samsung_galaxy_a31");
        product.setPrice(499);

        productRepository.save(product);

        Product updatedProduct=entityManager.find(Product.class,id);

        assertThat(updatedProduct.getPrice()).isEqualTo(499);
        assertThat(updatedProduct.getAlias()).isEqualTo("samsung_galaxy_a31");
    }

    @Test
    public void TestDeleteProduct(){
        Integer id=3;
        productRepository.deleteById(id);

        Optional<Product> result=productRepository.findById(id);

        assertThat(!result.isPresent());
    }

    @Test
    public void testSaveProductWithImages(){
        Integer productId=1;
        Product product=productRepository.findById(productId).get();

        product.setMainImage("main image.jpg");
        product.addExtraImage("extra image 1.png");
        product.addExtraImage("extra_image_2.png");
        product.addExtraImage("extra-image3.png");

        Product savedProduct=productRepository.save(product);

        assertThat(savedProduct.getImages().size()).isEqualTo(3);
    }

    @Test
    public  void testSaveProductWithDetails(){
        Integer productId=1;
        Product product=productRepository.findById(productId).get();

        product.addDetails("Device Memory","128 GB");
        product.addDetails("CPU Model","MediaTek");
        product.addDetails("OS","Android 10");

        Product savedProduct=productRepository.save(product);
        assertThat(savedProduct.getDetails()).isNotEmpty();
    }


}
