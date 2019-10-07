package com.markermall.cat.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by YangBoTian on 2019/6/11.
 */

public class ReleaseManage implements Serializable {
    private int id;  //蜜粉圈id
    private String name;   //用户昵称
    private String icon;   //用户头像
    private String content; //文字介绍，推荐理由
    private String picture; //图片
    private int width;   //宽
    private int height;   //宽
    private List<Subject> opens;  //  专题列表
    private List<ShopGoodInfo> goods;
    //朋友圈商品管理时间
    private String  createTime;//申请时间
    private String reviewTime; //审核时间
    private String  downTime; // 下架时间
    private String remark;  //备注
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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

    public List<Subject> getOpens() {
        return opens;
    }

    public void setOpens(List<Subject> opens) {
        this.opens = opens;
    }

    public List<ShopGoodInfo> getGoods() {
        return goods;
    }

    public void setGoods(List<ShopGoodInfo> goods) {
        this.goods = goods;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(String reviewTime) {
        this.reviewTime = reviewTime;
    }

    public String getDownTime() {
        return downTime;
    }

    public void setDownTime(String downTime) {
        this.downTime = downTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
