package com.xujian.service;

import com.lly835.bestpay.model.PayResponse;
import com.xujian.dto.OrderDTO;

public interface PayService {
    PayResponse create(OrderDTO orderDTO);

    PayResponse  notify(String notifyData);
}
