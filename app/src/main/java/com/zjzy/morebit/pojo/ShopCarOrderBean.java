package com.zjzy.morebit.pojo;

import java.io.Serializable;
import java.util.List;

public class ShopCarOrderBean implements Serializable {

    private String orderId;//订单号
    private String payMetaData;//数据源

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPayMetaData() {
        return payMetaData;
    }

    public void setPayMetaData(String payMetaData) {
        this.payMetaData = payMetaData;
    }
}
