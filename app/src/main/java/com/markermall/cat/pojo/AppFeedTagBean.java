package com.markermall.cat.pojo;

import java.io.Serializable;

/**
 * Created by fengrs on 2018/1/17.
 */

public class AppFeedTagBean implements Serializable {
    private String open ="";//1 开 0 关
    private String http ="";

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getHttp() {
        return http;
    }

    public void setHttp(String http) {
        this.http = http;
    }
}
