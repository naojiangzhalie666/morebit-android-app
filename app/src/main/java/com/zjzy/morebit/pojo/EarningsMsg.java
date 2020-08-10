package com.zjzy.morebit.pojo;

import java.io.Serializable;

/**
 * Created by YangBoTian on 2018/12/4.
 */

public class EarningsMsg implements Serializable {
    public static final int ONE_TYPE = 0;
    public static final int TWO_TYPE = 1;
    private String title;
    private String digest = "";
    private String picture = "";
    private String createTime = "";
    private String message = "";
    private String orderId = "";   //订单号
    private String orderSource = "";   //订类型
    private String payPrice = "";   //订单金额
    private String commission = "";   //commission订单收益
    private int readStatus;    //0未读,1已读

    private String replyName;//反馈名称
    private String replyContent;      //反馈回复内容
    private String yearMonthDay;        //回复时间-年月日
    private String hourMinutesSecond;   //回复时间-时分秒
    private int open;     //1跳转商品详情，2内部链接，3外部链接,4分类
    private int classId;

    //二级商品列表
    private String backgroundImage ; // 二级商品列表 背景图
    //二级商品列表
    //系统通知用到
    private int mediaType ; // 0 图片 1视频

    private int width;
    private int height;
    private int id;

    // 可能增加
    private String content;
    private String income;
    private String contentSourceUrl;
    private String taobao_id;
    private  int viewType;   //列表view分类自用

    private String subsidy;  //补贴

    private String headImg;//头像
    private boolean hasEnd;//是否结束 true结束  false不结束
    private String contentDesc;//内容描述
    private String goodsId;//商品id

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getReplyName() {
        return replyName;
    }

    public void setReplyName(String replyName) {
        this.replyName = replyName;
    }

    public String getContentDesc() {
        return contentDesc;
    }

    public void setContentDesc(String contentDesc) {
        this.contentDesc = contentDesc;
    }

    public boolean isHasEnd() {
        return hasEnd;
    }

    public void setHasEnd(boolean hasEnd) {
        this.hasEnd = hasEnd;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getSubsidy() {
        return subsidy;
    }

    public void setSubsidy(String subsidy) {
        this.subsidy = subsidy;
    }

    public int getOpen() {
        return open;
    }

    public void setOpen(int open) {
        this.open = open;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate_time() {
        return createTime;
    }

    public void setCreate_time(String create_time) {
        this.createTime = create_time;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }


    public String getContentSourceUrl() {
        return contentSourceUrl;
    }

    public void setContentSourceUrl(String contentSourceUrl) {
        this.contentSourceUrl = contentSourceUrl;
    }

    public String getTaobao_id() {
        return taobao_id;
    }

    public void setTaobao_id(String taobao_id) {
        this.taobao_id = taobao_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(int readStatus) {
        this.readStatus = readStatus;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource;
    }

    public String getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(String payPrice) {
        this.payPrice = payPrice;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public String getYearMonthDay() {
        return yearMonthDay;
    }

    public void setYearMonthDay(String yearMonthDay) {
        this.yearMonthDay = yearMonthDay;
    }

    public String getHourMinutesSecond() {
        return hourMinutesSecond;
    }

    public void setHourMinutesSecond(String hourMinutesSecond) {
        this.hourMinutesSecond = hourMinutesSecond;
    }
}
