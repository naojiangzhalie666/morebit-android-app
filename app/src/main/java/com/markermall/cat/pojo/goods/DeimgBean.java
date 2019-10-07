package com.markermall.cat.pojo.goods;

/**
 * Created by fengrs on 2018/11/3.
 * 备注:
 */

public class DeimgBean {
    private String picUrl;
    private int width;
    private int height;

    public DeimgBean(String picUrl, int width, int height) {
        this.picUrl = picUrl;
        this.width = width;
        this.height = height;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
