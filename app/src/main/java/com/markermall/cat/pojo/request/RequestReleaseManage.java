package com.markermall.cat.pojo.request;

/**
 * Created by YangBoTian on 2019/6/11.
 */

public class RequestReleaseManage extends RequestListBody {

    private int isShow;
    private int rows;
    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }
}
