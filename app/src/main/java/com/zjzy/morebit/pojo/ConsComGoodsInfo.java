package com.zjzy.morebit.pojo;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/7.
 */

public class ConsComGoodsInfo  implements Serializable{


    /**
     * token : zhitu_uSVnYLWLeGpk
     * orderSn : 167529301081711784-1
     * itemName : 盼盼梅尼耶干蛋糕奶香味240g早餐食品糕点面包干饼干休闲零食
     * itemImg : http://img2.tbcdn.cn/tfscom/i4/TB1b2ZlNXXXXXXYXFXXXXXXXXXX_!!0-item_pic.jpg
     * itemId : 538491816470
     * paymentAmount : 19.90
     * status : 2
     * orderType : 1
     * itemVoucherPrice : 0.07
     */
    private String orderSn = "";//订单号
    private String itemName = "";//商品名称
    private String itemImg = "";//商品主图
    private String itemId;//商品ID
    private String paymentAmount = "";//付款金额
    private String status; // 多点优选订单是:付款1，成功2，结算3，失效4    多点优选生活订单是:1.全部; 2.待结算; 3.已结算;
    private String orderType = "";//订单类型：（1）淘宝（2）天猫
    private String createTime = "";//订单创建时间
    private String couponUrl;//优惠券链接
    private String settlementTime = "";//订单结算时间
    private String itemVoucherPrice;//券后价
    private String commission;//佣金
    private String subsidy; //补贴
    private String shipUrl;//物流信息
    private String hasConfirm;//0:未收货，1：已收货。
    private String goodsNum;//购买数量
    private String unit;//规格
    private int type;
    private boolean zeroOrder;//true 是0元购  false 不是
    private boolean isOnSale; //true 是上架  false过期
    private boolean isSelf; //ture 自购 false 非自购

    public boolean isSelf() {
        return isSelf;
    }

    public void setSelf(boolean self) {
        isSelf = self;
    }

    public boolean isOnSale() {
        return isOnSale;
    }

    public void setOnSale(boolean onSale) {
        isOnSale = onSale;
    }

    public boolean isZeroOrder() {
        return zeroOrder;
    }

    public void setZeroOrder(boolean zeroOrder) {
        this.zeroOrder = zeroOrder;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(String goodsNum) {
        this.goodsNum = goodsNum;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getHasConfirm() {
        return hasConfirm;
    }

    public void setHasConfirm(String hasConfirm) {
        this.hasConfirm = hasConfirm;
    }

    public String getShipUrl() {
        return shipUrl;
    }

    public void setShipUrl(String shipUrl) {
        this.shipUrl = shipUrl;
    }

    public String getSubsidy() {
        return subsidy;
    }

    public void setSubsidy(String subsidy) {
        this.subsidy = subsidy;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getCouponUrl() {
        return couponUrl;
    }

    public void setCouponUrl(String couponUrl) {
        this.couponUrl = couponUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSettlementTime() {
        return settlementTime;
    }

    public void setSettlementTime(String settlementTime) {
        this.settlementTime = settlementTime;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemImg() {
        return itemImg;
    }

    public void setItemImg(String itemImg) {
        this.itemImg = itemImg;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getItemVoucherPrice() {
        return itemVoucherPrice;
    }

    public void setItemVoucherPrice(String itemVoucherPrice) {
        this.itemVoucherPrice = itemVoucherPrice;
    }
}
