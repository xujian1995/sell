package com.xujian.repository;

import com.xujian.dataobject.SellerInfo;
import com.xujian.util.KeyUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class SellerInfoRepositoryTest {
    @Autowired
    private SellerInfoRepository repository;
    @Test
    public void save(){
    SellerInfo sellerInfo=new SellerInfo();
    sellerInfo.setSellerId(KeyUtil.genUniqueKey());
    sellerInfo.setUsername("admin");
    sellerInfo.setPassword("123456");
    sellerInfo.setOpenid("abc");
    SellerInfo result =repository.save(sellerInfo);
        Assert.assertNotNull(result);
}

@Test
    public void findByOpneid() throws  Exception{
        SellerInfo sellerInfo=repository.findByOpenid("abc");
        Assert.assertNotNull(sellerInfo);
}
}