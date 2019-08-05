package com.xujian.dataobject;

import com.xujian.enums.OrderStatusEnum;
import com.xujian.enums.PayStatusEnum;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Proxy;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
@Proxy(lazy = false)
@Entity
@Data
@DynamicUpdate
public class OrderMaster {
    /** 订单id*/
    @Id
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
private Integer orderStatus= OrderStatusEnum.NEW.getCode();
    /** 支付状态 默认为0是未支付*/
private Integer payStatus= PayStatusEnum.WAIT.getCode();
    /** 创建时间*/
private Date createTime;
    /** 更新时间*/
private Date updateTime;//加了DynamicUpdate注解才会自动更新updateTime

}
