package com.zjzy.morebit.order;

import java.io.Serializable;

/**
 * 订单详情
 * Created by haiping.liu on 2019-12-13.
 */
public class OrderDetailInfo implements Serializable {
    /**
     * 支付宝单号
     */
    private String payId;
    /**
     * 商品名称
     *
     */
    private String detail;
    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 支付时间
     */
    private String payTime;
    /**
     * 发货时间
     */
    private String shipTime;
    /**
     * 成交时间
     */
    private String finalTime;

    /**
     * 物流单号
     */
    private String shipSn;
    /**
     * 物流公司
     */
    private String shipChannel;
    /**
     * 总金额
     */
    private double actualPrice;
    /**
     * 商品金额
     */
    private double goodsPrice;
    /**
     * 详细地址
     */
    private String addressDetail;
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 购买数量
     */
    private int number;
    /**
     * 收货人姓名
     */
    private String name;
    /**
     * 订单状态
     */
    private int orderStatus;
    /**
     * 订单号
     */
    private String orderId;
    /**
     * 佣金
     */
    private String commission;
    /**
     * 商品图片
     */
    private String goodsUrl;


    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }


    public String getShipSn() {
        return shipSn;
    }

    public void setShipSn(String shipSn) {
        this.shipSn = shipSn;
    }

    public String getShipChannel() {
        return shipChannel;
    }

    public void setShipChannel(String shipChannel) {
        this.shipChannel = shipChannel;
    }

    public double getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(double actualPrice) {
        this.actualPrice = actualPrice;
    }

    public double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getShipTime() {
        return shipTime;
    }

    public void setShipTime(String shipTime) {
        this.shipTime = shipTime;
    }

    public String getFinalTime() {
        return finalTime;
    }

    public void setFinalTime(String finalTime) {
        this.finalTime = finalTime;
    }

    public String getGoodsUrl() {
        return goodsUrl;
    }

    public void setGoodsUrl(String goodsUrl) {
        this.goodsUrl = goodsUrl;
    }
}
