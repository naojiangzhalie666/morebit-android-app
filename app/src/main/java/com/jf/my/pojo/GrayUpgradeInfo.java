package com.jf.my.pojo;

/**
 - @Description:  灰度升级
 - @Author:  
 - @Time:  2019/9/8 10:25
 **/
public class GrayUpgradeInfo {
    private String id;
    private int version;
    private String name;
    private String info;
    private String edition;
    private String download;
    private int appVersion;
    private String md5;
    private String size;
    private String popUpsContent;
    private int userIdStart;
    private int userIdEnd;
    private String jumpUrl;
    private String createTime;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public int getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(int appVersion) {
        this.appVersion = appVersion;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPopUpsContent() {
        return popUpsContent;
    }

    public void setPopUpsContent(String popUpsContent) {
        this.popUpsContent = popUpsContent;
    }

    public int getUserIdStart() {
        return userIdStart;
    }

    public void setUserIdStart(int userIdStart) {
        this.userIdStart = userIdStart;
    }

    public int getUserIdEnd() {
        return userIdEnd;
    }

    public void setUserIdEnd(int userIdEnd) {
        this.userIdEnd = userIdEnd;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }
}
