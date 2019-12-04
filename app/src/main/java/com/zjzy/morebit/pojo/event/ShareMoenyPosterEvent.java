package com.zjzy.morebit.pojo.event;

/**
 * Created by fengrs on 2019/1/16.
 * 备注:
 */

public class ShareMoenyPosterEvent {
    private int bitmapPosition;
    public ShareMoenyPosterEvent(int bitmapPosition) {
        this.bitmapPosition= bitmapPosition;
    }

    public int getBitmapPosition() {
        return bitmapPosition;
    }

    public void setBitmapPosition(int bitmapPosition) {
        this.bitmapPosition = bitmapPosition;
    }
}
