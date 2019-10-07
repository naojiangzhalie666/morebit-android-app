package com.jf.my.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by YangBoTian on 2019/6/14.
 */

public class CircleGoods implements Serializable {

    /**
     * rid :
     * icon :
     * content :
     * name :
     * picture :
     * type : 0
     * height : 0
     * width : 0
     * CategoryListDtos : [{"id":0,"title":"","type":"3","child":[{"id":0,"title":"","type":0}]}]
     * goods : [{"itemSourceId":"","itemTitle":"","itemPrice":"","couponPrice":"","itemVoucherPrice":"","commission":"","couponStartTime":"","couponEndTime":"","couponUrl":"","itemBanner":"","itemDesc":"","itemPicture":"","saleMonth":0,"shopId":0,"sellerName":"","sellerPicture":"","shopName":"","shopType":0}]
     */

    private String rid;
    private String icon;
    private String content;
    private String name;
    private String picture;
    private int type;
    private int height;
    private int width;
    private List<ShopGoodInfo> goods;
    private List<CategoryListDtos> categoryListDtos;


    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public List<ShopGoodInfo> getGoods() {
        return goods;
    }

    public void setGoods(List<ShopGoodInfo> goods) {
        this.goods = goods;
    }

    public List<CategoryListDtos> getCategoryListDtos() {
        return categoryListDtos;
    }

    public void setCategoryListDtos(List<CategoryListDtos> categoryListDtos) {
        this.categoryListDtos = categoryListDtos;
    }
}
