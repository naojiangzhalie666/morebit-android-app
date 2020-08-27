package com.zjzy.morebit.pojo;

import java.io.Serializable;

public class UserBalanceBean implements Serializable {

    private String rebateAmount;//返利
    private String integralAmount;//积分
    private String rewardAmount;//奖励

    public String getRebateAmount() {
        return rebateAmount;
    }

    public void setRebateAmount(String rebateAmount) {
        this.rebateAmount = rebateAmount;
    }

    public String getIntegralAmount() {
        return integralAmount;
    }

    public void setIntegralAmount(String integralAmount) {
        this.integralAmount = integralAmount;
    }

    public String getRewardAmount() {
        return rewardAmount;
    }

    public void setRewardAmount(String rewardAmount) {
        this.rewardAmount = rewardAmount;
    }
}
