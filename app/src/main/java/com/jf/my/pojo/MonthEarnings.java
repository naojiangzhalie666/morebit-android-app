package com.jf.my.pojo;

import java.io.Serializable;


/**
 * Created by YangBoTian on 2018/6/1 13:41
 */

public class MonthEarnings implements Serializable {

    private String thisMonthEstimateMoney = "";   //本月预估收入<为>
    private String thisMonthMoney = "";   // 本月结算

    private String prevMonthEstimateMoney = "";   //上月预估收入
    private String prevMonthMoney = "";   //上月结算收入

    public String getThisMonthEstimateMoney() {
        return thisMonthEstimateMoney;
    }

    public void setThisMonthEstimateMoney(String thisMonthEstimateMoney) {
        this.thisMonthEstimateMoney = thisMonthEstimateMoney;
    }

    public String getThisMonthMoney() {
        return thisMonthMoney;
    }

    public void setThisMonthMoney(String thisMonthMoney) {
        this.thisMonthMoney = thisMonthMoney;
    }

    public String getPrevMonthEstimateMoney() {
        return prevMonthEstimateMoney;
    }

    public void setPrevMonthEstimateMoney(String prevMonthEstimateMoney) {
        this.prevMonthEstimateMoney = prevMonthEstimateMoney;
    }

    public String getPrevMonthMoney() {
        return prevMonthMoney;
    }

    public void setPrevMonthMoney(String prevMonthMoney) {
        this.prevMonthMoney = prevMonthMoney;
    }
}
