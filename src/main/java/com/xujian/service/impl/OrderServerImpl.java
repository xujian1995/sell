package com.xujian.service.impl;


import com.xujian.converter.OrderMaster2OrderDTO;
import com.xujian.dataobject.OrderDetail;
import com.xujian.dataobject.OrderMaster;
import com.xujian.dataobject.ProductInfo;
import com.xujian.dto.CartDTO;
import com.xujian.dto.OrderDTO;
import com.xujian.enums.OrderStatusEnum;
import com.xujian.enums.PayStatusEnum;
import com.xujian.enums.ResultEnum;
import com.xujian.exception.SellException;
import com.xujian.repository.OrderDetailRepository;
import com.xujian.repository.OrderMasterRepository;
import com.xujian.service.OrderService;
import com.xujian.service.ProductService;
import com.xujian.util.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class OrderServerImpl implements OrderService {
    @Autowired
    private ProductService productService;  //把这个接口注入进来是因为我们要调用这个接口的方法 也就是当前这个类自己
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderMasterRepository orderMasterRepository;
    @Override
    @Transactional   //事务 抛异常就会回滚
    public OrderDTO create(OrderDTO orderDTO) {
        String orderId= KeyUtil.genUniqueKey();//生成订单id
        BigDecimal orderAmount=new BigDecimal(0);//订单总价
        //1.查询商品（数量，价格）
        for(OrderDetail orderDetail:orderDTO.getOrderDetailList()){ //多个orderDetail同属一个订单
            ProductInfo productInfo= productService.findOne(orderDetail.getProductId());//通过orderId来查productInfo
            if(productInfo==null){//如果商品不存在 抛出异常
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIS);
            }
            //2.计算订单总价
            orderAmount=productInfo.getProductPrice()//传参进来的时候不一定有orederDetail
                    .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                    .add(orderAmount);
            //写入订单详情数据
            orderDetail.setDetailId(KeyUtil.genUniqueKey());
            orderDetail.setOrderId(orderId);
            BeanUtils.copyProperties(productInfo,orderDetail);
            orderDetailRepository.save(orderDetail);

        }
        //3.写入订单数据
        OrderMaster orderMaster=new OrderMaster();
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO,orderMaster);
        //虽然在创建OrderMaster对象的时候设定的默认的订单状态 和支付状态
        //但是状态拷贝的时候被覆盖了

        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMasterRepository.save(orderMaster);

        //4.扣库存
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream().map(e ->
                new CartDTO(e.getProductId(), e.getProductQuantity())
                ).collect(Collectors.toList());//把数据收集进流Stream 再map到CarDTO里 再全都转成List
        productService.decreaseStock(cartDTOList);
        return orderDTO;
        //(多线程环境下会出现超卖问题 就是两个人同时访问正确的库存 同时下单 一个人买不了 用redis锁机制解决)
    }

    @Override
    public OrderDTO findOne(String orderId) {//通过orderId来查productInfo    出现过懒加载的问题
        OrderMaster orderMaster=orderMasterRepository.getOne(orderId);
        if(orderMaster==null){
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        List<OrderDetail> orderDetailList=orderDetailRepository.findByOrderId(orderId);//查出全部orderDetail
        if(CollectionUtils.isEmpty(orderDetailList)){
            throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
        }
        OrderDTO orderDTO=new OrderDTO();
        BeanUtils.copyProperties(orderMaster,orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);//把全部orderDetail都装进去了
        return orderDTO;
    }

    @Override//查找列表 返回一个Page对象
    public Page<OrderDTO> findList(String buyerOpenid, Pageable pageable) {
        //1.通过orderMasterRepository里写好的findByBuyerOpenid方法查出一个Page<OrderMaster>对象
        Page<OrderMaster> orderMasterPage =orderMasterRepository.findByBuyerOpenid(buyerOpenid,pageable);
        //2.用page对象的getContent方法把Page<OrderMaster>对象里的OrderMaster对象取出来 放在一个List里
        //3.通过转换器把OrderMaster全都转换成OrderDTO  存进另外一个List
        List<OrderDTO> orderDTOList=OrderMaster2OrderDTO.convert(orderMasterPage.getContent());//getContent返回一个list
        //4.构造一个Page<OrderDTO>返回
        Page<OrderDTO> orderDTOPage=new PageImpl<OrderDTO>(orderDTOList,pageable,orderMasterPage.getTotalElements());
        return orderDTOPage;
    }

    @Override
    @Transactional
    public OrderDTO cancel(OrderDTO orderDTO) {
        OrderMaster orderMaster=new OrderMaster();
        // 1、判断订单状态   只有某些状态可以取消
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
          log.error("【取消订单】订单状态不正确，orderId={}，orderStatus={}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        // 2、若可以取消 则修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderDTO,orderMaster);
        OrderMaster updateResult=orderMasterRepository.save(orderMaster);
        if(updateResult==null){
            log.error("【取消订单】更新失败，orderMaster={}",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        // 3、返还库存
        if(CollectionUtils.isEmpty(orderDTO.getOrderDetailList())){
            log.error("【取消订单】订单中无商品详情，orderDTO={}",orderDTO);
            throw new SellException(ResultEnum.ORDER_DETAIL_EMPTY);
        }
        List<CartDTO> cartDTOList=orderDTO.getOrderDetailList().stream().map(e->
                new CartDTO(e.getProductId(),e.getProductQuantity())
                ).collect(Collectors.toList());
        productService.increaseStock(cartDTOList);
        // 4、如果已支付 需要退款
        if(orderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())){
            //TODO
        }

        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO finish(OrderDTO orderDTO) {
        //判断订单状态  （新下单可以完结）
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("【订单完结】订单状态不正确，orderId={},orderStatus={}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        OrderMaster orderMaster=new OrderMaster();
        BeanUtils.copyProperties(orderDTO,orderMaster);
        OrderMaster updateResult=orderMasterRepository.save(orderMaster);
        if(updateResult==null){
            log.error("【完结订单】更新失败，orderMaster={}",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO paid(OrderDTO orderDTO) {
        //判断订单状态
        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())){
            log.error("【订单支付】订单状态不正确，orderId={},orderStatus={}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //判断支付状态
        if(!orderDTO.getOrderStatus().equals(PayStatusEnum.WAIT.getCode())){
            log.error("【订单支付】订单状态不正确，orderDTO={}",orderDTO);
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }
        //修改状态
        orderDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        OrderMaster orderMaster=new OrderMaster();
        BeanUtils.copyProperties(orderDTO,orderMaster);
        OrderMaster updateResult=orderMasterRepository.save(orderMaster);
        if(updateResult==null){
            log.error("【订单支付】更新失败，orderMaster={}",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(Pageable pageable) {
       Page<OrderMaster> orderMasterPage=orderMasterRepository.findAll(pageable);
        List<OrderDTO> orderDTOList=OrderMaster2OrderDTO.convert(orderMasterPage.getContent());//getContent返回一个list
        //4.构造一个Page<OrderDTO>返回
        Page<OrderDTO> orderDTOPage=new PageImpl<OrderDTO>(orderDTOList,pageable,orderMasterPage.getTotalElements());
        return orderDTOPage;
    }
}
