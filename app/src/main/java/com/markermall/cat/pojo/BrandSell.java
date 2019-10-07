package com.markermall.cat.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by YangBoTian on 2018/9/11.
 */

public class BrandSell implements Serializable {

    /**
     * id : 1
     * title : 三只松鼠
     * logo :
     * status : 1
     * sort : 0
     * create_time : 1970-01-01 08:00:00
     * background : http://zhitu-api.oss-cn-shenzhen.aliyuncs.com/picture/20180823/153499105631912.jpg
     * goods : [{"taobao":"3423563570","title":"LAVER脱毛膏套装全身去腿毛腋下抑毛学生男女士专用非私处不永久","voucher_price":"38.00","coupon_price":"20.00"},{"taobao":"5292254272","title":"膜法世家绿豆泥面膜美白控油清洁祛痘魔法世家去黑头收缩毛孔男女","voucher_price":"84.90","coupon_price":"5.00"},{"taobao":1887400496,"title":"天天特价陶瓷碗骨瓷大号保鲜泡面碗学生饭盒微波炉带盖密封套装","voucher_price":"24.61","coupon_price":"5.00"},{"taobao":"4083230526","title":"包邮欧式卧室灯铁艺美式吊灯现代简约客厅灯餐厅书房大气复古灯具","voucher_price":"48.00","coupon_price":"10.00"}]
     */
    /**品牌id*/
    private int id;
    /**品牌名称*/
    private String brandName;
    /**品牌logo*/
    private String brandLogo;
    private String backImage;

    private List<ShopGoodInfo> items;

//    自用
    public  String title;
//    自用


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBranndName() {
        return brandName;
    }

    public void setBranndName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandLogo() {
        return brandLogo;
    }

    public void setBrandLogo(String brandLogo) {
        this.brandLogo = brandLogo;
    }

    public List<ShopGoodInfo> getItems() {
        return items;
    }

    public void setItems(List<ShopGoodInfo> items) {
        this.items = items;
    }

    public String getBackImage() {
        return backImage;
    }

    public void setBackImage(String backImage) {
        this.backImage = backImage;
    }
}
