package com.zjzy.morebit.Module.service;

import com.zjzy.morebit.pojo.ProgramCatItemBean;
import com.zjzy.morebit.pojo.ProgramGetGoodsDetailBean;
import com.zjzy.morebit.pojo.ProgramSearchKeywordBean;
import com.zjzy.morebit.pojo.VideoClassBean;
import com.zjzy.morebit.pojo.address.AddressInfo;
import com.zjzy.morebit.pojo.address.AddressInfoList;
import com.zjzy.morebit.pojo.address.AllRegionInfoList;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.pojo.goods.FloorBean;
import com.zjzy.morebit.pojo.goods.PddShareContent;
import com.zjzy.morebit.pojo.goods.VideoBean;
import com.zjzy.morebit.pojo.order.OrderDetailInfo;
import com.zjzy.morebit.pojo.order.OrderSyncResult;
import com.zjzy.morebit.pojo.order.ResponseOrderInfo;
import com.zjzy.morebit.pojo.AccountDestroy;
import com.zjzy.morebit.pojo.AgentDetailList;
import com.zjzy.morebit.pojo.AppUpgradeInfo;
import com.zjzy.morebit.pojo.AreaCodeBean;
import com.zjzy.morebit.pojo.Article;
import com.zjzy.morebit.pojo.BrandSell;
import com.zjzy.morebit.pojo.CategoryListDtos;
import com.zjzy.morebit.pojo.CheckWithDrawBean;
import com.zjzy.morebit.pojo.CircleBrand;
import com.zjzy.morebit.pojo.CollegeHome;
import com.zjzy.morebit.pojo.ConsComGoodsInfo;
import com.zjzy.morebit.pojo.DayEarnings;
import com.zjzy.morebit.pojo.DayHotBean;
import com.zjzy.morebit.pojo.EarningExplainBean;
import com.zjzy.morebit.pojo.EarningsMsg;
import com.zjzy.morebit.pojo.FansInfo;
import com.zjzy.morebit.pojo.FloorInfo;
import com.zjzy.morebit.pojo.GrayUpgradeInfo;
import com.zjzy.morebit.pojo.HotKeywords;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.MarkermallCircleInfo;
import com.zjzy.morebit.pojo.MarkermallInformation;
import com.zjzy.morebit.pojo.MonthEarnings;
import com.zjzy.morebit.pojo.OfficialNotice;
import com.zjzy.morebit.pojo.PanicBuyTiemBean;
import com.zjzy.morebit.pojo.ProtocolRuleBean;
import com.zjzy.morebit.pojo.PushSwitchBean;
import com.zjzy.morebit.pojo.RankingTitleBean;
import com.zjzy.morebit.pojo.ReleaseGoodsPermission;
import com.zjzy.morebit.pojo.ReleaseManage;
import com.zjzy.morebit.pojo.SearchArticleBody;
import com.zjzy.morebit.pojo.SearchHotKeyBean;
import com.zjzy.morebit.pojo.ShareUrlBaen;
import com.zjzy.morebit.pojo.ShopGoodBean;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.StudyRank;
import com.zjzy.morebit.pojo.SystemConfigBean;
import com.zjzy.morebit.pojo.TeamData;
import com.zjzy.morebit.pojo.TeamInfo;
import com.zjzy.morebit.pojo.UpgradeCarousel;
import com.zjzy.morebit.pojo.UpgradeInstructions;
import com.zjzy.morebit.pojo.UserFeedback;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.UserscoreBean;
import com.zjzy.morebit.pojo.VipUseInfoBean;
import com.zjzy.morebit.pojo.goods.BandingAliRelationBean;
import com.zjzy.morebit.pojo.goods.CheckCouponStatusBean;
import com.zjzy.morebit.pojo.goods.CouponUrlBean;
import com.zjzy.morebit.pojo.goods.GoodCategoryInfo;
import com.zjzy.morebit.pojo.goods.HandpickBean;
import com.zjzy.morebit.pojo.goods.NewRecommendBean;
import com.zjzy.morebit.pojo.goods.RecommendBean;
import com.zjzy.morebit.pojo.goods.ShareUrlListBaen;
import com.zjzy.morebit.pojo.goods.ShareUrlStringBaen;
import com.zjzy.morebit.pojo.goods.TKLBean;
import com.zjzy.morebit.pojo.home.ByItemSourceIdBean;
import com.zjzy.morebit.pojo.home.SysNoticeBean;
import com.zjzy.morebit.pojo.home.TmallActivityBean;
import com.zjzy.morebit.pojo.login.InviteUserInfoBean;
import com.zjzy.morebit.pojo.myInfo.ApplyUpgradeBean;
import com.zjzy.morebit.pojo.myInfo.MakeMoenyBean;
import com.zjzy.morebit.pojo.myInfo.OssKeyBean;
import com.zjzy.morebit.pojo.myInfo.UpdateInfoBean;
import com.zjzy.morebit.pojo.number.NumberGoodsInfo;
import com.zjzy.morebit.pojo.number.NumberGoodsList;
import com.zjzy.morebit.pojo.pddjd.ProgramItem;
import com.zjzy.morebit.pojo.request.ClassroomBean;
import com.zjzy.morebit.pojo.request.RequestALiCodeBean;
import com.zjzy.morebit.pojo.request.RequestAddAddressBean;
import com.zjzy.morebit.pojo.request.RequestAppFeedBackBean;
import com.zjzy.morebit.pojo.request.RequestArticleBean;
import com.zjzy.morebit.pojo.request.RequestAuthCodeBean;
import com.zjzy.morebit.pojo.request.RequestBannerBean;
import com.zjzy.morebit.pojo.request.RequestBaseBean;
import com.zjzy.morebit.pojo.request.RequestBindWxBean;
import com.zjzy.morebit.pojo.request.RequestByGoodList;
import com.zjzy.morebit.pojo.request.RequestCheckGoodsBean;
import com.zjzy.morebit.pojo.request.RequestCheckOutPhoneBean;
import com.zjzy.morebit.pojo.request.RequestCheckPhoneNumBean;
import com.zjzy.morebit.pojo.request.RequestCircleBransBean;
import com.zjzy.morebit.pojo.request.RequestCircleCollectBean;
import com.zjzy.morebit.pojo.request.RequestCircleFeedBackBean;
import com.zjzy.morebit.pojo.request.RequestCircleSearchBean;
import com.zjzy.morebit.pojo.request.RequestCircleShareCountBean;
import com.zjzy.morebit.pojo.request.RequestCollectionListBean;
import com.zjzy.morebit.pojo.request.RequestConfigKeyBean;
import com.zjzy.morebit.pojo.request.RequestCouponUrlBean;
import com.zjzy.morebit.pojo.request.RequestCreateOrderBean;
import com.zjzy.morebit.pojo.request.RequestDeleteAddressIdBean;
import com.zjzy.morebit.pojo.request.RequestFansInfoBean;
import com.zjzy.morebit.pojo.request.RequestFeedBackTypeBean;
import com.zjzy.morebit.pojo.request.RequestGoodsCollectBean;
import com.zjzy.morebit.pojo.request.RequestGoodsListByIdBean;
import com.zjzy.morebit.pojo.request.RequestGoodsOrderBean;
import com.zjzy.morebit.pojo.request.RequestGradeBean;
import com.zjzy.morebit.pojo.request.RequestGradeUrlBean;
import com.zjzy.morebit.pojo.request.RequestIncomeBean;
import com.zjzy.morebit.pojo.request.RequestListBody;
import com.zjzy.morebit.pojo.request.RequestLoginBean;
import com.zjzy.morebit.pojo.request.RequestLoginCodeBean;
import com.zjzy.morebit.pojo.request.RequestMaterial;
import com.zjzy.morebit.pojo.request.RequestMaterialLink;
import com.zjzy.morebit.pojo.request.RequestMarkermallCircleBean;
import com.zjzy.morebit.pojo.request.RequestMtopJsonpBean;
import com.zjzy.morebit.pojo.request.RequestNickNameBean;
import com.zjzy.morebit.pojo.request.RequestNoticeListBean;
import com.zjzy.morebit.pojo.request.RequestNumberGoodsDetailBean;
import com.zjzy.morebit.pojo.request.RequestOrderDetailBean;
import com.zjzy.morebit.pojo.request.RequestPanicBuyTabBean;
import com.zjzy.morebit.pojo.request.RequestPddShareContent;
import com.zjzy.morebit.pojo.request.RequestPopupBean;
import com.zjzy.morebit.pojo.request.RequestPromotionUrlBean;
import com.zjzy.morebit.pojo.request.RequestPushBean;
import com.zjzy.morebit.pojo.request.RequestPutErrorBean;
import com.zjzy.morebit.pojo.request.RequestRecommendBean;
import com.zjzy.morebit.pojo.request.RequestRegisterAndBindWeChatBean;
import com.zjzy.morebit.pojo.request.RequestRegisterloginBean;
import com.zjzy.morebit.pojo.request.RequestReleaseCategory;
import com.zjzy.morebit.pojo.request.RequestReleaseGoods;
import com.zjzy.morebit.pojo.request.RequestReleaseGoodsDelete;
import com.zjzy.morebit.pojo.request.RequestReleaseManage;
import com.zjzy.morebit.pojo.request.RequestRemarkBean;
import com.zjzy.morebit.pojo.request.RequestRemoveCircleCollectBean;
import com.zjzy.morebit.pojo.request.RequestReplyBean;
import com.zjzy.morebit.pojo.request.RequestSearchBean;
import com.zjzy.morebit.pojo.request.RequestSearchForPddBean;
import com.zjzy.morebit.pojo.request.RequestSearchGuideBean;
import com.zjzy.morebit.pojo.request.RequestSearchOrderBean;
import com.zjzy.morebit.pojo.request.RequestSearchStatistics;
import com.zjzy.morebit.pojo.request.RequestSetAlipayBean;
import com.zjzy.morebit.pojo.request.RequestSetPasswordBean;
import com.zjzy.morebit.pojo.request.RequestSplashStatistics;
import com.zjzy.morebit.pojo.request.RequestSyncPayResultResultBean;
import com.zjzy.morebit.pojo.request.RequestSystemNoticeBean;
import com.zjzy.morebit.pojo.request.RequestTKLBean;
import com.zjzy.morebit.pojo.request.RequestTeanmListBean;
import com.zjzy.morebit.pojo.request.RequestTmallActivityLinkBean;
import com.zjzy.morebit.pojo.request.RequestUpPasswordBean;
import com.zjzy.morebit.pojo.request.RequestUpdateAddressBean;
import com.zjzy.morebit.pojo.request.RequestUpdateHeadPortraitBean;
import com.zjzy.morebit.pojo.request.RequestUpdateNewPhoneBean;
import com.zjzy.morebit.pojo.request.RequestUpdateUserBean;
import com.zjzy.morebit.pojo.request.RequestUserInfoBean;
import com.zjzy.morebit.pojo.request.RequestVaultListBean;
import com.zjzy.morebit.pojo.request.RequestVideoGoodsBean;
import com.zjzy.morebit.pojo.request.RequestVideoId;
import com.zjzy.morebit.pojo.request.RequestWechatCodeBean;
import com.zjzy.morebit.pojo.request.RequestWeixiLoginBean;
import com.zjzy.morebit.pojo.request.RequestWhatLike;
import com.zjzy.morebit.pojo.request.base.RequestBaseTypeBean;
import com.zjzy.morebit.pojo.requestbodybean.RequestAnalysisTKL;
import com.zjzy.morebit.pojo.requestbodybean.RequestCheckWithdrawBean;
import com.zjzy.morebit.pojo.requestbodybean.RequestConfirmUpgradeData;
import com.zjzy.morebit.pojo.requestbodybean.RequestDeleteCollection;
import com.zjzy.morebit.pojo.requestbodybean.RequestGetActivitiesDetails;
import com.zjzy.morebit.pojo.requestbodybean.RequestGetGoodsByBrand;
import com.zjzy.morebit.pojo.requestbodybean.RequestGetNinepinkageGoods;
import com.zjzy.morebit.pojo.requestbodybean.RequestGetRankings;
import com.zjzy.morebit.pojo.requestbodybean.RequestGetTimedSpikeList;
import com.zjzy.morebit.pojo.requestbodybean.RequestGoodsLike;
import com.zjzy.morebit.pojo.requestbodybean.RequestInviteCodeBean;
import com.zjzy.morebit.pojo.requestbodybean.RequestItemSourceId;
import com.zjzy.morebit.pojo.requestbodybean.RequestKeyBean;
import com.zjzy.morebit.pojo.requestbodybean.RequestModelId;
import com.zjzy.morebit.pojo.requestbodybean.RequestModelIdData;
import com.zjzy.morebit.pojo.requestbodybean.RequestNumberGoodsList;
import com.zjzy.morebit.pojo.requestbodybean.RequestOfficialNoticeBean;
import com.zjzy.morebit.pojo.requestbodybean.RequestOfficialRecommend;
import com.zjzy.morebit.pojo.requestbodybean.RequestOsBean;
import com.zjzy.morebit.pojo.requestbodybean.RequestPage;
import com.zjzy.morebit.pojo.requestbodybean.RequestShareGoods;
import com.zjzy.morebit.pojo.requestbodybean.RequestShopId;
import com.zjzy.morebit.pojo.requestbodybean.RequestSystemStaticPage;
import com.zjzy.morebit.pojo.requestbodybean.RequestThreeGoodsClassify;
import com.zjzy.morebit.pojo.requestbodybean.RequestTwoLevel;
import com.zjzy.morebit.pojo.requestbodybean.RequestUploadCouponInfo;
import com.zjzy.morebit.pojo.requestbodybean.RequestWithdrawBean;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by YangBoTian on 2018/6/14 17:27
 */

public interface CommonService {
    /**
     * 我的收益(日)
     *
     * @return
     */
    @POST("/api/user/getDayIncome")
    public Observable<BaseResponse<DayEarnings>> getDayIncome();


    /**
     * 我的收益(月)
     *
     * @return
     */
    @POST("/api/user/getMonthIncome")
    public Observable<BaseResponse<MonthEarnings>> getMonthIncome();


    /**
     * 京东、苏宁、拼多多收益-日
     *
     * @return
     */
    @POST("/api/order/getOrderIncomeTodayByJdSn")
    public Observable<BaseResponse<DayEarnings>> getPlatformDayIncome(@Body RequestIncomeBean requestBean);

    /**
     * 京东、苏宁、拼多多收益-月
     *
     * @return
     */
    @POST("/api/order/getOrderIncomeMonthByJdSn")
    public Observable<BaseResponse<MonthEarnings>> getPlatformMonthIncome(@Body RequestIncomeBean requestBean);

    /**
     * 京东、苏宁、拼多多收益-月
     *
     * @return
     */
    @POST("/api/order/confirmOrder")
    public Observable<BaseResponse<Boolean>> confirmOrder(@Body RequestOrderDetailBean requestBean);



    /**
     * 精品推荐
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/goods/getRecommend")

    public Observable<BaseResponse<RecommendBean>> getRecommend(
            @Body RequestRecommendBean requestBean);
//            @Field("page") int page,
//            @Field("extra") String extra,
//            @Field("type") int type);

    /**
     * 轮播图展示（广告加载）
     *
     * @param
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/system/getWheelChartDisplay")
    public Observable<BaseResponse<List<ImageInfo>>> getBanner(
            @Body RequestBannerBean requestBean);
//            @Field("type") int type,
//            @Field("os") int os);


    /**
     * 楼层
     *
     * @param
     * @return
     */
    @POST("/api/system/graphicInfo/sorting/list")
    public Observable<BaseResponse<List<FloorInfo>>> getFloor();


    /**
     * 分类信息(一级分类)/2.超级分类信息(三级分类)
     *
     * @param
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/goods/getGoodsListByCategoryId")
    public Observable<BaseResponse<List<ShopGoodInfo>>> getGoodsListByCategoryId(
            @Body RequestGoodsListByIdBean requestBean);
//            @Field("type") int type,
//            @Field("categoryId") String categoryId,
//            @Field("keyword") String keyword,
//            @Field("page") int page,
//            @Field("sort") String sort,
//            @Field("order") String order);

    /**
     * 0:限时抢购时间,1:淘抢购时间
     *
     * @return
     */
    @POST("/api/system/getRushHour")
    public Observable<BaseResponse<List<PanicBuyTiemBean>>> getPanicBuyTabData(
            @Body RequestPanicBuyTabBean requestBean);
//            @Field("type") int type);

    /**
     * 根据type 获取商品
     *
     * @return
     */
    @POST("/api/goods/list")
    public Observable<BaseResponse<List<ShopGoodInfo>>> findTypeByGoodList(
            @Body RequestByGoodList requestBean);


    /**
     * 关键词搜索
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/goods/searchKeyword")
    public Observable<BaseResponse<ShopGoodBean>> getSearchGoodsList(
            @Body RequestSearchBean requestBody);

    /**
     * 关键词搜索
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/goods/program/searchKeyword")
    public Observable<BaseResponse<List<ShopGoodInfo>>> getSearchGoodsListForPdd(
            @Body RequestSearchForPddBean requestBody);



    /**
     * 关键词搜索
     *
     * @return
     */

    @POST("/api/goods/searchKeywordManual")
    public Observable<BaseResponse<ShopGoodBean>> getSearchManual(
            @Body RequestSearchBean requestBody);


    /**
     * 我的团队A级别/我的团队B级别/我的团队C级别
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/user/userScore/getUsersTeam")
    public Observable<BaseResponse<TeamData>> getTeanmList(
            @Body RequestTeanmListBean requestBody);


    /**
     * 提交数据到我们后台接下淘宝返回的数据
     *
     * @return
     */

    @POST("/api/goods/goodsDetailsCode")
    public Observable<BaseResponse<ShopGoodInfo>> postTaobaoData(
            @Body RequestBody route
    );

    /**
     * 收藏（添加/移除)
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/user/collection/addUserCollection")
    public Observable<BaseResponse<Integer>> getGoodsCollect(
            @Body RequestGoodsCollectBean requestBean
//            @Field("itemSourceId") String itemSourceId,
//            @Field("itemTitle") String itemTitle,
//            @Field("itemPicture") String itemPicture,
//            @Field("itemPrice") String itemPrice,
//            @Field("couponPrice") String couponPrice,
//            @Field("itemVoucherPrice") String itemVoucherPrice,
//            @Field("couponUrl") String couponUrl,
//            @Field("couponEndTime") String couponEndTime,
//            @Field("commission") String commission,
//            @Field("shopType") String shopType,
//            @Field("shopName") String shopName  ,
//            @Field("saleMonth") String saleMonth  ,
//            @Field("rid") String rid,
//            @Field("sign") String sign
    );


    /**
     * 会员商品详情
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/order/goodsDetail")
    public Observable<BaseResponse<NumberGoodsInfo>> getNumberGoodsDetail(
            @Body RequestNumberGoodsDetailBean requestBean
    );

    /**
     * 获取收货地址列表
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/user/getAllAddresses")
    public Observable<BaseResponse<AddressInfoList>>  getAddressList();

    /**
     * 获取默认收货地址
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/user/getDefaultAddresses")
    public Observable<BaseResponse<AddressInfo>>  getDefaultAddresses();

    /**
     * 删除用户收货地址
     * @param requestBean
     * @return
     */
    //    @FormUrlEncoded
    @POST("/api/user/deleteAddress")
    public Observable<BaseResponse<Boolean>>  deleteAddress(
            @Body RequestDeleteAddressIdBean requestBean);

    /**
     * 更新收货地址
     * @param requestBean
     * @return
     */
    //    @FormUrlEncoded
    @POST("/api/user/updateAddress")
    public Observable<BaseResponse<Boolean>>  updateAddress(
            @Body RequestUpdateAddressBean requestBean);

    /**
     * 添加收货地址
     * @param requestBean
     * @return
     */
    //    @FormUrlEncoded
    @POST("/api/user/addAddress")
    public Observable<BaseResponse<Boolean>>  addAddress(
            @Body RequestAddAddressBean requestBean);

    /**
     * 获取所有行政区域
     * @return
     */
    //    @FormUrlEncoded
    @POST("/api/user/getAllRegions")
    public Observable<BaseResponse<AllRegionInfoList>>  getAllRegions();

    /**
     * 创建会员商品订单
     * @return
     */
    //    @FormUrlEncoded
    @POST("/api/order/createOrderForVip")
    public Observable<BaseResponse<ResponseOrderInfo>>   createOrderForVip(
            @Body RequestCreateOrderBean requestBean);


    /**
     * 取消订单(会员商品)
     * @return
     */
    //    @FormUrlEncoded
    @POST("/api/order/cancelOrder")
    public Observable<BaseResponse<Boolean>>   cancelOrder(
            @Body RequestOrderDetailBean requestBean);

    /**
     * 订单详情(会员商品)
     * @return
     */
    //    @FormUrlEncoded
    @POST("/api/order/orderDetail")
    public Observable<BaseResponse<OrderDetailInfo>>   getOrderDetail(
            @Body RequestOrderDetailBean requestBean);

    /**
     * 重新支付(会员商品)
     * @return
     */
    //    @FormUrlEncoded
    @POST("/api/order/payAgain")
    public Observable<BaseResponse<ResponseOrderInfo>>   rePay(
            @Body RequestOrderDetailBean requestBean);

    /**
     * 重新支付(会员商品)
     * @return
     */
    //    @FormUrlEncoded
    @POST("/api/order/syncPayResult")
    public Observable<BaseResponse<OrderSyncResult>>   syncPayResult(
            @Body RequestSyncPayResultResultBean requestBean);
    /**
     * 确认收货（会员商品）
     * @return
     */
    //    @FormUrlEncoded
    @POST("/api/order/confirmOrder")
    public Observable<BaseResponse<Integer>>   confirmOrderGoods(
            @Body RequestOrderDetailBean requestBean);

    /**
     * 生成淘口令
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/goods/getGenerate")
    public Observable<BaseResponse<TKLBean>> getTKL(
            @Body RequestTKLBean requestBean);

    /**
     * 生成拼多多的分享
     * @param requestBean
     * @return
     */
    @POST("/api/goods/getGenerateForPDD")
    public Observable<BaseResponse<PddShareContent>> getGenerateForPDD(
            @Body RequestPddShareContent requestBean);
    /**
     * 生成京东推广链接
     *
     * @return
     */
    @POST("/api/goods/program/generatePromotionUrl")
    public Observable<BaseResponse<TKLBean>> getPromotionUrl(
            @Body RequestTKLBean requestBean);

    /**
     * 获取优惠卷地址
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/goods/getCouponUrl")
    public Observable<BaseResponse<CouponUrlBean>> getCouponUrl(
            @Body RequestCouponUrlBean requestBean);
//            @Field("itemSourceId") String itemSourceId,
//            @Field("couponUrl") String couponUrl );


    /**
     * 批量解析商品详情图片(定时定量)
     *
     * @return
     */
    @POST("/api/goods/uploadItemDetailPictures")
    public Observable<BaseResponse<Boolean>> putDetailPictures(
            @Body RequestBody route);


    /**
     * 弹窗配置
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/system/getPopup")
    public Observable<BaseResponse<List<ImageInfo>>> getPopup(
            @Body RequestPopupBean requestBean);
//            @Field("itemSourceId") String itemSourceId,
//            @Field("couponUrl") String couponUrl,
//            @Field("pid") String pid);


    /**
     * 获取用户信息
     *
     * @return
     */
    @POST("/api/user/getUserInfo")
    public Observable<BaseResponse<UserInfo>> getUserInfo();

//    /**
//     * 获取用户信息
//     *
//     * @return
//     */
//    @POST("/api/openapi/user/agent/upgradeUser")
//    public Observable<BaseResponse<UpdateInfoBean>> upgradeUser();


    /**
     * 修改支付宝
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/user/updateAlipay")
    public Observable<BaseResponse<String>> getUserSetAlipay(
            @Body RequestSetAlipayBean requestBean);
//            @Field("alipayNumber") String alipayNumber,
//            @Field("realName") String realName,
//            @Field("verCode") String verCode,
//            @Field("sign") String sign);

    /**
     * 我的账单列表
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/user/userScore/getUsersVaultList")
    public Observable<BaseResponse<List<AgentDetailList>>> getUsersVaultList(
            @Body RequestVaultListBean requestBean);
//            @Field("page") int page);

    /**
     * 查询订单列表
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/order/getOrderList")
    public Observable<BaseResponse<List<ConsComGoodsInfo>>> getGoodsOrder(
            @Body RequestGoodsOrderBean requestBean);
//            @Field("orderStatus") int orderStatus,
//            @Field("page") int page);

    /**
     * 搜索订单列表
     *
     * @param
     * @return
     */
    @POST("/api/order/getOrderByOrderNo")
    Observable<BaseResponse<List<ConsComGoodsInfo>>> searchGoodsOrder(
            @Body RequestSearchOrderBean requestBean
            //            @Field("page") int page
    );

    /**
     * @param picture 反馈图片地址(多张图片请使用，隔开)
     * @param message 反馈文本
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/user/feeback/app/add")
    public Observable<BaseResponse<String>> getSubmitAppFeedBack(
            @Body RequestAppFeedBackBean requestBean);
//            @Field("pictrue") String pictrue,
//            @Field("message") String message,
//            @Field("goodsId") String goodsId,
//            @Field("type") String type,
//            @Field("os") int os,
//            @Field("sign") String sign);

    /**
     * 拼多多的推广短链接。
     * @param requestBean
     * @return
     */
    @POST("/api/goods/program/generatePromotionUrl")
    public Observable<BaseResponse<String>> generatePromotionUrlForPdd(
            @Body RequestPromotionUrlBean requestBean );
    /**
     * 反馈标题
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/user/feeback/type")
    public Observable<BaseResponse<String>> getFeedBackType(
            @Body RequestFeedBackTypeBean requestBean);
//            @Field("type") int type);


    /**
     * 获取验证码
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/user/sendCode")
    public Observable<BaseResponse<String>> sendAuthCode(
            @Body RequestAuthCodeBean requestBean);
//            @Field("phone") String phone,
//            @Field("type") int type);

    /**
     * 验证手机号码
     *
     * @return
     */
    @POST("/api/user/checkPhone")
    public Observable<BaseResponse<String>> checkoutPhone(
            @Body RequestCheckOutPhoneBean requestBean);

    /**
     * 获取头像昵称
     *
     * @return
     */
    @POST("/api/user/getUserSimpleInfo")
    public Observable<BaseResponse<InviteUserInfoBean>> getInviteUserInfo(
            @Body RequestUserInfoBean requestBean);

    /**
     * 密码登录
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/user/login/password")
    public Observable<BaseResponse<UserInfo>> getLogin(
            @Body RequestLoginBean requestBean);
//            @Field("phone") String phone,
//            @Field("password") String password,
//            @Field("sign") String sign);

    /**
     * 用户验证码登录
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/user/login/vercode")
    public Observable<BaseResponse<UserInfo>> getLoginCode(
            @Body RequestLoginCodeBean requestBean);
//            @Field("phone") String phone,
//            @Field("verCode") String verCode,
//            @Field("sign") String sign);


    /**
     * 注册后设置密码
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/user/setPasswd")
    public Observable<BaseResponse<String>> getRegistrationPassword(
            @Body RequestSetPasswordBean requestBean
//            @Field("password") String password,
//            @Field("sign") String sign
    );

    /**
     * 修改密码
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/user/updatePass")
    public Observable<BaseResponse<String>> modifyPassword(
            @Body RequestUpPasswordBean requestBean
//            @Field("phone") String phone,
//            @Field("password") String password,
//            @Field("verCode") String verCode,
//            @Field("sign") String sign
    );


    /**
     * 注册登录
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/user/register")
    public Observable<BaseResponse<UserInfo>> getRegisterlogin(
            @Body RequestRegisterloginBean requestBean
//            @Field("phone") String back,
//            @Field("verCode") String verCode,
//            @Field("yqmCodeOrPhone") String yqmCodeOrPhone,
//            @Field("sign") String sign
    );

    /**
     * 微信登录
     *
     * @return
     */
    @POST("/api/user/login/wechat")
    public Observable<BaseResponse<UserInfo>> getWeixiLogin(
            @Body RequestWeixiLoginBean requestBean
//            @Field("oauthWx") String oauthWx,
//            @Field("sign") String sign
    );

    /**
     * 超级分类
     *
     * @return
     */
    @POST("/api/goods/getCategory")
    public Observable<BaseResponse<List<GoodCategoryInfo>>> getHomeSuerListData(
    );

    /**
     * 收藏列表
     *
     * @return http://rap2api.taobao.org/api/collection/getCollectionList
     */
//    @FormUrlEncoded
    @POST("/api/user/collection/getCollectionList")
    public Observable<BaseResponse<List<ShopGoodInfo>>> getCollectData(
            @Body RequestCollectionListBean requestBean
//            @Field("page") int page
    );


    /**
     * 获取发圈数据
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/system/sharerange/page")
    public Observable<BaseResponse<List<MarkermallCircleInfo>>> getMarkermallCircle(
            @Body RequestMarkermallCircleBean requestBean
//            @Field("type") int type,
//            @Field("page") int page
    );

    /**
     * 获取个人素材
     *
     * @return
     */
    @POST("/api/system/sharerange/getMaterial")
    public Observable<BaseResponse<List<MarkermallCircleInfo>>> getMaterial(
            @Body RequestMarkermallCircleBean requestBean
    );

    /**
     * 多点优选圈列表 分享+1
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/system/sharerange/incrCount")
    public Observable<BaseResponse> getMarkermallShareCount(
            @Body RequestCircleShareCountBean requestBean
//            @Field("id") int id,
//            @Field("sign") String sign
    );

    /**
     * 系统通知
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/system/selectNoticeListPage")

    public Observable<BaseResponse<List<EarningsMsg>>> systemNotice(
            @Body RequestSystemNoticeBean requestBean
//            @Field("type") int type,
//            @Field("page") int page
    );


    /**
     * 每日爆款
     *
     * @return
     */
    @POST("/api/system/everydayHotCommodityList")
    public Observable<BaseResponse<List<DayHotBean>>> dayHotMsg();


    /**
     * 修改头像
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/user/updateUserInfo")

    public Observable<BaseResponse<String>> setHead(
            @Body RequestUpdateHeadPortraitBean requestBean
//            @Field("headImg") String headImg,
//            @Field("sign") String sign
    );

    /**
     * 修改name
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/user/updateUserInfo")
    public Observable<BaseResponse<String>> setNickname(
            @Body RequestNickNameBean requestBean
//            @Field("nickname") String nickName,
//            @Field("sign") String sign
    );

    /**
     * 修改专属微信
     *
     * @return
     */
    @POST("/api/user/updateUserInfo")
    public Observable<BaseResponse<String>> setWechatCode(
            @Body RequestWechatCodeBean requestBean
//            @Field("wxQrCode") String wxQrCode,
//            @Field("wxNumber") String wxNumber,
//            @Field("sign") String sign
    );

    /**
     * 绑定微信
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/user/setWechat")

    public Observable<BaseResponse<String>> bindWx(
            @Body RequestBindWxBean requestBean
//            @Field("verCode") String verCode,
//            @Field("oauthWx") String oauthWx,
//            @Field("nickname") String nickName,
//            @Field("sex") String sex,
//            @Field("headImg") String headImg,
//            @Field("country") String country,
//            @Field("province") String province,
//            @Field("city") String city,
//            @Field("sign") String sign
    );


    /**
     * 修改用户登录手机号 - 检验
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/user/checkNewPhone")
    public Observable<BaseResponse<String>> checkPhoneNum(
            @Body RequestCheckPhoneNumBean requestBean
//            @Field("phone") String phone,
//            @Field("realName") String realName,
//            @Field("password") String password,
//            @Field("sign") String sign

    );


    /**
     * 修改手机号
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/user/updateNewPhone")

    public Observable<BaseResponse<String>> bindNewPhoneNum(
            @Body RequestUpdateNewPhoneBean requestBean
//            @Field("phone") String phone,
//            @Field("verCode") String verCode,
//            @Field("check") String check,
//            @Field("sign") String sign

    );


    /**
     * 绑定微信
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/user/wechatRegister")

    public Observable<BaseResponse<UserInfo>> registerAndBindWeixin(
            @Body RequestRegisterAndBindWeChatBean requestBean
//            @Field("phone") String phone,
//            @Field("verCode") String verCode,
//            @Field("oauthWx") String oauthWx,
//            @Field("yqmCodeOrPhone") String yqmCodeOrPhone,
//            @Field("nickname") String nickname,
//            @Field("sex") String sex,
//            @Field("headImg") String headImg,
//            @Field("country") String country,
//            @Field("province") String province,
//            @Field("city") String city,
//            @Field("sign") String sign

    );

//>>>>>>>>>>>>>>>>>>>>>>>>>>>


    /**
     * 版本升级
     *
     * @return
     */
    //    @FormUrlEncoded
    @POST("/api/system/appversion/newone")
    Observable<BaseResponse<AppUpgradeInfo>> getAppVersion(
            //            @Field("os") int os
            @Body RequestOsBean os
    );

    /**
     * 灰度升级
     *
     * @return
     */
    @POST("/api/system/grayscale/version")
    Observable<BaseResponse<GrayUpgradeInfo>> getGrayAppVersion(
            @Body RequestOsBean os
    );


    /**
     * 提现
     *
     * @return
     */
    //    @FormUrlEncoded
    @POST("/api/user/userWithdrawApply")
    Observable<BaseResponse<String>> getWithdraw(
            //            @Field("amount") String amount,
            //            @Field("sign") String sign

            @Body RequestWithdrawBean requestWithdrawBean
    );

    /**
     * 检测是否存在提现记录
     *
     * @return
     */
    //    @FormUrlEncoded
    @POST("/api/user/checkWithdrawalRecord")
    Observable<BaseResponse<CheckWithDrawBean>> checkWithdrawalRecord(
            //            @Field("amount") String amount,
            //            @Field("sign") String sign

            @Body RequestCheckWithdrawBean requestWithdrawBean
    );

    /**
     * 官方推荐
     *
     * @return
     */
    //    @FormUrlEncoded
    @POST("/api/goods/getOfficialRecommendation")
    Observable<BaseResponse<List<ShopGoodInfo>>> getOfficalList(
            //            @Field("type") int type,
            //            @Field("page") int pageNum,
            //            @Field("keyword") String keyword

            @Body RequestOfficialRecommend requestOfficialRecommend
    );


    /**
     * 获取商品二维码链接  多商品分享链接
     *
     * @return
     */
    //    @FormUrlEncoded
    @POST("/api/goods/share/getItemLinks")
    Observable<BaseResponse<ShareUrlListBaen>> getShareGoodsUrlListObservable(
            @Body RequestShareGoods requestShareGoods
    );


    /**
     * 获取商品二维码链接  多商品分享链接
     *
     * @return
     */
    //    @FormUrlEncoded
    @POST("/api/goods/share/getItemLinks")
    Observable<BaseResponse<ShareUrlStringBaen>> getShareGoodsUrlStringObservable(
            @Body RequestShareGoods requestShareGoods
    );

    /**
     * 分享好友下载链接 / 推荐通讯录好友
     *
     * @return
     */
    @POST("/api/goods/share/getDownloadLink")
    Observable<BaseResponse<ShareUrlBaen>> getAppShareUrl();

    /**
     * 官方公告
     */
    @POST("/api/system/getComponentConfigForKey")
    Observable<BaseResponse<List<OfficialNotice>>> getOfficialNotice(

            @Body RequestOfficialNoticeBean requestOfficialNoticeBean
    );

    /**
     * 获取系统配置
     */
    //    @FormUrlEncoded
    @POST("/api/system/getConfigForKey")
    Observable<BaseResponse<HotKeywords>> getConfigForKey(
            //            @Field("key") String key

            @Body RequestKeyBean requestKeyBean
    );

    /**
     * 获取html页面链接
     */
    //    @FormUrlEncoded
    @POST("/api/system/systemStaticPage")
    Observable<BaseResponse<List<ProtocolRuleBean>>> getSystemStaticPage(
            //            @Field("scope") int scope,
            //            @Field("page") int page

            @Body RequestSystemStaticPage requestSystemStaticPage
    );

    /**
     * 发送MQ异步通知订单/用户信息做缓存
     *
     * @param
     * @return
     */
    @POST("/api/user/heart")
    Observable<BaseResponse> putHeart();


    /**
     * 批量移除收藏数据
     *
     * @return
     */
    //    @FormUrlEncoded
    @POST("/api/user/collection/delUserCollection")
    Observable<BaseResponse<String>> delUserCollection(
            //            @Field("ids") String ids,
            //            @Field("sign") String sign

            @Body RequestDeleteCollection requestDeleteCollection
    );


    /**
     * 解析淘口令
     *
     * @return
     */
    //    @FormUrlEncoded
    @POST("/api/goods/analysisGoodsTkl/")
    Observable<BaseResponse<ShopGoodInfo>> getAnalysis(
            //            @Field("tkl") String tkl

            @Body RequestAnalysisTKL requestAnalysisTKL
    );


    /**
     * 申请升级 (ok)
     *
     * @return
     */
    @POST("/api/user/userApplyUpgrade")
    Observable<BaseResponse<ApplyUpgradeBean>> applyUpgradeData();

    /**
     * 确认升级
     *
     * @return
     */
    //    @FormUrlEncoded
    @POST("/api/user/confirmUpgrade")
    Observable<BaseResponse<String>> confirmUpgradeData(
            //            @Field("isApply") String isApply,
            //            @Field("sign") String sign

            @Body RequestConfirmUpgradeData requestConfirmUpgradeData
    );

    /**
     * 赚钱三部曲 (ok)
     *
     * @param
     * @return
     */
    @POST("/api/user/getMakeMoneyRule")
    Observable<BaseResponse<MakeMoenyBean>> getMakeMoneyRule();


    /**
     * 1.实时排行 / 2.今日排行
     *
     * @return
     */
    //    @FormUrlEncoded
    @POST("/api/goods/ranking")
    Observable<BaseResponse<List<ShopGoodInfo>>> getRankings(
            //            @Field("type") String type,
            //            @Field("page") String page,
            //            @Field("order") String order,
            //            @Field("sort") String sort

            @Body RequestGetRankings requestGetRankings
    );


    /**
     * 品牌列表 品牌特卖
     *
     * @return
     */
    //    @FormUrlEncoded
    @POST("/api/goods/getBrandList")
    Observable<BaseResponse<List<BrandSell>>> getBrandsList(
            @Body RequestPage requestPage
    );


    /**
     * 品牌推荐
     *
     * @return
     */
    //    @FormUrlEncoded
    @POST("/api/goods/getBrandItems")
    Observable<BaseResponse<List<BrandSell>>> getBrandSellGoodsList(
            //            @Field("page") int page

            @Body RequestPage requestPage
    );
    /**
     * 会员商品
     *
     * @return
     */
    //    @FormUrlEncoded
    @POST("/api/goods/listGoodsFromLitemall")
    Observable<BaseResponse<NumberGoodsList>> getNumberGoodsList(
            //            @Field("page") int page
            @Body RequestNumberGoodsList requestPage
    );

    /**
     * 通过品牌ID获取商品列表
     *
     * @param brandId 品牌ID
     * @param page
     * @return
     */
    //    @FormUrlEncoded
    @POST("/api/goods/getBrandItemList")
    Observable<BaseResponse<List<ShopGoodInfo>>> getGoodsByBrand(
            //            @Field("brandId") String brandId,
            //            @Field("page") String page,
            //            @Field("order") String order,
            //            @Field("sort") String sort

            @Body RequestGetGoodsByBrand requestGetGoodsByBrand
    );

    /**
     * 大部分列表 限时抢购
     *
     * @param
     * @return
     */
    //    @FormUrlEncoded
    @POST("/api/goods/getTimedSpikeList")
    Observable<BaseResponse<List<ShopGoodInfo>>> getTimedSpikeList(
            //            @Field("hourType") String hourType,
            //            @Field("page") int page

            @Body RequestGetTimedSpikeList requestGetTimedSpikeList
    );

    /**
     * 精选活动
     *
     * @param
     * @return
     */
    @POST("/api/system/getActivity")
    Observable<BaseResponse<List<HandpickBean>>> getActivities();

    /**
     * 精选活动商品列表
     *
     * @return
     */
    //    @FormUrlEncoded
    @POST("/api/system/getActivityList")
    Observable<BaseResponse<List<ShopGoodInfo>>> getActivitiesDetails(
            //            @Field("page") int page,
            //            @Field("order") String order,
            //            @Field("sort") String sort,
            //            @Field("activityId") String activityId

            @Body RequestGetActivitiesDetails requestGetActivitiesDetails
    );


    /**
     * 三级分类
     *
     * @return
     */
    //    @FormUrlEncoded
    @POST("/api/goods/getGoodsListByCategoryId")
    Observable<BaseResponse<List<ShopGoodInfo>>> getThreeGoods(
            //            @Field("type") String type,
            //            @Field("categoryId") String categoryId,
            //            @Field("keyword") String keyword,
            //            @Field("page") String page,
            //            @Field("sort") String sort,
            //            @Field("order") String order

            @Body RequestThreeGoodsClassify requestThreeGoodsClassify);

    /**
     * 九块九包邮
     *
     * @return
     */
    //    @FormUrlEncoded
    @POST("/api/goods/getFreeShipping")
    Observable<BaseResponse<List<ShopGoodInfo>>> getNinepinkageGoods(
            //            @Field("page") String page,
            //            @Field("sort") String sort,
            //            @Field("order") String order

            @Body RequestGetNinepinkageGoods requestGetNinepinkageGoods
    );

    /**
     * 商家同店商品
     *
     * @return
     */
    //    @FormUrlEncoded
    @POST("/api/goods/getIdentical")
    Observable<BaseResponse<List<ShopGoodInfo>>> getIdentical(
            //            @Field("shopId") String shopId
            @Body RequestShopId requestShopId
    );

    /**
     * 超值大牌活动商品列表
     *
     * @return
     */
    @POST("/api/marketing/getGreatValueList")
    Observable<BaseResponse<List<ShopGoodInfo>>> getGreatValueList(
            @Body RequestByGoodList requestShopId
    );

    /**
     * 第三方活动-预告单
     *
     * @return
     */
    @POST("/api/marketing/handanku")
    Observable<BaseResponse<List<ShopGoodInfo>>> getHandanku(
            @Body RequestByGoodList requestShopId
    );

    /**
     * 第三方活动-预告单
     *
     * @return
     */
    @POST("/api/goods/listItemsGuessLike")
    Observable<BaseResponse<List<ShopGoodInfo>>> getWhatLike(
            @Body RequestWhatLike requestShopId
    );

    /**
     * 会场，一元购，五折，精选爆款
     *
     * @return
     */
    @POST("/api/goods/tlj/material/app")
    Observable<BaseResponse<List<ShopGoodInfo>>> getGoodsMaterial(
            @Body RequestMaterial requestShopId
    );


    /**
     * 获取oss秘钥
     *
     * @return
     */
    @POST("/api/system/oss/getKey")
    Observable<BaseResponse<OssKeyBean>> getOssKey();

    /**
     * 获取oss秘钥
     *
     * @return
     */
    //    @FormUrlEncoded
    @POST("/api/user/collection/judgeUserIfCollectioin")
    Observable<BaseResponse<String>> judgeUserIfCollectioin(
            //            @Field("itemSourceId") String itemSourceId

            @Body RequestItemSourceId requestItemSourceId
    );

    /**
     * 获取oss秘钥
     *
     * @return
     */
    @POST("api/system/time/getSystemTime")
    Observable<BaseResponse<Long>> getServerTime();



    /**
     * 商品详情信息
     *
     * @return
     */
    //    @FormUrlEncoded
    @POST("/api/goods/getItemDetails")
    Observable<BaseResponse<ShopGoodInfo>> getDetailData(
            //            @Field("itemSourceId") String itemSourceId

            @Body RequestItemSourceId requestItemSourceId
    );

    /**
     * 上传优惠券json
     *
     * @return
     */
    //    @FormUrlEncoded
    @POST("/api/goods/uploadCouponInfo")
    Observable<BaseResponse<CheckCouponStatusBean>> uploadCouponInfo(
            //            @Field("itemSourceId") String itemSourceId,
            //            @Field("couponJson") String couponJson,
            //            @Field("count") int count,
            //            @Field("beParsed") int beParsed

            @Body RequestUploadCouponInfo requestUploadCouponInfo
    );

    /**
     * 获取团队管理微信信息
     *
     * @param acition
     * @return
     */
    @POST("/api/user/findWechatInfo")
    public Observable<BaseResponse<AccountDestroy>> getAccountDestroyHint();

    /**
     * 注销用户提示
     *
     * @param acition
     * @return
     */
    @POST("/api/user/logout/rtnInfo")
    public Observable<BaseResponse<HotKeywords>> getRtnInfo();

    /**
     * 注销用户
     *
     * @param
     * @return
     */
    @POST("/api/user/logout")
    public Observable<BaseResponse<String>> getAccountDestroy(
            @Body RequestLoginCodeBean bean
    );

    /**
     * 修改专属微信
     *
     * @return
     */
    @POST("/api/user/saveWechatInfo")
    public Observable<BaseResponse<String>> saveWechatInfo(
            @Body RequestWechatCodeBean requestBean
    );


    /**
     * 获取商学院学习等级模块
     *
     * @return
     */
    @POST("/api/system/commercial/module")
    public Observable<BaseResponse<List<StudyRank>>> getStudyRank();

    /**
     * 获取商学院学模块文章列表
     *
     * @return
     */
    @POST("/api/system/commercial/getModuleArticleList")
    public Observable<BaseResponse<List<Article>>> getArticleList(
            @Body RequestTwoLevel requestTwoLevel);

    /**
     * 搜索商学院学模块文章列表
     *
     * @return
     */
    @POST("/api/system/commercial/search")
    public Observable<BaseResponse<List<Article>>> searchArticleList(
            @Body SearchArticleBody body);

    /**
     * 获取商学院推荐文章列表
     *
     * @return
     */
    @POST("/api/system/commercial/recommendList")
    public Observable<BaseResponse<List<Article>>> getRecommendArticleList(@Body RequestListBody body);


    /**
     * 轮播图展示（商学院广告）
     *
     * @param
     * @return
     */
    @POST("/api/system/commercial/rotationChart")
    public Observable<BaseResponse<List<ImageInfo>>> getCommercialBanner();


    /**
     * 查询订单列表(多点优选生活的订单)
     *
     * @param acition
     * @return
     */
    @POST("/api/order/life/getOrderList")
    public Observable<BaseResponse<List<ConsComGoodsInfo>>> getGoodsLiveOrder(@Body RequestGoodsOrderBean requestBean);


    /**
     * 我的团队 获取团队长信息
     *
     * @param action
     * @return
     */
    @POST("/api/user/getQrCode")
    public Observable<BaseResponse<TeamInfo>> getServiceQrcode(
            @Body RequestInviteCodeBean inviteCode);

    /**
     * 获取消息(收益,粉丝,系统)
     *
     * @param action
     * @return
     */
    @POST("/api/system/systemNotice")
    public Observable<BaseResponse<List<EarningsMsg>>> getMsg(

    );


    /**
     * 显示未读数
     *
     * @param action
     * @return
     */
    @POST("/api/system/countNoticeByReadStatus")
    public Observable<BaseResponse<UserscoreBean>> getNoreadCount(
    );

    /**
     * 标记已读
     *
     * @param
     * @param
     */
    @POST("/api/system/updateBatchNoticeStatus")
    public Observable<BaseResponse<String>> setReadNotice(
            @Body RequestBaseTypeBean bean
    );

    /**
     *
     */
    @POST("/api/system/updateNoticeStatus")
    public Observable<BaseResponse<String>> updateNoticeStatus(
            @Body RequestCircleShareCountBean bean
    );

    /**
     * 商学院轮播图
     */
    @POST("/api/system/commercial/rotationChart")
    public Observable<BaseResponse<String>> getBanner();


    /**
     * app闪屏页点击统计
     *
     * @param acition
     * @return
     */
    @POST("/api/system/clickStatistics")
    public Observable<BaseResponse> statisticsStartAdOnclick(@Body RequestSplashStatistics requestSplashStatistics);

    /**
     * vip个人中心
     *
     * @param requestBean
     * @return
     */
    @POST("/api/user/userApplyToUpgrade/list")
    public Observable<BaseResponse<VipUseInfoBean>> getVipUserInfo();

    /**
     * vip个人中心
     *
     * @return
     */
    @POST("/api/user/userApplyToUpgrade/up")
    public Observable<BaseResponse<String>> upgradeVip();

    /**
     * 用户升级说明
     *
     * @return
     */
    @POST("/api/user/UpgradeInstructions")
    public Observable<BaseResponse<String>> upgradeInstructions(
            @Body RequestGradeUrlBean bean);

    /**
     * 用户升级说明(2019-03-18) 弹框
     *
     * @return
     */
    @POST("/api/system/getMakeMoneyPlanPopup")
    public Observable<BaseResponse<UpgradeInstructions>> upgradeInstructions2(
            @Body RequestGradeBean bean);

    /**
     * 获取后台appkey
     *
     * @return
     */
    @POST("/api/user/getAllianceAppKey")
    public Observable<BaseResponse<String>> getAllianceAppKey(
            @Body RequestBaseBean bean);


    /**
     * 传code给后台
     *
     * @return
     */
    @POST("/api/user/bandingAliRelation")
    public Observable<BaseResponse<BandingAliRelationBean>> senALiCode(
            @Body RequestALiCodeBean bean);

    /**
     * 上传错误日志
     *
     * @return
     */
    @POST("/api/user/getErroLogInfo")
    public Observable<BaseResponse<String>> sendMyServerLog(
            @Body RequestPutErrorBean bean);

    /**
     * 获取活动生成链接
     *
     * @return
     */
    @POST("/api/marketing/official/getGenerateActivityLink")
    public Observable<BaseResponse<TmallActivityBean>> getGenerateActivityLink(
            @Body RequestTmallActivityLinkBean bean);

    /**
     * 查找佣金
     *
     * @return
     */
    @POST("/api/marketing/official/getItemDetails")
    public Observable<BaseResponse<ShopGoodInfo>> getItemByItemSourceId(
            @Body ByItemSourceIdBean bean);

    /**
     * 开/关 粉丝推送
     *
     * @return
     */
//    @POST("/api/manage/push/generalPush/fansJpush")
    @POST("/api/user/labelAffiliation/pushMessage/edit")
    Observable<BaseResponse<String>> editJpush(
            @Body RequestPushBean bean);

//    /**
//     * 开/关 收益推送
//     * @return
//     */
//    @POST("/api/manage/push/generalPush/earningsJpush")
//    Observable<BaseResponse<String>> earningsJpush(
//            @Body RequestPushBean bean );

    /**
     * 获取推送消息开关
     *
     * @return
     */
//    @POST("/api/manage/businessConfig/laserPushSetting/phonePushSystem")
    @POST("/api/user/labelAffiliation/pushMessage/list")
    Observable<BaseResponse<PushSwitchBean>> getPushSwitch();

    /**
     * vip页面, 三条课程
     *
     * @return
     */
    @POST("/api/system/commercial/myCurriculum")
    Observable<BaseResponse<List<ClassroomBean>>> myCurriculum();

    /**
     * vip页面,浏览视频
     *
     * @return
     */
    @POST("/api/system/commercial/updateVideoClicks")
    Observable<BaseResponse<String>> mp4Browse(
            @Body RequestVideoId bean);


    /**
     * 获取二级分类下的文章视频列表
     *
     * @return
     */
    @POST("/api/system/commercial/getContentList")
    public Observable<BaseResponse<List<Article>>> getTutorialData(@Body RequestTwoLevel requestTwoLevel);

    /**
     * 获取商学院 模块下面的所有二级分类
     *
     * @return
     */
    @POST("/api/system/commercial/getTwoLevel")
    Observable<BaseResponse<List<RequestModelIdData>>> getCollegeListData(@Body RequestModelId requestModelId);


    @POST("/api/user/checkWithdrawTime")
    public Observable<BaseResponse<String>> checkWithdrawTime();

    Observable<BaseResponse<List<Article>>> getCollegeListData();

    /**
     * 获取系统提示消息
     *
     * @return
     */
    @POST("/api/system/getMainPageNotice")
    Observable<BaseResponse<List<SysNoticeBean>>> getImportantNotice();

    /**
     * 获取新手教程模块下面的所有二级分类
     *
     * @return
     */
    @POST("/api/system/commercial/more")
    Observable<BaseResponse<List<RequestModelIdData>>> getNewCollegeListData();

    /**
     * 获取新手教程模块下面的所有二级分类
     *
     * @return
     */
    @POST("/api/system/listBottomNotification")
    Observable<BaseResponse<List<ImageInfo>>> getSysNotification();


    /**
     * 搜索统计
     *
     * @return
     */
    @POST("/api/system/searchStatistics")
    Observable<BaseResponse<String>> searchStatistics(@Body RequestSearchStatistics requestBody);

    /**
     * 首页多点优选热门搜索统计
     *
     * @return
     */
    @POST("/api/system/clickStatistics")
    Observable<BaseResponse<String>> clickStatistics(@Body RequestSearchStatistics requestBody);


    /**
     * 订单列表查询商品是否存在
     *
     * @return
     */
    @POST("/api/goods/getItemDetailByItemSourceId")
    public Observable<BaseResponse<ShopGoodInfo>> onCheckGoods(@Body RequestCheckGoodsBean requestBody);




    /**
     * 查询粉丝信息
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/user/userScore/findUserById")
    public Observable<BaseResponse<FansInfo>> getFansDetail(
            @Body RequestFansInfoBean requestBody);

    /**
     * 搜索团队粉丝
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/user/userScore/searchUser")
    public Observable<BaseResponse<List<TeamInfo>>> searchUser(
            @Body RequestFansInfoBean requestBody);

    /**
     * 联系客服地址
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/user/getSystemCustomService")
    public Observable<BaseResponse<String>> getCustomService();

    /**
     * 用户反馈回复消息
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/user/feeback/reply/list")

    public Observable<BaseResponse<List<EarningsMsg>>> getFeedbackMsg(
            @Body RequestListBody requestBean
//            @Field("type") int type,
//            @Field("page") int page
    );

    /**
     * 获取发布管理列表数据
     *
     * @return
     */
    @POST("/api/system/sharerange/getReleaseManage")
    public Observable<BaseResponse<List<ReleaseManage>>> getReleaseManageList(
            @Body RequestReleaseManage body);


    /**
     * 发圈商品专题列表接口
     *
     * @return
     */
    @POST("/api/system/sharerange/getShareRangCategory")
    public Observable<BaseResponse<List<CategoryListDtos>>> getShareRangCategory(@Body RequestReleaseCategory body);

    /**
     * 发圈商品专题提交审核接口
     *
     * @return
     */
    @POST("/api/system/sharerange/save/user/application")
    public Observable<BaseResponse<String>> submitReleaseGoods(@Body RequestReleaseGoods releaseGoods);

    /**
     * 获取商品发布管理列表
     *
     * @return
     */
    @POST("/api/system/commercial/recommendList")
    public Observable<BaseResponse<List<Article>>> getRecommendMoreList(@Body RequestListBody body);

    /**
     * 发布的商品删除
     *
     * @return
     */
    @POST("/api/system/sharerange/del/release")
    public Observable<BaseResponse<String>> getReleaseGoodsDelete(@Body RequestReleaseGoodsDelete body);

    /**
     * 检查商品是否有权限发布
     *
     * @return
     */
    @POST("/api/system/sharerange/getReleaseAddPermissions")
    public Observable<BaseResponse<ReleaseGoodsPermission>> checkPermission(@Body RequestReleaseGoods body);

    /**
     * 检查商品是否有权限发布
     *
     * @return
     */
    @POST("/api/goods/tlj/sendUrl")
    public Observable<BaseResponse<String>> materialLinkList(
            @Body RequestMaterialLink body
    );

    @POST("/api/system/graphicInfo/sorting/linkList")
    Observable<BaseResponse<List<String>>> getTaobaoLink();


    /**
     * 获取地区
     *
     * @return
     */
    @POST("/api/user/getCountryList")
    public Observable<BaseResponse<List<AreaCodeBean>>> getCountryList();

    /**
     * 获取系统配置
     */
    //    @FormUrlEncoded
    @POST("/api/user/getSystemCustomService")
    Observable<BaseResponse<String>> getSystemCustomService();

    /**
     * 商品详情页下的猜你喜欢
     */
    @POST("/api/goods/listNewItemsGuessLike")
    Observable<BaseResponse<List<ShopGoodInfo>>> getRecommendItemsById(
            @Body RequestGoodsLike body
    );

    /**
     * 爆款排行_类目
     */
    @POST("/api/goods/ranking/category")
    Observable<BaseResponse<List<RankingTitleBean>>> getCategory(
    );

    /**
     * 爆款排行_新接口
     */
    @POST("/api/goods/ranking/new")
    Observable<BaseResponse<List<ShopGoodInfo>>> getRankingNews(
            @Body RankingTitleBean body
    );


    /**
     * 京东、拼多多商品列表_新接口
     */
    @POST("/api/goods/program/getItemByCatId")
    Observable<BaseResponse<List<ShopGoodInfo>>> getJdPddGoodsList(
            @Body ProgramCatItemBean body
    );

    /**
     * 京东、拼多多的商品详情_新接口
     */
    @POST("/api/goods/program/goodsDetail")
    Observable<BaseResponse<ShopGoodInfo>> getJdPddGoodsDetail(
            @Body ProgramGetGoodsDetailBean body
    );



    /**
     * 京东、拼多多的搜索商品列表_新接口
     */
    @POST("/api/goods/program/searchKeyword")
    Observable<BaseResponse<List<ProgramItem>>> getSearchKeywordForJdPdd(
            @Body ProgramSearchKeywordBean body
    );

    /**
     * 根据key获取配置信息
     */
    @POST("/api/system/getConfigByKey")
    Observable<BaseResponse<SystemConfigBean>> getConfigByKey(@Body RequestConfigKeyBean body);

    /**
     * 获取公告列表
     */
    @POST("/api/system/getNoticeList")
    Observable<BaseResponse<List<ImageInfo>>> getNoticeList(@Body RequestNoticeListBean body);

    /**
     * 新精品推荐
     *
     * @return
     */
    @POST("/api/goods/getNewRecommend")
    public Observable<BaseResponse<NewRecommendBean>> getNewRecommend(
            @Body RequestRecommendBean requestBean);

    /**
     * 查询页面底部通知
     *
     * @return
     */
    @POST("/api/system/listBottomNotification")
    public Observable<BaseResponse<List<ImageInfo>>> getListBottomNotification();

    /**
     * 商学院文字走马灯
     *
     * @return
     */
    @POST("/api/system/commercial/getCarousel")
    public Observable<BaseResponse<List<MarkermallInformation>>> getCarousel();

    /**
     * 商学院反馈意见
     *
     * @param picture 反馈图片地址(多张图片请使用，隔开)
     * @param message 反馈文本
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/system/commercial/addUserFeedback")
    public Observable<BaseResponse<String>> getSubmitCircleFeedBack(
            @Body RequestCircleFeedBackBean requestBean);

    /**
     * 获取商学院预览功能
     *
     * @return
     */
    @POST("/api/system/commercial/preview")
    public Observable<BaseResponse<List<Article>>> getReViewArticleList(@Body RequestListBody body);

    /**
     * 获取搜索引导的配置
     */
    //    @FormUrlEncoded
    @POST("/api/system/searchGuide/list")
    Observable<BaseResponse<List<ImageInfo>>> getSearchGuideConfig(@Body RequestSearchGuideBean requestKeyBean
    );

    /**
     * 获取搜索关键词列表
     */
    //    @FormUrlEncoded
    @POST("/api/system/searchKeyWord/list")
    Observable<BaseResponse<List<SearchHotKeyBean>>> getSearchKey(@Body RequestSearchGuideBean requestKeyBean
    );

    /**
     * 获取今日推荐商品
     */
    @POST("/api/goods/search/today/recommend")
    Observable<BaseResponse<List<ShopGoodInfo>>> getTodayGoods();

    /**
     * 根据key获取配置信息(新）
     */
    @POST("/api/system/getConfigList")
    Observable<BaseResponse<List<SystemConfigBean>>> getConfigList(@Body RequestConfigKeyBean body);

    /**
     * 解析商品详情图片
     */
    @POST("/api/goods/getItemDetailPictureCode")
    Observable<BaseResponse<List<String>>> getItemDetailPictureCode(@Body RequestBody route);

    /**
     * 解析商品详情图片
     */
    @POST("/api/goods/parse/mtopjsonp")
    Observable<BaseResponse<List<String>>> getMtopjsonp(@Body RequestMtopJsonpBean body);

    /**
     * 收益说明
     */
    @POST("/api/system/incomeDescription/list")
    Observable<BaseResponse<EarningExplainBean>> getEarningsExplain();


    /**
     * @return
     */
    @POST("/api/user/getUpgradeCarousel")
    public Observable<BaseResponse<List<UpgradeCarousel>>> getUpgradeCarousel();

    /**
     * @return
     */
    @POST("/api/user/getIsUpgrade")
    public Observable<BaseResponse<UpgradeCarousel>> getIsUpgrade();

    /**
     * 获取商学院首页
     *
     * @return
     */
    @POST("/api/system/commercial/getPageAggregationList")
    public Observable<BaseResponse<List<CollegeHome>>> getPageAggregationList(@Body RequestListBody body);


    /**
     * 修改出生日期
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/user/updateUserInfo")
    public Observable<BaseResponse<String>> setBirthDate(
            @Body RequestNickNameBean requestBean
    );

    /**
     * 商学院首页换一换
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/system/commercial/forChange")
    public Observable<BaseResponse<List<Article>>> forChange(@Body RequestArticleBean requestBean);

    /**
     * 商学院意见返回信息
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/system/commercial/getUserFeedback")
    public Observable<BaseResponse<UserFeedback>> getUserFeedback(@Body RequestReplyBean bean);

    /**
     * 商学院首页多点优选资讯点击上报
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/system/commercial/updateThemeClicks")
    public Observable<BaseResponse<String>> updateThemeClicks(@Body RequestVideoId bean);

    /**
     * 商学院热门关键字
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/system/commercial/getSearchSet")
    public Observable<BaseResponse<List<SearchHotKeyBean>>> getSearchSet(@Body RequestCircleSearchBean RequestCircleSearchBean);


    /**
     * 发圈收藏
     *
     * @return
     */
    @POST("/api/system/sharerange/collection/addShareRangCollection")
    public Observable<BaseResponse<String>> addShareRangCollection(
            @Body RequestCircleCollectBean requestBean
    );

    /**
     * 发圈取消收藏
     *
     * @return
     */
    @POST("/api/system/sharerange/collection/deleteShareRangCollectionById")
    public Observable<BaseResponse<String>> removeShareRangCollection(
            @Body RequestRemoveCircleCollectBean requestBean
    );

    /**
     * 发圈收藏列表
     *
     * @return
     */
    @POST("/api/system/sharerange/collection/selectSystemShareRangCollectionDtoList")
    public Observable<BaseResponse<List<MarkermallCircleInfo>>> getCollectList(@Body RequestCircleBransBean requestBean);


    /**
     * 发圈搜索
     *
     * @return
     */
    @POST("/api/system/sharerange/searchSystemShareRangDtoList")
    public Observable<BaseResponse<List<MarkermallCircleInfo>>> getCollectSearchList(@Body RequestCircleBransBean requestBean);


    /**
     * 发圈猜你喜欢
     *
     * @return
     */
    @POST("/api/system/sharerange/collection/selectSystemLoveShareRangDtoList")
    public Observable<BaseResponse<List<CircleBrand>>> getCircleGuessList(@Body RequestCircleBransBean requestBean);

    /**
     * 解绑微信-V5.2.0
     *
     * @return
     */
    @POST("/api/user/untied/wechat")
    public Observable<BaseResponse<String>> untiedWechat();


    /**
     * 获取密粉圈热门搜索
     *
     * @return
     */
    @POST("/api/system/sharerange/getSearchSet")
    public Observable<BaseResponse<List<SearchHotKeyBean>>> getCircleHotKey(@Body RequestCircleSearchBean requestBean);


    /**
     * 绑定微信
     *
     * @return
     */
    @POST("/api/user/setWechatNew")
    public Observable<BaseResponse<String>> setWechatNew(
            @Body RequestBindWxBean requestBean
    );

    /**
     * 绑定微信
     *
     * @return
     */
    @POST("/api/user/userScore/updateRemark")
    public Observable<BaseResponse<String>> updateRemark(
            @Body RequestRemarkBean requestBean
    );

    /**
     * 我的粉丝动态排行
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/user/userScore/teamRanking")
    public Observable<BaseResponse<List<TeamInfo>>> getTeamRanking(
            @Body RequestTeanmListBean requestBody);

    /**
     * 用户升级
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/openapi/user/agent/upgradeUser")
    public Observable<BaseResponse<UpdateInfoBean>> updateUserGrade(
            @Body RequestUpdateUserBean requestBody);


    /**
     * 检查用户是否可购买0元购
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/user/checkUserCondition")
    public Observable<BaseResponse<String>> checkPruchase();

    /**
     * 零元购商品
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/goods/getZeroActivityGoods")
    public Observable<BaseResponse<List<ShopGoodInfo>>> getPurchaseGoods(@Body RequestPage requestBean);

    /**
     * 零元购好货商品
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/goods/getMustSeizeActivityGoods")
    public Observable<BaseResponse<List<ShopGoodInfo>>> getProductGoods();

    /**
     * 首页抖货
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/goods/listTikTokGoods")
    public Observable<BaseResponse<List<ShopGoodInfo>>> getVideo();

    /**
     * 获取楼层活动数据
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/goods/floorPicture")
    public Observable<BaseResponse<FloorBean>> getFloorPicture();



    /**
     * 获取抖货分类条目
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/goods/listTikTokTab")
    public Observable<BaseResponse<List<VideoClassBean>>> getVideoClass();


    /**
     * 获取抖货分类商品
     *
     * @return
     */
//    @FormUrlEncoded
    @POST("/api/goods/queryTikTokGoods")
    public Observable<BaseResponse<List<ShopGoodInfo>>> getVideoGoods(@Body RequestVideoGoodsBean requestBean);
}
