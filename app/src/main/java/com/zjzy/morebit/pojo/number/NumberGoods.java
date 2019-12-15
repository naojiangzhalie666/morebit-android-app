package com.zjzy.morebit.pojo.number;

import java.io.Serializable;

/**
 * 列表中会员商品item
 * Created by haiping.liu on 2019-12-11.
 */

public class NumberGoods implements Serializable {
    /**
     * 商品Id
     */
    private int id;
    /**
     *
     */

    private String name;

    /**
     * 商品图片
     */
    private String picUrl;
    /**
     * 商品描述
     */
    private String brief;
    /**
     * 商品销售价格
     */
    private String retailPrice;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(String retailPrice) {
        this.retailPrice = retailPrice;
    }
}
