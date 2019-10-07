package com.markermall.cat.pojo.requestbodybean;

import com.markermall.cat.pojo.request.RequestBaseBean;

/**
 * Created by JerryHo on 2019/1/8
 * Description:九块九包邮
 */
public class RequestGetNinepinkageGoods  extends RequestBaseBean {

    private String page;
    private String sort;
    private String order;

    public String getPage() {
        return page;
    }

    public RequestGetNinepinkageGoods setPage(String page) {
        this.page = page;
        return this;
    }

    public String getSort() {
        return sort;
    }

    public RequestGetNinepinkageGoods setSort(String sort) {
        this.sort = sort;
        return this;
    }

    public String getOrder() {
        return order;
    }

    public RequestGetNinepinkageGoods setOrder(String order) {
        this.order = order;
        return this;
    }
}


