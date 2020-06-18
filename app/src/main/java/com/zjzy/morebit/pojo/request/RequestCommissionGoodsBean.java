package com.zjzy.morebit.pojo.request;

public class RequestCommissionGoodsBean extends RequestBaseBean {
    private String catId;
    private int minId=1;
    private int back=10;

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public int getMinId() {
        return minId;
    }

    public void setMinId(int minId) {
        this.minId = minId;
    }

    public int getBack() {
        return back;
    }

    public void setBack(int back) {
        this.back = back;
    }
}
