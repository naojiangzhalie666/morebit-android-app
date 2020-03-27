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

    private String page;

    private String keyword;

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

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }



    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keywords) {
        this.keyword = keywords;
    }


}
