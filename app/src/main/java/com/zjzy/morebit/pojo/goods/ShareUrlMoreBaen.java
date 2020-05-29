package com.zjzy.morebit.pojo.goods;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fengrs on 2018/8/24.
 */

public class ShareUrlMoreBaen implements Serializable {
    String itemId;
    String shareUrl;

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
