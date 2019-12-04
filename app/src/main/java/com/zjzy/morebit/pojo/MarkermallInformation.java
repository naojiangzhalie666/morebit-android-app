package com.zjzy.morebit.pojo;

import java.io.Serializable;

/**
 * @author YangBoTian
 * @date 2019/9/4
 * @des
 */
public class MarkermallInformation implements Serializable {
    private int id;
    private String title; //标题
    private int open ;;//打开方式(3:外站链接 9：商学院文章)
    private String url;// 跳转地址，商品ID，app包名
    private int classId; //  商品分类id、内站列表
    private int  status; //  使用状态（0：下架 ，1：正常
    private String createTime; // 创建时间
    private String updateTime; //  更新时间
    private int  virtualRead; //  虚拟阅读量 后台可配置
    private int  virtualShare; // 虚拟分享数 后台可配置
    private int  readNum ;//  真实阅读量
    private int  shareNum ;//  真实分享数

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

    public int getOpen() {
        return open;
    }

    public void setOpen(int open) {
        this.open = open;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getVirtualRead() {
        return virtualRead;
    }

    public void setVirtualRead(int virtualRead) {
        this.virtualRead = virtualRead;
    }

    public int getVirtualShare() {
        return virtualShare;
    }

    public void setVirtualShare(int virtualShare) {
        this.virtualShare = virtualShare;
    }

    public int getReadNum() {
        return readNum;
    }

    public void setReadNum(int readNum) {
        this.readNum = readNum;
    }

    public int getShareNum() {
        return shareNum;
    }

    public void setShareNum(int shareNum) {
        this.shareNum = shareNum;
    }


}
