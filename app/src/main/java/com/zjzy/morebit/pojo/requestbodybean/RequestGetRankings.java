package com.zjzy.morebit.pojo.requestbodybean;

import com.zjzy.morebit.pojo.request.RequestBaseBean;

/**
 * Created by JerryHo on 2019/1/8
 * Description: 1.实时排行 / 2.今日排行
 */
public class RequestGetRankings  extends RequestBaseBean {
    private String type;
    private String page;
    private String order;
    private String sort;

    public String getType() {
        return type;
    }

    public RequestGetRankings setType(String type) {
        this.type = type;
        return this;
    }

    public String getPage() {
        return page;
    }

    public RequestGetRankings setPage(String page) {
        this.page = page;
        return this;
    }

    public String getOrder() {
        return order;
    }

    public RequestGetRankings setOrder(String order) {
        this.order = order;
        return this;
    }

    public String getSort() {
        return sort;
    }

    public RequestGetRankings setSort(String sort) {
        this.sort = sort;
        return this;
    }
}
