package com.jf.my.pojo;

import java.io.Serializable;

/**
 * Created by YangBoTian on 2018/8/3.
 */

public class OfficialNotice implements Serializable {
    private String title;
    private String htmlUrl;
    private String icon;
    private String createTime;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String create_time) {
        this.createTime = createTime;
    }

}
