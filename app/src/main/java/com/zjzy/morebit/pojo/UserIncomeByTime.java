package com.zjzy.morebit.pojo;

import java.io.Serializable;

public class UserIncomeByTime implements Serializable {
    private String estimateIntegral;//积分
    private String estimateRewardMoney;//奖励收益
    private String estimateZgMoney;//自购收益
    private String directUser;//新增专属粉丝
    private String indirectUser;//新增普通粉丝

    public String getEstimateIntegral() {
        return estimateIntegral;
    }

    public void setEstimateIntegral(String estimateIntegral) {
        this.estimateIntegral = estimateIntegral;
    }

    public String getEstimateRewardMoney() {
        return estimateRewardMoney;
    }

    public void setEstimateRewardMoney(String estimateRewardMoney) {
        this.estimateRewardMoney = estimateRewardMoney;
    }

    public String getEstimateZgMoney() {
        return estimateZgMoney;
    }

    public void setEstimateZgMoney(String estimateZgMoney) {
        this.estimateZgMoney = estimateZgMoney;
    }

    public String getDirectUser() {
        return directUser;
    }

    public void setDirectUser(String directUser) {
        this.directUser = directUser;
    }

    public String getIndirectUser() {
        return indirectUser;
    }

    public void setIndirectUser(String indirectUser) {
        this.indirectUser = indirectUser;
    }
}
