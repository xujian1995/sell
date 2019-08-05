package com.xujian.service;

import com.xujian.dto.OrderDTO;
import org.hibernate.criterion.Order;

public interface BuyerService {
    //先判断是否是本人 再查询订单
    OrderDTO findOrderOne(String openid,String orderId);
    //取消订单
    OrderDTO cancelOrder(String openid,String orderId);
}
