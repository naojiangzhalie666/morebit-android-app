package com.markermall.cat.pojo.requestbodybean;

import com.markermall.cat.pojo.request.RequestBaseBean;

/**
 * Created by JerryHo on 2019/1/7
 * Description:
 */
public class RequestDeleteCollection extends RequestBaseBean {

    private String ids;
    private String sign;

    public String getIds() {
        return ids;
    }

    public RequestDeleteCollection setIds(String ids) {
        this.ids = ids;
        return this;
    }

    public String getSign() {
        return sign;
    }

    public RequestDeleteCollection setSign(String sign) {
        this.sign = sign;
        return this;
    }
}
