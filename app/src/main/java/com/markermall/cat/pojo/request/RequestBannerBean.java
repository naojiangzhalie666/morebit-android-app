package com.markermall.cat.pojo.request;

import com.markermall.cat.utils.C;

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



