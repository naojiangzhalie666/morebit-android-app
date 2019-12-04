package com.zjzy.morebit.pojo.event;

/**
 * Created by fengrs on 2019/1/16.
 * 备注: 退出登录
 */

public class MyFansEvent {
    private String curType;
    private int consumerCount;
    private int agentCount;
    private int operatorCount;
    private int allFansCount;
    private  boolean isVisible;
    public int getConsumerCount() {
        return consumerCount;
    }

    public void setConsumerCount(int consumerCount) {
        this.consumerCount = consumerCount;
    }

    public int getAgentCount() {
        return agentCount;
    }

    public void setAgentCount(int agentCount) {
        this.agentCount = agentCount;
    }

    public int getOperatorCount() {
        return operatorCount;
    }

    public void setOperatorCount(int operatorCount) {
        this.operatorCount = operatorCount;
    }

    public int getAllFansCount() {
        return allFansCount;
    }

    public void setAllFansCount(int allFansCount) {
        this.allFansCount = allFansCount;
    }

    public String getCurType() {
        return curType;
    }

    public void setCurType(String curType) {
        this.curType = curType;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
