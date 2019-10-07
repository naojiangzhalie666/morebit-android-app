package com.markermall.cat.pojo;

import java.io.Serializable;

/**
 * Created by YangBoTian on 2018/11/28.
 */

public class AccountDestroy implements Serializable {
    private String wxQrCode;//微信二维码
    private String wxNumber;//微信号
    private int wxShowType;//选择展示位置 0全部，1粉丝，2客服
    private String alterDec;//弹出框提示
    private String downDec;//微信下方提示

    public String getWxQrCode() {
        return wxQrCode;
    }

    public void setWxQrCode(String wxQrCode) {
        this.wxQrCode = wxQrCode;
    }

    public String getWxNumber() {
        return wxNumber;
    }

    public void setWxNumber(String wxNumber) {
        this.wxNumber = wxNumber;
    }

    public int getWxShowType() {
        return wxShowType;
    }

    public void setWxShowType(int wxShowType) {
        this.wxShowType = wxShowType;
    }

    public String getAlterDec() {
        return alterDec;
    }

    public void setAlterDec(String alterDec) {
        this.alterDec = alterDec;
    }

    public String getDownDec() {
        return downDec;
    }

    public void setDownDec(String downDec) {
        this.downDec = downDec;
    }
}
