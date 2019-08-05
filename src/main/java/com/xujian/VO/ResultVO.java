package com.xujian.VO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * http请求返回的最外层对象
 *
 *
 * */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)//加这个注释是原因是不返回为空的数据给前台造成麻烦  直接不返回
public class ResultVO<T> implements  Serializable{


    /** 错误码 */
    private Integer code;
    /** 提示信息 */
    private String msg;
    /** 具体内容 */
    private T data;  //里面是对象
}
