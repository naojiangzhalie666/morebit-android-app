package com.zjzy.morebit.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.gyf.barlibrary.ImmersionBar;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.zjzy.morebit.App;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.MainActivity;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Activity.ImagePagerActivity;
import com.zjzy.morebit.Module.common.Dialog.ClearSDdataDialog;
import com.zjzy.morebit.Module.common.Dialog.DownloadDialog;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.Module.common.View.BaseCustomTabEntity;
import com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout;
import com.zjzy.morebit.R;
import com.zjzy.morebit.circle.ui.ReleaseGoodsActivity;
import com.zjzy.morebit.contact.EventBusAction;
import com.zjzy.morebit.goods.shopping.contract.GoodsDetailContract;
import com.zjzy.morebit.goods.shopping.contract.GoodsDetailForPddContract;
import com.zjzy.morebit.goods.shopping.presenter.GoodsDetailForPddPresenter;
import com.zjzy.morebit.goods.shopping.presenter.GoodsDetailPresenter;
import com.zjzy.morebit.goods.shopping.ui.PddWebActivity;
import com.zjzy.morebit.goods.shopping.ui.fragment.GoodsDetailImgForPddFragment;
import com.zjzy.morebit.goods.shopping.ui.fragment.GoodsDetailImgFragment;
import com.zjzy.morebit.goods.shopping.ui.view.GoodsDetailUpdateView;
import com.zjzy.morebit.info.ui.AppFeedActivity;
import com.zjzy.morebit.main.model.SearchStatisticsModel;
import com.zjzy.morebit.main.ui.fragment.GoodsDetailLikeFragment;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpActivity;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.GoodsDetailForPdd;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.ReleaseGoodsPermission;
import com.zjzy.morebit.pojo.ReleaseManage;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.event.GoodsHeightUpdateEvent;
import com.zjzy.morebit.pojo.goods.ConsumerProtectionBean;
import com.zjzy.morebit.pojo.goods.EvaluatesBean;
import com.zjzy.morebit.pojo.goods.GoodsImgDetailBean;
import com.zjzy.morebit.pojo.goods.TKLBean;
import com.zjzy.morebit.pojo.request.RequestReleaseGoods;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.DateTimeUtils;
import com.zjzy.morebit.utils.GlideImageLoader;
import com.zjzy.morebit.utils.GoodsUtil;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.MyGsonUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.SensorsDataUtil;
import com.zjzy.morebit.utils.StringsUtils;
import com.zjzy.morebit.utils.TaobaoUtil;
import com.zjzy.morebit.utils.UI.ActivityUtils;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.action.MyAction;
import com.zjzy.morebit.utils.helper.ActivityLifeHelper;
import com.zjzy.morebit.view.AspectRatioView;
import com.zjzy.morebit.view.goods.ShareMoneySwitchForPddTemplateView;
import com.zjzy.morebit.view.main.SysNotificationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Action;

/**
 * 拼多多商品详情页
 */

public class GoodsDetailForPddActivity extends MvpActivity<GoodsDetailForPddPresenter> implements View.OnClickListener, GoodsDetailForPddContract.View {

    @BindView(R.id.iv_feedback)
    ImageView iv_feedback;
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
    @BindView(R.id.momVolume)
    TextView momVolume;
    @BindView(R.id.coupon_prise)
    TextView coupon_prise;
    @BindView(R.id.tv_pdd)
    TextView tv_pdd;
    @BindView(R.id.rl_prise)
    View rl_prise;
    @BindView(R.id.arv_prise)
    AspectRatioView arv_prise;
    @BindView(R.id.roll_view_pager)
    Banner mRollViewPager;
    List<ImageInfo> indexbannerdataArray = new ArrayList<>();

    @BindView(R.id.go_top)
    ImageView go_top;
    @BindView(R.id.tv_desc)
    TextView tv_desc;
    @BindView(R.id.tv_describe)
//    TextView tv_describe;
//    @BindView(R.id.tv_logistics)
//    TextView tv_logistics;
//    @BindView(R.id.tv_sellel)
//    TextView tv_sellel;
//    @BindView(R.id.tv_collect)
    TextView tv_collect;
    @BindView(R.id.collect_bg)
    ImageView collect_bg;
    @BindView(R.id.shop_img)
    ImageView shop_img;
    @BindView(R.id.videopaly_btn)
    ImageView videopaly_btn;
    @BindView(R.id.shop_name)
    TextView shop_name;
    @BindView(R.id.tv_coupon_time)
    TextView tv_coupon_time;
    @BindView(R.id.tv_provcity)
    TextView tv_provcity;
    @BindView(R.id.allIncomeTv)
    TextView allIncomeTv;

    @BindView(R.id.rl_desc)
    View rl_desc;
    @BindView(R.id.nsv_view)
    NestedScrollView nsv_view;
    @BindView(R.id.srl_view)
    SwipeRefreshLayout srl_view;
    @BindView(R.id.ll_fw)
    LinearLayout ll_fw;
    @BindView(R.id.view_bar)
    View view_bar;

    @BindView(R.id.rl_urgency_notifi)
    RelativeLayout rl_urgency_notifi;
    @BindView(R.id.gduv_view)
    GoodsDetailUpdateView gduv_view;
    @BindView(R.id.tv_buy)
    TextView tv_buy;
    @BindView(R.id.tv_line)
    TextView tv_line;
    @BindView(R.id.fl_img)
    FrameLayout fl_img;
    @BindView(R.id.fl_list)
    FrameLayout fl_list;
    @BindView(R.id.tablayout)
    CommonTabLayout tablayout;

    //描述状态
    @BindView(R.id.tv_descTxt_status)
    TextView tv_desc_status;

    //服务态度
    @BindView(R.id.tv_servTxt_status)
    TextView tv_servTxt_status;

    //物流状态
    @BindView(R.id.tv_lgstTxt_status)
    TextView tv_lgstTxt_status;

    @BindView(R.id.re_tab)
    RelativeLayout re_tab;
    @BindView(R.id.search_statusbar_rl)
    LinearLayout search_statusbar_rl;


    private ShopGoodInfo mGoodsInfo;
    private Bundle bundle;
    private GoodsDetailImgForPddFragment mDetailImgFragment;
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


    private String[] mTitles;
    ArrayList mTabArrayList = new ArrayList<BaseCustomTabEntity>();

    private GoodsDetailForPdd mGoodsDetailForPdd;

    //拼多多的推广链接。
    private String mPromotionUrl;

    public static void start(Context context, ShopGoodInfo info) {
        Intent intent = new Intent((Activity) context, GoodsDetailForPddActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(C.Extras.GOODSBEAN, info);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_pdd_goodsdetail;
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

        search_statusbar_rl.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        search_statusbar_rl.getViewTreeObserver().removeOnPreDrawListener(this);
                        int i = search_statusbar_rl.getHeight() - mTitleHeight;
                        mListHeight = i; // 获取高度
                        return true;
                    }
                });
        fl_list.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        fl_list.getViewTreeObserver().removeOnPreDrawListener(this);
                        int listHeight = fl_list.getHeight() == 0 ? 50 : fl_list.getHeight();
                        mIngHeight = listHeight + mListHeight; // 获取高度
                        return true;
                    }
                });

        MyLog.d("setOnScrollChangeListener  ", "mIngHeight " + mIngHeight);
        MyLog.d("setOnScrollChangeListener  ", "mListHeight " + mListHeight);
    }

    private void initData(boolean isRefresh) {
        if (mGoodsInfo == null) return;
        mPresenter.getDetailDataForPdd(this, mGoodsInfo, isRefresh);
        mPresenter.generatePromotionUrl(this,mGoodsInfo.getGoodsId(),mGoodsInfo.getCouponUrl());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitles = new String[]{getString(R.string.goods_detail_baby), getString(R.string.goods_detail_det)};
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.margin_small);
        mConsumerPadding = getResources().getDimensionPixelSize(R.dimen.goods_consumer_itme_padding);
        mTitleHeight = getResources().getDimensionPixelSize(R.dimen.goods_detail_title_height);
        mWidth = AppUtil.getTaobaoIconWidth() + dimensionPixelSize;
        duration = getWindowManager().getDefaultDisplay().getWidth() - mTitleHeight + 0.0F;
        //初始化
        mGoodsDetailForPdd = new GoodsDetailForPdd();
        EventBus.getDefault().register(this);
        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .titleBar(view_bar)    //解决状态栏和布局重叠问题，任选其一
                .init();
        initBundle();
        initView();
//        initImgFragment();
        initViewData(mGoodsInfo);
        initData(false);
        mPresenter.getSysNotification(this);
        mHandler = new Handler();

    }

    private void refreshVipUpdate() {
        gduv_view.refreshView();
        setEstimateData();
        setUPdateData();
    }

    private void initImgFragment(GoodsDetailForPdd goodsDetailForPdd) {
        mDetailImgFragment = GoodsDetailImgForPddFragment.newInstance(mGoodsDetailForPdd);
        ActivityUtils.replaceFragmentToActivity(
                getSupportFragmentManager(), mDetailImgFragment, R.id.fl_img);

    }

    private void initBundle() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            mGoodsInfo = (ShopGoodInfo) bundle.getSerializable(C.Extras.GOODSBEAN);
            //商铺:拼多多
            mGoodsInfo.setShopType(3);
            //商品Id
            mGoodsInfo.setItemSourceId(mGoodsInfo.getGoodsId().toString());
        }
    }

    private void initView() {
        initTab();

        srl_view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData(true);
            }
        });
        if (mGoodsInfo == null || TextUtils.isEmpty(mGoodsInfo.getVideoid()) || "0".equals(mGoodsInfo.getVideoid())) {
            videopaly_btn.setVisibility(View.GONE);
        } else {
            videopaly_btn.setVisibility(View.VISIBLE);
        }
        go_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollViewToLocation(0);
            }
        });
        re_tab.setAlpha(0);
        view_bar.setAlpha(0);
        nsv_view.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                scrollTitleChange(scrollY);
                scrollTabChange(scrollY);
                if (scrollY > 1000) {
                    go_top.setVisibility(View.VISIBLE);
                } else {
                    go_top.setVisibility(View.GONE);
                }
            }


            private void scrollTitleChange(int scrollY) {
                float alpha = (float) (scrollY / duration);
                if (duration > scrollY) {
                    if (scrollY < 5) {
                        getViewLocationOnScreen();
                        re_tab.setVisibility(View.GONE);
                        re_tab.setAlpha(0);
                        view_bar.setAlpha(0);
                        re_tab.setBackgroundColor(ContextCompat.getColor(GoodsDetailForPddActivity.this, R.color.white));
                        view_bar.setBackgroundColor(ContextCompat.getColor(GoodsDetailForPddActivity.this, R.color.white));
                    } else {
                        if (re_tab.getVisibility() == View.GONE) {
                            re_tab.setVisibility(View.VISIBLE);
                        }
                        re_tab.setAlpha(alpha);
                        view_bar.setAlpha(alpha);
                        isTitleBarSetBg = false;
                    }
                } else {

                    if (re_tab.getVisibility() == View.GONE) {
                        re_tab.setVisibility(View.VISIBLE);
                    }
                    if (!isTitleBarSetBg) {
                        re_tab.setAlpha(1);
                        view_bar.setAlpha(1);
                        re_tab.setBackgroundColor(ContextCompat.getColor(GoodsDetailForPddActivity.this, R.color.white));
                        view_bar.setBackgroundColor(ContextCompat.getColor(GoodsDetailForPddActivity.this, R.color.white));
                    }
                }
            }

            private void scrollTabChange(int scrollY) {

                MyLog.d("setOnScrollChangeListener  ", "mIngHeight " + mIngHeight);
                MyLog.d("setOnScrollChangeListener  ", "mListHeight " + mListHeight);
                if (tablayout == null || mListHeight == 0 || mIngHeight == 0 || isContinueScrollTabChange) {
                    return;
                }

                int currentTab = tablayout.getCurrentTab();
                if (scrollY <= mIngHeight) {
                    if (currentTab != 0)
                        tablayout.setCurrentTab(0);
                } else if (scrollY > mIngHeight) {
                    if (currentTab != 1)
                        tablayout.setCurrentTab(1);
                }
            }
        });


    }

    private void initTab() {
        for (int i = 0; i < mTitles.length; i++) {
            mTabArrayList.add(new BaseCustomTabEntity(mTitles[i], 0, 0));
        }
        tablayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                isContinueScrollTabChange = true;
                switch (position) {
                    case 0:
                        scrollViewToLocation(0);
                        break;
                    case 1:
                        scrollViewToLocation(mListHeight);
                        break;
                    case 2:
                        scrollViewToLocation(mIngHeight);
                        break;

                    default:
                        break;
                }
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isContinueScrollTabChange = false;
                    }
                }, 500);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        tablayout.setTabData(mTabArrayList);
    }

    private void scrollViewToLocation(int location) {
        nsv_view.fling(location);
        nsv_view.smoothScrollTo(location, location);
    }

    private boolean isTitleBarSetBg = true;
    private boolean isContinueScrollTabChange = false;

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
        if (!TextUtils.isEmpty(Info.getComeFrom())) {
            mGoodsInfo.setComeFrom(Info.getComeFrom());
        }
        if (!TextUtils.isEmpty(Info.getItemSource())) {
            mGoodsInfo.setItemSource(Info.getItemSource());
        }
        if (!StringsUtils.isEmpty(Info.getTitle())) {
            mGoodsInfo.setTitle(Info.getTitle());
        }
        if (!StringsUtils.isEmpty(Info.getVoucherPriceForPdd())) {
            mGoodsInfo.setVoucherPriceForPdd(Info.getVoucherPriceForPdd());
            mGoodsInfo.setVoucherPrice(Info.getVoucherPriceForPdd());
            textview_original.setText("" + MathUtils.getnum(Info.getVoucherPriceForPdd()));
        }


        if (StringsUtils.isEmpty(Info.getItemDesc())) {
            rl_desc.setVisibility(View.GONE);
        } else {
            rl_desc.setVisibility(View.VISIBLE);
            mGoodsInfo.setItemDesc(Info.getItemDesc());
            tv_desc.setText(Info.getItemDesc());
        }
        mGoodsInfo.setItemSource(Info.getItemSource());
        if (!StringsUtils.isEmpty(Info.getTitle())) {

                StringsUtils.retractTitleForPdd(tv_pdd, title, Info.getTitle());
        }


        if (UserLocalData.getUser().getReleasePermission() == 1) {
            findViewById(R.id.iv_release_goods).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.iv_release_goods).setVisibility(View.GONE);
        }

        if (!StringsUtils.isEmpty(Info.getPriceForPdd())) {
            mGoodsInfo.setPriceForPdd(Info.getPriceForPdd());
            mGoodsInfo.setPrice(Info.getPriceForPdd());
            text_two.setText(" ¥" + MathUtils.getnum(Info.getPriceForPdd()));
            text_two.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
        }
        if (!StringsUtils.isEmpty(Info.getSaleMonth())) {
            mGoodsInfo.setSaleMonth(Info.getSaleMonth());

            momVolume.setText(getString(R.string.sales, MathUtils.getSales(Info.getSaleMonth())));
        }

        if (!StringsUtils.isEmpty(Info.getShopName())) {
            shop_name.setText(Info.getShopName());
            mGoodsInfo.setShopName(Info.getShopName());
        }

        if (!StringsUtils.isEmpty(Info.getSellerPicture())) {
            Info.setSellerPicture(Info.getSellerPicture());
            LoadImgUtils.loadingCornerBitmap(this, shop_img, Info.getSellerPicture());
        }
        String dateStart = Info.getCouponStartTime();
        String dateEnd = Info.getCouponEndTime();
        if (TextUtils.isEmpty(tv_coupon_time.getText())) {

            if (!TextUtils.isEmpty(dateStart) && !TextUtils.isEmpty(dateEnd)) {
                tv_coupon_time.setText("有效日期：" +dateStart.substring(5,7)+"."+dateStart.substring(8,10)
                        + " - " + dateEnd.substring(5,7)+"."+dateEnd.substring(9,11));
            } else {
                tv_coupon_time.setText("D I S C O U N T  C O U P O N");
            }

            if (TextUtils.isEmpty(Info.getCouponStartTime())&&!TextUtils.isEmpty(Info.getCouponEndTime())){
                tv_coupon_time.setText("有效日期至："+ dateEnd.substring(5,7)+"."+dateEnd.substring(8,10));
            }
        } else {
            if (!TextUtils.isEmpty(dateEnd) && !TextUtils.isEmpty(dateEnd)) {
                tv_coupon_time.setText("有效日期：" + dateStart.substring(5,7)+"."+dateStart.substring(8,10)
                        + " - " + dateEnd.substring(5,7)+"."+dateEnd.substring(8,10));
            }
        }
        if (!StringsUtils.isEmpty(Info.getCouponPrice())) {
            mGoodsInfo.setCouponPrice(Info.getCouponPrice());
            arv_prise.setVisibility(View.VISIBLE);
            coupon_prise.setText( MathUtils.getnum(Info.getCouponPrice())+"");
            setBuyText(Info.getCommission(), Info.getCouponPrice(), Info.getSubsidiesPrice());
        }else{
            arv_prise.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(Info.getSubsidiesPrice())) {
            mGoodsInfo.setSubsidiesPrice(Info.getSubsidiesPrice());
            tv_Share_the_money.setText(getString(R.string.now_share));
        }

        if (!TextUtils.isEmpty(Info.getCommission())) {
            mGoodsInfo.setCommission(Info.getCommission());
        }

        if (TextUtils.isEmpty(UserLocalData.getUser(GoodsDetailForPddActivity.this).getPartner())) {
            tv_Share_the_money.setText(getString(R.string.now_share));
            setEstimateData();
            setUPdateData();
        } else {
            if (!StringsUtils.isEmpty(Info.getCommission())) {
                if (getString(R.string.now_share).equals(tv_Share_the_money.getText())) {
                    mGoodsInfo.setCommission(Info.getCommission());
                    String muRatioComPrice = MathUtils.getMuRatioComPrice(UserLocalData.getUser(GoodsDetailForPddActivity.this).getCalculationRate(), Info.getCommission());
                    setBuyText(Info.getCommission(), Info.getCouponPrice(), Info.getSubsidiesPrice());
                    if (!TextUtils.isEmpty(muRatioComPrice)) {
                        String getRatioSubside = MathUtils.getMuRatioSubSidiesPrice(UserLocalData.getUser(GoodsDetailForPddActivity.this).getCalculationRate(), Info.getSubsidiesPrice());
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
        if (!isLogin && !invalidMoney) {//（1）未登录，商品没券：立即购买
            tv_buy.setText(getString(R.string.immediately_buy));
        } else if (isLogin && !invalidMoney && C.UserType.member.equals(user.getPartner())) {//（6）已登录，普通会员，商品没券：立即购买
            tv_buy.setText(getString(R.string.immediately_buy));
        } else {
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
        if (info == null) {
            return;
        }
        initViewData(info);
        initViewShopData(info);
        if (!StringsUtils.isEmpty(info.getCouponUrl())) {
            mGoodsInfo.setCouponUrl(info.getCouponUrl());
        }
        if (info.getColler() != 0) {
            switchColler(info);
        }

        if (TextUtils.isEmpty(tv_provcity.getText())) {
            if (!StringsUtils.isEmpty(info.getProvcity())) {
                tv_provcity.setVisibility(View.VISIBLE);
                tv_line.setVisibility(View.VISIBLE);
                tv_provcity.setText(info.getProvcity());
                mGoodsInfo.setProvcity(info.getProvcity());
            } else {
                tv_line.setVisibility(View.GONE);
                tv_provcity.setVisibility(View.GONE);
            }
        }


        if (!StringsUtils.isEmpty(info.getCouponUrl())) {
            mGoodsInfo.setCouponUrl(info.getCouponUrl());
        }
        if (!StringsUtils.isEmpty(info.getShopId())) {
            mGoodsInfo.setShopId(info.getShopId());
        }
        String imageUrl = info.getImageUrl();
        if (!StringsUtils.isEmpty(imageUrl)){
            mGoodsInfo.setPicture(imageUrl);
        }
        if (isSeavDao && !isRefresh) {
            SensorsDataUtil.getInstance().browseProductTrack("", info.getItemSourceId());
            if (LoginUtil.checkIsLogin(this, false) && !TextUtils.isEmpty(mGoodsInfo.getTitle()) && !TextUtils.isEmpty(mGoodsInfo.getPrice())) {
                mPresenter.saveGoodsHistor(this, mGoodsInfo);
            }
        }
        //示详情图片
        List<String> imgs = info.getItemBanner();
        if (imgs != null && imgs.size() > 0){

            mGoodsDetailForPdd.setGoodsDetails(imgs);
            initImgFragment(mGoodsDetailForPdd);
        }

        //描述状态
        tv_desc_status.setText(info.getDescTxt());
        //服务状态
        tv_servTxt_status.setText(info.getServTxt());
        //物流状态
        tv_lgstTxt_status.setText(info.getLgstTxt());

        getViewLocationOnScreen();
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
            indexbannerdataArray.clear();
            List<String> getBanner = info.getItemBanner();

            if (getBanner == null || getBanner.size() == 0) {
                if (!TextUtils.isEmpty(mGoodsInfo.getPicture())) {
                    getBanner = new ArrayList<>();
                    getBanner.add(StringsUtils.checkHttp(mGoodsInfo.getPicture()));
                } else {
                    return;
                }
            } else {
                if (!TextUtils.isEmpty(mGoodsInfo.getPicture())) {
                    getBanner.add(0, StringsUtils.checkHttp(mGoodsInfo.getPicture()));
                }
                mGoodsInfo.setBanner(getBanner);
            }
            for (int i = 0; i < getBanner.size(); i++) {
                String s = StringsUtils.checkHttp(getBanner.get(i));
                if (TextUtils.isEmpty(s)) return;
                if (LoadImgUtils.isPicture(s)) {
                    ImageInfo imageInfo = new ImageInfo();
                    imageInfo.setThumb(getBanner.get(i));
                    indexbannerdataArray.add(imageInfo);
                }
            }
            for (int i = 0; i < indexbannerdataArray.size(); i++) {
                if (!mBannerList.contains(indexbannerdataArray.get(i).getThumb()))
                    mBannerList.add(indexbannerdataArray.get(i).getThumb());
            }

            //简单使用
            mRollViewPager.setImages(mBannerList)
                    .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                    .setImageLoader(new GlideImageLoader())
                    .setOnBannerListener(new OnBannerListener() {
                        @Override
                        public void OnBannerClick(int position) {
                            Intent intent = new Intent(GoodsDetailForPddActivity.this, ImagePagerActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putStringArrayList(ImagePagerActivity.EXTRA_IMAGE_URLS, (ArrayList<String>) mBannerList);
                            bundle.putInt(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                            bundle.putString(ImagePagerActivity.TAOBAO_ID, mGoodsInfo.getItemSourceId());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    })
                    .isAutoPlay(true)
                    .setDelayTime(4000)
                    .start();
        }
    }

//    /**
//     * 评价
//     *
//     * @param info
//     */
//    private void showSellerEvaluate(ShopGoodInfo info) {
//        String evaluatesStr = info.getEvaluates();
//        if (TextUtils.isEmpty(evaluatesStr))
//            return;
//        ArrayList<EvaluatesBean> evaluatesBeansList = null;
//        try {
//            evaluatesBeansList = (ArrayList<EvaluatesBean>) MyGsonUtils.getListBeanWithResult(evaluatesStr, EvaluatesBean.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        if (evaluatesBeansList != null && evaluatesBeansList.size() != 0) {
//            for (int i = 0; i < evaluatesBeansList.size(); i++) {
//                EvaluatesBean evaluatesBean = evaluatesBeansList.get(i);
//                String levelText = evaluatesBean.getLevelText();
//                switch (i) {
//                    case 0:
//                        tv_describe.setText(evaluatesBean.getTitle() + "：" + evaluatesBean.getScore());
//                        break;
//                    case 1:
//                        tv_logistics.setText(evaluatesBean.getTitle() + "：" + evaluatesBean.getScore());
//                        break;
//                    case 2:
//                        tv_sellel.setText(evaluatesBean.getTitle() + "：" + evaluatesBean.getScore());
//                        break;
//                }
//            }
//        }
//    }

    /**
     * 设置店铺信息
     *
     * @param data
     */
    public void initViewShopData(ShopGoodInfo data) {

        showConsumerProtection(data);
//        showSellerEvaluate(data);
    }

    /**
     * 权益
     *
     * @param data
     */
    private void showConsumerProtection(ShopGoodInfo data) {
        String consumerProtection = data.getConsumerProtection();
        if (TextUtils.isEmpty(consumerProtection))
            ll_fw.setVisibility(View.GONE);
        ArrayList<ConsumerProtectionBean> coList = null;
        try {
            if (!TextUtils.isEmpty(consumerProtection)) {
                coList = (ArrayList) MyGsonUtils.getListBeanWithResult(consumerProtection, ConsumerProtectionBean.class);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (coList == null || coList.size() == 0) {
            ll_fw.setVisibility(View.GONE);
            return;
        }
        ll_fw.setVisibility(View.VISIBLE);
        if (coList != null && coList.size() != 0) {
            ll_fw.removeAllViews();
            for (int i = 0; i < coList.size(); i++) {
                if (i >= 4) {
                    continue;
                }
                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setPadding(mConsumerPadding, 0, 0, 0);
                linearLayout.setGravity(Gravity.CENTER_VERTICAL);
                ConsumerProtectionBean item = coList.get(i);
                TextView textView2 = new TextView(this);
                textView2.setGravity(Gravity.CENTER);
                textView2.setTextSize(11);
                textView2.setTextColor(ContextCompat.getColor(this, R.color.color_666666));
                textView2.setText(item.getTitle());
                textView2.setPadding(6, 0, 0, 0);

                ImageView imageView = new ImageView(this);
                imageView.setImageResource(R.drawable.goods_describe);
                linearLayout.addView(imageView);
                linearLayout.addView(textView2);

                ll_fw.addView(linearLayout);

            }
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
            tv_collect.setTextColor(ContextCompat.getColor(GoodsDetailForPddActivity.this, R.color.color_FF3F29));
            collect_bg.setImageResource(R.drawable.icon_shoucanxuanzhong);
            SensorsDataUtil.getInstance().collectProductTrack("", mGoodsInfo.getItemSourceId(), mGoodsInfo.getTitle(), mGoodsInfo.getPrice());
        } else {
            collect_bg.setImageResource(R.drawable.icon_shoucang);
            tv_collect.setTextColor(ContextCompat.getColor(GoodsDetailForPddActivity.this, R.color.mmhuisezi));
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
        mPromotionUrl = promotionUrl;
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


    @OnClick({R.id.btn_back, R.id.iv_feedback, R.id.iv_img_download, R.id.iv_release_goods, R.id.ll_share_money, R.id.bottomLy, R.id.btn_sweepg, R.id.rl_prise, R.id.videopaly_btn, R.id.collect_ly, R.id.rl_shop_taobao, R.id.ll_home, R.id.btn_tltle_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottomLy:
                break;
            case R.id.btn_back:
            case R.id.btn_tltle_back:
                finish();
                break;
            case R.id.iv_feedback:
                if (!LoginUtil.checkIsLogin(GoodsDetailForPddActivity.this)) {
                    return;
                }

                Intent feedBackIt = new Intent(GoodsDetailForPddActivity.this, AppFeedActivity.class);
                Bundle feedBackBundle = new Bundle();
                feedBackBundle.putString("title", "意见反馈");
                feedBackBundle.putString("fragmentName", "GoodsFeedBackFragment");
                feedBackBundle.putString("gid", mGoodsInfo.getItemSourceId());
                feedBackIt.putExtras(feedBackBundle);
                startActivity(feedBackIt);

                break;
            case R.id.ll_share_money:
                if (isGoodsLose()) return;
                    if (mGoodsInfo != null) {
                        mGoodsInfo.setAdImgUrl(indexbannerdataArray);
                    }
                ShareMoneyForPddActivity.start(this,mGoodsInfo,mPromotionUrl);

                break;
            case R.id.btn_sweepg: //立即购买
            case R.id.rl_prise: //立即购买
                if (LoginUtil.checkIsLogin(this)) {
                    if (mPromotionUrl != null) {
//                    String content = "pinduoduo://com.xunmeng.pinduoduo/app.html?use_reload=1&launch_url=duo_coupon_landing.html%3Fgoods_id%3D4249333262%26pid%3D9672007_131083858%26cpsSign%3DCC_200322_9672007_131083858_2185d1115d543ff315f28695b09ff65e%26duoduo_type%3D2&campaign=ddjb&cid=launch_dl_force_";
//                    Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(content));
//                    startActivity(intent);
//                    mPromotionUrl = "https://mobile.yangkeduo.com/app.html?use_reload=1&launch_url=duo_coupon_landing.html%3Fgoods_id%3D4249333262%26pid%3D9672007_131083858%26cpsSign%3DCC_200322_9672007_131083858_2185d1115d543ff315f28695b09ff65e%26duoduo_type%3D2&campaign=ddjb&cid=launch_dl_force_";
                        if (isHasInstalledPdd() && mPromotionUrl.contains("https://mobile.yangkeduo.com")) {
                            String content = mPromotionUrl.replace("https://mobile.yangkeduo.com",
                                    "pinduoduo://com.xunmeng.pinduoduo");
//                        String content = "pinduoduo://com.xunmeng.pinduoduo"+mPromotionUrl.substring(8);
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(content));
                            startActivity(intent);
                        } else {
                            PddWebActivity.start(this, mPromotionUrl, mGoodsInfo.getTitle());
                        }
                    }
                }
                break;

            case R.id.videopaly_btn: //视频播放
                if (TextUtils.isEmpty(mGoodsInfo.getVideoid())) {
                    return;
                }

                Intent it = new Intent(GoodsDetailForPddActivity.this, ShortVideoPlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(C.Extras.ITEMVIDEOID, mGoodsInfo.getVideoid());
                it.putExtras(bundle);
                startActivity(it);
                break;
            case R.id.collect_ly: //收藏
                if (isGoodsLose()) return;
                sumbitCollect();
                break;
//            case R.id.rl_shop_taobao: // 跳转店铺，别多想
//                goToShopCoupon();
//                break;
            case R.id.ll_home: // 跳转首页
                ActivityLifeHelper.getInstance().finishActivity(MainActivity.class);
                break;

//            case R.id.iv_release_goods:   //商品发布管理
//                RequestReleaseGoods request = new RequestReleaseGoods();
//                request.setItemId(mGoodsInfo.getItemSourceId());
//                mPresenter.checkPermission(this, request);
//                break;
            case R.id.iv_img_download:   //下载图片
                if (mBannerList.size() > 0) {
                    DownloadDialog downloadDialog = new DownloadDialog(this, R.style.dialog, mBannerList);
                    downloadDialog.show();
                }

                break;
            default:
                break;
        }
    }


    /**
     * 收藏或取消收藏
     */
    private void sumbitCollect() {

        if (mGoodsInfo == null || mGoodsInfo.getGoodsId() == 0
                || TextUtils.isEmpty(mGoodsInfo.getTitle())) {
            ViewShowUtils.showShortToast(GoodsDetailForPddActivity.this, "收藏失败,请稍后再试");
            return;
        }
        LoadingView.showDialog(GoodsDetailForPddActivity.this);
        //todo：修复从足迹到收藏报错
        String couponEndTime = mGoodsInfo.getCouponEndTime();

        if (couponEndTime != null && couponEndTime.length() > 5){
            couponEndTime = DateTimeUtils.toMMdd(couponEndTime);
            mGoodsInfo.setCouponEndTime(couponEndTime);
        }

        mPresenter.switchCollect(this, mGoodsInfo);
    }

//    /**
//     * 跳转到店铺优惠券
//     */
//    private void goToShopCoupon() {
//        if (mGoodsInfo == null || TextUtils.isEmpty(mGoodsInfo.getShopId())) {
//            return;
//        }
//        LoadingView.showDialog(this);
//        mPresenter.getShopList(this, mGoodsInfo);
//    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }


    private void showGotoNotification(final ShopGoodInfo goodsInfo) {
        ClearSDdataDialog mDialog = new ClearSDdataDialog(this, R.style.dialog, getString(R.string.hint), "活动马上开始，敬请期待喔~~~", new ClearSDdataDialog.OnOkListener() {
            @Override
            public void onClick(View view, String text) {
                GoodsUtil.getCouponInfo(GoodsDetailForPddActivity.this, goodsInfo);
            }
        });
        mDialog.setOkTextAndColor(R.color.color_FF4848, "确定");
        mDialog.setCancelTextAndColor(R.color.color_333333, "取消");
        mDialog.show();

    }

    /**
     * 商品是否过期
     *
     * @return
     */
    private boolean isGoodsLose() {
        if (!LoginUtil.checkIsLogin(GoodsDetailForPddActivity.this)) {
            return true;
        }
        if (AppUtil.isFastClick(200)) {
            return true;
        }
        if (mGoodsInfo == null) {
            return true;
        }
        if (TextUtils.isEmpty(mGoodsInfo.getPriceForPdd())) {
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

//    /**
//     * 预加载淘口令
//     */
//    @Override
//    public void getTaoKouLing() {// 获取淘口令
//        //没有登录  有数据  没有title 没有价格
//        if (mTKLBean != null ||
//                !LoginUtil.checkIsLogin(this, false) ||
//                TextUtils.isEmpty(mGoodsInfo.getTitle()) ||
//                TaobaoUtil.isAuth()
//        ) {
//            return;
//        }
//
//
//
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == C.Result.shareMoneyToEditTemplateCoad && resultCode == RESULT_OK) {
//            getTaoKouLing();
        }
    }

    @Override
    protected boolean isImmersionBarEnabled() {
        return true;
    }


    /**
     * 判断是否安装拼多多
     * @return
     */
    private boolean isHasInstalledPdd(){
        return  AppUtil.checkHasInstalledApp(this, "com.xunmeng.pinduoduo");
    }
}
