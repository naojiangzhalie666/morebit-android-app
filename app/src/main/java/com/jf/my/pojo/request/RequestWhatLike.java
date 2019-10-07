package com.jf.my.pojo.request;

public class RequestWhatLike extends RequestBaseBean {
    private int page;
    private String taobaoUserId;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getTaobaoUserId() {
        return taobaoUserId;
    }

    public void setTaobaoUserId(String taobaoUserId) {
        this.taobaoUserId = taobaoUserId;
    }
}
