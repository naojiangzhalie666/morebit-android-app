package com.zjzy.morebit.network;

import java.io.Serializable;

/**
 * Created by YangBoTian on 2018/6/6 20:59
 */

public class BaseResponse<T> implements Serializable {
    public int status;
    public String msg;
    public String code; // 状态码(业务)  java
    public T data;

    public int getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

