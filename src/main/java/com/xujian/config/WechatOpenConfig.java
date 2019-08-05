package com.xujian.config;

import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class WechatOpenConfig {
    @Autowired
    private WechatAccountConfig accountConfig;
    @Bean
    public WxMpService wxOpenService(){
        WxMpService wxOpenService=new WxMpServiceImpl();
        wxOpenService.setWxMpConfigStorage(wxOpneConfigStorage());
        return wxOpenService;
    }

    @Bean
    public WxMpConfigStorage wxOpneConfigStorage(){
        WxMpInMemoryConfigStorage wxMpInMemoryConfigStorage= new WxMpInMemoryConfigStorage();
        wxMpInMemoryConfigStorage.setAppId(accountConfig.getOpenAppId());
        wxMpInMemoryConfigStorage.setSecret(accountConfig.getOpenAppSecret());
        return wxMpInMemoryConfigStorage;
    }
}
