package com.zjzy.morebit.pojo.request;

/**
 * Created by YangBoTian on 2019/6/18.
 */

public class RequestReleaseCategory extends RequestBaseBean {
    private int type; // 0：用户可选 1：发圈上架

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
