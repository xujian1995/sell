package com.xujian.handler;

import com.xujian.VO.ResultVO;
import com.xujian.config.ProjectUrl;
import com.xujian.enums.ResultEnum;
import com.xujian.exception.SellException;
import com.xujian.exception.SellerAuthorizeException;
import com.xujian.util.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice//统一捕获异常并处理
public class SellExceptionHandler {

    @Autowired
    private ProjectUrl projectUrl;

    //拦截登陆异常
    @ExceptionHandler(value= SellerAuthorizeException.class)
    public ModelAndView handlerAuthorizeException(){
        //http://eatxujian1995.natapp1.cc/sell/wechat/qrAuthorize?returnUrl=http://eatxujian1995.natapp1.cc/sell/seller/login
        return new ModelAndView("redirect:"
                .concat(projectUrl.getWechatMpAuthorize())
                .concat("/sell/wechat/qrAuthorize")
                .concat("?returnUrl=")
                .concat(projectUrl.getSell())
                .concat("/sell/seller/login"));
    }


    //拦截下单错误的异常
    @ExceptionHandler(value= SellException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResultVO handlerSellException(SellException e){//返回Json格式
        return ResultVOUtil.error(e.getCode(),e.getMessage());
    }
}
