package com.zjzy.morebit.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.gyf.barlibrary.ImmersionBar;
import com.tencent.mm.opensdk.utils.Log;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.zjzy.morebit.App;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.MainActivity;
import com.zjzy.morebit.Module.common.Activity.ImagePagerActivity;
import com.zjzy.morebit.Module.common.Dialog.DownloadDialog;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.Module.common.View.BaseCustomTabEntity;
import com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout;
import com.zjzy.morebit.R;
import com.zjzy.morebit.circle.ui.ReleaseGoodsActivity;
import com.zjzy.morebit.contact.EventBusAction;
import com.zjzy.morebit.fragment.NumberFragment;
import com.zjzy.morebit.goods.shopping.contract.GoodsDetailForPddContract;
import com.zjzy.morebit.goods.shopping.presenter.GoodsDetailForPddPresenter;
import com.zjzy.morebit.goods.shopping.ui.fragment.GoodsDetailImgForPddFragment;
import com.zjzy.morebit.goods.shopping.ui.view.GoodsDetailUpdateView;
import com.zjzy.morebit.info.ui.AppFeedActivity;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpActivity;
import com.zjzy.morebit.pojo.GoodsDetailForPdd;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.ReleaseGoodsPermission;
import com.zjzy.morebit.pojo.ReleaseManage;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.event.GoodsHeightUpdateEvent;
import com.zjzy.morebit.pojo.goods.TKLBean;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.DateTimeUtils;
import com.zjzy.morebit.utils.GlideImageLoader;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.OpenFragmentUtils;
import com.zjzy.morebit.utils.SensorsDataUtil;
import com.zjzy.morebit.utils.StringsUtils;
import com.zjzy.morebit.utils.UI.ActivityUtils;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.helper.ActivityLifeHelper;
import com.zjzy.morebit.view.main.SysNotificationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 唯品会商品详情页
 */

public class GoodsDetailForWphActivity extends MvpActivity<GoodsDetailForPddPresenter> implements View.OnClickListener, GoodsDetailForPddContract.View {


    @BindView(R.id.tv_Share_the_money)
    TextView tv_Share_the_money;
    @BindView(R.id.ll_share_money)
    LinearLayout ll_share_money;
    @BindView(R.id.textview_original)
    TextView textview_original;
    @BindView(R.id.text_two)
    TextView text_two;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.btn_sweepg)
    LinearLayout btn_sweepg;

    @BindView(R.id.roll_view_pager)
    Banner mRollViewPager;
    List<ImageInfo> indexbannerdataArray = new ArrayList<>();


    @BindView(R.id.tv_collect)
    TextView tv_collect;
    @BindView(R.id.collect_bg)
    ImageView collect_bg;
    @BindView(R.id.videopaly_btn)
    ImageView videopaly_btn;
    @BindView(R.id.allIncomeTv)
    TextView allIncomeTv;
    @BindView(R.id.nsv_view)
    NestedScrollView nsv_view;
    @BindView(R.id.srl_view)
    SwipeRefreshLayout srl_view;
    @BindView(R.id.view_bar)
    View view_bar;

    @BindView(R.id.rl_urgency_notifi)
    RelativeLayout rl_urgency_notifi;
    @BindView(R.id.gduv_view)
    GoodsDetailUpdateView gduv_view;
    @BindView(R.id.tv_buy)
    TextView tv_buy;


    private String shopid;
    private ShopGoodInfo mGoodsInfo;
    private Bundle bundle;
    private int mWidth;
    final List<String> mBannerList = new ArrayList<>();
    private TKLBean mTKLBean;
    private List<ImageInfo> mSysNotificationData;
    private int mConsumerPadding;

    private float duration = 855.0f;//在0-855.0之间去改变头部的透明度
    private int mIngHeight;
    private int mListHeight;
    private Handler mHandler;
    private int mTitleHeight;
    private TextView iv_taobao;
    private TextView tv_zhaun;
    private LinearLayout ll_shen;


    private String[] mTitles;
    ArrayList mTabArrayList = new ArrayList<BaseCustomTabEntity>();

    private GoodsDetailForPdd mGoodsDetailForPdd;

    public static void start(Context context, String id) {
        Intent intent = new Intent((Activity) context, GoodsDetailForWphActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(C.Extras.GOODSBEAN, id);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_wph_goodsdetail;
    }

    @Subscribe  //订阅事件
    public void onEventMainThread(MessageEvent event) {
        switch (event.getAction()) {
            case EventBusAction.LOGINA_SUCCEED:
                initData(false);
                mPresenter.getSysNotification(this);
                refreshVipUpdate();
                break;
        }
    }

    @Subscribe  //订阅事件
    public void onEventMainThread(GoodsHeightUpdateEvent event) {
        getViewLocationOnScreen();
    }

    private void getViewLocationOnScreen() {

//        search_statusbar_rl.getViewTreeObserver().addOnPreDrawListener(
//                new ViewTreeObserver.OnPreDrawListener() {
//
//                    @Override
//                    public boolean onPreDraw() {
//                        search_statusbar_rl.getViewTreeObserver().removeOnPreDrawListener(this);
//                        int i = search_statusbar_rl.getHeight() - mTitleHeight;
//                        mListHeight = i; // 获取高度
//                        return true;
//                    }
//                });


        MyLog.d("setOnScrollChangeListener  ", "mIngHeight " + mIngHeight);
        MyLog.d("setOnScrollChangeListener  ", "mListHeight " + mListHeight);
    }

    private void initData(boolean isRefresh) {
        mPresenter.generateDetailForWph(this,shopid,isRefresh);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mTitles = new String[]{getString(R.string.goods_detail_baby), getString(R.string.goods_detail_det)};
//        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.margin_small);
//        mConsumerPadding = getResources().getDimensionPixelSize(R.dimen.goods_consumer_itme_padding);
//        mTitleHeight = getResources().getDimensionPixelSize(R.dimen.goods_detail_title_height);
//        mWidth = AppUtil.getTaobaoIconWidth() + dimensionPixelSize;
//        duration = getWindowManager().getDefaultDisplay().getWidth() - mTitleHeight + 0.0F;
        //初始化
        mGoodsDetailForPdd = new GoodsDetailForPdd();
        EventBus.getDefault().register(this);
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .titleBar(view_bar)    //解决状态栏和布局重叠问题，任选其一
                .init();
        initBundle();
        initView();
        initData(false);
        mPresenter.getSysNotification(this);
        mHandler = new Handler();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.Grade");//名字
        this.registerReceiver(mRefreshBroadcastReceiver, intentFilter);


    }

    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action.Grade")) {  //接收到广播通知的名字，在当前页面应与注册名称一致
                refreshVipUpdate();//需要去做的事
            }
        }
    };

    private void refreshVipUpdate() {
        gduv_view.refreshView();
        setEstimateData();
        setUPdateData();
    }


    private void initBundle() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            shopid = (String) bundle.getSerializable(C.Extras.GOODSBEAN);
        }
    }

    private void initView() {
       // initTab();
        iv_taobao= (TextView) findViewById(R.id.iv_taobao);
        tv_zhaun= (TextView) findViewById(R.id.tv_zhaun);
        ll_shen= (LinearLayout) findViewById(R.id.ll_shen);
        srl_view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData(true);
                refreshVipUpdate();
            }
        });

        view_bar.setAlpha(0);




    }



    /**
     * 初始化界面数据
     *
     * @param Info
     * @param Info
     */
    private void initViewData(final ShopGoodInfo Info) {
        if (Info == null) {
            return;
        }
        setGoodsAdImg(Info);


        if (!StringsUtils.isEmpty(Info.getGoodsName())) {
            mGoodsInfo.setGoodsTitle(Info.getGoodsName());
        }
        if (!StringsUtils.isEmpty(Info.getVipPrice())) {
            textview_original.setText("" + MathUtils.getnum(Info.getVipPrice()));
        }


//        if (UserLocalData.getUser().getReleasePermission() == 1) {
//            findViewById(R.id.iv_release_goods).setVisibility(View.VISIBLE);
//        } else {
//            findViewById(R.id.iv_release_goods).setVisibility(View.GONE);
//        }

        if (!StringsUtils.isEmpty(Info.getMarketPrice())) {

            text_two.setText(" ¥" + MathUtils.getnum(Info.getMarketPrice()));
            text_two.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
        }


        if (!TextUtils.isEmpty(Info.getCommission())) {
            mGoodsInfo.setCommission(Info.getCommission());
            tv_Share_the_money.setText(getString(R.string.now_share));
        }

        if (!TextUtils.isEmpty(Info.getCommission())) {
            mGoodsInfo.setCommission(Info.getCommission());
        }

        if (TextUtils.isEmpty(UserLocalData.getUser(GoodsDetailForWphActivity.this).getPartner())) {
            tv_Share_the_money.setText(getString(R.string.now_share));
            setEstimateData();
            setUPdateData();
        } else {
            if (!StringsUtils.isEmpty(Info.getCommission())) {
                if (getString(R.string.now_share).equals(tv_Share_the_money.getText())) {
                    mGoodsInfo.setCommission(Info.getCommission());
                    String muRatioComPrice = MathUtils.getMuRatioComPrice(UserLocalData.getUser(GoodsDetailForWphActivity.this).getCalculationRate(), Info.getCommission());
                    setBuyText(Info.getCommission(), Info.getCouponPrice(), Info.getSubsidiesPrice());
                    if (!TextUtils.isEmpty(muRatioComPrice)) {
                        String getRatioSubside = MathUtils.getMuRatioSubSidiesPrice(UserLocalData.getUser(GoodsDetailForWphActivity.this).getCalculationRate(), Info.getSubsidiesPrice());
                        String totalSubside = MathUtils.getTotleSubSidies(muRatioComPrice, getRatioSubside);
                        tv_Share_the_money.setText(getString(R.string.goods_share_moeny, totalSubside));
                        setAllIncomeData(muRatioComPrice, getRatioSubside);
                    }
                    setEstimateData();
                    setUPdateData();
                }
            } else {
                setEstimateData();
                setUPdateData();
            }
        }
    }

    /**
     * 设置预告佣金
     */
    private void setEstimateData() {
        if (gduv_view != null) {
            gduv_view.setEstimateData(mGoodsInfo);
        }
    }


    /**
     * 设置升级佣金
     */
    private void setUPdateData() {
        if (TextUtils.isEmpty(C.SysConfig.COMMISSION_PERCENT_VALUE)) {
            mPresenter.getSysCommissionPercent(this);
        } else {
            gduv_view.setUpdateView(mGoodsInfo, C.SysConfig.COMMISSION_PERCENT_VALUE);
        }
    }

    //设置补贴+总佣金
    private void setAllIncomeData(String ratioComPrice, String subsidiesPrice) {
        StringBuilder incomeBuild = new StringBuilder();
        if (!TextUtils.isEmpty(subsidiesPrice) && !TextUtils.isEmpty(ratioComPrice)) {
            incomeBuild.append(getString(R.string.subsidiesTips, subsidiesPrice) + " + ");
            incomeBuild.append(getString(R.string.incomeTips, ratioComPrice));
            allIncomeTv.setVisibility(View.VISIBLE);
            allIncomeTv.setText(incomeBuild.toString());
        } else {
            allIncomeTv.setVisibility(View.GONE);
        }

    }

    /**
     * @param commission  总佣金
     * @param couponPrice 优惠券金额
     */
    private void setBuyText(String commission, String couponPrice, String subsidiesPrice) {
        UserInfo user = UserLocalData.getUser();
        boolean isLogin = LoginUtil.checkIsLogin(this, false);
        boolean invalidMoney = MathUtils.checkoutInvalidMoney(couponPrice);// 是否有券
        if (!isLogin /*&& !invalidMoney*/) {//（1）未登录，商品没券：立即购买
            tv_buy.setText(getString(R.string.immediately_buy));
        }/* else if (isLogin && !invalidMoney && C.UserType.member.equals(user.getPartner())) {//（6）已登录，普通会员，商品没券：立即购买
            tv_buy.setText(getString(R.string.immediately_buy));
        }*/ else {
            double allDiscountsMoney = MathUtils.allDiscountsMoney(user.getCalculationRate(), commission, couponPrice);
            String discountsMoneyStr = MathUtils.formatTo2Decimals(allDiscountsMoney + "");
            String getRatioSubside = MathUtils.getMuRatioSubSidiesPrice(user.getCalculationRate(), subsidiesPrice);
            String allDiscountsMoneyStr = MathUtils.getTotleSubSidies(discountsMoneyStr, getRatioSubside);
            tv_buy.setText(getString(R.string.immediately_buy_discounts, allDiscountsMoneyStr));
        }
    }

    /**
     * 显示详情页数据
     *
     * @param info
     * @param isSeavDao
     * @param isRefresh
     */
    @Override
    public void showDetailsView(ShopGoodInfo info, boolean isSeavDao, boolean isRefresh) {


    }


    @Override
    public void OngetDetailDataFinally() {
        srl_view.setRefreshing(false);
    }

    @Override
    public void checkPermission(ReleaseGoodsPermission data) {
        if ("1".equals(data.getItemStatus())) {
            ReleaseManage releaseManage = (ReleaseManage) App.getACache().getAsObject(C.sp.RELEASE_GOODS + mGoodsInfo.getItemSourceId() + UserLocalData.getUser().getInviteCode());
            List<ShopGoodInfo> shopGoodInfoList = new ArrayList<>();
            shopGoodInfoList.add(mGoodsInfo);
            if (releaseManage == null) {
                ReleaseGoodsActivity.start(this, shopGoodInfoList, null, ReleaseGoodsActivity.SUBMIT, 1);
            } else {
                ReleaseGoodsActivity.start(this, shopGoodInfoList, releaseManage, ReleaseGoodsActivity.SUBMIT, 1);
            }
        } else {
            ViewShowUtils.showShortToast(this, data.getHint());
        }
    }


    /**
     * 设置商品轮播图
     *
     * @param
     */

    private void setGoodsAdImg(final ShopGoodInfo info) {
        //如果没有轮播图,初始化默认商品logo图
        //头顶图片填充数据
        if (info == null) {
            return;
        }

        if (mBannerList.size() <= 1) {
            List<String> getBanner = info.getGoodsCarouselPictures();

            indexbannerdataArray.clear();
            for (int i = 0; i < getBanner.size(); i++) {
                String s = StringsUtils.checkHttp(getBanner.get(i));
                if (TextUtils.isEmpty(s)) return;
                //http://kaola-pop.oss.kaolacdn.com/01a2a565-a565-4bbc-9aa3-4b8560f60a2a.jpg
                Log.e("ssss",s+"1");
                Log.e("ssss",getBanner.get(i)+"2");

                    ImageInfo imageInfo = new ImageInfo();
                    imageInfo.setThumb(getBanner.get(i));
                    indexbannerdataArray.add(imageInfo);
                    mBannerList.add(indexbannerdataArray.get(i).getThumb());

            }
            //简单使用
            mRollViewPager.setImages(getBanner)
                    .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                    .setImageLoader(new GlideImageLoader())
                    .setOnBannerListener(new OnBannerListener() {
                        @Override
                        public void OnBannerClick(int position) {
                            Intent intent = new Intent(GoodsDetailForWphActivity.this, ImagePagerActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putStringArrayList(ImagePagerActivity.EXTRA_IMAGE_URLS, (ArrayList<String>) mBannerList);
                            bundle.putInt(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                            bundle.putString(ImagePagerActivity.TAOBAO_ID, info.getItemSourceId());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    })
                    .isAutoPlay(true)
                    .setDelayTime(4000)
                    .start();
        }
    }


    /**
     * 切换收藏ui
     *
     * @param info
     */
    public void switchColler(ShopGoodInfo info) {
        mGoodsInfo.setColler(info.getColler());
        if (info.getColler() != 0) {
            tv_collect.setText(getString(R.string.also_collect));
            tv_collect.setTextColor(ContextCompat.getColor(GoodsDetailForWphActivity.this, R.color.color_FF3F29));
            collect_bg.setImageResource(R.drawable.icon_shoucanxuanzhong);
            SensorsDataUtil.getInstance().collectProductTrack("", mGoodsInfo.getItemSourceId(), mGoodsInfo.getGoodsTitle(), mGoodsInfo.getCurrentPrice());
        } else {
            collect_bg.setImageResource(R.drawable.icon_shoucang);
            tv_collect.setTextColor(ContextCompat.getColor(GoodsDetailForWphActivity.this, R.color.mmhuisezi));
            tv_collect.setText(getString(R.string.collect));
        }
    }

    /**
     * 设置系统通知
     *
     * @param data
     */
    @Override
    public void setSysNotificationData(List<ImageInfo> data) {
        this.mSysNotificationData = data;
        setSysNotificationView();
    }

    /**
     * 设置升级之后滴view
     *
     * @param sysValue
     */
    @Override
    public void setUpdateView(String sysValue) {
        gduv_view.setUpdateView(mGoodsInfo, sysValue);
    }

    @Override
    public void setPromotionUrl(String promotionUrl) {

    }

    @Override
    public void setPromotionJdUrl(String promotionJdUrl) {

    }

    /*
     *
     * 考拉详情数据
     * */
    @Override
    public void setDetaisData(ShopGoodInfo data,boolean isSeavDao, boolean isRefresh) {

    }

    @Override
    public void setDetaisDataWph(ShopGoodInfo data, boolean seavDao, boolean isRefresh) {
        mGoodsInfo=data;
        if (mGoodsInfo == null) {
            return;
        }
        initViewData(mGoodsInfo);
        if (mGoodsInfo.getColler() != 0) {
            switchColler(mGoodsInfo);
        }
        //示详情图片
        List<String> imgs = mGoodsInfo.getGoodsDetailPictures();
        if (imgs != null && imgs.size() > 0) {
            mGoodsDetailForPdd.setGoodsDetails(imgs);
        }
        if (!StringsUtils.isEmpty(data.getMarketPrice())) {
            mGoodsInfo.setItemPrice(data.getMarketPrice());
        }
        if (!StringsUtils.isEmpty(data.getVipPrice())) {
            mGoodsInfo.setItemVoucherPrice(data.getVipPrice());
        }
        if (!StringsUtils.isEmpty(data.getGoodsName())) {
            mGoodsInfo.setItemTitle(data.getGoodsName());
        }
        if (!StringsUtils.isEmpty(String.valueOf(data.getGoodsId()))) {
            mGoodsInfo.setItemSourceId(String.valueOf(data.getGoodsId()));
        }
        if (!TextUtils.isEmpty(mGoodsInfo.getGoodsMainPicture())) {
            mGoodsInfo.setPicture(mGoodsInfo.getGoodsMainPicture());
        }
        if (!TextUtils.isEmpty(mGoodsInfo.getStoreName())) {
            mGoodsInfo.setShopName(mGoodsInfo.getStoreName());
        }
        if (seavDao && !isRefresh) {
            SensorsDataUtil.getInstance().browseProductTrack("", String.valueOf(data.getGoodsId()));
            if (LoginUtil.checkIsLogin(this, false) && !TextUtils.isEmpty(mGoodsInfo.getGoodsName()) && !TextUtils.isEmpty(mGoodsInfo.getGoodsMainPicture())) {
                mPresenter.saveGoodsHistor(this, mGoodsInfo);
            }
        }

        if (!TextUtils.isEmpty(mGoodsInfo.getCommission())) {
            tv_zhaun.setText("赚 ¥ " + MathUtils.getMuRatioComPrice(UserLocalData.getUser(this).getCalculationRate(), mGoodsInfo.getCommission() + "") + "元");
        }
       StringsUtils.retractTitles(title,data.getGoodsName(),iv_taobao.getWidth()+10);
        getViewLocationOnScreen();
    }

    private void setSysNotificationView() {
        rl_urgency_notifi.removeAllViews();
        if (mSysNotificationData == null || mSysNotificationData.size() == 0) return;
        for (int i = 0; i < mSysNotificationData.size(); i++) {
            final ImageInfo imageInfo = mSysNotificationData.get(i);
            //1【首页】、2【商品详情页】、3【分类】、4【发圈】、5【我的】
            if (2 == imageInfo.getDisplayPage()) {
                SysNotificationView sysNotificationView = new SysNotificationView();
                sysNotificationView.addSysNotificationView(this, rl_urgency_notifi, imageInfo);
            }
        }
    }


    @OnClick({R.id.btn_back,/* R.id.iv_feedback, R.id.iv_img_download, R.id.iv_release_goods,*/ R.id.ll_share_money, R.id.ll_shen, R.id.btn_sweepg, R.id.videopaly_btn, R.id.collect_ly, R.id.ll_home})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_shen:
                if (LoginUtil.checkIsLogin(this)) {
                    OpenFragmentUtils.goToSimpleFragment(this, NumberFragment.class.getName(), null);
                }
                break;
            case R.id.btn_back:
                finish();
                break;
//            case R.id.iv_feedback:
//                if (!LoginUtil.checkIsLogin(GoodsDetailForWphActivity.this)) {
//                    return;
//                }
//
//                Intent feedBackIt = new Intent(GoodsDetailForWphActivity.this, AppFeedActivity.class);
//                Bundle feedBackBundle = new Bundle();
//                feedBackBundle.putString("title", "意见反馈");
//                feedBackBundle.putString("fragmentName", "GoodsFeedBackFragment");
//                feedBackBundle.putString("gid", mGoodsInfo.getItemSourceId());
//                feedBackIt.putExtras(feedBackBundle);
//                startActivity(feedBackIt);
//
//                break;
            case R.id.ll_share_money:
                if (isGoodsLose()) return;
                if (mGoodsInfo != null) {
                    mGoodsInfo.setAdImgUrl(indexbannerdataArray);
                    mGoodsInfo.setPrice(mGoodsInfo.getMarketPrice());
                    mGoodsInfo.setVoucherPrice(mGoodsInfo.getVipPrice());
                    mGoodsInfo.setTitle(mGoodsInfo.getGoodsName());
                    mGoodsInfo.setClickURL(mGoodsInfo.getPurchaseLink());
                }
                ShareMoneyForPddActivity.start(this, mGoodsInfo, mGoodsInfo.getPurchaseLink());

                break;
            case R.id.btn_sweepg: //立即购买
                if (LoginUtil.checkIsLogin(this)) {
                    if (mGoodsInfo.getPurchaseLink()!=null){
                        if (isHasInstalledWph()){
                            if (mGoodsInfo.getDeepLinkUrl()!=null){
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mGoodsInfo.getDeepLinkUrl()));
                                startActivity(intent);
                            }
                        }else{
                            ShowWebActivity.start(this,mGoodsInfo.getPurchaseLink(), "");
                        }
                    }

                }

                break;
            case R.id.collect_ly: //收藏
                if (isGoodsLose()) return;
                sumbitCollect();
                break;

            case R.id.ll_home: // 跳转首页
                ActivityLifeHelper.getInstance().finishActivity(MainActivity.class);
                break;

//            case R.id.iv_img_download:   //下载图片
//                if (mBannerList.size() > 0) {
//                    DownloadDialog downloadDialog = new DownloadDialog(this, R.style.dialog, mBannerList);
//                    downloadDialog.show();
//                }

//                break;
            default:
                break;
        }
    }


    /**
     * 收藏或取消收藏
     */
    private void sumbitCollect() {

        if (mGoodsInfo == null || mGoodsInfo.getGoodsId() == 0
                || TextUtils.isEmpty(mGoodsInfo.getGoodsName())) {
            ViewShowUtils.showShortToast(GoodsDetailForWphActivity.this, "收藏失败,请稍后再试");
            return;
        }
        LoadingView.showDialog(GoodsDetailForWphActivity.this);
        //todo：修复从足迹到收藏报错

        mPresenter.switchWphCollect(this, mGoodsInfo);
    }



    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        mHandler.removeCallbacksAndMessages(null);
        unregisterReceiver(mRefreshBroadcastReceiver);
        super.onDestroy();
    }


    /**
     * 商品是否过期
     *
     * @return
     */
    private boolean isGoodsLose() {
        if (!LoginUtil.checkIsLogin(GoodsDetailForWphActivity.this)) {
            return true;
        }
        if (AppUtil.isFastClick(200)) {
            return true;
        }
        if (mGoodsInfo == null) {
            return true;
        }
        if (TextUtils.isEmpty(mGoodsInfo.getVipPrice())) {
            ViewShowUtils.showLongToast(this, "商品已经过期，请联系管理员哦");
            return true;
        }
        return false;
    }

    /**
     * 确定IView类型
     *
     * @return
     */
    @Override
    public BaseView getBaseView() {
        return this;
    }

    @Override
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    /**
     * 判断是否安装唯品会
     *
     * @return
     */
    private boolean isHasInstalledWph() {
        return AppUtil.checkHasInstalledApp(this, "com.achievo.vipshop");
    }
}
