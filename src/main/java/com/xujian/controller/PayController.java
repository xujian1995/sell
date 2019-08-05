package com.xujian.controller;

import com.lly835.bestpay.model.PayResponse;
import com.xujian.dto.OrderDTO;
import com.xujian.enums.ResultEnum;
import com.xujian.exception.SellException;
import com.xujian.service.OrderService;
import com.xujian.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Slf4j
@Controller   //返回的是一个界面
@RequestMapping("/pay")
public class PayController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private PayService payService;

    @GetMapping("/create")
    public ModelAndView create(@RequestParam("orderId") String orderId,
                               @RequestParam("returnUrl") String returnUrl,
                               Map<String,Object> map){

        //1.查询订单
        OrderDTO orderDTO=orderService.findOne(orderId);
        if(orderDTO==null){
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        //2.发起支付
        PayResponse payResponse=payService.create(orderDTO);

        map.put("payResponse", payResponse);
        map.put("returnUrl", returnUrl);

        log.info("【发起支付】orderDTO={}",orderDTO);
        ModelAndView re=new ModelAndView("pay/create");
        return re;
    }
    /**微信异步通知*/
    @PostMapping("/notify")
    public ModelAndView notify(@RequestBody String notifyData){//微信那边会传来XML格式的notifyData
        payService.notify(notifyData);
        return new ModelAndView("pay/success");//返回给微信处理结果
    }

}
