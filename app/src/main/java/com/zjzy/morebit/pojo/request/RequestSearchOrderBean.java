package com.zjzy.morebit.pojo.request;

public class RequestSearchOrderBean extends RequestBaseBean {

    private String orderNo;
    private int type;
    private int page;
    private String rid;

    public String getRid() {
        return rid;
    }

    public RequestSearchOrderBean setRid(String rid) {
        this.rid = rid;
        return this;
    }

    public int getPage() {
        return page;
    }

    public RequestSearchOrderBean setPage(int page) {
        this.page = page;
        return this;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
