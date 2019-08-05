package com.xujian.exception;

import com.xujian.enums.ResultEnum;
import lombok.Getter;

@Getter
public class SellException extends RuntimeException{
    private  Integer code;

    public SellException(ResultEnum resultEnum) {
        //需要在有参构造上显式调用父类的构造
        super(resultEnum.getMessage());//Exceotion 这个类有一个构造 需要传入message
        this.code=resultEnum.getCode();
    }

    public SellException(Integer code,String message){
        super(message);
        this.code=code;
    }
}
