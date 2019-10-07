package com.markermall.cat.pojo.requestbodybean;

import com.markermall.cat.pojo.request.RequestBaseBean;

/**
 * Created by JerryHo on 2019/1/8
 * Description:通过品牌ID获取商品列表
 */
public class RequestGetGoodsByBrand  extends RequestBaseBean {

    private String brandId;
    private String page;
    private String order;
    private String sort;

    public String getBrandId() {
        return brandId;
    }

    public RequestGetGoodsByBrand setBrandId(String brandId) {
        this.brandId = brandId;
        return this;
    }

    public String getPage() {
        return page;
    }

    public RequestGetGoodsByBrand setPage(String page) {
        this.page = page;
        return this;
    }

    public String getOrder() {
        return order;
    }

    public RequestGetGoodsByBrand setOrder(String order) {
        this.order = order;
        return this;
    }

    public String getSort() {
        return sort;
    }

    public RequestGetGoodsByBrand setSort(String sort) {
        this.sort = sort;
        return this;
    }
}
