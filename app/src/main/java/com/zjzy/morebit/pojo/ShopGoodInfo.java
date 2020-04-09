package com.zjzy.morebit.pojo;

import com.zjzy.morebit.pojo.goods.GoodsImgDetailBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by fengrs on 2018/1/17.
 * 商品itme
 */

public class ShopGoodInfo implements Serializable {

    private int id;  //收藏id

    private List<ImageInfo> adImgUrl; //分享出去图片


    private int grab_type; //1快抢即将开始；2快抢商品已抢光；3快抢商品正在快抢中


    private String itemSourceId = "";//淘宝商品ID
    private String itemTitle = "";//商品标题
    private String itemPrice = "";//商品价格
    private String saleMonth = "";//商品销量
    private String shopName = ""; // 店铺名字`
    private String sellerPicture = ""; // 店铺logo`
    private String itemPicture = ""; //商品主图原始图像
    private String itemPictureMax = ""; //商品主图原始图像  大图
    private int shopType; //店铺类型: 天猫 - 2 / 淘宝 - 1
    private String itemVideoid = "";//商品视频ID

    private String itemVoucherPrice = "";  // 券后价格
    private String commission = "";//扣税后总佣金；预计可得（（商品原价 * 佣金比例）* 0.88）
    private String couponPrice = "";//优惠券金额
    private String couponUrl = "";//优惠券链接
    private String itemFrom = "1"; // 1内网  0 全网
    private String shopId = "";//店主的userId
    private String itemDesc = "";  // 商品描述
    private String subsidyPrice="";//补贴金额

    public String getSubsidyPrice() {
        return subsidyPrice;
    }

    public void setSubsidyPrice(String subsidyPrice) {
        this.subsidyPrice = subsidyPrice;
    }
//  private st

    private int coller; // 有收藏就返回收藏id

    private List<String> itemBanner;  // 商品轮播
    private String provcity = "";  // 省市
    private String evaluates = "";  // 服务评价
    private String consumerProtection = "";  // 消费者权益
    private String taobaoDetailUrl = "";  // 淘宝商品详情地址
    private String couponStartTime = "";  // 优惠券开始时间
    private String couponEndTime = "";  // 优惠券结束时间
    private String itemLabeling = "";  // 标识
    private String itemSubhead = "";  // 二级标题
    //超级大牌
    private int itemIndex  ;  // 推荐指数
    //超级大牌
    private List<String> banner;

    private GoodsImgDetailBean picUrls;
    /**
     * 是否需要前端解析 0需要解析，1不需要
     */
    private int analysisFlag = 0;


    private int isExpire; // 发圈是否过期 1：是 0：否
     private String createTime;   //收藏时间
    //预告单
    private int countDown;// 倒计时，分钟 60
    private String countDownStr= ""; // 开抢时间，格式MM-dd HH:mm或HH:mm, 小于一小时为具体分钟
    //预告单

    private String  subsidiesPrice; //补贴金额


    // 自用
    public String inType = "";// 当前页
    public int isSearchType; //  0 默认 1,全网搜索 2,猜你喜欢
    public boolean isCollect; //  是否收藏
    public boolean isSelect; //是否选择收藏商品
    public boolean isCountDownEnd; //预告单倒计时是否结束
    public String moduleDescUrl; //详情页图片请求地址
    private int type; // 1：收藏商品列表，为2：猜你喜欢列表
     private String time;

    public  boolean isImg; // 是否图片密封圈用到
    public  String material; // 是否是物料
    private String itemSource; //商品来源（1：淘宝 2：京东 3：聚划算，11-物料ID）//商品 来源  1 京东 2拼多多 3 苏宁
    private String comeFrom; //调度 1：后台放单 2：后台录入,N-物料ID'
    // 自用
    public String markValue; //标识内容（本地商品专属，后台关闭则不显示）

    //小程序对接-佣金比例
    private String commissionRare;

    //商品价格
    private String price;

    //商品券后价
    private String voucherPrice;

    //商品id
    private Long goodsId;

    //商品主图
    private String imageUrl;

    //推荐类型  1 为你推荐
    private int recommendType;

    //是否拼购   苏宁专用 优先跳 拼购小程序  1是 0 否
    private int isPin;

    //店家 编码
    private String supplierCode;

    //优惠券活动ID
    private String couponActivityId;

    //拼购ID
    private String pgActionId;

    //描述分
    private String descTxt;

    //服务分
    private String servTxt;

    //物流分
    private String lgstTxt;


    public String getDescTxt() {
        return descTxt;
    }

    public void setDescTxt(String descTxt) {
        this.descTxt = descTxt;
    }

    public String getServTxt() {
        return servTxt;
    }

    public void setServTxt(String servTxt) {
        this.servTxt = servTxt;
    }

    public String getLgstTxt() {
        return lgstTxt;
    }

    public void setLgstTxt(String lgstTxt) {
        this.lgstTxt = lgstTxt;
    }

    public String getCommissionRare() {
        return commissionRare;
    }

    public void setCommissionRare(String commissionRare) {
        this.commissionRare = commissionRare;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getRecommendType() {
        return recommendType;
    }

    public void setRecommendType(int recommendType) {
        this.recommendType = recommendType;
    }

    public int getIsPin() {
        return isPin;
    }

    public void setIsPin(int isPin) {
        this.isPin = isPin;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getCouponActivityId() {
        return couponActivityId;
    }

    public void setCouponActivityId(String couponActivityId) {
        this.couponActivityId = couponActivityId;
    }

    public String getPgActionId() {
        return pgActionId;
    }

    public void setPgActionId(String pgActionId) {
        this.pgActionId = pgActionId;
    }

    public String getSubsidiesPrice() {
        return subsidiesPrice;
    }

    public void setSubsidiesPrice(String subsidiesPrice) {
        this.subsidiesPrice = subsidiesPrice;
    }

    public String getItemSource() {
        return itemSource;
    }

    public void setItemSource(String itemSource) {
        this.itemSource = itemSource;
    }

    public String getComeFrom() {
        return comeFrom;
    }

    public void setComeFrom(String comeFrom) {
        this.comeFrom = comeFrom;
    }

    public String getMarkValue() {
        return markValue;
    }

    public void setMarkValue(String markValue) {
        this.markValue = markValue;
    }

    public int getItemIndex() {
        return itemIndex;
    }

    public void setItemIndex(int itemIndex) {
        this.itemIndex = itemIndex;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shop_name) {
        this.shopName = shop_name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIsExpire() {
        return isExpire;
    }

    public String getItemFrom() {
        return itemFrom;
    }

    public void setItemFrom(String item_from) {
        this.itemFrom = item_from;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImages() {
        return getPicture();
    }


    public String getTitle() {
        return itemTitle;
    }

    public void setTitle(String title) {
        this.itemTitle = title;
    }


    public String getCouponUrl() {
        return couponUrl;
    }

    public void setCouponUrl(String couponUrl) {
        this.couponUrl = couponUrl;
    }

    public String getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(String couponPrice) {
        this.couponPrice = couponPrice;
    }


    public void setTaobao_id(String taobao_id) {
        setTaobao(taobao_id);
    }


    public List<String> getBanner() {
        return banner;
    }

    public void setBanner(List<String> banner) {
        this.banner = banner;
    }


    public String getVideo() {
        return getVideoid();
    }

    public List<ImageInfo> getAdImgUrl() {
        return adImgUrl;
    }

    public void setAdImgUrl(List<ImageInfo> adImgUrl) {
        this.adImgUrl = adImgUrl;
    }

    public String getItemSourceId() {
        return itemSourceId;
    }

    public void setTaobao(String taobao) {
        this.itemSourceId = taobao;
    }

    public String getVoucherPriceForPdd() {
        return voucherPrice;
    }

    public void setVoucherPriceForPdd(String voucherPrice) {
        this.voucherPrice = voucherPrice;
    }

    public String getVoucherPrice() {
        return itemVoucherPrice;
    }

    public void setVoucherPrice(String voucherPrice) {
        this.itemVoucherPrice = voucherPrice;
    }

    public String getPicture() {
        return itemPicture;
    }

    public void setPicture(String picture) {
        this.itemPicture = picture;
    }

    public int getShopType() {
        return shopType;
    }

    public void setShopType(int shopType) {
        this.shopType = shopType;
    }

    public String getPriceForPdd() {
        return price;
    }

    public void setPriceForPdd(String price) {
        this.price = price;
    }

    public String getPrice() {
        return itemPrice;
    }

    public void setPrice(String price) {
        this.itemPrice = price;
    }

    public String getVideoid() {
        return itemVideoid;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String showdesc) {
        this.itemDesc = showdesc;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }


    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getCouponEndTime() {
        return couponEndTime;
    }

    public void setCouponEndTime(String couponEndTime) {
        this.couponEndTime = couponEndTime;
    }

    public int getColler() {
        return coller;
    }

    public ShopGoodInfo setColler(int coller) {
        this.coller = coller;
        return this;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getGrab_type() {
        return grab_type;
    }


    public String getSaleMonth() {
        return saleMonth;
    }

    public void setSaleMonth(String saleMouth) {
        this.saleMonth = saleMouth;
    }

    public ShopGoodInfo() {
    }

    public ShopGoodInfo(int isSearchType) {
        this.isSearchType = isSearchType;
    }



    public void setGrab_type(int grab_type) {
        this.grab_type = grab_type;
    }


    public ShopGoodInfo setItemSourceId(String itemSourceId) {
        this.itemSourceId = itemSourceId;
        return this;
    }


    public List<String> getItemBanner() {
        return itemBanner;
    }

    public void setItemBanner(List<String> itemBanner) {
        this.itemBanner = itemBanner;
    }

    public String getProvcity() {
        return provcity;
    }

    public void setProvcity(String provcity) {
        this.provcity = provcity;
    }

    public String getEvaluates() {
        return evaluates;
    }

    public void setEvaluates(String evaluates) {
        this.evaluates = evaluates;
    }

    public String getConsumerProtection() {
        return consumerProtection;
    }

    public void setConsumerProtection(String consumerProtection) {
        this.consumerProtection = consumerProtection;
    }

    public String getTaobaoDetailUrl() {
        return taobaoDetailUrl;
    }

    public void setTaobaoDetailUrl(String taobaoDetailUrl) {
        this.taobaoDetailUrl = taobaoDetailUrl;
    }

    public void setIsExpire(int isExpire) {
        this.isExpire = isExpire;
    }

    public String getInType() {
        return inType;
    }

    public void setInType(String inType) {
        this.inType = inType;
    }

    public int getIsSearchType() {
        return isSearchType;
    }

    public void setIsSearchType(int isSearchType) {
        this.isSearchType = isSearchType;
    }

    public GoodsImgDetailBean getPicUrls() {
        return picUrls;
    }

    public void setPicUrls(GoodsImgDetailBean picUrls) {

        this.picUrls = picUrls;
    }

    public String getSellerPicture() {
        return sellerPicture;
    }

    public void setSellerPicture(String sellerPicture) {
        this.sellerPicture = sellerPicture;
    }

    public String getCouponStartTime() {
        return couponStartTime;
    }

    public void setCouponStartTime(String couponStartTime) {
        this.couponStartTime = couponStartTime;
    }

    public int getCountDown() {
        return countDown;
    }

    public void setCountDown(int countDown) {
        this.countDown = countDown;
    }

    public String getCountDownStr() {
        return countDownStr;
    }

    public void setCountDownStr(String countDownStr) {
        this.countDownStr = countDownStr;
    }

    public String getItemLabeling() {
        return itemLabeling;
    }

    public void setItemLabeling(String itemLabeling) {
        this.itemLabeling = itemLabeling;
    }

    public String getItemSubhead() {
        return itemSubhead;
    }

    public void setItemSubhead(String itemSubhead) {
        this.itemSubhead = itemSubhead;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemPicture() {
        return itemPicture;
    }

    public void setItemPicture(String itemPicture) {
        this.itemPicture = itemPicture;
    }

    public String getItemPictureMax() {
        return itemPictureMax;
    }

    public void setItemPictureMax(String itemPictureMax) {
        this.itemPictureMax = itemPictureMax;
    }

    public String getItemVoucherPrice() {
        return itemVoucherPrice;
    }

    public void setItemVoucherPrice(String itemVoucherPrice) {
        this.itemVoucherPrice = itemVoucherPrice;
    }

    public int getAnalysisFlag() {
        return analysisFlag;
    }

    public void setAnalysisFlag(int analysisFlag) {
        this.analysisFlag = analysisFlag;
    }

    @Override
    public boolean equals(Object arg0) {
        ShopGoodInfo p = (ShopGoodInfo) arg0;
        return itemSourceId.equals(p.itemSourceId);
    }

    @Override
    public int hashCode() {
        String str = itemSourceId;
        return str.hashCode();
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

