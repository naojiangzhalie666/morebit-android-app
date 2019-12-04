package com.zjzy.morebit.pojo.request;

import com.zjzy.morebit.utils.C;

public class RequestBannerBean extends RequestBaseBean {

    private int type;
    private int os = C.Setting.os;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setOs(int os) {
        this.os = os;
    }
}



