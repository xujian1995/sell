package com.xujian.VO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/** 商品包含类目*/
@Data
public class ProductVO {
    @JsonProperty("name")
    private  String categoryName;//这样做是因为对象里有两个name 会混淆 在这里起成categoryName 然后返回还是需要是name
    @JsonProperty("type")
    private  Integer categoryType;
    @JsonProperty("foods")
    private List<ProductInfoVO> productInfoVOList;


}
