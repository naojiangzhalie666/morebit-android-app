package com.jf.my.pojo.request;

import com.jf.my.utils.C;

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



