package com.zjzy.morebit.pojo.request;

import com.zjzy.morebit.pojo.request.RequestBaseBean;

public class RequestVideoGoodsBean extends RequestBaseBean {
    private String catId;
    private int page;

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
