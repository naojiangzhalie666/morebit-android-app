package com.zjzy.morebit.pojo.pddjd;

import java.io.Serializable;

/**
 * 搜索item
 * Created by haiping.liu on 2020-03-25.
 */
public class SearchItem implements Serializable {
    private int type;
    private String title;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
