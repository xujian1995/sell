package com.xujian.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.xujian.dataobject.OrderDetail;
import com.xujian.enums.OrderStatusEnum;
import com.xujian.enums.PayStatusEnum;
import com.xujian.util.EnumUtil;
import com.xujian.util.serializer.Date2Long;
import lombok.Data;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * 用来装OrderMaster的容器 多了一个List
 *
 * */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)//加这个注释是原因是不返回为空的数据给前台造成麻烦  直接不返回
public class OrderDTO {
    /** 订单id*/
    private String orderId;  //主键
    /** 买家名字*/
    private String buyerName;
    /** 买家手机号*/
    private String buyerPhone;
    /** 买家地址*/
    private String buyerAddress;
    /** 买家openid*/
    private String buyerOpenid;
    /** 订单价格*/
    private BigDecimal orderAmount;
    /** 订单状态 默认为0是新订单*/
    private Integer orderStatus;
    /** 支付状态 默认为0是未支付*/
    private Integer payStatus;
    /** 创建时间*/
    @JsonSerialize(using= Date2Long.class)
    private Date createTime;
    /** 更新时间*/
    @JsonSerialize(using= Date2Long.class)
    private Date updateTime;//加了DynamicUpdate注解才会自动更新updateTime

    List<OrderDetail> orderDetailList;
    @JsonIgnore  //返回Json格式的数据时 忽略这两个字段
    public OrderStatusEnum getOrderStatusEnum(){
        return EnumUtil.getByCode(orderStatus,OrderStatusEnum.class);//通过code获取枚举
    }
    @JsonIgnore
    public PayStatusEnum getPayStatusEnum(){
        return EnumUtil.getByCode(payStatus,PayStatusEnum.class);
    }
}
