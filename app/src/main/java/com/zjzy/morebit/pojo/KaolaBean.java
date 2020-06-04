package com.zjzy.morebit.pojo;

import java.io.Serializable;
import java.util.List;

public class KaolaBean implements Serializable {
    private List<String> imageList;//轮播
    private List<String> detailImgList;//图文详情中的图片
    private String goodsTitle;//标题
    private String commission;//佣金
    private String goodsId;//商品id
    private String goodsDetailUrl;//商品详情链接
    private String purchaseLink;//购买链接
    private String currentPrice;//现价
    private String marketPrice;//原价
    private int coller;//是否收藏

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public List<String> getDetailImgList() {
        return detailImgList;
    }

    public void setDetailImgList(List<String> detailImgList) {
        this.detailImgList = detailImgList;
    }

    public String getGoodsTitle() {
        return goodsTitle;
    }

    public void setGoodsTitle(String goodsTitle) {
        this.goodsTitle = goodsTitle;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsDetailUrl() {
        return goodsDetailUrl;
    }

    public void setGoodsDetailUrl(String goodsDetailUrl) {
        this.goodsDetailUrl = goodsDetailUrl;
    }

    public String getPurchaseLink() {
        return purchaseLink;
    }

    public void setPurchaseLink(String purchaseLink) {
        this.purchaseLink = purchaseLink;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(String marketPrice) {
        this.marketPrice = marketPrice;
    }

    public int getColler() {
        return coller;
    }

    public void setColler(int coller) {
        this.coller = coller;
    }
}
