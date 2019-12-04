package com.zjzy.morebit.pojo.request;

/**
 * 推送开关请求
 */
public class RequestPushBean extends RequestBaseBean {

    //参数
    private String fansMessage = "0"; //粉丝信息推送消息：默认1、开启，0、未开启
    private String incomeMessage = "0"; //收益信息推送消息：默认1、开启，0、未开启
    private String shareItemMessage = "0"; //共享商品推送：默认1、开启，0、未开启

    /**
     *
     * @param isFans 是否开启 粉丝推送
     * @param isMessage 是否开启 收益提醒
     */
    public RequestPushBean(boolean isFans, boolean isMessage,boolean isGoodsMessage) {
        //外面调用只需关注"开关"即可, 无需关注"开"或"关"对应传的的参数.
        if(isFans){
            this.fansMessage = "1";
        }
        if(isMessage){
            incomeMessage = "1";
        }
        if(isGoodsMessage){
            shareItemMessage = "1";
        }
    }

    public String getFansMessage() {
        return fansMessage;
    }

    public void setFansMessage(String fansMessage) {
        this.fansMessage = fansMessage;
    }

    public String getIncomeMessage() {
        return incomeMessage;
    }

    public void setIncomeMessage(String incomeMessage) {
        this.incomeMessage = incomeMessage;
    }
}



