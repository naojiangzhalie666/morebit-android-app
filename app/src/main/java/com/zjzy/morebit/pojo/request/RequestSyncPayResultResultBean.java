package com.zjzy.morebit.pojo.request;

/**
 * Created by haiping.liu on 2019-12-18.
 */
public class RequestSyncPayResultResultBean extends RequestBaseBean{
    private String orderId;
    private int payStatus;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }
}
