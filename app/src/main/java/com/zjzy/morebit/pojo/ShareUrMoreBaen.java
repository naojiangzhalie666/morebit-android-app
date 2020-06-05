package com.zjzy.morebit.pojo;

import java.util.List;

/**
 * Created by fengrs on 2018/8/24.
 */

public class ShareUrMoreBaen {
    private String itemId;// 商品id
    private String shareUrl;// 分享链接（生成二维码用）

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }
}
