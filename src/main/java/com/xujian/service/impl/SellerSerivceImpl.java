package com.xujian.service.impl;

import com.xujian.dataobject.SellerInfo;
import com.xujian.repository.SellerInfoRepository;
import com.xujian.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerSerivceImpl implements SellerService {
    @Autowired
    private SellerInfoRepository repository;


    @Override
    public SellerInfo findSellerInfoByOpenid(String openid) {
        return repository.findByOpenid(openid);
    }
}
