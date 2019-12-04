package com.zjzy.morebit.pojo;

import java.io.Serializable;
/**
 * Created by YangBoTian on 2018/6/2 18:23
 * 专属微信
 *
 */

public class ExclusiveWechat implements Serializable {
    private String wxQrCode = ""; //二维码
    private String wxNumber = ""; //微信号


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
}
