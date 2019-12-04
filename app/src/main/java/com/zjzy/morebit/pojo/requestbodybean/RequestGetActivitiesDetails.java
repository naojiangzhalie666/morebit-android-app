package com.zjzy.morebit.pojo.requestbodybean;

import com.zjzy.morebit.pojo.request.RequestBaseBean;

/**
 * Created by JerryHo on 2019/1/8
 * Description: 精选活动商品列表
 */
public class RequestGetActivitiesDetails  extends RequestBaseBean {
    private int page;
    private String order;
    private String sort;
    private String activityId;

    public int getPage() {
        return page;
    }

    public RequestGetActivitiesDetails setPage(int page) {
        this.page = page;
        return this;
    }

    public String getOrder() {
        return order;
    }

    public RequestGetActivitiesDetails setOrder(String order) {
        this.order = order;
        return this;
    }

    public String getSort() {
        return sort;
    }

    public RequestGetActivitiesDetails setSort(String sort) {
        this.sort = sort;
        return this;
    }

    public String getActivityId() {
        return activityId;
    }

    public RequestGetActivitiesDetails setActivityId(String activityId) {
        this.activityId = activityId;
        return this;
    }
}
