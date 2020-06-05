package com.zjzy.morebit.pojo.request;

import java.io.Serializable;

/**
 * Created by YangBoTian on 2018/12/24.
 */

public class WxCodeBean implements Serializable {
    private String clickUrl;
    private String shortClickUrl;
    private String wxMiniprogramPath;
    private String wxQrcodeUrl; //微信二维码链接

    public String getClickUrl() {
        return clickUrl;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }

    public String getShortClickUrl() {
        return shortClickUrl;
    }

    public void setShortClickUrl(String shortClickUrl) {
        this.shortClickUrl = shortClickUrl;
    }

    public String getWxMiniprogramPath() {
        return wxMiniprogramPath;
    }

    public void setWxMiniprogramPath(String wxMiniprogramPath) {
        this.wxMiniprogramPath = wxMiniprogramPath;
    }

    public String getWxQrcodeUrl() {
        return wxQrcodeUrl;
    }

    public void setWxQrcodeUrl(String wxQrcodeUrl) {
        this.wxQrcodeUrl = wxQrcodeUrl;
    }
}
