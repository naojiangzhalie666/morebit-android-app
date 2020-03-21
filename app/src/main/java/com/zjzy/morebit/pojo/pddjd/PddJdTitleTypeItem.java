package com.zjzy.morebit.pojo.pddjd;

import java.io.Serializable;

/**
 * Created by haiping.liu on 2020-03-07.
 */
public class PddJdTitleTypeItem implements Serializable {
    private String title;
    private int catId;
    private int type;

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
