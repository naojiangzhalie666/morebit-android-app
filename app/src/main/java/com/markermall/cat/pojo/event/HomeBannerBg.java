package com.markermall.cat.pojo.event;

/**
 * Created by fengrs on 2018/9/13.
 * 发送首頁banner图片
 */

public class HomeBannerBg {
    String imgUrl;
    int position;

    public HomeBannerBg(String imgUrl, int position) {
        this.imgUrl = imgUrl;
        this.position = position;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}

