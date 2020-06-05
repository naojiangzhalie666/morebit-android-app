package com.zjzy.morebit.pojo.request;

/**
 * @author fengrs
 * @date 2019/6/16
 */
public class RequestShareGoodsMoreUrlBean {

    /**
     * itemSourceId : xxx
     * itemTitle : XXX
     * itemPicture : xxx
     * couponUrl : xxx
     */

    private String shopType;
    private String itemId;
    private String couponUrl;


    public RequestShareGoodsMoreUrlBean(String shopType, String itemId, String couponUrl) {
        this.shopType = shopType;
        this.itemId = itemId;
        this.couponUrl = couponUrl;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getCouponUrl() {
        return couponUrl;
    }

    public void setCouponUrl(String couponUrl) {
        this.couponUrl = couponUrl;
    }
}
