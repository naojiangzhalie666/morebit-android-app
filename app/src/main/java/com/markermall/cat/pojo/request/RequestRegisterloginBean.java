package com.markermall.cat.pojo.request;

public class RequestRegisterloginBean extends  RequestBaseBean{

    private String phone;
    private String verCode;
    private String yqmCodeOrPhone;
    private String sign;
    private String areaCode;

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVerCode() {
        return verCode;
    }

    public void setVerCode(String verCode) {
        this.verCode = verCode;
    }

    public String getYqmCodeOrPhone() {
        return yqmCodeOrPhone;
    }

    public void setYqmCodeOrPhone(String yqmCodeOrPhone) {
        this.yqmCodeOrPhone = yqmCodeOrPhone;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
