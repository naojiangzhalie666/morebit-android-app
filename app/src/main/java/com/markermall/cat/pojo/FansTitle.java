package com.markermall.cat.pojo;

import java.io.Serializable;

/**
 * Created by YangBoTian on 2018/12/17.
 */

public class FansTitle implements Serializable {
    private String name;
    private String action;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
