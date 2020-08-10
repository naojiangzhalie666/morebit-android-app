package com.zjzy.morebit.pojo;

/**
 * Created by fengrs on 2018/6/8.
 */

public class PanicBuyTiemBean  {

    /**
     * start_time : 0
     * end_time : 6
     * type : 2
     * title : 00.00
     * subtitle : 已开抢
     */

    private String subTitle="";
    private String title ="";  //
    private String type; // 0=没有开抢     1=正在抢购    2=已经抢过
    private String endTime;
    private String startTime;

    public PanicBuyTiemBean(String subTitle, String title) {
        this.subTitle = subTitle;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subTitle;
    }

    public void setSubtitle(String subtitle) {
        this.subTitle = subtitle;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
