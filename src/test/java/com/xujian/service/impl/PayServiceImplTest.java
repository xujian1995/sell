package com.xujian.service.impl;

import com.xujian.dto.OrderDTO;
import com.xujian.service.OrderService;
import com.xujian.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class PayServiceImplTest {
@Autowired
    private PayService payService;
@Autowired
    private OrderService orderService;
    @Test
    public void create() throws Exception {
        OrderDTO orderDTO=orderService.findOne("1554364603373769752");
        payService.create(orderDTO);
    }

}