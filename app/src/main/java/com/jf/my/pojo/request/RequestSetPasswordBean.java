package com.jf.my.pojo.request;

public class RequestSetPasswordBean extends RequestBaseBean {

    private String password;
    private String sign;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
