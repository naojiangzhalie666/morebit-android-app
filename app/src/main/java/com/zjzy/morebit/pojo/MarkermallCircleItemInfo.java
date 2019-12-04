package com.zjzy.morebit.pojo;

import java.io.Serializable;

/**
 * Created by YangBoTian on 2019/7/23.
 */

public class MarkermallCircleItemInfo implements Serializable {
    private int isExpire ;// 商品是否过期/已抢光 1：是 0：否
    private String template ;// 推广语
    private String itemSourceId ;// 商品id
    private String itemTitle ;// 商品标题
    private String itemPicture ;//  商品图片
    private String itemPrice ;//  在售价
    private String couponPrice;// 优惠券价格
    private String itemVoucherPrice;// 商品券后价
    private String commission ;//  佣金
    private String saleMonth ;// 月销量
    private String itemVideoid;
    private String picture;
    private int imageSource;
    private String link;
    private String pictureTitle;

    public String getPictureTitle() {
        return pictureTitle;
    }

    public void setPictureTitle(String pictureTitle) {
        this.pictureTitle = pictureTitle;
    }

    public int getImageSource() {
        return imageSource;
    }

    public void setImageSource(int imageSource) {
        this.imageSource = imageSource;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getIsExpire() {
        return isExpire;
    }

    public void setIsExpire(int isExpire) {
        this.isExpire = isExpire;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
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

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(String couponPrice) {
        this.couponPrice = couponPrice;
    }

    public String getItemVoucherPrice() {
        return itemVoucherPrice;
    }

    public void setItemVoucherPrice(String itemVoucherPrice) {
        this.itemVoucherPrice = itemVoucherPrice;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getSaleMonth() {
        return saleMonth;
    }

    public void setSaleMonth(String saleMonth) {
        this.saleMonth = saleMonth;
    }

    public String getItemVideoid() {
        return itemVideoid;
    }

    public void setItemVideoid(String itemVideoid) {
        this.itemVideoid = itemVideoid;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
