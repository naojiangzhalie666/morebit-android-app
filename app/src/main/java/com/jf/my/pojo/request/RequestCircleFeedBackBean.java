package com.jf.my.pojo.request;

public class RequestCircleFeedBackBean extends RequestBaseBean {
    private String accountName;
    private String phone;
    private String title;
    private String feedbackInfo;
    private String feedbackPicture;
    private String articleId;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
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

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }
}
