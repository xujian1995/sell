package com.xujian.service;

import com.xujian.dataobject.ProductCategory;

import java.util.List;

public interface CategoryService {
 //查询商品类目
    //查询单个
    ProductCategory findOne(Integer categoryId);
    //查询全部
    List<ProductCategory> findAll();
    //查询几个类
    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);
    //新增
    ProductCategory save(ProductCategory productCategory);
    //删除








}
