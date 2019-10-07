package com.jf.my.pojo;

import java.io.Serializable;

/**
 * Created by fengrs on 2018/12/3.
 */

public class UserscoreBean implements Serializable {
    private int sysMsgCount; //系统未读数
    private int fansMsgCount;   //	粉丝未读数
    private int incomeCount; //	收益未读数

    public int getSystemCount() {
        return sysMsgCount;
    }

    public void setSystemCount(int systemCount) {
        this.sysMsgCount = systemCount;
    }

    public int getFansCount() {
        return fansMsgCount;
    }

    public void setFansCount(int fansCount) {
        this.fansMsgCount = fansCount;
    }

    public int getIncomeCount() {
        return incomeCount;
    }

    public void setIncomeCount(int incomeCount) {
        this.incomeCount = incomeCount;
    }
}
