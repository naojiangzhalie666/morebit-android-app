package com.zjzy.morebit.pojo;

import java.io.Serializable;

public class RequestNineBean  implements Serializable {
    private  int page;
    private  String order;
    private String sort;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
