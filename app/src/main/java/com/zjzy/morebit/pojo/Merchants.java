package com.zjzy.morebit.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by YangBoTian on 2018/8/3.
 */

public class Merchants implements Serializable {
    int open;    //1 开，0关闭
    String qrcode;
    String content;
    Itme weixin; // 微信号
    List<Itme> contact;

    public Itme getWeixin() {
        return weixin;
    }

    public void setWeixin(Itme weixin) {
        this.weixin = weixin;
    }

    public List<Itme> getContact() {
        return contact;
    }

    public void setContact(List<Itme> contact) {
        this.contact = contact;
    }

    public int getOpen() {
        return open;
    }

    public void setOpen(int open) {
        this.open = open;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static class Itme implements Serializable {
        String name ="";
        String value="";
        int type ;//1:拨打，2：复制

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }


}
