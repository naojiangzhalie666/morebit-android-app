package com.jf.my.pojo.request;

public class RequestTKLBean extends RequestBaseBean {

    private String itemSourceId;
    private String itemTitle;
    private String itemDesc;
    private String itemPicture;
    private String itemPrice;
    private String couponPrice;
    private String itemVoucherPrice;
    private String saleMonth;
    private String couponUrl;
    private String template;
    private String commission;
    private String isShortLink;//是否短链，1-短链；2-长链；0-默认值
    private String material;

    private int isInviteCode ;//是否需要邀请码 0 否 1是
    private int isDownLoadUrl ;//是否需要下载链接 0 否 1是


    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getIsShortLink() {
        return isShortLink;
    }

    public void setIsShortLink(String isShortLink) {
        this.isShortLink = isShortLink;
    }

    public String getItemSourceId() {
        return itemSourceId;
    }

    public void setItemSourceId(String itemSourceId) {
        this.itemSourceId = itemSourceId;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getItemPicture() {
        return itemPicture;
    }

    public void setItemPicture(String itemPicture) {
        this.itemPicture = itemPicture;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(String couponPrice) {
        this.couponPrice = couponPrice;
    }

    public String getItemVoucherPrice() {
        return itemVoucherPrice;
    }

    public void setItemVoucherPrice(String itemVoucherPrice) {
        this.itemVoucherPrice = itemVoucherPrice;
    }

    public String getSaleMouth() {
        return saleMonth;
    }

    public void setSaleMouth(String saleMouth) {
        this.saleMonth = saleMouth;
    }

    public String getCouponUrl() {
        return couponUrl;
    }

    public void setCouponUrl(String couponUrl) {
        this.couponUrl = couponUrl;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public int getIsInviteCode() {
        return isInviteCode;
    }

    public void setIsInviteCode(int isInviteCode) {
        this.isInviteCode = isInviteCode;
    }

    public int getIsDownLoadUrl() {
        return isDownLoadUrl;
    }

    public void setIsDownLoadUrl(int isDownLoadUrl) {
        this.isDownLoadUrl = isDownLoadUrl;
    }
}
