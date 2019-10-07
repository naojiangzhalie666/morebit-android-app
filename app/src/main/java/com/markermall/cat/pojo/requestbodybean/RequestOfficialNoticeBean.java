package com.markermall.cat.pojo.requestbodybean;

import com.markermall.cat.pojo.request.RequestBaseBean;

/**
 * Created by JerryHo on 2019/1/7
 * Description: 请求 官方公告 bean
 */
public class RequestOfficialNoticeBean  extends RequestBaseBean {

    private String key;
    private int page;

    public String getKey() {
        return key;
    }

    public RequestOfficialNoticeBean setKey(String key) {
        this.key = key;
        return this;
    }

    public int getPage() {
        return page;
    }

    public RequestOfficialNoticeBean setPage(int page) {
        this.page = page;
        return this;
    }
}