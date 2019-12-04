package com.zjzy.morebit.pojo;

import java.io.Serializable;

/**
 * @Author: wuchaowen
 * @Description:
 * @Time: $date$ $time$
 **/
public class NoticeBean implements Serializable {
   private String time;
   private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
