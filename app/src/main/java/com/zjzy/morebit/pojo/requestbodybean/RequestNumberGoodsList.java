package com.zjzy.morebit.pojo.requestbodybean;

import com.zjzy.morebit.pojo.request.RequestBaseBean;

/**
 * Created by haiping.liu on 2019-12-21.
 */
public class RequestNumberGoodsList extends RequestBaseBean {
    private int page;
    private int limit;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
