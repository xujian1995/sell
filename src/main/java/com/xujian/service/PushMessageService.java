package com.xujian.service;

import com.xujian.dto.OrderDTO;

/**推送消息*/
public interface PushMessageService {
    /**
     * 订单状态变更
     * */
    void orderStatus(OrderDTO orderDTO);
}
