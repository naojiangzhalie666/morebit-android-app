package com.zjzy.morebit.pojo.home;

import java.io.Serializable;

/**
 * Created by fengrs on 2018/8/29.
 */

public class HomeTopRedPagckageBean implements Serializable {
    private int status;//1：开启，2：不开启
    private String code;//红包口令

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
