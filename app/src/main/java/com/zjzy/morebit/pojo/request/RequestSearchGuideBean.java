package com.zjzy.morebit.pojo.request;

/**
 * @Author: wuchaowen
 * @Description:
 * @Time: $date$ $time$
 **/
public class RequestSearchGuideBean extends RequestBaseBean{
    private int type;
    private int os;

    public int getOs() {
        return os;
    }

    public void setOs(int os) {
        this.os = os;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
