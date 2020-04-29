package com.zjzy.morebit.pojo.request;

public class RequestRegisterAndBindWeChatBean extends RequestBaseBean {

    private String phone;
    private String verCode;
    private String oauthWx;
    private String yqmCodeOrPhone;
    private String nickname;
    private String sex;
    private String headImg;
    private String country;
    private String province;
    private String city;
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

    public String getOauthWx() {
        return oauthWx;
    }

    public void setOauthWx(String oauthWx) {
        this.oauthWx = oauthWx;
    }

    public String getYqmCodeOrPhone() {
        return yqmCodeOrPhone;
    }

    public void setYqmCodeOrPhone(String yqmCodeOrPhone) {
        this.yqmCodeOrPhone = yqmCodeOrPhone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
