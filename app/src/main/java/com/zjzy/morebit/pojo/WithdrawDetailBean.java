package com.zjzy.morebit.pojo;

import java.io.Serializable;

public class WithdrawDetailBean implements Serializable {
    private String type;//账单类型（1：出账 2：入账）
    private String changeAmount;//操作金额
    private String remarkTime;//创建时间
    private String yearMonthDay;//年 / 月 / 日
    private String hourBranchSeconds;// 时 / 分 / 秒
    private String remark;//描述(比如：8月佣金，9月佣金）
    private String reason;//原因

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(String changeAmount) {
        this.changeAmount = changeAmount;
    }

    public String getRemarkTime() {
        return remarkTime;
    }

    public void setRemarkTime(String remarkTime) {
        this.remarkTime = remarkTime;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
