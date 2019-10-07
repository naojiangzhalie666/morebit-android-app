package com.jf.my.pojo;

import java.io.Serializable;

/**
 * @Description: 获取推送 开关状态
 * @Author: liys
 * @CreateDate: 2019/3/15 14:49
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/3/15 14:49
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class PushSwitchBean implements Serializable {

    private int fansMessage; //粉丝信息推送消息：默认1、开启，0、未开启
    private int incomeMessage; //收益信息推送消息：默认1、开启，0、未开启
    private int shareItemMessage; //分享商品信息推送消息：默认1、开启，0、未开启

    public int getFansMessage() {
        return fansMessage;
    }

    public void setFansMessage(int fansMessage) {
        this.fansMessage = fansMessage;
    }

    public int getIncomeMessage() {
        return incomeMessage;
    }

    public void setIncomeMessage(int incomeMessage) {
        this.incomeMessage = incomeMessage;
    }

    public int getShareItemMessage() {
        return shareItemMessage;
    }

    public void setShareItemMessage(int shareItemMessage) {
        this.shareItemMessage = shareItemMessage;
    }
}
