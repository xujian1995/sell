package com.xujian.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix="projecturl")  //将配置文件的值映射到类上使用。
@Component
public class ProjectUrl {
    /**微信公众平台授权Url*/
    public String wechatMpAuthorize;
    /**微信开放平台授权Url*/
    public String wechatOpenAuthorize;
    /**点餐系统Url*/
    public String sell;
}
