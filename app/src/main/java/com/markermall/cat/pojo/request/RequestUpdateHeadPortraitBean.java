package com.markermall.cat.pojo.request;

public class RequestUpdateHeadPortraitBean extends RequestBaseBean {

    private String headImg;
    private String sign;

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
