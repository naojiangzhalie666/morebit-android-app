package com.jf.my.pojo;

import java.io.Serializable;

/**
 * Created by YangBoTian on 2018/8/17.
 */

public class BattlefieldReport implements Serializable {

    /**
     * id : 1900544
     * nickname :
     * userhead :
     * invite_num : 0
     */

    private int id;
    private String nickname;
    private String userhead;
    private int invite_num;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserhead() {
        return userhead;
    }

    public void setUserhead(String userhead) {
        this.userhead = userhead;
    }

    public int getInvite_num() {
        return invite_num;
    }

    public void setInvite_num(int invite_num) {
        this.invite_num = invite_num;
    }
}
