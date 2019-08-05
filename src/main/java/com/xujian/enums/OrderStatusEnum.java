package com.xujian.enums;

import lombok.Getter;

@Getter
public enum OrderStatusEnum implements CodeEnum{//继承了这个接口但是
    NEW(0,"新订单"),
    FINISHED(1,"完结"),
    CANCEL(2,"取消"),
    ;
    private Integer code;
    private String message;
    OrderStatusEnum(Integer code, String message){
        this.code =code;
        this.message=message;
    }
}
