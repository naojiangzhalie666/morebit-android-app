package com.jf.my.pojo.requestbodybean;

import com.jf.my.pojo.request.RequestBaseBean;

/**
 * Created by JerryHo on 2019/1/8
 * Description:
 */
public class RequestItemSourceId  extends RequestBaseBean {

    private String itemSourceId;
    private String itemFrom;// 1内网 0 全网

    public String getItemFrom() {
        return itemFrom;
    }

    public RequestItemSourceId setItemFrom(String itemFrom) {
        this.itemFrom = itemFrom;
        return this;
    }

    public String getItemSourceId() {
        return itemSourceId;
    }

    public RequestItemSourceId setItemSourceId(String itemSourceId) {
        this.itemSourceId = itemSourceId;
        return this;
    }
}
