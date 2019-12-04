package com.zjzy.morebit.pojo.goods;

import com.zjzy.morebit.pojo.ShopGoodInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fengrs on 2018/12/12.
 * 备注: 精选活动
 */

public class HandpickBean implements Serializable {

    /**
     * id : 422
     * title : 313311
     * picture : https://img.gzzhitu.com/picture/20180908/153639161926363.png
     * items : [{"itemSourceId":1887400496,"couponUrl":"fccc869e6df14c37bd5969fc4f9d43d2","title":"天天特价陶瓷碗骨瓷大号保鲜泡面碗学生饭盒微波炉带盖密封套装","price":"29.61","couponPrice":"5.00","voucherPrice":"24.61"},{"itemSourceId":"2576874779","couponUrl":"e24f01d48069474ca9006ded38f90913","title":"浮德堡简易折叠浴缸成人沐浴泡澡桶家用免充气洗澡盆加厚持久保温","price":"380.00","couponPrice":"20.00","voucherPrice":"360.00"},{"itemSourceId":"5861290244","couponUrl":"b539f1edc9954d0190006360e629547f","title":"同仁堂美白祛斑面膜补水保湿淡斑提亮肤色七子白粉去黄气暗沉祛黄","price":"51.00","couponPrice":"10.00","voucherPrice":"41.00"},{"itemSourceId":"7486909827","couponUrl":"3542ba74ecba4e9a9bd9ffe45ff4f6c7","title":"吉祥春领带男礼盒装商务正装职业新郎韩版条纹休闲学生面试领带","price":"29.90","couponPrice":"10.00","voucherPrice":"19.90"}]
     */

    private int id;
    private String title;
    private String picture;
    private String backgroundImage ; // 二级商品列表 背景图
    private List<ShopGoodInfo> items;

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public List<ShopGoodInfo> getItems() {
        return items;
    }

    public void setItems(List<ShopGoodInfo> items) {
        this.items = items;
    }

}
