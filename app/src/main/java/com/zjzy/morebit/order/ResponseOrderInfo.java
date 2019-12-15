package com.zjzy.morebit.order;

import java.io.Serializable;

/**
 * 创建订单后返回的实体
 * Created by haiping.liu on 2019-12-12.
 */
public class ResponseOrderInfo implements Serializable {
    private String orderId;
    private String body;
    private String currentTime;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }
}
