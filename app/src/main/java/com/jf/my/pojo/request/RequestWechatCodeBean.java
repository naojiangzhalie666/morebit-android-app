package com.jf.my.pojo.request;

public class RequestWechatCodeBean extends RequestBaseBean {

    private String wxQrCode;
    private String wxNumber;
    private String wxShowType;
    private String sign;

    public String getWxShowType() {
        return wxShowType;
    }

    public void setWxShowType(String wxShowType) {
        this.wxShowType = wxShowType;
    }

    public String getWxQrCode() {
        return wxQrCode;
    }

    public void setWxQrCode(String wxQrCode) {
        this.wxQrCode = wxQrCode;
    }

    public String getWxNumber() {
        return wxNumber;
    }

    public void setWxNumber(String wxNumber) {
        this.wxNumber = wxNumber;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
