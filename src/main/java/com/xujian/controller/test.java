package com.xujian.controller;

import com.xujian.enums.ResultEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.*;


@RestController
@RequestMapping("/test")
public class test {
    private int num=0;
    @GetMapping("/haha")
    public String haha(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        System.out.print(df.format(new Date()));// new Date()为获取当前系统时间
        System.out.println(" 访问"+num++);
            return "一起抽烟么咯，肺黑黑的那种喔";
    }
}
