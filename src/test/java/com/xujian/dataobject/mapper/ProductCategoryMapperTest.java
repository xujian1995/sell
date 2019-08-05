package com.xujian.dataobject.mapper;

import com.xujian.dataobject.ProductCategory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryMapperTest {
 @Autowired
    private  ProductCategoryMapper mapper;
 @Test
    public void insertByMap(){
     Map<String,Object> map=new HashMap<>();
     map.put("categoryName","我的最爱");
     map.put("categoryType",100);
    int result= mapper.insertByMap(map);
     Assert.assertEquals(1,result);
 }

    @Test
    public  void insertByObject(){
        ProductCategory productCategory=new ProductCategory();
        productCategory.setCategoryName("我最讨厌");
        productCategory.setCategoryType(new Integer(102));
        int result=mapper.insertByObject(productCategory);
        Assert.assertEquals(1,result);
    }
}