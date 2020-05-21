package com.zjzy.morebit.pojo.request;

public class RequestWeixiLoginBean extends RequestBaseBean {

    private String oauthWx;
    private String sign;
    private String unionid;

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

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
