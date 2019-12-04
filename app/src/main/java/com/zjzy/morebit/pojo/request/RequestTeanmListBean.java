package com.zjzy.morebit.pojo.request;

public class RequestTeanmListBean extends RequestBaseBean {

    private String level;
    private int page;
    private String userId;
    private  int type;
    private String order;
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
