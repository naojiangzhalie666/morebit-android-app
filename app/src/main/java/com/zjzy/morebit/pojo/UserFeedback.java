package com.zjzy.morebit.pojo;

import java.io.Serializable;

/**
 * @author YangBoTian
 * @date 2019/9/5
 * @des
 */
public class UserFeedback implements Serializable {
    private int id ; // 反馈id
    private String phone; //手机号码
    private String  title; // 标题
    private String  feedbackInfo; // 反馈信息
    private String  feedbackPicture; // 反馈图片
    private String  replyContent; // 回复内容

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFeedbackInfo() {
        return feedbackInfo;
    }

    public void setFeedbackInfo(String feedbackInfo) {
        this.feedbackInfo = feedbackInfo;
    }

    public String getFeedbackPicture() {
        return feedbackPicture;
    }

    public void setFeedbackPicture(String feedbackPicture) {
        this.feedbackPicture = feedbackPicture;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }
}
