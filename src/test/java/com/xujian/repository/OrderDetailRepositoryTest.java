package com.xujian.repository;

import com.xujian.dataobject.OrderDetail;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderDetailRepositoryTest {
@Autowired
    private OrderDetailRepository repository;

@Test
    public  void saveTest(){
    OrderDetail orderDetail=new OrderDetail();
    orderDetail.setDetailId("12");
    orderDetail.setOrderId("12346");
    orderDetail.setProductName("苹果");
    orderDetail.setProductId("apple");
    orderDetail.setProductIcon("asd");
    orderDetail.setProductPrice(new BigDecimal(100));
    orderDetail.setProductQuantity(30);

    repository.save(orderDetail);
}
@Test
    public void findByOrderId(){
    List<OrderDetail> orderDetailList=repository.findByOrderId("12346");
    Assert.assertNotEquals(0,((List) orderDetailList).size());
}
}