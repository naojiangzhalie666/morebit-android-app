package com.markermall.cat.pojo.event;

import com.markermall.cat.pojo.MarkermallCircleInfo;

/**
 - @Description:  刷新蜜粉圈
 - @Author:
 - @Time:  2019/9/18 17:30
 **/

public class RefreshCircleEvent {
    private MarkermallCircleInfo item;

    public RefreshCircleEvent(MarkermallCircleInfo item) {
        this.item = item;
    }

    public MarkermallCircleInfo getItem() {
        return item;
    }
}
