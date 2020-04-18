package com.zjzy.morebit.pojo;

public class PushMessageVip {
    private String msg;//内容
    private String level;//当前级别  0 会员  1 vip  2团长
    private String growthFlag;//升级标记 1 可升级 0不可
    private String growth;//当前成长值

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getGrowthFlag() {
        return growthFlag;
    }

    public void setGrowthFlag(String growthFlag) {
        this.growthFlag = growthFlag;
    }

    public String getGrowth() {
        return growth;
    }

    public void setGrowth(String growth) {
        this.growth = growth;
    }
}
