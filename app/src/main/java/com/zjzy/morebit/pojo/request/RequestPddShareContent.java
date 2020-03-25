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
