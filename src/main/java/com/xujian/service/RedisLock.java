package com.xujian.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
public class RedisLock {
    @Autowired
    private StringRedisTemplate  redisTemplate;
    /**加锁
     * @param key
     * @param value 当前时间+超时时间
     * @return
     * */
    public boolean lock(String key,String value){
        //value是上锁的时刻
        if(redisTemplate.opsForValue().setIfAbsent(key,value)){
            //setIfAbsent就是redis的setnx命令
            //当key不存在，则把key-value设定进去 上锁成功
                return true;
        }
        //如果key存在，则上锁失败
        String currentValue=redisTemplate.opsForValue().get(key);//获取上一把锁的超时时间
        if(!StringUtils.isEmpty((currentValue)) && Long.parseLong(currentValue)<System.currentTimeMillis()){
            //如果锁过期
            String oldValue=redisTemplate.opsForValue().getAndSet(key,value); //获取上一个锁的时间 并把当前时间设置进去
            if(!StringUtils.isEmpty(oldValue)&&oldValue.equals(currentValue)){
                //如果取回来的oldValue与currentValue相等，则说明此时间内没有其他锁去上锁，取锁成功
                return true;
            }
        }
            return  false;
    }


    /**
     * 解锁
     * @param key
     * @param value
     */
    public void unlock(String key, String value) {
        try {
            String currentValue = redisTemplate.opsForValue().get(key);
            if (!StringUtils.isEmpty(currentValue) && currentValue.equals(value)) {
                redisTemplate.opsForValue().getOperations().delete(key);//解锁就是删了这个key
            }
        }catch (Exception e) {
            log.error("【redis分布式锁】解锁异常, {}", e);
        }
    }
}
