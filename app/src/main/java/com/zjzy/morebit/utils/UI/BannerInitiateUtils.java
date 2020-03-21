package com.zjzy.morebit.utils.UI;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.zjzy.morebit.Activity.GoodsDetailActivity;
import com.zjzy.morebit.Activity.MyMaterialActivity;
import com.zjzy.morebit.Activity.OneFragmentDefaultActivity;
import com.zjzy.morebit.Activity.SearchActivity;
import com.zjzy.morebit.Activity.ShowWebActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Activity.SinglePaneActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.circle.CollegeListActivity;
import com.zjzy.morebit.circle.ui.RecommendListActivity;
import com.zjzy.morebit.circle.ui.ReleaseManageActivity;
import com.zjzy.morebit.contact.EventBusAction;
import com.zjzy.morebit.fragment.NewcomersFragment;
import com.zjzy.morebit.fragment.OfficialRecomFragment;
import com.zjzy.morebit.fragment.PanicBuyFragment;
import com.zjzy.morebit.goods.shopping.ui.TmallWebActivity;
import com.zjzy.morebit.goods.shopping.ui.fragment.BrandListFragment;
import com.zjzy.morebit.goods.shopping.ui.fragment.BrandSellFragment;
import com.zjzy.morebit.info.ui.AppFeedActivity;
import com.zjzy.morebit.info.ui.GoodsBrowsingHistoryActivity;
import com.zjzy.morebit.info.ui.OfficialNoticeFragment;
import com.zjzy.morebit.info.ui.VipActivity;
import com.zjzy.morebit.main.ui.CollectFragment2;
import com.zjzy.morebit.main.ui.NoticeActivity;
import com.zjzy.morebit.main.ui.fragment.ForeshowFragment;
import com.zjzy.morebit.main.ui.fragment.GoodNewsFramgent;
import com.zjzy.morebit.main.ui.fragment.PddChildFragment;
import com.zjzy.morebit.main.ui.fragment.PddFragment;
import com.zjzy.morebit.main.ui.fragment.RankingFragment;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.event.OpenCategoryEvent;
import com.zjzy.morebit.pojo.goods.GoodCategoryInfo;
import com.zjzy.morebit.pojo.home.TmallActivityBean;
import com.zjzy.morebit.pojo.request.RequestSplashStatistics;
import com.zjzy.morebit.pojo.request.RequestTmallActivityLinkBean;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.GlideImageLoader;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.OpenFragmentUtils;
import com.zjzy.morebit.utils.SensorsDataUtil;
import com.zjzy.morebit.utils.StringsUtils;
import com.zjzy.morebit.utils.TaobaoUtil;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.WechatUtil;
import com.zjzy.morebit.view.AspectRatioView;
import com.zjzy.morebit.view.FliggyDialog;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;

/**
 * Created by fengrs on 2017/11/24.
 * banner 跳转
 * 位置 -- banner：获取首页轮播，poster：推广海报图，recommend：推荐栏，taobao：聚划算活动图，activity：活动内容，
 * <p>
 * qrcode ：客服联系二维码，start：启动页，guide：引导页，mistress：小蜜推荐，cashback：返现商城，popup：首页弹窗，
 * <p>
 * personal：个人轮播，welfare：福利津贴，noticebar：标题轮播，selected_activities：精选活动，brandsale：品牌特卖，taobao_v2：聚划算v2，makemoney：赚钱计划
 * <p>
 * <p>
 * <p>
 * 打开方式：1 打开商品  2 站内列表模块  3 外站链接 4 商品分类  5 返利商品  6 打开APP  7 打开店铺userid  8 打开专题列表（轮播记录ID）
 * <p>
 * <p>
 * <p>
 * 内站列表： {"1":"聚划算","2":"淘抢购","3":"9块9包邮/白菜价","4":"食天下","5":"母婴淘","6":"超级划算","7":"视频购","8":"断码清仓","9":"限时抢购","10":"免单商城","11":"官方推荐","12":"我的战报","13":"品牌特卖","14":"赚钱计划"}
 * <p>
 * <p>
 * <p>
 * 打开方式为1、3、5、6、7、8时，使用字段为url
 * <p>
 * 打开方式为2、4时，即站内模块和商品分类，使用字段为class_id
 */

public class BannerInitiateUtils {


    public static void gotoAction(Activity activity, ImageInfo imageInfo) {
        try {
            if (imageInfo == null) {
                return;
            }
            if (AppUtil.isFastClick(50)) return;
            int open = imageInfo.getOpen();
            if (open == 1) {//跳到商品详情页
                //跳转到商品详情
                if (TextUtils.isEmpty(imageInfo.getUrl())) {
                    return;
                }
                Intent intent = new Intent(activity, GoodsDetailActivity.class);
                Bundle bundle = new Bundle();
                ShopGoodInfo shopGoodInfo = new ShopGoodInfo();
                shopGoodInfo.setTitle("");
                shopGoodInfo.setTaobao(imageInfo.getUrl());  //如果是1类型，url返回的是taobao_id
                bundle.putSerializable(C.Extras.GOODSBEAN, shopGoodInfo);
                intent.putExtras(bundle);
                activity.startActivity(intent);
//                SensorsDataAPI.sharedInstance(activity).advClickTrack("",imageInfo.getId()+"");
            } else if (open == 2) {  //跳到模块列表
                gotoMenu(activity, imageInfo.getClassId(), imageInfo);  //如果是2类型，class_id就是分类ID
            } else if (open == 3) {   //跳到网页
                //跳转到网页
                showWebIntent(activity, imageInfo);
            } else if (open == 4) {   //跳到分类
                EventBus.getDefault().post(new OpenCategoryEvent());
                if (activity instanceof NoticeActivity || activity instanceof SearchActivity || activity instanceof SinglePaneActivity) {
                    activity.finish();
                }
            } else if (open == 6) {   // 打开其他app
                try {
                    if (!TextUtils.isEmpty(imageInfo.getDesc())) {
                        AppUtil.coayTextPutNative(activity, imageInfo.getDesc());
                        if (!TextUtils.isEmpty(imageInfo.getUrl())) {
                            PackageManager packageManager
                                    = activity.getApplicationContext().getPackageManager();
                            Intent intent = packageManager.
                                    getLaunchIntentForPackage(imageInfo.getUrl());
                            activity.startActivity(intent);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (open == 7) {   // 打开店铺userid


                if (TextUtils.isEmpty(imageInfo.getTitle())) {
                    imageInfo.setTitle(activity.getString(R.string.discount_stores));
                }
                imageInfo.categoryId = imageInfo.getUrl();
                GoodNewsFramgent.start(activity, imageInfo, C.GoodsListType.DiscountStores);


            } else if (open == 8) {   // 打开专题列表（轮播记录ID）
                GoodNewsFramgent.start(activity, imageInfo);
            } else if (open == 9) {   //商学院文章
                //url=123&type=1&id=1&title=aa 取type的值,1是打开商学院的首页,2是商学院的文章,3是h5
                Map<String, String> params = AppUtil.getUrlParms(imageInfo.getUrl());
                String type = params.get("type");
                String id = params.get("id");
                String url = params.get("url");
                String title = params.get("title");
                if (null != type && type.equals("1")) {
                    //商学院首页
                    MessageEvent messageEvent = new MessageEvent();
                    messageEvent.setAction(EventBusAction.ACTION_SCHOOL);
                    EventBus.getDefault().post(messageEvent);
                    if (activity instanceof NoticeActivity) {
                        activity.finish();
                    }
                } else if (null != type && type.equals("2")) {
                    //商学院二级模块  5&title=大咖分享&type=2
                    Intent intent = new Intent(activity, CollegeListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("title", title);
                    bundle.putString("id", id);
                    intent.putExtras(bundle);
                    activity.startActivity(intent);
                } else if (null != type && type.equals("3")) {
                    //文章 9&url=http://pp.gzmiyuan.com/api/h5/businessSchool/#/detail?id=9&type=3
                    ShowWebActivity.start(activity, url, imageInfo.getTitle());
                }
            } else if (open == 10) {   //第三方活动列表
                gotoMenu(activity, imageInfo.getClassId(), imageInfo);  //如果是2类型，class_id就是分类ID
            } else if (open == 11) {   //物料id列表
                if (LoginUtil.checkIsLogin(activity)) {
                    if (TaobaoUtil.isAuth()) {//淘宝授权
                        TaobaoUtil.getAllianceAppKey((BaseActivity) activity, false);
                    } else {
                        GoodNewsFramgent.start(activity, imageInfo, C.GoodsListType.MATERIAL);
                    }
                }
            } else if (open == 12) {   //我的素材
                if (LoginUtil.checkIsLogin(activity)) {
                    MyMaterialActivity.start(activity);
                }
            } else if (open == 13) {   //发布管理
                if (LoginUtil.checkIsLogin(activity)) {
                    ReleaseManageActivity.start(activity, 0);
                }
            } else if (open == 14) { //小程序
                WechatUtil.openMiNi(imageInfo);
            } else if (open == 15) { //飞猪
                if (LoginUtil.checkIsLogin(activity)) {
                    if (TaobaoUtil.isAuth()) {//淘宝授权
                        TaobaoUtil.getAllianceAppKey((BaseActivity) activity, false);
                    } else {
                        //显示弹窗
                        openFligyDialog(activity, imageInfo);
                    }
                }
            } else {
                showUptate(activity, open);
            }
            // 不能再加16了,会和之前飞猪有兼容
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 提示升级
    private static void showUptate(Activity activity, int type) {
        if (type != 0) {
            ViewShowUtils.showShortToast(activity, activity.getString(R.string.update_show));
        }
    }


    /**
     * 跳到分类
     *
     * @param activity
     * @param arrGcInfoSuper
     * @param categoryId
     */
    public static void gotoCategoryDetail(Activity activity, List<GoodCategoryInfo> arrGcInfoSuper, int categoryId) {
        if (arrGcInfoSuper == null || arrGcInfoSuper.size() == 0) {
            return;
        }
        GoodCategoryInfo categoryInfo = null;
        for (int j = 0; j < arrGcInfoSuper.size(); j++) {   //在超级分类列表中找到相应的分类ID
            if (categoryId == arrGcInfoSuper.get(j).getParentId()) {
                categoryInfo = arrGcInfoSuper.get(j);
                break;
            }
        }
        if (categoryInfo == null) {
            return;
        }

        ImageInfo imageInfo = new ImageInfo();
        imageInfo.categoryId = categoryInfo.getId() + "";
        imageInfo.setTitle(categoryInfo.getName());
        GoodNewsFramgent.start((Activity) activity, imageInfo, C.GoodsListType.THREEGOODS);
    }

    /**
     * 商品分类id、内站列表 {
     * "1": "聚划算",
     * "2": "淘抢购",
     * "3": "9块9包邮/白菜价",
     * "4": "限时秒杀",
     * "5": "官方推荐",
     * "6": "品牌特卖",
     * "7": "赚钱计划",
     * "8": "专题列表",
     * "9": "新人课堂",
     * "10": "官方公告",
     * "11": "我的收藏",
     * "12": "专属客服",
     * "13": "意见反馈",
     * "14": "五星好评",
     * "15": "关于我们",
     * "16": "投诉建议" } }
     */
    public static void gotoMenu(Activity activity, int type, ImageInfo info) {
        if (type == C.BannerIntentionType.JHS) {
            GoodNewsFramgent.start(activity, info, C.GoodsListType.JVHUASUAN);
        } else if (type == C.BannerIntentionType.TQG) {
            GoodNewsFramgent.start(activity, info, C.GoodsListType.TAOQIANGGOU);
        } else if (type == C.BannerIntentionType.NINE) {
            GoodNewsFramgent.start(activity, info, C.GoodsListType.NINEPINKAGE);
        } else if (type == C.BannerIntentionType.PANICBUY) {
            PanicBuyFragment.start(activity, info);
        } else if (type == C.BannerIntentionType.OFFICIALRECOM) {    //官方推荐
            OfficialRecomFragment.start(activity);
        } else if (type == C.BannerIntentionType.BrandSell) {//品牌特卖
            BrandSellFragment.start(activity);
        } else if (type == C.BannerIntentionType.MakeMoney) {//赚钱计划
//            MakeMoneyFragment.start(activity);
            if (LoginUtil.checkIsLogin(activity)) {
                VipActivity.start(activity);
            }

        } else if (type == C.BannerIntentionType.TypeActivity) {// 专题列表
            GoodNewsFramgent.start(activity, info);
        } else if (type == C.BannerIntentionType.Newcomers) {// "新人课堂",
            NewcomersFragment.start(activity);
        } else if (type == C.BannerIntentionType.OfficialNotice) {// "官方公告",
            Bundle bundle = new Bundle();
            bundle.putString("title", "官方公告");
            OpenFragmentUtils.goToSimpleFragment(activity, OfficialNoticeFragment.class.getName(), bundle);
        } else if (type == C.BannerIntentionType.Collect) {// "我的收藏",
            OpenFragmentUtils.goToSimpleFragment(activity, CollectFragment2.class.getName(), null);
        } else if (type == C.BannerIntentionType.service) {// "专属客服",
            showWebIntent(activity, info);
        } else if (type == C.BannerIntentionType.AppFeed) {// "意见反馈",
            Intent feedBackIt = new Intent(activity, AppFeedActivity.class);
            Bundle feedBackBundle = new Bundle();
            feedBackBundle.putString("title", "意见反馈");
            feedBackBundle.putString("fragmentName", "AppFeedBackFragment");
            feedBackIt.putExtras(feedBackBundle);
            activity.startActivity(feedBackIt);
        } else if (type == C.BannerIntentionType.AppAbout) {// "关于我们",
            Intent aboutIt = new Intent(activity, OneFragmentDefaultActivity.class);
            Bundle aboutBundle = new Bundle();
            aboutBundle.putString("title", "关于");
            aboutBundle.putString("fragmentName", "AppAboutFragment");
            aboutIt.putExtras(aboutBundle);
            activity.startActivity(aboutIt);
        } else if (type == C.BannerIntentionType.suggest) {// "投诉建议",
            showWebIntent(activity, info);
        } else if (type == C.BannerIntentionType.life) {//多点优选生活
            showWebIntent(activity, info);
        } else if (type == C.BannerIntentionType.Ranking) {
            Bundle bundle = new Bundle();
            bundle.putBoolean(C.Extras.openFragment_isSysBar, true);
            OpenFragmentUtils.goToSimpleFragment(activity, RankingFragment.class.getName(), bundle);
        } else if (type == C.BannerIntentionType.DAYRECOMMEND) {
            GoodNewsFramgent.startTiemSale(activity, info);
        } else if (type == C.BannerIntentionType.Browsing) { //  20 足迹
            GoodsBrowsingHistoryActivity.start(activity);
        } else if (type == C.BannerIntentionType.operator) { // 运营专员后台
            showWebIntent(activity, info);
        } else if (type == C.BannerIntentionType.officialActivity) { // 天猫国际
            loginTaobao((BaseActivity) activity, info);
        } else if (type == C.BannerIntentionType.BigShop) { // 超值大牌
            GoodNewsFramgent.start(activity, info, C.GoodsListType.BigShopList);
        } else if (type == C.BannerIntentionType.ForeShow) { // 预告单
            OpenFragmentUtils.goToSimpleFragment(activity, ForeshowFragment.class.getName(), null);
        } else if (type == C.BannerIntentionType.CIRCLE_REVIEW) { // 商学院预览列表
            RecommendListActivity.start(activity, RecommendListActivity.ARTICLE_REVIEW);
        } else if (type == C.BannerIntentionType.BRAND_LIST) { // 品牌列表
            OpenFragmentUtils.goToSimpleFragment(activity, BrandListFragment.class.getName(), new Bundle());
        } else if (type == C.BannerIntentionType.THREE_GOODS) { // 分类
            GoodNewsFramgent.start((Activity) activity, info, C.GoodsListType.THREEGOODS);
        } else if (type == C.BannerIntentionType.GOODS_BYBRAND) { // 品牌列表
            GoodNewsFramgent.startGoodsByBrand(activity, info);
        } else if (type == C.BannerIntentionType.JD){//京东

        } else if (type == C.BannerIntentionType.PDD){//拼多多
            Bundle bundle = new Bundle();
            bundle.putBoolean(C.Extras.openFragment_isSysBar, true);
            OpenFragmentUtils.goToSimpleFragment(activity, PddFragment.class.getName(), bundle);
        } else {
            showUptate(activity, type);
        }
    }

    /**
     * 跳转web页面
     *
     * @param activity
     * @param info
     */
    private static void showWebIntent(Activity activity, ImageInfo info) {
        if (TextUtils.isEmpty(info.getUrl())) return;
        ShowWebActivity.start(activity,getAppendUrl(info),info.getTitle());

//        Intent it = new Intent(activity, ShowWebActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("title", info.getTitle());
//        bundle.putString("url", getAppendUrl(info));
//        it.putExtras(bundle);
//        activity.startActivity(it);
    }

    /**
     * 获取活动生成链接
     *
     * @param activity
     * @param info
     */
    private static void getGenerateActivityLink(final BaseActivity activity, final ImageInfo info) {
        if (info.getUrl().startsWith("http")) {
            if (TextUtils.isEmpty(info.getUrl())) return;
            TmallWebActivity.start(activity, getAppendUrl(info), info.getTitle());
        } else {
            RequestTmallActivityLinkBean requestBean = new RequestTmallActivityLinkBean();
            requestBean.setActivityId(info.getUrl());
            RxHttp.getInstance().getGoodsService()
                    .getGenerateActivityLink(requestBean)
                    .compose(RxUtils.<BaseResponse<TmallActivityBean>>switchSchedulers())
                    .compose(activity.<BaseResponse<TmallActivityBean>>bindToLifecycle())
                    .subscribe(new DataObserver<TmallActivityBean>() {
                        @Override
                        protected void onSuccess(TmallActivityBean data) {
                            String activityLink = data.getActivityLink();
                            if (TextUtils.isEmpty(activityLink)) return;
                            TmallWebActivity.start(activity, activityLink, info.getTitle());

                        }
                    });
        }
    }

    private static void loginTaobao(final BaseActivity activity, final ImageInfo info) {
        if (!LoginUtil.checkIsLogin(activity)) {
            return;
        }

        if (TaobaoUtil.isAuth()) {//淘宝授权
            TaobaoUtil.getAllianceAppKey(activity, false);
        } else {
            if (!AlibcLogin.getInstance().isLogin()) {
                TaobaoUtil.authTaobao(new AlibcLoginCallback() {
                    @Override
                    public void onSuccess(int loginResult, String openId, String userNick) {
                        // 参数说明：
                        // loginResult(0--登录初始化成功；1--登录初始化完成；2--登录成功)
                        // openId：用户id
                        // userNick: 用户昵称
                        //授权成功回调
                        getGenerateActivityLink((BaseActivity) activity, info);
                    }

//                    @Override
//                    public void onSuccess(int i) {
//                        getGenerateActivityLink((BaseActivity) activity, info);
//                    }

                    @Override
                    public void onFailure(int i, String s) {
                        getGenerateActivityLink((BaseActivity) activity, info);
                    }
                });
            } else {
                getGenerateActivityLink((BaseActivity) activity, info);
            }
        }
    }


    /**
     * 设置轮播图 不用设置比例
     *
     * @param data
     */
    public static void setBrandBanner(final Activity activity, final List<ImageInfo> data, Banner banner) {
        setBrandBanner(activity, data, banner, null, BannerConfig.RIGHT);
    }

    public static void setBrandBanner(final Activity activity, final List<ImageInfo> data, Banner banner, AspectRatioView aspectRatioView) {
        setBrandBanner(activity, data, banner, aspectRatioView, BannerConfig.RIGHT);
    }
    public static void setBrandBanner(final Activity activity, final List<ImageInfo> data, Banner banner, AspectRatioView aspectRatioView,int bannerConfigType) {
        final int bannerId = banner.getId();
        if (data == null || data.size() == 0) {
            return;
        }
        if (aspectRatioView != null) {
            ImageInfo imageInfo = data.get(0);
            float width = imageInfo.getWidth();
            float height = imageInfo.getHeight();
            if (width != 0 && width / height != 0) {
                aspectRatioView.setAspectRatio(width / height);
            }
        }
        List<String> imgUrls = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            imgUrls.add(data.get(i).getThumb());
        }

        //简单使用
        banner.setImages(imgUrls)
                .setIndicatorGravity(bannerConfigType)
                .setImageLoader(new GlideImageLoader())
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
//                        StatService.onEventDuration(getActivity(), "浏览", "图片 "+position, 3000);
                        /**
                         @param eventId 事件Id，提前在网站端创建
                         @param eventLabel 事件标签，附加参数，不能为空字符串
                         @param acc 事件发生次数
                         @param attributes 事件属性，对应的key需要在网站上创建，注意：key，value只接受String
                         */
//                        CountEvent cEvent = new CountEvent( );
//                        cEvent.setEventId("1000");
//                        CountEvent cEvent = new CountEvent("1000");
//                        cEvent.addKeyValue("1000","value1").addKeyValue("key2","value2");
//                        JAnalyticsInterface.onEvent(getActivity(), cEvent);
                        ImageInfo imageInfo = data.get(position);
                        String mudule = "";
                        if(bannerId == R.id.roll_view_pager){
                            mudule = C.BigData.AD_A;
                        }else if(bannerId == R.id.banner_offical){
                            mudule = C.BigData.AD_B;
                        }else if(bannerId == R.id.banner_make_money){
                            mudule = C.BigData.AD_C;
                        }
                        SensorsDataUtil.getInstance().advClickTrack(imageInfo.getTitle(),imageInfo.getId()+"",imageInfo.getOpen()+"",mudule,position,imageInfo.getClassId()+"",imageInfo.getUrl());
                        BannerInitiateUtils.gotoAction(activity, imageInfo);

                    }
                })
                .isAutoPlay(true)
                .setDelayTime(4000)
                .start();
    }

    /**
     * app点击统计
     */

    public static void statisticsStartAdOnclick(BaseActivity activity, String id, int type) {
        RequestSplashStatistics requestBean = new RequestSplashStatistics();
        requestBean.setAdId(id);
        requestBean.setType(type);
        RxHttp.getInstance().getCommonService().statisticsStartAdOnclick(requestBean)
                .compose(RxUtils.<BaseResponse>switchSchedulers())
                .compose(activity.<BaseResponse>bindToLifecycle())
                .subscribe(new Consumer<BaseResponse>() {
                    @Override
                    public void accept(BaseResponse baseResponse) throws Exception {
                        MyLog.i("test", "已统计");
                    }
                });

    }

    /**
     * 是否追加pid和rid级成新的url
     * @param info
     * @return
     */
    public static String getAppendUrl(ImageInfo info){
        String url = info.getUrl();
        if(null != info && !TextUtils.isEmpty(info.getSplicePid()) && !info.getSplicePid().equals("0")){
            UserInfo user = UserLocalData.getUser();
            String[] keyList = info.getSplicePid().split(",");
            //获取user里的url参数map组合
            Map<String,String> params = StringsUtils.getUrlParams(user.getSpliceIdsName());
            Map<String,String> passParams = new LinkedHashMap<>();
            for (String key: keyList) {
                Iterator<Map.Entry<String, String>> iter = params.entrySet().iterator();
                int position = 0;
                while (iter.hasNext()) {
                    position++;
                    Map.Entry<String, String> entry = iter.next();
                    if(key.equals("3")){
                        if(!TextUtils.isEmpty(entry.getValue()) && !entry.getValue().equals("null")){
                            passParams.put(entry.getKey(),entry.getValue());
                        }
                    }else{
                        if(position == Integer.parseInt(key) && !TextUtils.isEmpty(entry.getValue()) && !entry.getValue().equals("null")){
                            passParams.put(entry.getKey(),entry.getValue());
                        }
                    }

                }
            }
            url = StringsUtils.appendUrlParams(info.getUrl(),passParams);
        }
        return   url;
    }

    public static void openFligyDialog(final Activity activity, final ImageInfo info){

         FliggyDialog fliggyDialog = new FliggyDialog(activity, R.style.dialog, new FliggyDialog.GoToFliggyListener() {
             @Override
             public void onClick(Dialog dialog, String text) {
                         //前往飞猪
                 if (info.getUrl().startsWith("http")) {
                     if (TextUtils.isEmpty(info.getUrl())) return;
                     TmallWebActivity.start(activity, getAppendUrl(info), info.getTitle());
                 }
             }

         }, new FliggyDialog.GoToFindOrderListener() {
             @Override
             public void onClick(Dialog dialog, String text) {
                        //订单找回
             }
         });

        if (fliggyDialog != null) {
            try {
                fliggyDialog.show();
            } catch (Exception e) {

            }
        }
    }


    public static void goToArticle(Activity activity,String allUrl,String allTitle) {
        //url=123&type=1&id=1&title=aa 取type的值,1是打开商学院的首页,2是商学院的文章,3是h5
        Map<String, String> params = AppUtil.getUrlParms(allUrl);
        if(params==null){
            return;
        }
        String type = params.get("type");
        String id = params.get("id");
        String url = params.get("url");
        String title = params.get("title");
        if (null != type && type.equals("1")) {
            //商学院首页
            MessageEvent messageEvent = new MessageEvent();
            messageEvent.setAction(EventBusAction.ACTION_SCHOOL);
            EventBus.getDefault().post(messageEvent);
            if (activity instanceof NoticeActivity) {
                activity.finish();
            }
        } else if (null != type && type.equals("2")) {
            //商学院二级模块  5&title=大咖分享&type=2
            Intent intent = new Intent(activity, CollegeListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("title", title);
            bundle.putString("id", id);
            intent.putExtras(bundle);
            activity.startActivity(intent);
        } else if (null != type && type.equals("3")) {
            //文章 9&url=http://pp.gzmiyuan.com/api/h5/businessSchool/#/detail?id=9&type=3
            ShowWebActivity.start(activity, url, allTitle);
        }
    }
}
