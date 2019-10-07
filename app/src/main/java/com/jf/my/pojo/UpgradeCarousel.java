package com.jf.my.pojo;

import java.io.Serializable;

/**
 * @author YangBoTian
 * @date 2019/9/2
 * @des
 */
public class UpgradeCarousel implements Serializable {
    private int userId;    //下级用户ID
    private int newUsers;    //拉新人数
    private int isUpgrade;    //是否升级 0：否 1：是; 2审核中
    private String nickname;    //用户昵称
    private String headImg;   //用户头像
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getNewUsers() {
        return newUsers;
    }

    public void setNewUsers(int newUsers) {
        this.newUsers = newUsers;
    }

    public int getIsUpgrade() {
        return isUpgrade;
    }

    public void setIsUpgrade(int isUpgrade) {
        this.isUpgrade = isUpgrade;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }
}
