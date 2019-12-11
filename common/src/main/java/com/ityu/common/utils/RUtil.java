package com.ityu.common.utils;




public class RUtil {
    public final  static int SUCCESS = 0;
    public final static int FAILED = 1;
    public static R success(Object obj){
        R resultVO = new R();
        resultVO.setData(obj);
        resultVO.setCode(SUCCESS);
        resultVO.setMsg("成功");
        resultVO.setStatus(true);
        return resultVO;
    }
    public static R error(Integer code,String msg){
        R resultVO = new R();
        resultVO.setCode(code);
        resultVO.setMsg(msg);
        resultVO.setStatus(false);
        return resultVO;
    }
}
