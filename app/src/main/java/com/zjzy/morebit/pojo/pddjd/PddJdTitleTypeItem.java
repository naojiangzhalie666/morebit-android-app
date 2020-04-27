package com.zjzy.morebit.pojo.pddjd;

import java.io.Serializable;

/**
 * Created by haiping.liu on 2020-03-07.
 */
public class PddJdTitleTypeItem implements Serializable {
    private String title;
    private int catId;
    private int type;
    private String tabNo;//栏目id
    private String tabName;//栏目名称
    private String eliteId;//栏目id
    private String eliteName;//栏目名称

    public String getEliteId() {
        return eliteId;
    }

    public void setEliteId(String eliteId) {
        this.eliteId = eliteId;
    }

    public String getEliteName() {
        return eliteName;
    }

    public void setEliteName(String eliteName) {
        this.eliteName = eliteName;
    }

    public String getTabNo() {
        return tabNo;
    }

    public void setTabNo(String tabNo) {
        this.tabNo = tabNo;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

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
