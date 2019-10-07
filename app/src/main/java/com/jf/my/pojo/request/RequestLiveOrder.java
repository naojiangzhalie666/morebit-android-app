package com.jf.my.pojo.request;

/**
 * Created by YangBoTian on 2019/1/8.
 */

public class RequestLiveOrder extends RequestListBody {
    private int orderStatus;

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }
}
