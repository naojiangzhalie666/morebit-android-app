package com.markermall.cat.pojo;

/**
 * Created by Administrator on 2017/11/3.
 */

public class AgentDetailList {


    /**
     * record : 代理佣金奖励
     * record_time : 2018-01-19 20:11:16
     * account : 1
     */

    private String changeAmount = "";//交易金额

    private String remark = "";
    private String remarkTime = "";//申请时间
    private int type;//类型:1：出账 2：入账

    private int viewType;   //自用
    private String yearMonthDay ;   //年月日
    private String hourBranchSeconds; //时分秒
    private String reason; //原因
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRecordTime() {
        return remarkTime;
    }

    public void setRecordTime(String remarkTime) {
        this.remarkTime = remarkTime;
    }

    public String getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(String changeAmount) {
        this.changeAmount = changeAmount;
    }



    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getYearMonthDay() {
        return yearMonthDay;
    }

    public void setYearMonthDay(String yearMonthDay) {
        this.yearMonthDay = yearMonthDay;
    }

    public String getHourBranchSeconds() {
        return hourBranchSeconds;
    }

    public void setHourBranchSeconds(String hourBranchSeconds) {
        this.hourBranchSeconds = hourBranchSeconds;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
