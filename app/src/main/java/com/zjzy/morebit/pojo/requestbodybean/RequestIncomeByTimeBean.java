package com.zjzy.morebit.pojo.requestbodybean;

import java.io.Serializable;

/**
 * Created by YangBoTian on 2018/12/24.
 */

public class RequestIncomeByTimeBean implements Serializable {


    private String time;//时间戳

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
