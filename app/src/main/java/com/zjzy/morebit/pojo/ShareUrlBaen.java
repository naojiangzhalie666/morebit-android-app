package com.zjzy.morebit.pojo;

/**
 * Created by fengrs on 2018/6/26.
 */

public class ShareUrlBaen {
    private  String link="";//下载链接
    private  String sharp=""; //推广语
    private  String message="";// 通讯录分享
    private  String extension =""; //推广语

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getSharp() {
        return sharp;
    }

    public void setSharp(String sharp) {
        this.sharp = sharp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
