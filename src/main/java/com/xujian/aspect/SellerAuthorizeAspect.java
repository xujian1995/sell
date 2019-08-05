package com.xujian.aspect;

import com.xujian.Constant.CookieConstant;
import com.xujian.Constant.RedisConstant;
import com.xujian.exception.SellerAuthorizeException;
import com.xujian.util.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@Aspect
@Component
public class SellerAuthorizeAspect {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Pointcut("execution(public * com.xujian.controller.Seller*.*(..))" +
            "&&!execution(public * com.xujian.controller.SellerUserController.*(..))        ")
    public void verify() {//定义切入点
    }
    @Before("verify()")
    public void doVerify(){
        /**
         * 校验不通过抛出异常 再对异常捕获
         * */
        ServletRequestAttributes attributes= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request=attributes.getRequest();
        //查询cookie
        Cookie cookie= CookieUtil.get(request, CookieConstant.TOKEN);
        if(cookie==null){
            log.warn("【登陆校验】Cookie中查不到token");
            throw new SellerAuthorizeException();
        }
        //去redis查
        String tokenValue=redisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN_PREFIX,cookie.getValue()));
        if(StringUtils.isEmpty(tokenValue)){
            log.warn("【登陆校验】Redis中查不到token");
            throw new SellerAuthorizeException();
        }
    };
}
