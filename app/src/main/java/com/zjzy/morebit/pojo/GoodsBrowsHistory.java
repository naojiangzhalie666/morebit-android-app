package com.zjzy.morebit.pojo;


import com.zjzy.morebit.pojo.goods.GoodsImgDetailBean;
import com.zjzy.morebit.utils.dao.GoodsImgDetailBeanConverter;
import com.zjzy.morebit.utils.dao.StringConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;

/**
 * Created by YangBoTian on 2018/11/26.
 */

@Entity
public class GoodsBrowsHistory {

	@Id(autoincrement = false)
	private String itemSourceId;  //淘宝商品ID

	private int grab_type; //1快抢即将开始；2快抢商品已抢光；3快抢商品正在快抢中
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
	private String commission = "";//扣税后总佣金；预计可得（（商品原价 * 佣金比例）* 0.90）
	private String couponPrice = "";//优惠券金额
	private String couponUrl = "";//优惠券链接
	private String itemFrom; // 1内网  0 全网
	private String shopId = "";//店主的userId
	private String itemDesc = "";  // 商品描述
	private int coller ; // 有收藏就返回收藏id
	private String provcity = "";  // 省市
	private String evaluates = "";  // 服务评价
	private String consumerProtection = "";  // 消费者权益
	private String taobaoDetailUrl = "";  // 淘宝商品详情地址
	private String couponStartTime = "";  // 优惠券开始时间
	private String couponEndTime = "";  // 优惠券结束时间
	@Convert(/**指定转换器 **/converter = StringConverter.class,/**指定数据库中的列字段**/columnType =String.class )
	private List<String> itemBanner;
	@Convert(/**指定转换器 **/converter = GoodsImgDetailBeanConverter.class,/**指定数据库中的列字段**/columnType =String.class )
	private GoodsImgDetailBean picUrls;
	private int isExpire; // 发圈是否过期
	private String phone = "";
	private Long browse_time; // 浏览时间   yyyy-MM-dd HH:mm
	private String  subsidiesPrice; //补贴金额


	// 自用
	@Transient
	public boolean isSelect; //是否选择
	@Transient
	private  int viewType;   //列表view分类自用
	@Transient
	private  String time;   // 浏览时间  yyyy-MM-dd
	@Generated(hash = 1784562638)
	public GoodsBrowsHistory(String itemSourceId, int grab_type, String itemTitle, String itemPrice,
									String saleMonth, String shopName, String sellerPicture, String itemPicture, String itemPictureMax,
									int shopType, String itemVideoid, String itemVoucherPrice, String commission, String couponPrice,
									String couponUrl, String itemFrom, String shopId, String itemDesc, int coller, String provcity,
									String evaluates, String consumerProtection, String taobaoDetailUrl, String couponStartTime,
									String couponEndTime, List<String> itemBanner, GoodsImgDetailBean picUrls, int isExpire, String phone,
									Long browse_time, String subsidiesPrice) {
					this.itemSourceId = itemSourceId;
					this.grab_type = grab_type;
					this.itemTitle = itemTitle;
					this.itemPrice = itemPrice;
					this.saleMonth = saleMonth;
					this.shopName = shopName;
					this.sellerPicture = sellerPicture;
					this.itemPicture = itemPicture;
					this.itemPictureMax = itemPictureMax;
					this.shopType = shopType;
					this.itemVideoid = itemVideoid;
					this.itemVoucherPrice = itemVoucherPrice;
					this.commission = commission;
					this.couponPrice = couponPrice;
					this.couponUrl = couponUrl;
					this.itemFrom = itemFrom;
					this.shopId = shopId;
					this.itemDesc = itemDesc;
					this.coller = coller;
					this.provcity = provcity;
					this.evaluates = evaluates;
					this.consumerProtection = consumerProtection;
					this.taobaoDetailUrl = taobaoDetailUrl;
					this.couponStartTime = couponStartTime;
					this.couponEndTime = couponEndTime;
					this.itemBanner = itemBanner;
					this.picUrls = picUrls;
					this.isExpire = isExpire;
					this.phone = phone;
					this.browse_time = browse_time;
					this.subsidiesPrice = subsidiesPrice;
	}
	@Generated(hash = 2011938702)
	public GoodsBrowsHistory() {
	}
	public String getItemSourceId() {
					return this.itemSourceId;
	}
	public void setItemSourceId(String itemSourceId) {
					this.itemSourceId = itemSourceId;
	}
	public int getGrab_type() {
					return this.grab_type;
	}
	public void setGrab_type(int grab_type) {
					this.grab_type = grab_type;
	}
	public String getItemTitle() {
					return this.itemTitle;
	}
	public void setItemTitle(String itemTitle) {
					this.itemTitle = itemTitle;
	}
	public String getItemPrice() {
					return this.itemPrice;
	}
	public void setItemPrice(String itemPrice) {
					this.itemPrice = itemPrice;
	}
	public String getSaleMonth() {
					return this.saleMonth;
	}
	public void setSaleMonth(String saleMonth) {
					this.saleMonth = saleMonth;
	}
	public String getShopName() {
					return this.shopName;
	}
	public void setShopName(String shopName) {
					this.shopName = shopName;
	}
	public String getSellerPicture() {
					return this.sellerPicture;
	}
	public void setSellerPicture(String sellerPicture) {
					this.sellerPicture = sellerPicture;
	}
	public String getItemPicture() {
					return this.itemPicture;
	}
	public void setItemPicture(String itemPicture) {
					this.itemPicture = itemPicture;
	}
	public String getItemPictureMax() {
					return this.itemPictureMax;
	}
	public void setItemPictureMax(String itemPictureMax) {
					this.itemPictureMax = itemPictureMax;
	}
	public int getShopType() {
					return this.shopType;
	}
	public void setShopType(int shopType) {
					this.shopType = shopType;
	}
	public String getItemVideoid() {
					return this.itemVideoid;
	}
	public void setItemVideoid(String itemVideoid) {
					this.itemVideoid = itemVideoid;
	}
	public String getItemVoucherPrice() {
					return this.itemVoucherPrice;
	}
	public void setItemVoucherPrice(String itemVoucherPrice) {
					this.itemVoucherPrice = itemVoucherPrice;
	}
	public String getCommission() {
					return this.commission;
	}
	public void setCommission(String commission) {
					this.commission = commission;
	}
	public String getCouponPrice() {
					return this.couponPrice;
	}
	public void setCouponPrice(String couponPrice) {
					this.couponPrice = couponPrice;
	}
	public String getCouponUrl() {
					return this.couponUrl;
	}
	public void setCouponUrl(String couponUrl) {
					this.couponUrl = couponUrl;
	}
	public String getItemFrom() {
					return this.itemFrom;
	}
	public void setItemFrom(String itemFrom) {
					this.itemFrom = itemFrom;
	}
	public String getShopId() {
					return this.shopId;
	}
	public void setShopId(String shopId) {
					this.shopId = shopId;
	}
	public String getItemDesc() {
					return this.itemDesc;
	}
	public void setItemDesc(String itemDesc) {
					this.itemDesc = itemDesc;
	}
	public int getColler() {
					return this.coller;
	}
	public void setColler(int coller) {
					this.coller = coller;
	}
	public String getProvcity() {
					return this.provcity;
	}
	public void setProvcity(String provcity) {
					this.provcity = provcity;
	}
	public String getEvaluates() {
					return this.evaluates;
	}
	public void setEvaluates(String evaluates) {
					this.evaluates = evaluates;
	}
	public String getConsumerProtection() {
					return this.consumerProtection;
	}
	public void setConsumerProtection(String consumerProtection) {
					this.consumerProtection = consumerProtection;
	}
	public String getTaobaoDetailUrl() {
					return this.taobaoDetailUrl;
	}
	public void setTaobaoDetailUrl(String taobaoDetailUrl) {
					this.taobaoDetailUrl = taobaoDetailUrl;
	}
	public String getCouponStartTime() {
					return this.couponStartTime;
	}
	public void setCouponStartTime(String couponStartTime) {
					this.couponStartTime = couponStartTime;
	}
	public String getCouponEndTime() {
					return this.couponEndTime;
	}
	public void setCouponEndTime(String couponEndTime) {
					this.couponEndTime = couponEndTime;
	}
	public List<String> getItemBanner() {
					return this.itemBanner;
	}
	public void setItemBanner(List<String> itemBanner) {
					this.itemBanner = itemBanner;
	}
	public GoodsImgDetailBean getPicUrls() {
					return this.picUrls;
	}
	public void setPicUrls(GoodsImgDetailBean picUrls) {
					this.picUrls = picUrls;
	}
	public int getIsExpire() {
					return this.isExpire;
	}
	public void setIsExpire(int isExpire) {
					this.isExpire = isExpire;
	}
	public String getPhone() {
					return this.phone;
	}
	public void setPhone(String phone) {
					this.phone = phone;
	}
	public Long getBrowse_time() {
					return this.browse_time;
	}
	public void setBrowse_time(Long browse_time) {
					this.browse_time = browse_time;
	}
	public String getSubsidiesPrice() {
					return this.subsidiesPrice;
	}
	public void setSubsidiesPrice(String subsidiesPrice) {
					this.subsidiesPrice = subsidiesPrice;
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean select) {
		isSelect = select;
	}

	public int getViewType() {
		return viewType;
	}

	public void setViewType(int viewType) {
		this.viewType = viewType;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
