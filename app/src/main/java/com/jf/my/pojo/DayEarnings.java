package com.jf.my.pojo;

import java.io.Serializable;


/**
 * Created by YangBoTian on 2018/6/1 13:41
 */

public class DayEarnings implements Serializable {
    private String totalMoney = "";   //账户余额
    private String todayMoney = "";   //今天成交预估收入
    private String todayPaymentTotal = "";   //今天付款笔数
    private String todayEstimateMoney = "";   //今天成交预估收入

    private String yesterdayEstimateMoney = "";   //昨天预估收入
    private String yesterdayPaymentTotal = "";   //昨天付款笔数
    private String yesterdayMoney= "";   //昨天结算预估金额
    private String accumulatedAmount= "";   //累计金额


    public String getTodayMoney() {
        return todayMoney;
    }

    public void setTodayMoney(String todayMoney) {
        this.todayMoney = todayMoney;
    }

    public String getTodayPamentTotal() {
        return todayPaymentTotal;
    }

    public void setTodayPamentTotal(String todayPamentTotal) {
        this.todayPaymentTotal = todayPamentTotal;
    }

    public String getTodayEstimateMoney() {
        return todayEstimateMoney;
    }

    public void setTodayEstimateMoney(String todayEstimateMoney) {
        this.todayEstimateMoney = todayEstimateMoney;
    }

    public String getYesterdayEstimateMoney() {
        return yesterdayEstimateMoney;
    }

    public void setYesterdayEstimateMoney(String yesterdayEstimateMoney) {
        this.yesterdayEstimateMoney = yesterdayEstimateMoney;
    }

    public String getYestredayPaymentTotal() {
        return yesterdayPaymentTotal;
    }

    public void setYestredayPaymentTotal(String yestredayPaymentTotal) {
        this.yesterdayPaymentTotal = yestredayPaymentTotal;
    }

    public String getYesterdayMoney() {
        return yesterdayMoney;
    }

    public void setYesterdayMoney(String yesterdayMoney) {
        this.yesterdayMoney = yesterdayMoney;
    }


    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getAccumulatedAmount() {
        return accumulatedAmount;
    }

    public void setAccumulatedAmount(String accumulatedAmount) {
        this.accumulatedAmount = accumulatedAmount;
    }
}
