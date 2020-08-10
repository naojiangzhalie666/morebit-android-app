package com.zjzy.morebit.pojo.request;

import java.io.Serializable;

/**
 * 拼多多的搜索结果
 */
public class RequestSearchForPddBean implements Serializable {

    private String type = "2";

    //排序字段 单价 price ，佣金比例 commissionShare，销量 inOrderCount30Days
    private String sort;
    //desc/asc
    private String order;

    private int page;

    private String keyword;
    private String  minPrice;
    private String  maxPrice;
    private String coupon;

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keywords) {
        this.keyword = keywords;
    }


}
