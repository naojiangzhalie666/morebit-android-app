package com.markermall.cat.pojo.goods;

import java.util.List;

public class SellerBean {

    /**
     * userId : 171827809
     * shopId : 37090299
     * shopName : 福喜小屋家居日用百货
     * evaluates : [{"title":"宝贝描述","score":"4.8 ","type":"desc","level":"1","levelText":"高","levelTextColor":"#FF5000","levelBackgroundColor":"#FFF1EB","tmallLevelTextColor":"#FF0036","tmallLevelBackgroundColor":"#FFF1EB"},{"title":"卖家服务","score":"4.8 ","type":"serv","level":"1","levelText":"高","levelTextColor":"#FF5000","levelBackgroundColor":"#FFF1EB","tmallLevelTextColor":"#FF0036","tmallLevelBackgroundColor":"#FFF1EB"},{"title":"物流服务","score":"4.8 ","type":"post","level":"1","levelText":"高","levelTextColor":"#FF5000","levelBackgroundColor":"#FFF1EB","tmallLevelTextColor":"#FF0036","tmallLevelBackgroundColor":"#FFF1EB"}]
     *
     标题:  item -->title
     商品轮播图:  item -->images
     销量 value -->  delivery  -->from
     商品价格
     券后价
     销量
     店铺类型
     优惠券
     优惠券开始结束时间
     推荐语
     店铺主图  seller-->shopIcon
     店铺名  seller-->shopName
     店铺服务评价评价 consumerProtection
     消费者权益 seller--> evaluates
     */


    private String userId ="";
    private String shopName="";
    private String shopIcon="";
    private List<EvaluatesBean> evaluates;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getShopIcon() {
        return shopIcon;
    }

    public void setShopIcon(String shopIcon) {
        this.shopIcon = shopIcon;
    }


    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public List<EvaluatesBean> getEvaluates() {
        return evaluates;
    }

    public void setEvaluates(List<EvaluatesBean> evaluates) {
        this.evaluates = evaluates;
    }

}
