package com.jf.my.pojo.requestbodybean;

import com.jf.my.pojo.request.RequestBaseBean;

/**
 * Created by JerryHo on 2019/1/8
 * Description:大部分列表
 */
public class RequestGetTimedSpikeList  extends RequestBaseBean {

   private String hourType;
   private int page;

    public String getHourType() {
        return hourType;
    }

    public RequestGetTimedSpikeList setHourType(String hourType) {
        this.hourType = hourType;
        return this;
    }

    public int getPage() {
        return page;
    }

    public RequestGetTimedSpikeList setPage(int page) {
        this.page = page;
        return this;
    }
}
