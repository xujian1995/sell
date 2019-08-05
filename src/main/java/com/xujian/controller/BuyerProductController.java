package com.xujian.controller;


import com.xujian.util.ResultVOUtil;
import com.xujian.VO.ProductInfoVO;
import com.xujian.VO.ProductVO;
import com.xujian.VO.ResultVO;
import com.xujian.dataobject.ProductCategory;
import com.xujian.dataobject.ProductInfo;
import com.xujian.service.CategoryService;
import com.xujian.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/list")
    @Cacheable(cacheNames = "product",key="123")
    public ResultVO list(){
        // 1、查询所有上架的商品
        List<ProductInfo> productInfoList=productService.findUpAll();
        // 2、查询上架商品的所有类目（一次性查询 不能for 这里是已经查询完了）
        List<Integer> categoryTypeList=new ArrayList<>();
        for(ProductInfo productInfo:productInfoList){
            categoryTypeList.add(productInfo.getCategoryType());
        }
        List<ProductCategory> productCategoryList=categoryService.findByCategoryTypeIn(categoryTypeList);
        // 3、数据拼装
        List<ProductVO> productVOList=new ArrayList<>();
        for(ProductCategory productCategory:productCategoryList){//针对不同的类目进行封装
            ProductVO productVO=new ProductVO();//商品对象
            productVO.setCategoryType(productCategory.getCategoryType());
            productVO.setCategoryName(productCategory.getCategoryName());
            List<ProductInfoVO> productInfoVOList=new ArrayList<>();
            for(ProductInfo productInfo:productInfoList){
                if(productInfo.getCategoryType().equals(productCategory.getCategoryType())){
                    ProductInfoVO productInfoVO=new ProductInfoVO();
                    BeanUtils.copyProperties(productInfo,productInfoVO);
                    productInfoVOList.add(productInfoVO);
                }
            }
            productVO.setProductInfoVOList(productInfoVOList);
            productVOList.add(productVO);
        }
        return ResultVOUtil.success(productVOList);
    }
}
