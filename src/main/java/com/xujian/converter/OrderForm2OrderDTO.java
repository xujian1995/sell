package com.xujian.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xujian.dataobject.OrderDetail;
import com.xujian.dto.OrderDTO;
import com.xujian.enums.ResultEnum;
import com.xujian.exception.SellException;
import com.xujian.form.OrderForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
@Slf4j
public class OrderForm2OrderDTO {
    public static OrderDTO convert(OrderForm orderForm){
       OrderDTO orderDTO=new OrderDTO();

       orderDTO.setBuyerName(orderForm.getName());
       orderDTO.setBuyerPhone(orderForm.getPhone());
       orderDTO.setBuyerAddress(orderForm.getAddress());
       orderDTO.setBuyerOpenid(orderForm.getOpenid());
        //这里不能用BeanUtils.copyProperties()，因为变量名不一样
        //购物车传过来的是一个字符 是JSON格式的 要转成对象
        Gson gson=new Gson();
        List<OrderDetail> orderDetailList=new ArrayList<>();
        try{//从JSON转成多个OrderDetail对象 存进List
             orderDetailList=gson.fromJson(orderForm.getItems(),new TypeToken<List<OrderDetail>>(){}.getType());
        }catch (Exception e){
            log.error("【对象转换】错误，String={}",orderForm.getItems());
            throw  new SellException(ResultEnum.PARAM_ERROR);
        }

       orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }
}
