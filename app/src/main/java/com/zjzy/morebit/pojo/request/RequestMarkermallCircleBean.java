package com.zjzy.morebit.pojo.request;

public class RequestMarkermallCircleBean extends RequestBaseBean {

    private int page;
    private int type;
    private String twoLevelId;//二级id
    private String oneLevelId; // 一级id 没有二级id就传一级id

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setOneLevelId(String oneLevelId) {
        this.oneLevelId = oneLevelId;
    }

    public String getOneLevelId() {
        return oneLevelId;
    }

    public String getTwoLevelId() {
        return twoLevelId;
    }

    public void setTwoLevelId(String twoLevelId) {
        this.twoLevelId = twoLevelId;
    }


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
