package com.zjzy.morebit.pojo.number;

import java.io.Serializable;

/**
 * 确认订单的实体（支付前的实体信息）
 * Created by haiping.liu on 2019-12-12.
 */
public class GoodsOrderInfo implements Serializable {
    /**
     * 商品Id
     */
    private int goodsId;
    /**
     * 商品介绍图
     */
    private String image;
    /**
     * 商品标题
     */
    private String title;
    /**
     * 商品规格
     */
    private String spec;
    /**
     * 商品价格
     */
    private String price;
    /**
     * 商品数量
     */
    private int count;
    /**
     * 商品总价
     */
    private String goodsTotalPrice;
    /**
     * 实际支付总额
     */
    private String payPrice;


    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getGoodsTotalPrice() {
        return goodsTotalPrice;
    }

    public void setGoodsTotalPrice(String goodsTotalPrice) {
        this.goodsTotalPrice = goodsTotalPrice;
    }

    public String getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(String payPrice) {
        this.payPrice = payPrice;
    }
}
