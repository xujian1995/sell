package com.xujian.repository;

import com.xujian.dataobject.ProductCategory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest

public class ProductCategoryRepositoryTest {
    @Autowired
    private ProductCategoryRepository repository;

    @Test
    public void findOneTest() {
        ProductCategory productCategory = repository.getOne(2);
        System.out.println(productCategory.getCategoryName());
    }

    @Test
    public void findByCategoryTypeIn() {
        List<Integer> list= Arrays.asList(2,3,4,9,10);
        List<ProductCategory> re=repository.findByCategoryTypeIn(list);
        Assert.assertNotEquals(0,re.size());
    }



}