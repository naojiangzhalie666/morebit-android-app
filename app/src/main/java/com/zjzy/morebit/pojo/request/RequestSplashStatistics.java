package com.zjzy.morebit.pojo.request;

/**
 * Created by YangBoTian on 2019/1/8.
 */

public class RequestSplashStatistics extends RequestBaseBean {
    private int type = 0;//type:类型  0.闪屏页 1.轮播图 2.首页活动栏icon
    private String adId;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }
}
