package com.zjzy.morebit.pojo.request;

/**
 * 取消订单
 * Created by haiping.liu on 2019-12-13.
 */
public class RequestOrderDetailBean extends RequestBaseBean {
    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
