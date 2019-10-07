package com.jf.my.pojo.request;

import com.jf.my.pojo.request.RequestBaseBean;

/**
 * Created by YangBoTian on 2019/1/7.
 */

public class RequestListBody extends RequestBaseBean {
    protected int page ;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

}
