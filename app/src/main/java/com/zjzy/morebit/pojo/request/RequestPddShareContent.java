package com.zjzy.morebit.pojo.request;

/**
 * Created by haiping.liu on 2020-03-24.
 */
public class RequestPddShareContent extends RequestBaseBean {
    private String goodsId;
    private String goodsTitle;
    private String goodsDesc;
    private String goodsPrice;
    private String goodsVoucherPrice;
    private String isShortLink;
    private int isInviteCode;
    private int isDownLoadUrl;
    private String  voucherPrice;
    private String price;
    private String itemTitle;
    private String clickURL;

    public String getClickURL() {
        return clickURL;
    }

    public void setClickURL(String clickURL) {
        this.clickURL = clickURL;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getVoucherPrice() {
        return voucherPrice;
    }

    public void setVoucherPrice(String voucherPrice) {
        this.voucherPrice = voucherPrice;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsTitle() {
        return goodsTitle;
    }

    public void setGoodsTitle(String goodsTitle) {
        this.goodsTitle = goodsTitle;
    }

    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsVoucherPrice() {
        return goodsVoucherPrice;
    }

    public void setGoodsVoucherPrice(String goodsVoucherPrice) {
        this.goodsVoucherPrice = goodsVoucherPrice;
    }

    public String getIsShortLink() {
        return isShortLink;
    }

    public void setIsShortLink(String isShortLink) {
        this.isShortLink = isShortLink;
    }

    public int getIsInviteCode() {
        return isInviteCode;
    }

    public void setIsInviteCode(int isInviteCode) {
        this.isInviteCode = isInviteCode;
    }

    public int getIsDownLoadUrl() {
        return isDownLoadUrl;
    }

    public void setIsDownLoadUrl(int isDownLoadUrl) {
        this.isDownLoadUrl = isDownLoadUrl;
    }
}
