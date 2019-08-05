package com.xujian.service.impl;

import com.xujian.dataobject.ProductCategory;
import com.xujian.repository.ProductCategoryRepository;
import com.xujian.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private ProductCategoryRepository repository;


    @Override
    public ProductCategory findOne(Integer categoryId) {
        //调用的是JpaRepository接口的方法
        return repository.getOne(categoryId);
    }

    @Override
    public List<ProductCategory> findAll() {
        //调用的是JpaRepository接口的方法
        return repository.findAll();
    }

    @Override
    public List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList) {
        //调用的是自定义的方法
        return repository.findByCategoryTypeIn(categoryTypeList);
    }

    @Override
    public ProductCategory save(ProductCategory productCategory) {
        return repository.save(productCategory);
    }
}
