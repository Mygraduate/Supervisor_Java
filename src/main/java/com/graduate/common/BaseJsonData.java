package com.graduate.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by konglinghai on 2017/3/21.
 */
@ApiModel()
public class BaseJsonData<T> implements Serializable {
    private static final long serialVersionUID = 6169666971803986733L;
    @ApiModelProperty(notes = "错误代码 1：代表成功，-1，代表失败")
    private int code = -1;
    @ApiModelProperty(notes = "错误信息")
    private String msg;
    @ApiModelProperty(notes = "数据")
    private T data;

    public BaseJsonData(){

    }
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
