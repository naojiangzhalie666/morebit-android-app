package com.markermall.cat.pojo.requestbodybean;

import com.markermall.cat.pojo.request.RequestBaseBean;

/**
 * Created by JerryHo on 2019/1/7
 * Description:
 */
public class RequestKeyBean  extends RequestBaseBean {

   private String key;

    public String getKey() {
        return key;
    }

    public RequestKeyBean setKey(String key) {
        this.key = key;
        return this;
    }
}
