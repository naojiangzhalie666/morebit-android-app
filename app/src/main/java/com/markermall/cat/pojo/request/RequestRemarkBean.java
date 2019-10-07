package com.markermall.cat.pojo.request;

public class RequestRemarkBean extends RequestBaseBean {
    private String remark;
     private String id;  //粉丝ID
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setId(String id) {
        this.id = id;
    }
}
