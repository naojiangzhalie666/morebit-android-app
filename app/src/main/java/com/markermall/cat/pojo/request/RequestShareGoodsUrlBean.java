package com.markermall.cat.pojo.request;

/**
 * @author fengrs
 * @date 2019/6/16
 */
public class RequestShareGoodsUrlBean {

    /**
     * itemSourceId : xxx
     * itemTitle : XXX
     * itemPicture : xxx
     * couponUrl : xxx
     */

    private String itemSourceId;
    private String itemTitle;
    private String itemPicture;
    private String couponUrl;

    public RequestShareGoodsUrlBean(String itemSourceId, String itemTitle, String itemPicture, String couponUrl) {
        this.itemSourceId = itemSourceId;
        this.itemTitle = itemTitle;
        this.itemPicture = itemPicture;
        this.couponUrl = couponUrl;
    }

    public String getItemSourceId() {
        return itemSourceId;
    }

    public void setItemSourceId(String itemSourceId) {
        this.itemSourceId = itemSourceId;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemPicture() {
        return itemPicture;
    }

    public void setItemPicture(String itemPicture) {
        this.itemPicture = itemPicture;
    }

    public String getCouponUrl() {
        return couponUrl;
    }

    public void setCouponUrl(String couponUrl) {
        this.couponUrl = couponUrl;
    }
}
