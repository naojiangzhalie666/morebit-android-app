package com.zjzy.morebit.pojo.request;

import com.zjzy.morebit.utils.C;

public class RequestBannerBean extends RequestBaseBean {

    private int type;
    private int os = C.Setting.os;
    private int page;
    private int rows;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setOs(int os) {
        this.os = os;
    }
}



