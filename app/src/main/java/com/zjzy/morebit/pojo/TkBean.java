package com.zjzy.morebit.pojo;

public class TkBean {

    /**
     * material : 0
     * itemDesc : 【中国科学院检测，优等品百分百进口原生木浆】久违的29.99元超值价，累计销售破百万箱，线下商场活动19.9才一提，我们3提42卷。别看我们个不大~我们质量好着呢~正经认证过的，母婴用纸！
     * itemTitle : 四合院儿卷纸无芯家用可湿水抽纸巾母婴实惠装酒店卫生纸原生木浆
     * couponUrl : https://uland.taobao.com/quan/detail?sellerId2201296327637=&activityId=
     * couponPrice : 15.0
     * itemPrice : 44.99
     * itemPicture : https://img.alicdn.com/bao/uploaded/i4/2201296327637/O1CN01U3Zztk26HnLPk4DZg_!!0-item_pic.jpg
     * saleMonth : 237302
     * isDownLoadUrl : 0
     * commission : 5.39
     * isShortLink : 2
     * isShowTkl : 1
     * itemVoucherPrice : 29.99
     * isInviteCode : 0
     * itemSourceId : 594682640490
     * type : 1
     */

    private String material;
    private String itemDesc;
    private String itemTitle;
    private String couponUrl;
    private String couponPrice;
    private String itemPrice;
    private String itemPicture;
    private String saleMonth;
    private String isDownLoadUrl;//是否需要下载链接 0 否 1 是
    private String commission;
    private int isShortLink;//是否为短链，默认0，0-短链，1-长链
    private String isShowTkl;//是否显示淘口令
    private String itemVoucherPrice;
    private String isInviteCode;//是否返回邀请码  0 否 1 是
    private String itemSourceId;
    private int type; //1 淘宝 2 京东 3 拼多多 4 考拉  6 唯品会

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
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

    public String getSaleMonth() {
        return saleMonth;
    }

    public void setSaleMonth(String saleMonth) {
        this.saleMonth = saleMonth;
    }

    public String getIsDownLoadUrl() {
        return isDownLoadUrl;
    }

    public void setIsDownLoadUrl(String isDownLoadUrl) {
        this.isDownLoadUrl = isDownLoadUrl;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public int getIsShortLink() {
        return isShortLink;
    }

    public void setIsShortLink(int isShortLink) {
        this.isShortLink = isShortLink;
    }

    public String getIsShowTkl() {
        return isShowTkl;
    }

    public void setIsShowTkl(String isShowTkl) {
        this.isShowTkl = isShowTkl;
    }

    public String getItemVoucherPrice() {
        return itemVoucherPrice;
    }

    public void setItemVoucherPrice(String itemVoucherPrice) {
        this.itemVoucherPrice = itemVoucherPrice;
    }

    public String getIsInviteCode() {
        return isInviteCode;
    }

    public void setIsInviteCode(String isInviteCode) {
        this.isInviteCode = isInviteCode;
    }

    public String getItemSourceId() {
        return itemSourceId;
    }

    public void setItemSourceId(String itemSourceId) {
        this.itemSourceId = itemSourceId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
