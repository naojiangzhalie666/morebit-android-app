package com.zjzy.morebit.pojo;

import java.io.Serializable;


/**
 * Created by YangBoTian on 2018/6/1 13:41
 */

public class MonthEarnings implements Serializable {

    private String thisMonthEstimateMoney = "";   //本月预估收入<为>
    private String thisMonthMoney = "";   // 本月结算

    private String prevMonthEstimateMoney = "";   //上月预估收入
    private String prevMonthMoney = "";   //上月结算收入

    private String thisMonthIntegral="";//本月积分收益
    private String prevMonthIntegral="";//上月积分收益
    private String totalIntegral=""	;//总积分收益
    private String totalTaoBaoEstimateMoney="";//淘宝预估总收益
    private String totalPddEstimateMoney="";//拼多多预估总收益
    private String totalJdEstimateMoney="";//京东预估总收益

    public String getTotalJdEstimateMoney() {
        return totalJdEstimateMoney;
    }

    public void setTotalJdEstimateMoney(String totalJdEstimateMoney) {
        this.totalJdEstimateMoney = totalJdEstimateMoney;
    }

    public String getThisMonthIntegral() {
        return thisMonthIntegral;
    }

    public void setThisMonthIntegral(String thisMonthIntegral) {
        this.thisMonthIntegral = thisMonthIntegral;
    }

    public String getPrevMonthIntegral() {
        return prevMonthIntegral;
    }

    public void setPrevMonthIntegral(String prevMonthIntegral) {
        this.prevMonthIntegral = prevMonthIntegral;
    }

    public String getTotalIntegral() {
        return totalIntegral;
    }

    public void setTotalIntegral(String totalIntegral) {
        this.totalIntegral = totalIntegral;
    }

    public String getTotalTaoBaoEstimateMoney() {
        return totalTaoBaoEstimateMoney;
    }

    public void setTotalTaoBaoEstimateMoney(String totalTaoBaoEstimateMoney) {
        this.totalTaoBaoEstimateMoney = totalTaoBaoEstimateMoney;
    }

    public String getTotalPddEstimateMoney() {
        return totalPddEstimateMoney;
    }

    public void setTotalPddEstimateMoney(String totalPddEstimateMoney) {
        this.totalPddEstimateMoney = totalPddEstimateMoney;
    }

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
