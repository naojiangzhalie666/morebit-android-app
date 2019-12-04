package com.zjzy.morebit.pojo.login;

/**
 * Created by fengrs on 2018/8/8.
 * 备注:
 */

public class InviteUserInfoBean {
    private String headImg = "";
    private String nickname= "";
    private String phone= "";
    private String intro= "";

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getUserhead() {
        return headImg;
    }

    public void setUserhead(String userImg) {
        this.headImg = userImg;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickName) {
        this.nickname = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
