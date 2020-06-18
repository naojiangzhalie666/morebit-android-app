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
    private String itemPic="";//主图
    private String couponMoney="";//优惠券
    private String tkMoney;//预估收益
    private String itemVideo="";//抖货视频
    private String itemSale="";//销量
    private String itemId="";//抖货商品id
    private String productUrl="";
    private String clickURL="";//京东推广链接
    private String dyLikeCount="";//抖货点赞数
    private String itemVideoPic="";//视频第一帧

    public String getDyLikeCount() {
        return dyLikeCount;
    }

    public void setDyLikeCount(String dyLikeCount) {
        this.dyLikeCount = dyLikeCount;
    }

    public String getItemVideoPic() {
        return itemVideoPic;
    }

    public void setItemVideoPic(String itemVideoPic) {
        this.itemVideoPic = itemVideoPic;
    }

    /*
    * 考拉详情
    * */
    private List<String> imageList;//轮播
    private List<String> detailImgList;//图文详情中的图片
    private String goodsTitle;//标题
    private String goodsDetailUrl;//商品详情链接
    private String purchaseLink;//购买链接
    private String currentPrice;//现价
    private String marketPrice;//原价
    private String goodsDetail;//原商品详情链接


    /*
     * 唯品会详情
     * */
    private List<String> goodsCarouselPictures;//轮播
    private List<String> goodsDetailPictures;//图文详情中的图片
    private String goodsName;//标题
    private String goodsMainPicture;//商品主图
    private int status;//商品状态：0-下架，1-上架
    private String discount;//折扣:唯品价/市场价 保留两位小数字符串
    private String commissionRate;//佣金比
    private int sourceType;//商品类型：0-自营，1-非自营
    private String brandName;//商品品牌名称
    private String vipPrice;//现价
    private String deepLinkUrl;//购买链接
    private String storeName;//店铺名称

    /*
     * 高佣详情
     * */
    private String itemid;//宝贝ID
    private String itemtitle;//宝贝标题
    private String itemprice;//宝贝原价
    private String itemendprice;//宝贝劵后价
    private String itemsale;//宝贝月销
    private String itempic;//宝贝主图
    private List<String> taobaoImageList;//宝贝轮播
    private int shoptype;//店铺类型： 天猫（B）淘宝店（C）
    private String tkrates;//佣金比例
    private String tkmoney;//佣金
    private String couponurl;//优惠券链接
    private String couponmoney;//优惠券金额
    private String shopname;//店铺名称
    private String couponstarttime;//优惠券开始时间
    private String couponendtime;//优惠券结束时间
    private String itemdesc;//推荐语

    public int getShoptype() {
        return shoptype;
    }

    public void setShoptype(int shoptype) {
        this.shoptype = shoptype;
    }

    public String getItemdesc() {
        return itemdesc;
    }

    public void setItemdesc(String itemdesc) {
        this.itemdesc = itemdesc;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getItemtitle() {
        return itemtitle;
    }

    public void setItemtitle(String itemtitle) {
        this.itemtitle = itemtitle;
    }

    public String getItemprice() {
        return itemprice;
    }

    public void setItemprice(String itemprice) {
        this.itemprice = itemprice;
    }

    public String getItemendprice() {
        return itemendprice;
    }

    public void setItemendprice(String itemendprice) {
        this.itemendprice = itemendprice;
    }

    public String getItemsale() {
        return itemsale;
    }

    public void setItemsale(String itemsale) {
        this.itemsale = itemsale;
    }

    public String getItempic() {
        return itempic;
    }

    public void setItempic(String itempic) {
        this.itempic = itempic;
    }

    public List<String> getTaobaoImageList() {
        return taobaoImageList;
    }

    public void setTaobaoImageList(List<String> taobaoImageList) {
        this.taobaoImageList = taobaoImageList;
    }

    public String getTkrates() {
        return tkrates;
    }

    public void setTkrates(String tkrates) {
        this.tkrates = tkrates;
    }

    public String getTkmoney() {
        return tkmoney;
    }

    public void setTkmoney(String tkmoney) {
        this.tkmoney = tkmoney;
    }

    public String getCouponurl() {
        return couponurl;
    }

    public void setCouponurl(String couponurl) {
        this.couponurl = couponurl;
    }

    public String getCouponmoney() {
        return couponmoney;
    }

    public void setCouponmoney(String couponmoney) {
        this.couponmoney = couponmoney;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getCouponstarttime() {
        return couponstarttime;
    }

    public void setCouponstarttime(String couponstarttime) {
        this.couponstarttime = couponstarttime;
    }

    public String getCouponendtime() {
        return couponendtime;
    }

    public void setCouponendtime(String couponendtime) {
        this.couponendtime = couponendtime;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getDeepLinkUrl() {
        return deepLinkUrl;
    }

    public void setDeepLinkUrl(String deepLinkUrl) {
        this.deepLinkUrl = deepLinkUrl;
    }

    public List<String> getGoodsCarouselPictures() {
        return goodsCarouselPictures;
    }

    public void setGoodsCarouselPictures(List<String> goodsCarouselPictures) {
        this.goodsCarouselPictures = goodsCarouselPictures;
    }

    public List<String> getGoodsDetailPictures() {
        return goodsDetailPictures;
    }

    public void setGoodsDetailPictures(List<String> goodsDetailPictures) {
        this.goodsDetailPictures = goodsDetailPictures;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsMainPicture() {
        return goodsMainPicture;
    }

    public void setGoodsMainPicture(String goodsMainPicture) {
        this.goodsMainPicture = goodsMainPicture;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(String commissionRate) {
        this.commissionRate = commissionRate;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getVipPrice() {
        return vipPrice;
    }

    public void setVipPrice(String vipPrice) {
        this.vipPrice = vipPrice;
    }

    public String getGoodsDetail() {
        return goodsDetail;
    }

    public void setGoodsDetail(String goodsDetail) {
        this.goodsDetail = goodsDetail;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public List<String> getDetailImgList() {
        return detailImgList;
    }

    public void setDetailImgList(List<String> detailImgList) {
        this.detailImgList = detailImgList;
    }

    public String getGoodsTitle() {
        return goodsTitle;
    }

    public void setGoodsTitle(String goodsTitle) {
        this.goodsTitle = goodsTitle;
    }

    public String getGoodsDetailUrl() {
        return goodsDetailUrl;
    }

    public void setGoodsDetailUrl(String goodsDetailUrl) {
        this.goodsDetailUrl = goodsDetailUrl;
    }

    public String getPurchaseLink() {
        return purchaseLink;
    }

    public void setPurchaseLink(String purchaseLink) {
        this.purchaseLink = purchaseLink;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(String marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getClickURL() {
        return clickURL;
    }

    public void setClickURL(String clickURL) {
        this.clickURL = clickURL;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemSale() {
        return itemSale;
    }

    public void setItemSale(String itemSale) {
        this.itemSale = itemSale;
    }

    public String getItemVideoid() {
        return itemVideoid;
    }

    public void setItemVideoid(String itemVideoid) {
        this.itemVideoid = itemVideoid;
    }

    public String getItemVideo() {
        return itemVideo;
    }

    public void setItemVideo(String itemVideo) {
        this.itemVideo = itemVideo;
    }

    public String getTkMoney() {
        return tkMoney;
    }

    public void setTkMoney(String tkMoney) {
        this.tkMoney = tkMoney;
    }

    public String getCouponMoney() {
        return couponMoney;
    }

    public void setCouponMoney(String couponMoney) {
        this.couponMoney = couponMoney;
    }

    public String getItemPic() {
        return itemPic;
    }

    public void setItemPic(String itemPic) {
        this.itemPic = itemPic;
    }

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

