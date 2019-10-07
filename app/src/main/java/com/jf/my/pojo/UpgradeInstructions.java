package com.jf.my.pojo;

import java.io.Serializable;

/**
 * @Description: Vip升级 使用说明
 * @Author: liys
 * @CreateDate: 2019/3/18 16:20
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/3/18 16:20
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class UpgradeInstructions implements Serializable {
    /**
     * picture : http://miyuan-static.oss-cn-shenzhen.aliyuncs.com/zhitu-api/1552640097352.png
     * desc : 赚钱计划弹窗管理
     * displayLevel : 1
     * displayLocal : 1
     * upgrade : 0
     * buttonDesc : 去邀请好友
     */

    private String picture;
    private String desc;
    private int displayLevel;
    private int displayLocal;
    private int upgrade;
    private String buttonDesc;
    private int open;
    private int classId;
    private String url;
    private String type;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getDisplayLevel() {
        return displayLevel;
    }

    public void setDisplayLevel(int displayLevel) {
        this.displayLevel = displayLevel;
    }

    public int getDisplayLocal() {
        return displayLocal;
    }

    public void setDisplayLocal(int displayLocal) {
        this.displayLocal = displayLocal;
    }

    public int getUpgrade() {
        return upgrade;
    }

    public void setUpgrade(int upgrade) {
        this.upgrade = upgrade;
    }

    public String getButtonDesc() {
        return buttonDesc;
    }

    public void setButtonDesc(String buttonDesc) {
        this.buttonDesc = buttonDesc;
    }
//    private String picture; //图片
//    private String desc; //描述
//    private int displayLevel; //展示等级 1/会员 2/代理商 3/全部
//    private int displayLocal; // 展示地方 1/帮助1, 2/帮助2, 3/申请展示 4/全部
//    private int upgrade; //是否已经申请升级. 0否  1是
//    private int buttonDesc; //会员 btn
//
//    public int getButtonDesc() {
//        return buttonDesc;
//    }
//
//    public void setButtonDesc(int buttonDesc) {
//        this.buttonDesc = buttonDesc;
//    }
//
//    public int getUpgrade() {
//        return upgrade;
//    }
//
//    public void setUpgrade(int upgrade) {
//        this.upgrade = upgrade;
//    }
//
//    public String getPicture() {
//        return picture;
//    }
//
//    public void setPicture(String picture) {
//        this.picture = picture;
//    }
//
//    public String getDesc() {
//        return desc;
//    }
//
//    public void setDesc(String desc) {
//        this.desc = desc;
//    }
//
//    public int getDisplayLevel() {
//        return displayLevel;
//    }
//
//    public void setDisplayLevel(int displayLevel) {
//        this.displayLevel = displayLevel;
//    }
//
//    public int getDisplayLocal() {
//        return displayLocal;
//    }
//
//    public void setDisplayLocal(int displayLocal) {
//        this.displayLocal = displayLocal;
//    }


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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
