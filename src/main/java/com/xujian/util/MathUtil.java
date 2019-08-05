package com.xujian.util;

public class MathUtil {
    private  static final double MONEY_RANGE=0.01;
    public static Boolean equals(double d1,double d2){
        /**
         * 比较两个double是否相等
         *
         * */

        Double result=Math.abs(d1-d2);
        if(result<MONEY_RANGE) return true;
        return false;
    }
}
