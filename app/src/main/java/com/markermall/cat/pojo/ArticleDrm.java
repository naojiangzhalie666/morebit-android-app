package com.markermall.cat.pojo;

import java.io.Serializable;

/**
 * Created by YangBoTian on 2018/12/25.
 */

public class ArticleDrm implements Serializable {
    private int show;    //是否有权限  1有2没有
    private String url;

    public int getShow() {
        return show;
    }

    public void setShow(int show) {
        this.show = show;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
