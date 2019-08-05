package com.xujian.repository;

import com.xujian.dataobject.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail,String> {

    //通过orderI查找这个订单的细节
    List<OrderDetail> findByOrderId(String orderId);

}
