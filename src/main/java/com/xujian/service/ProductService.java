package com.xujian.service;

import com.xujian.dataobject.ProductInfo;
import com.xujian.dto.CartDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    ProductInfo findOne(String productId);
    /**  查询所有在架商品列表 */
    List<ProductInfo> findUpAll();
    /** 给管理端查询商品类目 */
    Page<ProductInfo> findAll(Pageable pageable);
    /** 存商品信息 */
    ProductInfo save(ProductInfo productInfo);

    /** 加库存 */
    void increaseStock(List<CartDTO> cartDTOList);

    /** 减库存 */
    void decreaseStock(List<CartDTO> cartDTOList);

    /** 上架 */
    ProductInfo onSale(String productId);

    /** 下架 */
    ProductInfo offSale(String productId);
}
