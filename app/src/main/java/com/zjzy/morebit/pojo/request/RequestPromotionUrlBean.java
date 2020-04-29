package com.zjzy.morebit.pojo.request;

/**
 * Created by haiping.liu on 2020-03-22.
 */
public class RequestPromotionUrlBean extends RequestBaseBean {
    private int type;
    private Long goodsId;
    private String couponUrl;
    private String productUrl;

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public int getType(

    ) {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getCouponUrl() {
        return couponUrl;
    }

    public void setCouponUrl(String couponUrl) {
        this.couponUrl = couponUrl;
    }
}
