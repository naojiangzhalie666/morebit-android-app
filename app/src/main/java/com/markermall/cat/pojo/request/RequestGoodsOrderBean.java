package com.markermall.cat.pojo.request;

public class RequestGoodsOrderBean extends RequestBaseBean {

    private int orderStatus;
    private int page;
    private int type;//订单类型：1：淘宝，2：京东，3：苏宁，

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
