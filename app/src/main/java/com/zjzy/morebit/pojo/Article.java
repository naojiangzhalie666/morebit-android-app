package com.zjzy.morebit.pojo;

import java.io.Serializable;

/**
 * Created by YangBoTian on 2018/12/24.
 */

public class Article implements Serializable {
    private int id;   //文章视频id
    private String title;  //文章标题
    private String image;  //图片地址
    private String briefIntroduction;  //简介
    private String releaseTime; //上架时间
    private int virtualView;  //虚拟浏览量
    private int realView;     //真实浏览量
    private int authority; //权限（0：否 1：是）
    private String url; //跳转地址
    private int type; //1文章, 2视频
    private int twoLevel; //二级分类id
    private String videoUrl; //視頻url
    private String shareUrl; //分享地址
    private String  shareContent;//分享内容
    private String linkUrl;
    private int studyNum;   //真实真实学习数
    private int virtualStudy;   //虚拟学习数
    private int praiseNum;   //真实点赞数
    private int virtualPraise;   //虚拟点赞数
    private int shareNum;   //真实分享数
    private int virtualShare;   //虚拟分享数
    public int getTwoLevel() {
        return twoLevel;
    }
    private int status;

    //自用
    private int uType;

    public Article setTwoLevel(int twoLevel) {
        this.twoLevel = twoLevel;
        return this;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public Article setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
        return this;
    }

    public int getType() {
        return type;
    }

    public Article setType(int type) {
        this.type = type;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBriefIntroduction() {
        return briefIntroduction;
    }

    public void setBriefIntroduction(String briefIntroduction) {
        this.briefIntroduction = briefIntroduction;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public int getVirtualView() {
        return virtualView;
    }

    public void setVirtualView(int virtualView) {
        this.virtualView = virtualView;
    }

    public int getRealView() {
        return realView;
    }

    public void setRealView(int realView) {
        this.realView = realView;
    }

    public int getAuthority() {
        return authority;
    }

    public void setAuthority(int authority) {
        this.authority = authority;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getShareContent() {
        return shareContent;
    }

    public void setShareContent(String shareContent) {
        this.shareContent = shareContent;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setuType(int uType) {
        this.uType = uType;
    }

    public int getuType() {
        return uType;
    }

    public int getStudyNum() {
        return studyNum;
    }

    public void setStudyNum(int studyNum) {
        this.studyNum = studyNum;
    }

    public int getVirtualStudy() {
        return virtualStudy;
    }

    public void setVirtualStudy(int virtualStudy) {
        this.virtualStudy = virtualStudy;
    }

    public int getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(int praiseNum) {
        this.praiseNum = praiseNum;
    }

    public int getVirtualPraise() {
        return virtualPraise;
    }

    public void setVirtualPraise(int virtualPraise) {
        this.virtualPraise = virtualPraise;
    }

    public int getShareNum() {
        return shareNum;
    }

    public void setShareNum(int shareNum) {
        this.shareNum = shareNum;
    }

    public int getVirtualShare() {
        return virtualShare;
    }

    public void setVirtualShare(int virtualShare) {
        this.virtualShare = virtualShare;
    }
}
