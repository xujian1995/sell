package com.xujian.repository;

import com.xujian.dataobject.OrderMaster;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderMasterRepositoryTest {
    @Autowired
    private OrderMasterRepository repository;
    @Test
    public void saveTest(){
        OrderMaster orderMaster=new OrderMaster();
        orderMaster.setOrderId("1233");
        orderMaster.setBuyerName("徐剑");
        orderMaster.setBuyerPhone("18380101771");
        orderMaster.setBuyerAddress("电子科技大学科研楼B区");
        orderMaster.setBuyerOpenid("110");
        orderMaster.setOrderAmount(new BigDecimal(100));
        repository.save(orderMaster);
    }
    @Test
    public void findByBuyerOpenid(){
        PageRequest request=new PageRequest(0,10);
        Page<OrderMaster> result=repository.findByBuyerOpenid("110",request);
    System.out.println(result.getTotalElements());
}
}