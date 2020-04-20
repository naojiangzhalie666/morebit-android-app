package com.zjzy.morebit.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/10/16.
 */

public class ImageInfo implements Serializable {
    private int id;
    private int width;
    private int height;
    private String title="";
    private String url="";
    //跳转到商品详情用
    private String type =""; // 轮播类型 4 文本 5 图片


    private String picture="";  //分享图片
    private String desc="";  // 跳转外部app复制的字段

    private int open;     //1跳转商品详情，2内部链接，3外部链接,4分类
    private int classId;

    //二级商品列表
    private String backgroundImage ; // 二级商品列表 背景图
    //二级商品列表
    //系统通知用到
    private int displayPage ; //展示页面：1【首页】、2【商品详情页】、3【分类】、4【发圈】、5【我的】
    private String digest ; // 内容
    //系统通知用到
    private int mediaType ; // 0 图片 1视频
    private List<Records> records; //弹窗计数记录
    // 自用
    // 三级分类
    public String categoryId;//分类id
    // 三级分类
    public boolean isChecked;
    public boolean isPoster;
    public String videoPath="";  // 视频本地地址
    public int displayStyle; //展示样式 0/横向 1/纵向
    public String colorValue; //底色
    private String permission;  // 权限: 0为会员 1为VIP会员 2为运营专员 3为全部可见 字符串格式为:"0,1,2"......
    private String splicePid ;  //默认：0：不拼接，1：拼接pid，2：拼接relation_id，3：pid和relation_id都拼接
    private String updateTime;  //公告列表的时间
    private String subTitle; //公告列表的子标题
    private String icon; //图标

    private int sort;   //首页推荐列表广告位置
    private int mark;
    private String videoUrl; //搜索引导的视频

    private int popupType;//0 无限次 1 一天一次
    private int superType;//1  超级入口   0 普通页面

    public int getSuperType() {
        return superType;
    }

    public void setSuperType(int superType) {
        this.superType = superType;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getOpen() {
        return open;
    }

    public void setOpen(int open) {
        this.open = open;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
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


    public String getThumb() {
        return getPicture();
    }

    public void setThumb(String thumb) {
        setPicture(thumb);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Records> getRecords() {
        return records;
    }

    public void setRecords(List<Records> records) {
        this.records = records;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }

    public int getDisplayStyle() {
        return displayStyle;
    }

    public void setDisplayStyle(int displayStyle) {
        this.displayStyle = displayStyle;
    }

    public String getColorValue() {
        return colorValue;
    }

    public void setColorValue(String colorValue) {
        this.colorValue = colorValue;
    }

    public int getDisplayPage() {
        return displayPage;
    }

    public void setDisplayPage(int displayPage) {
        this.displayPage = displayPage;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getSplicePid() {
        return splicePid;
    }

    public void setSplicePid(String splicePid) {
        this.splicePid = splicePid;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public int getPopupType() {
        return popupType;
    }

    public void setPopupType(int popupType) {
        this.popupType = popupType;
    }
}
