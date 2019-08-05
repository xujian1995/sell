package com.xujian.repository;

import com.xujian.dataobject.OrderMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderMasterRepository extends JpaRepository<OrderMaster,String>{
    //查询一个OpenID的所有订单  分页查询
    Page<OrderMaster> findByBuyerOpenid(String buyerOpenid, Pageable pageable);


}
