package com.ityu.common.utils;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value="R",description="统一数据信息")
public class R<T> implements Serializable {
    @ApiModelProperty(value="状态吗 0成功 1失败",name="code",example="0")
    public int code;
    @ApiModelProperty(value="状态吗 true成功 false失败",name="status",example="0")
    public boolean status;
    @ApiModelProperty(value="错误信息",name="msg",example="密码不对")
    public String msg;
    @ApiModelProperty(value="对象信息",name="data")
    public T data;
}
