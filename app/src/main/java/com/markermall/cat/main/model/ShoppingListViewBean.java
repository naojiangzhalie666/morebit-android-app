package com.markermall.cat.main.model;

import java.io.Serializable;

/**
 * Created by fengrs on 2018/9/11.
 * 备注:  数据列表头部显示
 */

public class ShoppingListViewBean implements Serializable {
    int headViewType;//1 图片  2 .gradview
    String HeadImg; // 图片地址
    String cId; // 二级列表用到滴
    String keywords; // 二级列表用到滴
    String userId; //  店铺的id
    String typeName; //  跳转过来的类型

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getHeadViewType() {
        return headViewType;
    }

    public void setHeadViewType(int headViewType) {
        this.headViewType = headViewType;
    }

    public String getHeadImg() {
        return HeadImg;
    }

    public void setHeadImg(String headImg) {
        HeadImg = headImg;
    }
}
