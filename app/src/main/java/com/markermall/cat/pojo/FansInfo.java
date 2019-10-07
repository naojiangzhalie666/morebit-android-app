package com.markermall.cat.pojo;

import java.io.Serializable;

/**
 * Created by YangBoTian on 2019/5/16.
 */

public class FansInfo implements Serializable {
    private String prevMonthEstimateMoney  ;   //上月预估收入
    private String accumulatedAmount;                            //累计收益

    public String getPrevMonthEstimateMoney() {
        return prevMonthEstimateMoney;
    }

    public void setPrevMonthEstimateMoney(String prevMonthEstimateMoney) {
        this.prevMonthEstimateMoney = prevMonthEstimateMoney;
    }

    public String getAccumulatedAmount() {
        return accumulatedAmount;
    }

    public void setAccumulatedAmount(String accumulatedAmount) {
        this.accumulatedAmount = accumulatedAmount;
    }
}
