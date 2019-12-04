package com.zjzy.morebit.pojo.requestbodybean;

import com.zjzy.morebit.pojo.request.RequestBaseBean;

/**
 * Created by fengrs on 2019/7/15
 * Description:
 */
public class RequestGoodsLike extends RequestBaseBean {
    private String deviceValue;
    private String deviceType;
    private String itemSourceId;
    private int page;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getDeviceValue() {
        return deviceValue;
    }

    public void setDeviceValue(String deviceValue) {
        this.deviceValue = deviceValue;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getItemSourceId() {
        return itemSourceId;
    }

    public void setItemSourceId(String itemSourceId) {
        this.itemSourceId = itemSourceId;
    }
}
