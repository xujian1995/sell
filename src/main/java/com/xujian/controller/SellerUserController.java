package com.xujian.controller;

import com.xujian.Constant.CookieConstant;
import com.xujian.Constant.RedisConstant;
import com.xujian.config.ProjectUrl;
import com.xujian.dataobject.SellerInfo;
import com.xujian.enums.ResultEnum;
import com.xujian.service.SellerService;
import com.xujian.util.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**卖家用户*/
@Controller
@RequestMapping("/seller")
@Slf4j
public class SellerUserController {
    @Autowired
    private SellerService sellerService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ProjectUrl projectUrl;
    /**卖家登陆*/
    @GetMapping("/login")
    public ModelAndView login(@RequestParam("openid") String openid,
                              HttpServletResponse response,
                              Map<String,Object> map){//这个map是传进来的啊
        //1.和数据库匹配openid
        SellerInfo sellerInfo=sellerService.findSellerInfoByOpenid(openid);
        if(sellerInfo==null){
            map.put("msg", ResultEnum.LOGIN_FAIL);
            map.put("url","/sell/seller/order/list");//登陆失败 跳转到订单列表页
            log.info("【卖家登陆失败】 sellerinfo={}",sellerInfo);
            return new ModelAndView("common/error");
        }
        //2.设置token至redis
        String token= UUID.randomUUID().toString();
        Integer expire= RedisConstant.EXPIRE;
                 //设定key 、value、过期时间、过期时间单位
        redisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_PREFIX,token),openid,expire, TimeUnit.SECONDS);
        //3.设置token至cookie
        CookieUtil.set(response, CookieConstant.TOKEN,token,expire);
        return new ModelAndView("redirect:"+projectUrl.getSell()+"/sell/seller/order/list");
    }
    /**卖家登出
     *
     * 从redis消除token
     * */
    @GetMapping("/logout")
    public ModelAndView logout(HttpServletRequest request,
                               HttpServletResponse response,
                               Map<String ,Object> map){
        //1.从cookie里查询
        Cookie cookie=CookieUtil.get(request,CookieConstant.TOKEN);//输入cookie的名字 查询
        if(cookie!=null){
            //2.清除redis
                redisTemplate.opsForValue().getOperations().delete(String.format(RedisConstant.TOKEN_PREFIX,cookie.getValue()));

            //3.清除cookie
            CookieUtil.set(response,CookieConstant.TOKEN,null,0);
        }
        map.put("msg",ResultEnum.LOGOUT_SUCCESS.getMessage());
        map.put("url","/sell/seller/order/list");
        return new ModelAndView("common/success",map);

    }
}
