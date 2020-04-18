package com.zjzy.morebit.pojo.event;

/**
 * Created by haiping.liu on 2019-12-19.
 */
public class MyGrowthEvent {
    private  String growth;

    public MyGrowthEvent(String growth) {
        this.growth = growth;
    }

    public String getGrowth() {
        return growth;
    }

    public void setGrowth(String growth) {
        this.growth = growth;
    }
}
