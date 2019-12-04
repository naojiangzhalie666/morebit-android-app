package com.zjzy.morebit.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by YangBoTian on 2018/6/15 13:24
 */

public class TeamData implements Serializable {
    private int agentCount;        //代理商数量
    private int consumerCount;    //消费者数量
    private int operatorCount;    //运营专员数量
    private int allFansCount;   //所有粉丝数量,获取全部时返回
    private List<TeamInfo> child;

    public int getAgentCount() {
        return agentCount;
    }

    public void setAgentCount(int agent) {
        this.agentCount = agentCount;
    }

    public int getConsumerCount() {
        return consumerCount;
    }

    public void setConsumerCount(int consumerCount) {
        this.consumerCount = consumerCount;
    }

    public List<TeamInfo> getChild() {
        return child;
    }

    public void setChild(List<TeamInfo> child) {
        this.child = child;
    }

    public int getAllFansCount() {
        return allFansCount;
    }

    public void setAllFansCount(int allFansCount) {
        this.allFansCount = allFansCount;
    }

    public int getOperatorCount() {
        return operatorCount;
    }

    public void setOperatorCount(int operatorCount) {
        this.operatorCount = operatorCount;
    }
}
