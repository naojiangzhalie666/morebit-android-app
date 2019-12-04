package com.zjzy.morebit.pojo.requestbodybean;

import com.zjzy.morebit.pojo.request.RequestBaseBean;
import com.zjzy.morebit.utils.C;

/**
 * Created by JerryHo on 2019/1/7
 * Description:请求 获取html页面链接 bean
 */
public class RequestSystemStaticPage  extends RequestBaseBean {
    private int scope;
    private String page;
    private int os = C.Setting.os;

    public int getScope() {
        return scope;
    }

    public RequestSystemStaticPage setScope(int scope) {
        this.scope = scope;
        return this;
    }

    public String getPage() {
        return page;
    }

    public RequestSystemStaticPage setPage(String page) {
        this.page = page;
        return this;
    }
}
