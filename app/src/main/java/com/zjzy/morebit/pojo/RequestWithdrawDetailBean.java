package com.zjzy.morebit.pojo;

import java.io.Serializable;

/**
 * Created by YangBoTian on 2018/12/24.
 */

public class RequestWithdrawDetailBean implements Serializable {


    private String type;//1-自购返利 2-优选商品积分 3-活动奖励
    private int page=1;
    private int rows=10;
    private String date;//时间戳

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
