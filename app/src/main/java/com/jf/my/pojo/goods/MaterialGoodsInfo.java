package com.jf.my.pojo.goods;

import java.util.List;

/**
 * @author fengrs
 * @date 2019/6/21
 */
public class MaterialGoodsInfo {
    private String commissionRate;//佣金比例
    private String couponAmount;//优惠劵金额
    private String itemId;// 商品ID
    private String pictUrl;//主图
    private String title;//标题
    private String zkFinalPrice;// 原价
    private String commission;//总佣金
    private List<String> smallImages;//轮播图


    public String getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(String commissionRate) {
        this.commissionRate = commissionRate;
    }

    public String getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(String couponAmount) {
        this.couponAmount = couponAmount;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getPictUrl() {
        return pictUrl;
    }

    public void setPictUrl(String pictUrl) {
        this.pictUrl = pictUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getZkFinalPrice() {
        return zkFinalPrice;
    }

    public void setZkFinalPrice(String zkFinalPrice) {
        this.zkFinalPrice = zkFinalPrice;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }
}
