package com.zjzy.morebit.pojo.event;

/**
 * @Author: fengrs
 * @Description:
 **/
public class OrderLoadDataEvent {
    int type;

    public OrderLoadDataEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
