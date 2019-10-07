package com.jf.my.pojo.request;

public class RequestWeixiLoginBean extends RequestBaseBean {

    private String oauthWx;
    private String sign;

    public String getOauthWx() {
        return oauthWx;
    }

    public void setOauthWx(String oauthWx) {
        this.oauthWx = oauthWx;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
