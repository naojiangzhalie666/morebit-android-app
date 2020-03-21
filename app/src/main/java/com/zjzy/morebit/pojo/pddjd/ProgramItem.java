package com.zjzy.morebit.pojo.pddjd;



import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * @author CSR
 */

public class ProgramItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 佣金比例
     */
    private Integer commissionRare;
    /**
     * 预估可得
     */
    private BigDecimal commission;
    /**
     * 优惠券金额
     */
    private BigDecimal couponPrice;


    /**
     * 商品价格
     */
    private String price;
    /**
     * 商品券后价
     */
    private String voucherPrice;

    /**
     * 店铺名
     */
    private String shopName;

    /**
     * 商品ID
     * */
    private Long goodsId;

    /**
     * 商品主图
     */
    private String imageUrl;
    /**
     * 轮播图
     */
    private String itemBanner;

    /**
     * 30天销量
     */
    private String saleMonth;

    /**
     * 商品标题
     */
    private String itemTitle;


    /**
     * 优惠券链接
     */
    private String couponUrl;

    /**
     * 商品 来源  1 京东 2拼多多 3 苏宁
     */
    private Integer itemSource;

    /**
     * 优惠券开始时间
     */
    private Date couponStartTime;
    /**
     * 优惠券结束时间
     */
    private Date couponEndTime;

    /**
     * 推荐类型  1 为你推荐
     * @param o
     * @return
     */
    private Integer recommendType;

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        ProgramItem that = (ProgramItem) o;
        return Objects.equals(goodsId, that.goodsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(goodsId);
    }

    public Integer getCommissionRare() {
        return commissionRare;
    }

    public void setCommissionRare(Integer commissionRare) {
        this.commissionRare = commissionRare;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public BigDecimal getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(BigDecimal couponPrice) {
        this.couponPrice = couponPrice;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getVoucherPrice() {
        return voucherPrice;
    }

    public void setVoucherPrice(String voucherPrice) {
        this.voucherPrice = voucherPrice;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
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

    public String getItemBanner() {
        return itemBanner;
    }

    public void setItemBanner(String itemBanner) {
        this.itemBanner = itemBanner;
    }

    public String getSaleMonth() {
        return saleMonth;
    }

    public void setSaleMonth(String saleMonth) {
        this.saleMonth = saleMonth;
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

    public Integer getItemSource() {
        return itemSource;
    }

    public void setItemSource(Integer itemSource) {
        this.itemSource = itemSource;
    }

    public Date getCouponStartTime() {
        return couponStartTime;
    }

    public void setCouponStartTime(Date couponStartTime) {
        this.couponStartTime = couponStartTime;
    }

    public Date getCouponEndTime() {
        return couponEndTime;
    }

    public void setCouponEndTime(Date couponEndTime) {
        this.couponEndTime = couponEndTime;
    }

    public Integer getRecommendType() {
        return recommendType;
    }

    public void setRecommendType(Integer recommendType) {
        this.recommendType = recommendType;
    }
}
