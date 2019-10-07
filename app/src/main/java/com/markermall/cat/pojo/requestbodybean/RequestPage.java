package com.markermall.cat.pojo.requestbodybean;

import com.markermall.cat.pojo.request.RequestBaseBean;

/**
 * Created by JerryHo on 2019/1/8
 * Description:品牌列表
 */
public class RequestPage  extends RequestBaseBean {

    private int page;

    public int getPage() {
        return page;
    }

    public RequestPage setPage(int page) {
        this.page = page;
        return this;
    }
}
