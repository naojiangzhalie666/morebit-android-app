package com.jf.my.pojo;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.jf.my.utils.MyLog;

import java.io.Serializable;

/**
 * Created by YangBoTian on 2019/7/12.
 */

public class HomeRecommendGoods implements MultiItemEntity {
    public static final int TYPE_AD = 1;
    public static final int TYPE_GOODS= 2;
    private ShopGoodInfo shopGoodInfo;
    private ImageInfo imageInfo;
    private int itemType;
    public HomeRecommendGoods(int itemType){
        this.itemType = itemType;
    }
    public ShopGoodInfo getShopGoodInfo() {
        return shopGoodInfo;
    }

    public void setShopGoodInfo(ShopGoodInfo shopGoodInfo) {
        this.shopGoodInfo = shopGoodInfo;
    }

    public ImageInfo getImageInfo() {
        return imageInfo;
    }

    public void setImageInfo(ImageInfo imageInfo) {
        this.imageInfo = imageInfo;
    }


    @Override
    public boolean equals(Object arg0) {
        HomeRecommendGoods p = (HomeRecommendGoods) arg0;
        ShopGoodInfo shop = p.shopGoodInfo;
        if(shop!=null&&shopGoodInfo!=null){
            return shopGoodInfo.getItemSourceId().equals(shop.getItemSourceId());
        }
       return super.equals(arg0);
    }

    @Override
    public int hashCode() {
        if(shopGoodInfo!=null){
            return shopGoodInfo.getItemSourceId().hashCode();
        }
        return super.hashCode();
    }

    @Override
    public int getItemType() {
        return this.itemType;
    }
}
