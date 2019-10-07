package com.markermall.cat.pojo.request;

import com.markermall.cat.pojo.CategoryListDtos;
import com.markermall.cat.pojo.ShopGoodInfo;

import java.util.List;

/**
 * Created by YangBoTian on 2019/6/14.
 */

public class RequestReleaseGoods extends RequestBaseBean {
    private String id;
    private String itemId;
    private String icon;
    private String content;
    private String name;
    private String picture;
    private List<CategoryListDtos> categoryListDtos;
    private List<ShopGoodInfo> goods;
     private String itemBanner;
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

    public List<CategoryListDtos> getCategoryListDtos() {
        return categoryListDtos;
    }

    public void setCategoryListDtos(List<CategoryListDtos> categoryListDtos) {
        this.categoryListDtos = categoryListDtos;
    }

    public List<ShopGoodInfo> getGoods() {
        return goods;
    }

    public void setGoods(List<ShopGoodInfo> goods) {
        this.goods = goods;
    }

    public String getItemBanner() {
        return itemBanner;
    }

    public void setItemBanner(String itemBanner) {
        this.itemBanner = itemBanner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
