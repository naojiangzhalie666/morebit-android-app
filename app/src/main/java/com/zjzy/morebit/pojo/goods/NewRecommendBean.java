package com.zjzy.morebit.pojo.goods;

import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.ShopGoodInfo;

import java.util.List;

/**
 * Created by fengrs on 2018/6/29.
 */

public class NewRecommendBean {
    private int minNum;
    private int type;
    private List<ShopGoodInfo> itemList;
    private List<ImageInfo> ad;

    public int getMinNum() {
        return minNum;
    }

    public void setMinNum(int minNum) {
        this.minNum = minNum;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<ShopGoodInfo> getItemList() {
        return itemList;
    }

    public void setItemList(List<ShopGoodInfo> itemList) {
        this.itemList = itemList;
    }

    public List<ImageInfo> getAd() {
        return ad;
    }

    public void setAd(List<ImageInfo> ad) {
        this.ad = ad;
    }
}
