package com.markermall.cat.login.model;

/**
 * Created by fengrs on 2018/8/13.
 * 备注:
 */

public class WXRegisterException extends  Exception {
    String baseResponse;
    public WXRegisterException(String baseResponse) {
        this.baseResponse=baseResponse;
    }

    public String getBaseResponse() {
        return baseResponse;
    }
}
