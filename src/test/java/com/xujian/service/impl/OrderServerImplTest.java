package com.xujian.service.impl;

import com.xujian.dataobject.OrderDetail;
import com.xujian.dto.OrderDTO;
import com.xujian.enums.OrderStatusEnum;
import com.xujian.enums.PayStatusEnum;
import com.xujian.repository.OrderMasterRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class OrderServerImplTest {
@Autowired
private OrderServerImpl orderService;
private OrderMasterRepository orderMasterRepository;
private final String BUYER_OPENID="oTgZpwZIe0u8c9Fnrve952FQeBu0";
private final String ORDER_ID="1553087493923876344";
    @Test
    public void create() throws Exception{
        OrderDTO orderDTO=new OrderDTO();
        orderDTO.setBuyerName("徐剑");
        orderDTO.setBuyerAddress("电子科技大学");
        orderDTO.setBuyerPhone("123456789000");
        orderDTO.setBuyerOpenid(BUYER_OPENID);

        List<OrderDetail> orderDetailList=new ArrayList<>();
        OrderDetail o1=new OrderDetail();
        OrderDetail o2=new OrderDetail();
        o1.setProductId("apple");
        o1.setProductQuantity(1);
        o2.setProductId("pipi");
        o2.setProductQuantity(1);
        orderDetailList.add(o1);
        orderDetailList.add(o2);
        orderDTO.setOrderDetailList(orderDetailList);
        OrderDTO result=orderService.create(orderDTO);
        log.info("[ 创建订单]result=",result);
        Assert.assertNotNull(result);
    }

    @Test
    public void findOne() {//出现过懒加载问题
        OrderDTO result= orderService.findOne(ORDER_ID);
        log.info("【查询单个订单】result={}",result);
        Assert.assertEquals(ORDER_ID,result.getOrderId());
    }

    @Test
    public void findList() {
//        PageRequest request=new PageRequest(0,2);
        Page<OrderDTO> orderDTOPage=orderService.findList(BUYER_OPENID,PageRequest.of(0,2));
        Assert.assertNotEquals(0,orderDTOPage.getTotalElements());
    }

    @Test
    public void cancel() {
        OrderDTO orderDTO= orderService.findOne(ORDER_ID);
        OrderDTO result=orderService.cancel(orderDTO);
        Assert.assertEquals(OrderStatusEnum.CANCEL.getCode(),result.getOrderStatus());
    }

    @Test
    public void finish() {
        OrderDTO orderDTO= orderService.findOne(ORDER_ID);
        OrderDTO result=orderService.finish(orderDTO);
        Assert.assertEquals(OrderStatusEnum.FINISHED.getCode(),result.getOrderStatus());
    }

    @Test
    public void paid() {
        OrderDTO orderDTO= orderService.findOne(ORDER_ID);
        OrderDTO result=orderService.paid(orderDTO);
        Assert.assertEquals(PayStatusEnum.SUCCESS.getCode(),result.getPayStatus());
    }

    @Test
    public  void list(){
        PageRequest request=new PageRequest(0,2);
        Page<OrderDTO> orderDTOPage=orderService.findList(request);
        Assert.assertEquals(0,orderDTOPage.getTotalElements());
    }
}