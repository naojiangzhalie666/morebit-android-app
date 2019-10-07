package com.jf.my.pojo.requestbodybean;

import com.jf.my.pojo.request.RequestBaseBean;

/**
 * Created by JerryHo on 2019/1/7
 * Description: 请求 三级分类数据 bean
 */
public class RequestThreeGoodsClassify  extends RequestBaseBean {

    private String type;
    private String categoryId;
    private String keyword;
    private int page;
    private String sort;
    private String order;

    public String getType() {
        return type;
    }

    public RequestThreeGoodsClassify setType(String type) {
        this.type = type;
        return this;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public RequestThreeGoodsClassify setCategoryId(String categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public String getKeyword() {
        return keyword;
    }

    public RequestThreeGoodsClassify setKeyword(String keyword) {
        this.keyword = keyword;
        return this;
    }

    public int getPage() {
        return page;
    }

    public RequestThreeGoodsClassify setPage(int page) {
        this.page = page;
        return this;
    }

    public String getSort() {
        return sort;
    }

    public RequestThreeGoodsClassify setSort(String sort) {
        this.sort = sort;
        return this;
    }

    public String getOrder() {
        return order;
    }

    public RequestThreeGoodsClassify setOrder(String order) {
        this.order = order;
        return this;
    }
}
