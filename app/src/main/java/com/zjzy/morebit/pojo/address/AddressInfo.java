package com.zjzy.morebit.pojo.address;

import java.io.Serializable;

/**
 * 收货地址信息
 * Created by haiping.liu on 2019-12-12.
 */
public class AddressInfo implements Serializable {

    private Integer id;
    /**
     * 用户名称
     */
    private  String name ;
    /**
     * 是否默认(1:默认 0:否)
     */
    private int isDefault;
    /**
     * 手机号
     */
    private String tel;

    private String province;

    private String city;

    private String district;
    /**
     * 地址详细地址
     */
    private String detailAddress;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
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

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }


}