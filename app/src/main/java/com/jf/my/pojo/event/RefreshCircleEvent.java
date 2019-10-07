package com.jf.my.pojo.event;

import com.jf.my.pojo.MiYuanCircleInfo;

/**
 - @Description:  刷新蜜粉圈
 - @Author:
 - @Time:  2019/9/18 17:30
 **/

public class RefreshCircleEvent {
    private MiYuanCircleInfo item;

    public RefreshCircleEvent(MiYuanCircleInfo item) {
        this.item = item;
    }

    public MiYuanCircleInfo getItem() {
        return item;
    }
}
