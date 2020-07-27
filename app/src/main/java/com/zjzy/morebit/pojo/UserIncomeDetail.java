package com.zjzy.morebit.pojo;

import java.io.Serializable;


/**
 * Created by YangBoTian on 2018/6/1 13:41
 */

public class UserIncomeDetail implements Serializable {
    private String totalIncome = "";   //累计收益
    private String totalIntegral = "";   //今日预估收入
    private String todayEstimateMoney = "";   //今日预估收入
    private String totalEstimateIntegral = "";   //今日预估积分收益

    private String balance = "";   //可提现余额
    private String receiveAmount = "";   //已提现
    private String unsettleAmount= "";   //未结算收益
    private String unsettleIntegral= "";   //未结算积分

    private String yesterdayEstimateMoney= "";   //昨天预估收益

    private String yesterdayIntegral= "";   //昨日积分
    private String yesterdayDirectUser = "";   //昨日新增专属粉丝
    private String yesterdayIndirectUser = "";   //昨日新增普通粉丝

    private String thisMonthEstimateMoney = "";   //本月预估收入
    private String thisMonthIntegral = "";   //本月积分收益
    private String thisMonthDirectUser= "";   //本月新增专属粉丝
    private String thisMonthIndirectUser= "";   //本月新增普通粉丝

    private String prevMonthEstimateMoney= "";   //上个月预估金额

    private String prevMonthIntegral= "";   //上月积分收益
    private String prevMonthDirectUser = "";   //上月新增专属粉丝
    private String prevMonthIndirectUser = "";   //上月新增普通粉丝

    private String preDateStr = "";   //上月日期


    public String getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(String totalIncome) {
        this.totalIncome = totalIncome;
    }

    public String getTotalIntegral() {
        return totalIntegral;
    }

    public void setTotalIntegral(String totalIntegral) {
        this.totalIntegral = totalIntegral;
    }

    public String getTodayEstimateMoney() {
        return todayEstimateMoney;
    }

    public void setTodayEstimateMoney(String todayEstimateMoney) {
        this.todayEstimateMoney = todayEstimateMoney;
    }

    public String getTotalEstimateIntegral() {
        return totalEstimateIntegral;
    }

    public void setTotalEstimateIntegral(String totalEstimateIntegral) {
        this.totalEstimateIntegral = totalEstimateIntegral;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getReceiveAmount() {
        return receiveAmount;
    }

    public void setReceiveAmount(String receiveAmount) {
        this.receiveAmount = receiveAmount;
    }

    public String getUnsettleAmount() {
        return unsettleAmount;
    }

    public void setUnsettleAmount(String unsettleAmount) {
        this.unsettleAmount = unsettleAmount;
    }

    public String getUnsettleIntegral() {
        return unsettleIntegral;
    }

    public void setUnsettleIntegral(String unsettleIntegral) {
        this.unsettleIntegral = unsettleIntegral;
    }

    public String getYesterdayEstimateMoney() {
        return yesterdayEstimateMoney;
    }

    public void setYesterdayEstimateMoney(String yesterdayEstimateMoney) {
        this.yesterdayEstimateMoney = yesterdayEstimateMoney;
    }

    public String getYesterdayIntegral() {
        return yesterdayIntegral;
    }

    public void setYesterdayIntegral(String yesterdayIntegral) {
        this.yesterdayIntegral = yesterdayIntegral;
    }

    public String getYesterdayDirectUser() {
        return yesterdayDirectUser;
    }

    public void setYesterdayDirectUser(String yesterdayDirectUser) {
        this.yesterdayDirectUser = yesterdayDirectUser;
    }

    public String getYesterdayIndirectUser() {
        return yesterdayIndirectUser;
    }

    public void setYesterdayIndirectUser(String yesterdayIndirectUser) {
        this.yesterdayIndirectUser = yesterdayIndirectUser;
    }

    public String getThisMonthEstimateMoney() {
        return thisMonthEstimateMoney;
    }

    public void setThisMonthEstimateMoney(String thisMonthEstimateMoney) {
        this.thisMonthEstimateMoney = thisMonthEstimateMoney;
    }

    public String getThisMonthIntegral() {
        return thisMonthIntegral;
    }

    public void setThisMonthIntegral(String thisMonthIntegral) {
        this.thisMonthIntegral = thisMonthIntegral;
    }

    public String getThisMonthDirectUser() {
        return thisMonthDirectUser;
    }

    public void setThisMonthDirectUser(String thisMonthDirectUser) {
        this.thisMonthDirectUser = thisMonthDirectUser;
    }

    public String getThisMonthIndirectUser() {
        return thisMonthIndirectUser;
    }

    public void setThisMonthIndirectUser(String thisMonthIndirectUser) {
        this.thisMonthIndirectUser = thisMonthIndirectUser;
    }

    public String getPrevMonthEstimateMoney() {
        return prevMonthEstimateMoney;
    }

    public void setPrevMonthEstimateMoney(String prevMonthEstimateMoney) {
        this.prevMonthEstimateMoney = prevMonthEstimateMoney;
    }

    public String getPrevMonthIntegral() {
        return prevMonthIntegral;
    }

    public void setPrevMonthIntegral(String prevMonthIntegral) {
        this.prevMonthIntegral = prevMonthIntegral;
    }

    public String getPrevMonthDirectUser() {
        return prevMonthDirectUser;
    }

    public void setPrevMonthDirectUser(String prevMonthDirectUser) {
        this.prevMonthDirectUser = prevMonthDirectUser;
    }

    public String getPrevMonthIndirectUser() {
        return prevMonthIndirectUser;
    }

    public void setPrevMonthIndirectUser(String prevMonthIndirectUser) {
        this.prevMonthIndirectUser = prevMonthIndirectUser;
    }

    public String getPreDateStr() {
        return preDateStr;
    }

    public void setPreDateStr(String preDateStr) {
        this.preDateStr = preDateStr;
    }
}
