package com.zjzy.morebit.pojo.goods;

import java.io.Serializable;

/**
 * 生成拼多多模版
 * Created by haiping.liu on 2020-03-24.
 */
public class PddShareContent implements Serializable {
    private String template;
    private int isShortLink;

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public int getIsShortLink() {
        return isShortLink;
    }

    public void setIsShortLink(int isShortLink) {
        this.isShortLink = isShortLink;
    }
}
