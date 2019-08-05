package com.xujian.service.impl;

import com.xujian.dataobject.ProductInfo;
import com.xujian.enums.ProductStatusEnum;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceImplTest {
@Autowired
private  ProductServiceImpl productService;
    @Test
    public void findOne() {
        ProductInfo productInfo=productService.findOne("123456");
        Assert.assertEquals("123456",productInfo.getProductId());
    }

    @Test
    public void findUpAll() {
        List<ProductInfo> productInfoList=productService.findUpAll();
        Assert.assertNotEquals(0,productInfoList.size());
    }

    @Test
    public void findAll() {
        Pageable request=new PageRequest(0,2);//查询第0页的两条记录
        Page<ProductInfo> productInfoPage=productService.findAll(request);
        System.out.println(productInfoPage.getTotalElements());
    }

    @Test
    public void save() {
        ProductInfo productInfo= new ProductInfo();
        productInfo.setProductId("1234567");
        productInfo.setProductName("苹果");
        productInfo.setCategoryType(0);
        productInfo.setProductDescription("好吃的很");
        productInfo.setProductIcon("dasd");
        productInfo.setProductStock(1);
        productInfo.setProductStatus(ProductStatusEnum.DOWN.getCode());
        productInfo.setProductPrice(new BigDecimal(1));

        ProductInfo result=productService.save(productInfo);
        Assert.assertNotNull(result);
    }
}