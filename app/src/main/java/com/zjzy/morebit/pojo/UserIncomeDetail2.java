package com.zjzy.morebit.pojo;

import java.io.Serializable;


/**
 * Created by YangBoTian on 2018/6/1 13:41
 */

public class UserIncomeDetail2 implements Serializable {
    private String totalIncome = "";   //累计收益
    private String totalIntegral = "";   //今日预估收入
    private String todayEstimateMoney = "";   //今日预估收入
    private String totalEstimateIntegral = "";   //今日预估积分收益



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


}
