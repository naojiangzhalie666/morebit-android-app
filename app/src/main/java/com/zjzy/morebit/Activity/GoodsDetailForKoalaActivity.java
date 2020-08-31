package com.zjzy.morebit.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.blankj.utilcode.util.ToastUtils;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.gyf.barlibrary.ImmersionBar;
import com.tencent.mm.opensdk.utils.Log;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.zjzy.morebit.App;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.MainActivity;
import com.zjzy.morebit.Module.common.Activity.ImagePagerActivity;
import com.zjzy.morebit.Module.common.Dialog.ClearSDdataDialog;
import com.zjzy.morebit.Module.common.Dialog.DownloadDialog;
import com.zjzy.morebit.Module.common.Dialog.LoadingDialog;
import com.zjzy.morebit.Module.common.Dialog.ShopkeeperUpgradeDialog;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.Module.common.View.BaseCustomTabEntity;
import com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout;
import com.zjzy.morebit.R;
import com.zjzy.morebit.circle.ui.ReleaseGoodsActivity;
import com.zjzy.morebit.contact.EventBusAction;
import com.zjzy.morebit.goods.shopping.contract.GoodsDetailForPddContract;
import com.zjzy.morebit.goods.shopping.presenter.GoodsDetailForPddPresenter;
import com.zjzy.morebit.goods.shopping.ui.fragment.GoodsDetailImgForPddFragment;
import com.zjzy.morebit.goods.shopping.ui.view.GoodsDetailUpdateView;
import com.zjzy.morebit.info.ui.AppFeedActivity;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpActivity;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.GoodsDetailForPdd;
import com.zjzy.morebit.pojo.HotKeywords;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.KaolaBean;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.ReleaseGoodsPermission;
import com.zjzy.morebit.pojo.ReleaseManage;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.event.GoodsHeightUpdateEvent;
import com.zjzy.morebit.pojo.event.RefreshUserInfoEvent;
import com.zjzy.morebit.pojo.goods.ConsumerProtectionBean;
import com.zjzy.morebit.pojo.goods.TKLBean;
import com.zjzy.morebit.pojo.myInfo.UpdateInfoBean;
import com.zjzy.morebit.pojo.request.RequestUpdateUserBean;
import com.zjzy.morebit.pojo.requestbodybean.RequestKeyBean;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.CommInterface;
import com.zjzy.morebit.utils.DateTimeUtils;
import com.zjzy.morebit.utils.GlideImageLoader;
import com.zjzy.morebit.utils.GoodsUtil;
import com.zjzy.morebit.utils.KaipuleUtils;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.MyGsonUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.SensorsDataUtil;
import com.zjzy.morebit.utils.StringsUtils;
import com.zjzy.morebit.utils.UI.ActivityUtils;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.helper.ActivityLifeHelper;
import com.zjzy.morebit.view.AspectRatioView;
import com.zjzy.morebit.view.main.SysNotificationView;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;
import com.zyao89.view.zloading.clock.CircleBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.functions.Action;

/**
 * 考拉商品详情页
 */

public class GoodsDetailForKoalaActivity extends MvpActivity<GoodsDetailForPddPresenter> implements View.OnClickListener, GoodsDetailForPddContract.View {


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
    @BindView(R.id.details_img)
    ImageView details_img;
    @BindView(R.id.tv_viprice)
    TextView tv_viprice;
    @BindView(R.id.tv_sheng)
    TextView tv_sheng;



    private String shopid;
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
    private TextView iv_taobao;
    private TextView tv_zhaun;
    private LinearLayout ll_shen,tv_fan;
    private   ZLoadingDialog dialog;


    private String[] mTitles;
    ArrayList mTabArrayList = new ArrayList<BaseCustomTabEntity>();

    private GoodsDetailForPdd mGoodsDetailForPdd;

    //京东领劵领劵
    private String mPromotionJdUrl;


    public static void start(Context context, String id) {
        Intent intent = new Intent((Activity) context, GoodsDetailForKoalaActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(C.Extras.GOODSBEAN, id);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.activity_koala_goodsdetail;
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
//
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
//
//
//        MyLog.d("setOnScrollChangeListener  ", "mIngHeight " + mIngHeight);
//        MyLog.d("setOnScrollChangeListener  ", "mListHeight " + mListHeight);
    }

    private void initData(boolean isRefresh) {
        UserInfo  mUserInfo = UserLocalData.getUser(this);
        mPresenter.generateDetailForKaola(this,shopid,mUserInfo.getId(),isRefresh);
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
        initData(false);
        mPresenter.getSysNotification(this);
        mHandler = new Handler();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.Grade");//名字
        this.registerReceiver(mRefreshBroadcastReceiver, intentFilter);
        dialog = new ZLoadingDialog(this);
        dialog.setLoadingBuilder(Z_TYPE.SINGLE_CIRCLE)//设置类型
                .setLoadingColor(Color.parseColor("#F05557"))//颜色
                .setHintText("Loading...")
                .show();

    }

    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action.Grade")) {  //接收到广播通知的名字，在当前页面应与注册名称一致
               // refreshVipUpdate();//需要去做的事
            }
        }
    };

    private void refreshVipUpdate() {
        gduv_view.refreshView();
        setEstimateData();
        setUPdateData();
    }

//    private void initImgFragment(GoodsDetailForPdd goodsDetailForPdd) {
//        mDetailImgFragment = GoodsDetailImgForPddFragment.newInstance(mGoodsDetailForPdd);
//        ActivityUtils.replaceFragmentToActivity(
//                getSupportFragmentManager(), mDetailImgFragment, R.id.fl_img);
//
//    }

    private void initBundle() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            shopid = (String) bundle.getSerializable(C.Extras.GOODSBEAN);
        }
    }

    private void initView() {
        iv_taobao= (TextView) findViewById(R.id.iv_taobao);
        tv_fan= (LinearLayout) findViewById(R.id.tv_fan);
        getReturning();
        iv_taobao= (TextView) findViewById(R.id.iv_taobao);
        tv_zhaun= (TextView) findViewById(R.id.tv_zhaun);
        ll_shen= (LinearLayout) findViewById(R.id.ll_shen);
        initSheng();
    //    initTab();
//        tv_fan= (LinearLayout) findViewById(R.id.tv_fan);
//        getReturning();
//        srl_view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                initData(true);
//                refreshVipUpdate();
//            }
//        });
//
//        go_top.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                scrollViewToLocation(0);
//            }
//        });
      //  re_tab.setAlpha(0);
        view_bar.setAlpha(0);
//        nsv_view.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                scrollTitleChange(scrollY);
//                scrollTabChange(scrollY);
//                if (scrollY > 1000) {
//                    go_top.setVisibility(View.VISIBLE);
//                } else {
//                    go_top.setVisibility(View.GONE);
//                }
//            }
//
//
//            private void scrollTitleChange(int scrollY) {
//                float alpha = (float) (scrollY / duration);
//                if (duration > scrollY) {
//                    if (scrollY < 5) {
//                        getViewLocationOnScreen();
//                        re_tab.setVisibility(View.GONE);
//                        re_tab.setAlpha(0);
//                        view_bar.setAlpha(0);
//                        re_tab.setBackgroundColor(ContextCompat.getColor(GoodsDetailForKoalaActivity.this, R.color.white));
//                        view_bar.setBackgroundColor(ContextCompat.getColor(GoodsDetailForKoalaActivity.this, R.color.white));
//                    } else {
//                        if (re_tab.getVisibility() == View.GONE) {
//                            re_tab.setVisibility(View.VISIBLE);
//                        }
//                        re_tab.setAlpha(alpha);
//                        view_bar.setAlpha(alpha);
//                        isTitleBarSetBg = false;
//                    }
//                } else {
//
//                    if (re_tab.getVisibility() == View.GONE) {
//                        re_tab.setVisibility(View.VISIBLE);
//                    }
//                    if (!isTitleBarSetBg) {
//                        re_tab.setAlpha(1);
//                        view_bar.setAlpha(1);
//                        re_tab.setBackgroundColor(ContextCompat.getColor(GoodsDetailForKoalaActivity.this, R.color.white));
//                        view_bar.setBackgroundColor(ContextCompat.getColor(GoodsDetailForKoalaActivity.this, R.color.white));
//                    }
 //               }
//            }
//
//            private void scrollTabChange(int scrollY) {
//
//                MyLog.d("setOnScrollChangeListener  ", "mIngHeight " + mIngHeight);
//                MyLog.d("setOnScrollChangeListener  ", "mListHeight " + mListHeight);
//                if (tablayout == null || mListHeight == 0 || isContinueScrollTabChange) {
//                    return;
//                }
//
//                int currentTab = tablayout.getCurrentTab();
//                if (scrollY <= mListHeight) {
//                    if (currentTab != 0)
//                        tablayout.setCurrentTab(0);
//                } else if (scrollY > mListHeight) {
//                    if (currentTab != 1)
//                        tablayout.setCurrentTab(1);
//                }
//            }
//        });


    }

    private void initSheng() {
        UserInfo mUserInfo = UserLocalData.getUser(this);
        if (mUserInfo != null) {
            if (C.UserType.member.equals(mUserInfo.getUserType())) {
                details_img.setImageResource(R.mipmap.details_img_member);
                tv_viprice.setText("6800");
                tv_sheng.setText("去升级");
            }else if (C.UserType.vipMember.equals(mUserInfo.getUserType())){
                details_img.setImageResource(R.mipmap.details_img_vip);
                tv_viprice.setText("16800");
                tv_sheng.setText("查看权益");
            }else{
                details_img.setImageResource(R.mipmap.details_img_opetor);
                tv_viprice.setText("26800");
                tv_sheng.setText("查看权益");
            }
        }
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


        if (!StringsUtils.isEmpty(Info.getGoodsTitle())) {
            mGoodsInfo.setGoodsTitle(Info.getGoodsTitle());
        }
        if (!StringsUtils.isEmpty(Info.getCurrentPrice())) {
            textview_original.setText("" + MathUtils.getnum(Info.getCurrentPrice()));
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

        if (!TextUtils.isEmpty(mGoodsInfo.getCommission())) {
            tv_zhaun.setText("赚 ¥ " + MathUtils.getMuRatioComPrice(UserLocalData.getUser(this).getCalculationRate(), mGoodsInfo.getCommission() + "") + "元");
        }
        if (!TextUtils.isEmpty(Info.getCommission())) {
            mGoodsInfo.setCommission(Info.getCommission());
            tv_Share_the_money.setText(getString(R.string.now_share));
        }

        if (!TextUtils.isEmpty(Info.getCommission())) {
            mGoodsInfo.setCommission(Info.getCommission());
        }

        if (TextUtils.isEmpty(UserLocalData.getUser(GoodsDetailForKoalaActivity.this).getPartner())) {
            tv_Share_the_money.setText(getString(R.string.now_share));

        } else {
            if (!StringsUtils.isEmpty(Info.getCommission())) {
                if (getString(R.string.now_share).equals(tv_Share_the_money.getText())) {
                    mGoodsInfo.setCommission(Info.getCommission());
                    String muRatioComPrice = MathUtils.getMuRatioComPrice(UserLocalData.getUser(GoodsDetailForKoalaActivity.this).getCalculationRate(), Info.getCommission());
                    setBuyText(Info.getCommission(), Info.getCouponPrice(), Info.getSubsidiesPrice());
                    if (!TextUtils.isEmpty(muRatioComPrice)) {
                        String getRatioSubside = MathUtils.getMuRatioSubSidiesPrice(UserLocalData.getUser(GoodsDetailForKoalaActivity.this).getCalculationRate(), Info.getSubsidiesPrice());
                        String totalSubside = MathUtils.getTotleSubSidies(muRatioComPrice, getRatioSubside);
                        tv_Share_the_money.setText(getString(R.string.goods_share_moeny, totalSubside));
                        setAllIncomeData(muRatioComPrice, getRatioSubside);
                    }

                }
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
         //   gduv_view.setUpdateView(mGoodsInfo, C.SysConfig.COMMISSION_PERCENT_VALUE);
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
            List<String> getBanner = info.getImageList();

            indexbannerdataArray.clear();
            if (getBanner==null)return;
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
                            Intent intent = new Intent(GoodsDetailForKoalaActivity.this, ImagePagerActivity.class);
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
            tv_collect.setTextColor(ContextCompat.getColor(GoodsDetailForKoalaActivity.this, R.color.color_FF3F29));
            collect_bg.setImageResource(R.drawable.icon_shoucanxuanzhong);
            SensorsDataUtil.getInstance().collectProductTrack("", mGoodsInfo.getItemSourceId(), mGoodsInfo.getGoodsTitle(), mGoodsInfo.getCurrentPrice());
        } else {
            collect_bg.setImageResource(R.drawable.icon_shoucang);
            tv_collect.setTextColor(ContextCompat.getColor(GoodsDetailForKoalaActivity.this, R.color.mmhuisezi));
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
        mGoodsInfo=data;
        if (mGoodsInfo == null) {
            return;
        }
        initViewData(mGoodsInfo);
        if (mGoodsInfo.getColler() != 0) {
            switchColler(mGoodsInfo);
        }
        //示详情图片
        List<String> imgs = mGoodsInfo.getDetailImgList();
        if (imgs != null && imgs.size() > 0) {
            mGoodsDetailForPdd.setGoodsDetails(imgs);
          //  initImgFragment(mGoodsDetailForPdd);
        }
        if (!StringsUtils.isEmpty(data.getMarketPrice())) {
            mGoodsInfo.setItemPrice(data.getMarketPrice());
        }
        if (!StringsUtils.isEmpty(data.getCurrentPrice())) {
            mGoodsInfo.setItemVoucherPrice(data.getCurrentPrice());
        }
        if (!StringsUtils.isEmpty(data.getGoodsTitle())) {
            mGoodsInfo.setItemTitle(data.getGoodsTitle());
        }
        if (!StringsUtils.isEmpty(String.valueOf(data.getGoodsId()))) {
            mGoodsInfo.setItemSourceId(String.valueOf(data.getGoodsId()));
        }
        if (!StringsUtils.isEmpty(mGoodsInfo.getImageList().get(0))) {
            mGoodsInfo.setPicture(mGoodsInfo.getImageList().get(0));
        }
        if (isSeavDao && !isRefresh) {
            SensorsDataUtil.getInstance().browseProductTrack("", String.valueOf(data.getGoodsId()));
            if (LoginUtil.checkIsLogin(this, false) && !TextUtils.isEmpty(mGoodsInfo.getGoodsTitle()) && !TextUtils.isEmpty(mGoodsInfo.getImageList().get(0))) {
                mPresenter.saveGoodsHistor(this, mGoodsInfo);
            }
        }

        Paint paint = new Paint();
        paint.setTextSize(iv_taobao.getTextSize());
        float size = paint.measureText(iv_taobao.getText().toString());
        StringsUtils.retractTitles(title,data.getGoodsTitle(), (int) (size) + 35);
        getViewLocationOnScreen();
        dialog.dismiss();
    }

    @Override
    public void setDetaisDataWph(ShopGoodInfo data, boolean seavDao, boolean isRefresh) {

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


    @OnClick({R.id.btn_back, R.id.ll_share_money, R.id.bottomLy, R.id.btn_sweepg, R.id.videopaly_btn, R.id.collect_ly, R.id.ll_home,R.id.ll_shen})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottomLy:
                break;
            case R.id.btn_back:
           // case R.id.btn_tltle_back:
                finish();
                break;
//            case R.id.iv_feedback:
//                if (!LoginUtil.checkIsLogin(GoodsDetailForKoalaActivity.this)) {
//                    return;
//                }
//
//                Intent feedBackIt = new Intent(GoodsDetailForKoalaActivity.this, AppFeedActivity.class);
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
                    mGoodsInfo.setVoucherPrice(mGoodsInfo.getCurrentPrice());
                    mGoodsInfo.setTitle(mGoodsInfo.getGoodsTitle());
                }
                ShareMoneyForKaolaActivity.start(this, mGoodsInfo, mGoodsInfo.getPurchaseLink());

                break;
            case R.id.btn_sweepg: //立即购买
                if (LoginUtil.checkIsLogin(this)) {
                    if (mGoodsInfo.getGoodsDetail()!=null){
                        String burl = mGoodsInfo.getGoodsDetail().replace("https://", "kaola://");
                        if (isHasInstalledKaola()){
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(burl));
                            startActivity(intent);
                        }else{
                            ShowWebActivity.start(this,mGoodsInfo.getGoodsDetail(), "");
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

            case R.id.ll_shen:   //升级
                if (LoginUtil.checkIsLogin(this)) {
                    UserInfo usInfo = UserLocalData.getUser(this);
                    Long coin = usInfo.getMoreCoin();
                    if (usInfo!=null){
                        if (C.UserType.member.equals(usInfo.getUserType())){
                            if (coin>=360){
                                updateGrade();
                            }else{
                                startActivity(new Intent(this, ShopVipActivity.class));
                            }
                        }else{
                            GoodsUtil.getVipH5(this);
                        }
                    }
                }

                break;
            default:
                break;
        }
    }
    /**
     * 升级掌柜的弹框
     */
    private void updateGrade() {
        ShopkeeperUpgradeDialog upgradeDialog = new ShopkeeperUpgradeDialog(this);
        upgradeDialog.setmOkListener(new ShopkeeperUpgradeDialog.OnOkListener() {
            @Override
            public void onClick(View view) {

                updateGradePresenter(GoodsDetailForKoalaActivity.this, Integer.parseInt(C.UserType.vipMember));

            }
        });
        upgradeDialog.show();
    }

    /**
     * 升级
     *
     * @param fragment
     * @param userType
     */
    public void updateGradePresenter(RxAppCompatActivity fragment, int userType) {
        CommInterface.updateUserGrade(fragment, userType)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {


                    }
                })
                .subscribe(new DataObserver<UpdateInfoBean>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
//                        super.onError(errorMsg, errCode);
                        showError(errCode, errorMsg);
                    }

                    @Override
                    protected void onDataListEmpty() {

                    }

                    @Override
                    protected void onSuccess(UpdateInfoBean data) {
                        onGradeSuccess(data);
                    }
                });
    }
    public void showError(String errorNo, String msg) {
        MyLog.i("test", "onFailure: " + this);
        if ("B1100007".equals(errorNo)
                || "B1100008".equals(errorNo)
                || "B1100009".equals(errorNo)
                || "B1100010".equals(errorNo)) {
            ViewShowUtils.showShortToast(this, msg);
        }


    }

    public void onGradeSuccess(UpdateInfoBean info) {
        if (info != null) {
            UserInfo userInfo = UserLocalData.getUser();
            userInfo.setUserType(String.valueOf(info.getUserType()));
            userInfo.setMoreCoin(info.getMoreCoin());
            UserLocalData.setUser(this, userInfo);
            EventBus.getDefault().post(new RefreshUserInfoEvent());
            EventBus.getDefault().post(new MessageEvent(EventBusAction.UPGRADE_SEHNGJI));
//            refreshUserInfo(userInfo);
            initSheng();
            ToastUtils.showShort("升级成功");
        } else {
            MyLog.d("test", "用户信息为空");
        }

    }

    /**
     * 收藏或取消收藏
     */
    private void sumbitCollect() {

        if (mGoodsInfo == null || mGoodsInfo.getGoodsId() == 0
                || TextUtils.isEmpty(mGoodsInfo.getGoodsTitle())) {
            ViewShowUtils.showShortToast(GoodsDetailForKoalaActivity.this, "收藏失败,请稍后再试");
            return;
        }
        LoadingView.showDialog(GoodsDetailForKoalaActivity.this);
        //todo：修复从足迹到收藏报错
        String couponEndTime = mGoodsInfo.getCouponEndTime();

        if (couponEndTime != null && couponEndTime.length() > 5) {
            couponEndTime = DateTimeUtils.formatMMdd(couponEndTime);
            mGoodsInfo.setCouponEndTime(couponEndTime);
        }

        mPresenter.switchKaolaCollect(this, mGoodsInfo);
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
        if (!LoginUtil.checkIsLogin(GoodsDetailForKoalaActivity.this)) {
            return true;
        }
        if (AppUtil.isFastClick(200)) {
            return true;
        }
        if (mGoodsInfo == null) {
            return true;
        }
        if (TextUtils.isEmpty(mGoodsInfo.getCurrentPrice())) {
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
     * 判断是否安装考拉
     *
     * @return
     */
    private boolean isHasInstalledKaola() {
        return AppUtil.checkHasInstalledApp(this, "com.kaola");
    }

    public void getReturning() {

//        LoadingView.showDialog(this, "请求中...");

        RxHttp.getInstance().getCommonService().getConfigForKey(new RequestKeyBean().setKey(C.SysConfig.WEB_COMMSSION_RULE))
                .compose(RxUtils.<BaseResponse<HotKeywords>>switchSchedulers())
                .compose(this.<BaseResponse<HotKeywords>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                    }
                })
                .subscribe(new DataObserver<HotKeywords>() {
                    @Override
                    protected void onSuccess(HotKeywords data) {
                        final String commssionH5 = data.getSysValue();
                        if (!TextUtils.isEmpty(commssionH5)){
                            tv_fan.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ShowWebActivity.start(GoodsDetailForKoalaActivity.this,commssionH5,"");
                                }
                            });
                        }

                        android.util.Log.e("gggg",commssionH5+"");


                    }

                });
    }
}
