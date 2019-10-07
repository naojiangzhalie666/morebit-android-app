package com.jf.my.pojo.myInfo;

/**
 * Created by fengrs on 2018/9/8.
 * 备注:
 */

public class CommonProblemBean {

    /**
     * title : 安卓4.2.0版本更新
     * content : 333333
     * html_url : https://img.gzzhitu.com/apphtml/96.html
     * position : 1
     */

    private String title ="";
    private String content="";
    private String html_url="";
    private int position;
    private String icon="";

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

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
