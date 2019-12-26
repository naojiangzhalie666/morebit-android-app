package com.zjzy.morebit.pojo.order;

import java.io.Serializable;

/**
 * 同步支付结果。
 * Created by haiping.liu on 2019-12-18.
 */
public class OrderSyncResult implements Serializable {
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
