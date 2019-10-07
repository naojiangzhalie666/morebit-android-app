package com.jf.my.pojo.goods;

import java.io.Serializable;

/**
 * Created by YangBoTian on 2018/6/15 14:24
 */

public class TKLBean implements Serializable {
    private String tkl="";
    private String couponUrl="";
    private String temp="";
    private String template="";
    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getTkl() {
        return tkl;
    }

    public void setTkl(String tkl) {
        this.tkl = tkl;
    }

    public String getCouponUrl() {
        return couponUrl;
    }

    public void setCouponUrl(String couponUrl) {
        this.couponUrl = couponUrl;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
 }
