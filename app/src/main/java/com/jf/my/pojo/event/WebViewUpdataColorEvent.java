package com.jf.my.pojo.event;

/**
 * @author fengrs
 * @date 2019/8/30
 * web 修改颜色
 */
public class WebViewUpdataColorEvent {
    private String colorStr;
    private int iconType;

    public WebViewUpdataColorEvent(String colorStr, int iconType) {
        this.colorStr = colorStr;
        this.iconType = iconType;
    }

    public String getColorStr() {
        return colorStr;
    }

    public void setColorStr(String colorStr) {
        this.colorStr = colorStr;
    }

    public int getIconType() {
        return iconType;
    }

    public void setIconType(int iconType) {
        this.iconType = iconType;
    }
}
