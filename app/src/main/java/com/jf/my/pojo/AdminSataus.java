package com.jf.my.pojo;

import java.io.Serializable;

/**
 * Created by YangBoTian on 2018/7/4.
 * 运营专员后台状态开关实体类
 */

public class AdminSataus implements Serializable {
    private int status  ;//开关，0隐藏，1显示
    private String url = "";

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
