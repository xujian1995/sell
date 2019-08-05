package com.xujian.controller;

import com.xujian.VO.ResultVO;
import com.xujian.converter.OrderForm2OrderDTO;
import com.xujian.dto.OrderDTO;
import com.xujian.enums.ResultEnum;
import com.xujian.exception.SellException;
import com.xujian.form.OrderForm;
import com.xujian.service.BuyerService;
import com.xujian.service.impl.OrderServerImpl;
import com.xujian.util.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController//返回的是Json
@RequestMapping("/buyer/order")
@Slf4j
public class BuyerOrderController {
    @Autowired
    private OrderServerImpl orderService;
    @Autowired
    private BuyerService buyerService;
    //创建订单
    @PostMapping("/create")
    public ResultVO<Map<String,String>> create(@Valid OrderForm orderForm, BindingResult bindingResult){
        //BindingResult 和@Valid配合使用 校验传过来的OrderForm对象里的参数是否符合要求
        //不符合的信息在BindingResult对象里保存了
    if(bindingResult.hasErrors()){//订单信息不正确就抛出异常
        log.error("【创建订单】参数不正确，orderForm={}",orderForm);
        throw new SellException(ResultEnum.PARAM_ERROR.getCode(),bindingResult.getFieldError().getDefaultMessage());
    }
        OrderDTO orderDTO= OrderForm2OrderDTO.convert(orderForm);
        if(CollectionUtils.isEmpty(orderDTO.getOrderDetailList())){//格式转换完了 检查购物车是否为空
            log.error("【创建订单】购物车不能为空");
            throw new SellException(ResultEnum.CART_EMPTY);
        }
        OrderDTO createResult=orderService.create(orderDTO);
        Map<String,String> map=new HashMap<>();
        map.put("orderId",createResult.getOrderId());
        return ResultVOUtil.success(map);//创建一个成功返回的ResultVO，设定好code和messge 并往data里放对象、
    }
    //查询订单列表
    @GetMapping("/list")
        public ResultVO<List<OrderDTO>> list(@RequestParam("openid") String openid,
                                             @RequestParam(value="page",defaultValue = "0") Integer page,
                                             @RequestParam(value="size",defaultValue = "10") Integer size){
        //提升程序的健壮性  赋默认值 不报错
        if(StringUtils.isEmpty(openid)){
         log.error("【查询订单列表】openid为空");
         throw new SellException(ResultEnum.PARAM_ERROR);
        }
            PageRequest request=PageRequest.of(page,size);
            Page<OrderDTO> orderDTPPage=orderService.findList(openid,request);//查询
            return ResultVOUtil.success(orderDTPPage.getContent());
        }
    //订单详情
    @GetMapping("/detail")
        public ResultVO<List<OrderDTO>> detail(@RequestParam("openid") String openid,
                                               @RequestParam("orderId") String orderId){
        if(StringUtils.isEmpty(openid)){
            log.error("【查询订单列表】openid为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        if(StringUtils.isEmpty(orderId)){
            log.error("【查询订单列表】orderId为空");
            throw new SellException(ResultEnum.PARAM_ERROR);
        }
        OrderDTO orderDTO=buyerService.findOrderOne(openid,orderId);//鉴定了权限了
        return ResultVOUtil.success(orderDTO);

     }
    //取消订单
    @PostMapping("cancel")
    public ResultVO cancel(@RequestParam("openid") String openid,
                           @RequestParam ("orderId") String orderId){
        buyerService.cancelOrder(openid,orderId);
        return ResultVOUtil.success();
    }
}
