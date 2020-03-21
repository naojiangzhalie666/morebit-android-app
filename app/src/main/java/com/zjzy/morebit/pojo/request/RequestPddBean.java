package com.zjzy.morebit.pojo.request;

/**
 * 生产拼多多推广链接
 * Created by haiping.liu on 2020-03-17.
 */
public class RequestPddBean extends RequestBaseBean {
    private int type;
    private int goodsId;
    private String couponUrl;
    private String pid;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getCouponUrl() {
        return couponUrl;
    }

    public void setCouponUrl(String couponUrl) {
        this.couponUrl = couponUrl;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
