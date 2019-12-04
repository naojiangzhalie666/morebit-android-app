package com.zjzy.morebit.pojo;

import java.io.Serializable;

/**
 * @Author: wuchaowen
 * @Description:
 **/
public class AreaCodeBean implements Serializable {
    private String country;
    private String areaCode;
    private int phoneLength;
    private String prefix;



    public AreaCodeBean(String country, String areaCode) {
        this.country = country;
        this.areaCode = areaCode;
    }

    public AreaCodeBean(String country, String areaCode, int phoneLength, String prefix) {
        this.country = country;
        this.areaCode = areaCode;
        this.phoneLength = phoneLength;
        this.prefix = prefix;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public int getPhoneLength() {
        return phoneLength;
    }

    public void setPhoneLength(int phoneLength) {
        this.phoneLength = phoneLength;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
