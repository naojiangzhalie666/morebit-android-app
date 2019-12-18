package com.zjzy.morebit.pojo.number;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by haiping.liu on 2019-12-12.
 */
public class NumberGoodsInfo implements Serializable {
    /**
     * 商品编号
     */
    private  String goodsSn;
    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品轮播图
     */
    private ArrayList<String> gallery;


    private String brief;

    private String picUrl;
    /**
     * 商品单位
     */
    private String unit;
    /**
     *
     */
    private double retailPrice;
    /**
     * 商品详情
     */
    private String detail;
    /**
     * 库存
     */
    private int inventory;

    public String getGoodsSn() {
        return goodsSn;
    }

    public void setGoodsSn(String goodsSn) {
        this.goodsSn = goodsSn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getGallery() {
        return gallery;
    }

    public void setGallery(ArrayList<String> gallery) {
        this.gallery = gallery;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(double retailPrice) {
        this.retailPrice = retailPrice;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }



    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
