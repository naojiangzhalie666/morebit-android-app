package com.zjzy.morebit.pojo.request;

public class RequestSystemNoticeBean extends RequestBaseBean {

    private Integer type;
    private int page;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
