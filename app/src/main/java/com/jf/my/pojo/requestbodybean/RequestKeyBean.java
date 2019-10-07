package com.jf.my.pojo.requestbodybean;

import com.jf.my.pojo.request.RequestBaseBean;

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
