package com.zjzy.morebit.pojo;

import java.util.List;

/**

 */
public class MarkermallCircleInfo {


    /**
     * id : 7
     * create_time : 昨天 14:08
     * share_count : 0
     * extra : 2315770603,2776988304
     * content : 【在售价】元
     * 【优惠】元
     * 【券后限时秒杀价】元
     * 【剩余数】现在只剩下  张优惠券咯
     * 【实时销量】
     * 【下单链接】https://s.click.taobao.com/XlLOSQw
     * ---------------
     * 复制这条信息，，打开【手机淘宝】即可购买"
     * name : 女装爆款
     * icon : http://zhitushow.oss-cn-beijing.aliyuncs.com/picture/20180530/15276725225551.jpg
     * picture :
     * goods : [{"taobao":2315770603,"title":"AiSleep/睡眠博士慢回弹记忆枕太空颈椎枕头 脖子护颈枕单人枕芯","picture":"https://img.alicdn.com/imgextra/i2/857030436/TB2qWFjtStYBeNjSspaXXaOOFXa_!!857030436.jpg_200x200.jpg","price":"159.90","coupon_url":"173b6964d0974c59979865e2a5de7c06","voucher_price":"129.90","details":null,"commission":22.99,"coupon_price":"30.00"},{"taobao":2776988304,"title":"比度克祛痘产品前五强祛痘膏男女士痘痘去青春痘学生去痘印淡印霜","picture":"http://img.alicdn.com/imgextra/i1/255391319/TB254pMkDTI8KJjSsphXXcFppXa_!!255391319.jpg_200x200.jpg","price":"68.00","coupon_url":"af132f31a25f49c09f7cf8f4870b805b","voucher_price":"28.00","details":null,"commission":7.39,"coupon_price":"40.00"}]
     */
    /**发圈id*/
    private int id;
    /**数据创建时间*/
    private String createTime="";
    /**分享次数*/
    private int shareCount;
    /**发圈内容*/
    private String content="";
    /**标题*/
    private String name="";
    /**图片宽度*/
    private int width;
    /**图片高度*/
    private int height;

    private String icon="";
    /**评论内容*/
    private String comment="";
    /**用户标签*/
    private String userLabel="";
    private String labelPicture="";
    private List<String> picture;
    private List<ShopGoodInfo> goods;
    private List<MarkermallCircleItemInfo> shareRangItems;
    private List<MarkermallCircleInfo> circleBrands;
    private int isCollection;
    private List<String> pictureList; //发圈为你推荐
    private String collectionId;
    private String labelName;
    private String labelPic;
    private int isRecommend;



    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getLabelPic() {
        return labelPic;
    }

    public void setLabelPic(String labelPic) {
        this.labelPic = labelPic;
    }

    public int getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(int isRecommend) {
        this.isRecommend = isRecommend;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    private boolean isShowCircleBrand = false; //用于显示为你推荐

    public List<String> getPictureList() {
        return pictureList;
    }

    public void setPictureList(List<String> pictureList) {
        this.pictureList = pictureList;
    }

    public int getIsCollection() {
        return isCollection;
    }

    public void setIsCollection(int isCollection) {
        this.isCollection = isCollection;
    }

    public boolean isShowCircleBrand() {
        return isShowCircleBrand;
    }

    public void setShowCircleBrand(boolean showCircleBrand) {
        isShowCircleBrand = showCircleBrand;
    }

    public List<MarkermallCircleInfo> getCircleBrands() {
        return circleBrands;
    }

    public void setCircleBrands(List<MarkermallCircleInfo> circleBrands) {
        this.circleBrands = circleBrands;
    }

    public String getUserLabel() {
        return userLabel;
    }

    public void setUserLabel(String userLabel) {
        this.userLabel = userLabel;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String create_time) {
        this.createTime = create_time;
    }

    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }
    public String getContent() {
        if(content == null){
            content = "";
        }
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<String> getPicture() {
        return picture;
    }

    public void setPicture(List<String> picture) {
        this.picture = picture;
    }

    public List<ShopGoodInfo> getGoods() {
        return goods;
    }

    public void setGoods(List<ShopGoodInfo> goods) {
        this.goods = goods;
    }

    public String getLabelPicture() {
        return labelPicture;
    }

    public void setLabelPicture(String labelPicture) {
        this.labelPicture = labelPicture;
    }

    public static class GoodsBean {
        /**
         * taobao : 2315770603
         * title : AiSleep/睡眠博士慢回弹记忆枕太空颈椎枕头 脖子护颈枕单人枕芯
         * picture : https://img.alicdn.com/imgextra/i2/857030436/TB2qWFjtStYBeNjSspaXXaOOFXa_!!857030436.jpg_200x200.jpg
         * price : 159.90
         * coupon_url : 173b6964d0974c59979865e2a5de7c06
         * voucher_price : 129.90
         * details : null
         * commission : 22.99
         * coupon_price : 30.00
         * (title:商品标题，picture:商品图片，price:商品价格,taobao:商品ID,
         * coupon_url:优惠券链接,commission:预计可得，coupon_price:优惠券金额,voucher_price:券后价)
         * java.lang.IllegalStateException: Expected BEGIN_ARRAY but was NUMBER at line 1 column 4096 path $.data[4].goods
         */


        private long item;
        private String title="";
        private String picture="";
        private int m_sale ;
        private String price="";
        private String coupon_url="";
        private String voucher_price="";//券后价格
        private double commission;
        private String coupon_price="";
        private int is_expire ;
        private String shop_name = ""; // 店铺名字

        public String getShop_name() {
            return shop_name;
        }

        public void setShop_name(String shop_name) {
            this.shop_name = shop_name;
        }

        public int getM_sale() {
            return m_sale;
        }

        public void setM_sale(int m_sale) {
            this.m_sale = m_sale;
        }

        public int getIs_expire() {
            return is_expire;
        }

        public void setIs_expire(int is_expire) {
            this.is_expire = is_expire;
        }

        public long getItem() {
            return item;
        }

        public void setItem(long item) {
            this.item = item;
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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getCoupon_url() {
            return coupon_url;
        }

        public void setCoupon_url(String coupon_url) {
            this.coupon_url = coupon_url;
        }

        public String getVoucher_price() {
            return voucher_price;
        }

        public void setVoucher_price(String voucher_price) {
            this.voucher_price = voucher_price;
        }

        public double getCommission() {
            return commission;
        }

        public void setCommission(double commission) {
            this.commission = commission;
        }

        public String getCoupon_price() {
            return coupon_price;
        }

        public void setCoupon_price(String coupon_price) {
            this.coupon_price = coupon_price;
        }


    }

    public List<MarkermallCircleItemInfo> getShareRangItems() {
        return shareRangItems;
    }

    public void setShareRangItems(List<MarkermallCircleItemInfo> shareRangItems) {
        this.shareRangItems = shareRangItems;
    }
}


