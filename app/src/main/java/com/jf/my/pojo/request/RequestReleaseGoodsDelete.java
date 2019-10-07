package com.jf.my.pojo.request;

/**
 * Created by YangBoTian on 2019/6/17.
 */

public class RequestReleaseGoodsDelete extends RequestBaseBean {
    private int isShow;
    private String shareId;

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public String getShareId() {
        return shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }
}
