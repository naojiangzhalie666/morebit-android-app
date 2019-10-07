package com.markermall.cat.pojo.request;

public class RequestRemoveCircleCollectBean extends RequestBaseBean {
    private String id;
    private String rangId;

    public String getRangId() {
        return rangId;
    }

    public void setRangId(String rangId) {
        this.rangId = rangId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
