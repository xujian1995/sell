package com.xujian.service.impl;

import com.xujian.dataobject.ProductInfo;
import com.xujian.dto.CartDTO;
import com.xujian.enums.ProductStatusEnum;
import com.xujian.enums.ResultEnum;
import com.xujian.exception.SellException;
import com.xujian.repository.ProductInfoRepository;
import com.xujian.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Override
    public ProductInfo findOne(String productId) {
        return productInfoRepository.getOne(productId);
    }

    @Override
    public List<ProductInfo> findUpAll() {
        return productInfoRepository.findByProductStatus(ProductStatusEnum.UP.getCode());//通过状态来查询
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {//返回的是page对象
        return productInfoRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public ProductInfo save(ProductInfo productInfo) {
        return productInfoRepository.save(productInfo);
    }

    @Override
    @Transactional  //声明事务
    public void increaseStock(List<CartDTO> cartDTOList) {//加库存
        for(CartDTO cartDTO:cartDTOList){
            ProductInfo productInfo=productInfoRepository.getOne(cartDTO.getProductId());
            if(productInfo==null){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIS);//抛异常事务会回滚
            }
            Integer result=productInfo.getProductStock()+cartDTO.getProductQuantity();
            productInfo.setProductStock(result);
            productInfoRepository.save(productInfo);
        }
    }

    @Override
    @Transactional  //声明事务
    public void decreaseStock(List<CartDTO> cartDTOList) {//减库存
        for(CartDTO cartDTO:cartDTOList){
      ProductInfo productInfo= productInfoRepository.getOne(cartDTO.getProductId());
      if(productInfo==null){
          throw  new SellException(ResultEnum.PRODUCT_NOT_EXIS);
      }
       Integer result=productInfo.getProductStock()-cartDTO.getProductQuantity();
      if(result<0){
          throw new SellException(ResultEnum.PRODUCT_SOTCK_ERROR);
      }
      productInfo.setProductStock(result);
      productInfoRepository.save(productInfo);
        }
    }

    @Override
    public ProductInfo onSale(String productId) {
      ProductInfo productInfo=  productInfoRepository.getOne(productId);
      if(productInfo==null){
            throw new SellException(ResultEnum.PRODUCT_NOT_EXIS);
      }
      if(productInfo.getProductStatusEnum()==ProductStatusEnum.UP){
          throw new SellException(ResultEnum.PRODUCT_STATUS_ERROR);
      }

      //更新

        productInfo.setProductStatus(ProductStatusEnum.UP.getCode());
      return productInfoRepository.save(productInfo);
    }

    @Override
    public ProductInfo offSale(String productId) {
        ProductInfo productInfo=  productInfoRepository.getOne(productId);
        if(productInfo==null){
            throw new SellException(ResultEnum.PRODUCT_NOT_EXIS);
        }
        if(productInfo.getProductStatusEnum()==ProductStatusEnum.DOWN){
            throw new SellException(ResultEnum.PRODUCT_STATUS_ERROR);
        }

        //更新

        productInfo.setProductStatus(ProductStatusEnum.DOWN.getCode());
        return productInfoRepository.save(productInfo);
    }
}
