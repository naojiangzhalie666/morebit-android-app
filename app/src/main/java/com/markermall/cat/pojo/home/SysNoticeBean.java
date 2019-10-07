package com.markermall.cat.pojo.home;

import java.io.Serializable;

/**
 * Created by fengrs on 2019/3/19.
 */

public class SysNoticeBean implements Serializable {
    private String id;
    private String title;//标题
    private String content;//内容
    private String contentSourceUrl;//跳转路径

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentSourceUrl() {
        return contentSourceUrl;
    }

    public void setContentSourceUrl(String contentSourceUrl) {
        this.contentSourceUrl = contentSourceUrl;
    }
}
