package com.zjzy.morebit;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.zjzy.morebit.Activity.SearchActivity;
import com.zjzy.morebit.Activity.ShowWebActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.fragment.base.BaseMainFragmeng;
import com.zjzy.morebit.goods.shopping.ui.fragment.CategoryListFragment;
import com.zjzy.morebit.goodsvideo.ShopMallActivity;
import com.zjzy.morebit.home.fragment.HomeRecommendFragment;
import com.zjzy.morebit.info.ui.fragment.MsgFragment;
import com.zjzy.morebit.interfaces.GuideNextCallback;
import com.zjzy.morebit.interfaces.UpdateColorCallback;
import com.zjzy.morebit.main.model.ConfigModel;
import com.zjzy.morebit.main.ui.fragment.ShoppingListFragment2;
import com.zjzy.morebit.main.ui.myview.xtablayout.XTabLayout;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.HotKeywords;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.SystemConfigBean;
import com.zjzy.morebit.pojo.UserscoreBean;
import com.zjzy.morebit.pojo.event.HomeScore;
import com.zjzy.morebit.pojo.event.HomeSupeListRefreshEvent;
import com.zjzy.morebit.pojo.goods.Child2;
import com.zjzy.morebit.pojo.goods.GoodCategoryInfo;
import com.zjzy.morebit.pojo.home.HomeTopRedPagckageBean;
import com.zjzy.morebit.pojo.home.SysNoticeBean;
import com.zjzy.morebit.purchase.PurchaseActivity;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.ConfigListUtlis;
import com.zjzy.morebit.utils.GuideViewUtil;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MyGsonUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.OpenFragmentUtils;
import com.zjzy.morebit.utils.SensorsDataUtil;
import com.zjzy.morebit.utils.SwipeDirectionDetector;
import com.zjzy.morebit.utils.UI.BannerInitiateUtils;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.action.MyAction;
import com.zjzy.morebit.view.HomeCategoryPopWindow;
import com.zjzy.morebit.view.PerfectArcView;
import com.zjzy.morebit.view.UPMarqueeView;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Action;


/**
 * Created by YangBoTian on 2018/9/10.
 *
 * 首页fragment
 */

public class HomeFragment extends BaseMainFragmeng implements AppBarLayout.OnOffsetChangedListener, UpdateColorCallback {
    @BindView(R.id.xTablayout)
    XTabLayout mXTabLayout;
    @BindView(R.id.home_pager)
    ViewPager mViewPager;
    @BindView(R.id.status_bar)
    View status_bar;
    @BindView(R.id.searchLayout)
    View searchLayout;
    @BindView(R.id.iv_hongbao)
    ImageView iv_hongbao;
    @BindView(R.id.ll_home_search)
    LinearLayout ll_home_search;
    @BindView(R.id.categoryLayout)
    LinearLayout mCategoryLayout;
    @BindView(R.id.room_view)
    RelativeLayout mRoomView;
    @BindView(R.id.arcBgView)
    PerfectArcView mArcBgView;
    @BindView(R.id.perfectArcBg)
    View perfectArcBg;
    @BindView(R.id.home_msg)
    ImageView home_msg;
    @BindView(R.id.btn_service)
    ImageView btn_service;
    @BindView(R.id.more_category)
    AppCompatImageButton mMoreCategoryBtn;
    @BindView(R.id.search_rl)
    RelativeLayout search_rl;
    @BindView(R.id.rl_urgency_notifi)
    RelativeLayout rl_urgency_notifi;
    @BindView(R.id.searchTv)
    TextView searchTv;
    @BindView(R.id.searchIconIv)
    ImageView searchIconIv;
    View noLoginView;

    HomeAdapter mHomeAdapter;
    List<GoodCategoryInfo> mHomeColumns = new ArrayList<>();

    private String mRedpackagecode;
    private int mSearchHeight;
    private boolean mIsPageHome = true;
    //    public static UserscoreBean mUserscoreBean;
//    @BindView(R.id.dot)
//    View dot;
    HomeCategoryPopWindow mHomeCategoryPopWindow;
    private List<View> mHotviews = new ArrayList<>();

    //一个结束颜色
    int lastChangeColor;
    private List<SysNoticeBean> mSysNoticeData;
    private int mSysNoticePosition;
    private View mHomeNoticeView;
    private UPMarqueeView mUpview;
    private int currentViewPagerPosition = 0;
    private SwipeDirectionDetector swipeDirectionDetector;
    private boolean lastChangeBg = false;
    private boolean lastChangeTab = false;
    private int sumDy = 0;
    private boolean isStatusBarDark = false;
    private GuideNextCallback mGuideNextCallback;
    private boolean isShowGuide = false;
    private Handler mHandler;
    private List<HomeRecommendFragment> fragments = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home1, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow()
                .getDecorView()
                .getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            getActivity().getWindow()
                                    .getDecorView()
                                    .getViewTreeObserver()
                                    .removeOnGlobalLayoutListener(this);
                        } else {
                            getActivity().getWindow()
                                    .getDecorView()
                                    .getViewTreeObserver()
                                    .removeGlobalOnLayoutListener(this);
                        }

                    }
                });
        initView();
        initData();
        EventBus.getDefault().register(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        getDot();
        if (LoginUtil.checkIsLogin(getActivity(), false) && UserLocalData.isShowGuide() && !isShowGuide) {
            isShowGuide = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showGuideSearch();
                }
            }, 500);

        }
    }

    private void initData() {
        GoodCategoryInfo good = new GoodCategoryInfo();
        good.setName(getString(R.string.choiceness));
        mHomeColumns.add(good);
        GoodCategoryInfo goodWhatLink = new GoodCategoryInfo();
        goodWhatLink.setName(getString(R.string.what_like));
        mHomeColumns.add(goodWhatLink);
        getSupeListData();
        getTopRedPackage();
        getCommissionPercent();
//        getUserscore();
//        if (LoginUtil.checkIsLogin(getActivity(), false)) {
//            getImportantNotice();
//        }
    }

    private void getSupeListData() {
        List<GoodCategoryInfo> getArrGcInfo = (List<GoodCategoryInfo>) App.getACache().getAsObject(C.sp.homeFenleiData);
        if (getArrGcInfo != null) {  //有缓存
            mHomeColumns.addAll(getArrGcInfo);
            setupViewPager(mHomeColumns);
            getCategoryData(true);
        } else {
            getCategoryData(false);
        }
    }

    private void initView() {
        WindowManager wm1 = ((Activity) getActivity()).getWindowManager();
        int mWindowWidth = wm1.getDefaultDisplay().getWidth();
        mSearchHeight = (int) (mWindowWidth / 7.7125);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //处理全屏显示
            ViewGroup.LayoutParams viewParams = status_bar.getLayoutParams();
            viewParams.height = ActivityStyleUtil.getStatusBarHeight(getActivity());
            status_bar.setLayoutParams(viewParams);

        }
        fragments.clear();
        mHomeAdapter = new HomeAdapter(getChildFragmentManager());
        swipeDirectionDetector = new SwipeDirectionDetector();
        mViewPager.setAdapter(mHomeAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                swipeDirectionDetector.onPageScrolled(position, positionOffset, positionOffsetPixels);

            }

            @Override
            public void onPageSelected(int position) {
                swipeDirectionDetector.onPageSelected(position);
                currentViewPagerPosition = position;
                if (position != 0) {
                    mIsPageHome = false;
                    isStatusBarDark = true;
                    setTopBg(false, getResources().getColor(R.color.white), false);
                } else {
                    mIsPageHome = true;
                    isStatusBarDark = false;
                    setTopBg(true, lastChangeColor, true);
                }

                SensorsDataUtil.getInstance().catalogClickTrack("", mHomeColumns.get(position).getId() + "", mHomeColumns.get(position).getName());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                swipeDirectionDetector.onPageScrollStateChanged(state);
                MyLog.d("addOnPageChangeListener", " onPageScrollStateChanged  state = " + state);
            }
        });
        mViewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mViewPager.getViewTreeObserver().removeGlobalOnLayoutListener(this);

            }
        });
        getLoginView();
    }

    public void getLoginView(){
        boolean isLogin = LoginUtil.checkIsLogin(getActivity(), false);
        if (!isLogin) {
            if(getActivity()==null){
                return;
            }
            if(noLoginView==null){
                noLoginView= LayoutInflater.from(getActivity()).inflate(R.layout.view_home_no_login, null);
                noLoginView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LoginUtil.goToPasswordLogin(getActivity());
                    }
                });
            }

            if(rl_urgency_notifi!=null){
                rl_urgency_notifi.removeAllViews();
                rl_urgency_notifi.addView(noLoginView);
            }

        } else {
            if(rl_urgency_notifi!=null){
                rl_urgency_notifi.removeAllViews();
            }

        }
    }

    private void setupViewPager(final List<GoodCategoryInfo> homeColumns) {
        //MyLog.i("test","setupViewPager");
        mHomeAdapter.setHomeColumns(homeColumns);
        mHomeAdapter.notifyDataSetChanged();
        mXTabLayout.setupWithViewPager(mViewPager);
        // mXTabLayout.setViewPager(mViewPager);

    }

    /**
     * Called when the {@link AppBarLayout}'s layout offset has been changed. This allows
     * child views to implement custom behavior based on the offset (for instance pinning a
     * view at a certain y value).
     *
     * @param appBarLayout   the {@link AppBarLayout} which offset has changed
     * @param verticalOffset the vertical offset for the parent {@link AppBarLayout}, in px
     */
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        MyLog.d("verticalOffset  " + verticalOffset + " mSearchHeight " + mSearchHeight);
        if (!mIsPageHome) {
            return;
        }
        if (verticalOffset == 0) {
        } else if (verticalOffset == -mSearchHeight) {

        }
    }


    public void selectFirst() {
        try {
            if (mViewPager != null) {
                mViewPager.setCurrentItem(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSysNotificationData(List<ImageInfo> data) {
        boolean isLogin = LoginUtil.checkIsLogin(getActivity(), false);
        if (isLogin) {
            if (data != null && data.size() != 0) {
                for (int i = 0; i < data.size(); i++) {
                    ImageInfo imageInfo = data.get(i);
                    if (imageInfo.getDisplayPage() == 1 && imageInfo.getMediaType() == 3) {//首页
                        Integer index = (Integer) App.getACache().getAsObject(C.sp.CLESE_RECOMMEND_GOODS + UserLocalData.getUser().getPhone() + imageInfo.getId());
                        index = index == null ? 0 : index;
                        if (index != null && index < 2) {
                            addRecommendGoodsView(imageInfo, index);
                        }
                        return;
                    }
                }
            }

        }
    }

    private class HomeAdapter extends FragmentPagerAdapter {
        private List<GoodCategoryInfo> mHomeColumns = new ArrayList<>();

        public HomeAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }


        public void setHomeColumns(List<GoodCategoryInfo> homeColumns) {
            mHomeColumns.clear();
            if (homeColumns == null && homeColumns.size() == 0) {
                return;
            }
            if (mHomeColumns.size() == 1) {
                for (GoodCategoryInfo info : homeColumns) {
                    GoodCategoryInfo goodCategoryInfo = mHomeColumns.get(0);
                    if (goodCategoryInfo.getName().equals(info.getName())) {
                        continue;
                    }
                    mHomeColumns.add(info);
                }
            } else {
                mHomeColumns.addAll(homeColumns);
            }
        }


        @Override
        public Fragment getItem(int position) {
            GoodCategoryInfo homeColumn = mHomeColumns.get(position);
            if (getString(R.string.choiceness).equals(homeColumn.getName())) {
                HomeRecommendFragment homeRecommendFragment = HomeRecommendFragment.newInstance();
                homeRecommendFragment.setUpdateColorCallback(HomeFragment.this);
                fragments.add(homeRecommendFragment);
                return homeRecommendFragment;
            } else if (getString(R.string.what_like).equals(homeColumn.getName())) {
                ShoppingListFragment2 whatLikeFragment = ShoppingListFragment2.newInstance(C.GoodsListType.WHAT_LIKE);
                return whatLikeFragment;
            } else {
                List<Child2> childs = homeColumn.getChild2();
                return CategoryListFragment.newInstance(homeColumn.getName(), childs);

            }

        }

        @Override
        public int getCount() {
            return mHomeColumns.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            GoodCategoryInfo homeColumn = mHomeColumns.get(position);
            return homeColumn.getName();
        }

    }

    @OnClick({R.id.search_rl, R.id.home_msg, R.id.iv_hongbao, R.id.more_category, R.id.btn_service})
    public void Onclick(View v) {
        switch (v.getId()) {
            case R.id.search_rl:  //搜索
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.home_msg:
                if (!LoginUtil.checkIsLogin(getActivity())) {
                    return;
                }
                //消息
                OpenFragmentUtils.goToSimpleFragment(getActivity(), MsgFragment.class.getName(), new Bundle());


                break;
            case R.id.iv_hongbao:
                if (!TextUtils.isEmpty(mRedpackagecode)) {
                    try {
                        AppUtil.coayTextPutNative(getActivity(), mRedpackagecode);
                        PackageManager packageManager
                                = getActivity().getApplicationContext().getPackageManager();
                        Intent intent = packageManager.
                                getLaunchIntentForPackage("com.eg.android.AlipayGphone");
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        ViewShowUtils.showShortToast(getActivity(), "未安装支付宝哦");
                    }
                }
                break;
            case R.id.more_category:
//                setCategoryChange();
//                showPopupWindow();
                gotoCategory();
//                FenleiFragment.start(getActivity());
                break;
            case R.id.btn_service:

                //客服
                if (LoginUtil.checkIsLogin(getActivity())) {
                    getService();
                }

                break;
        }
    }

    private void gotoCategory() {
        ConfigListUtlis.getConfigList(this,ConfigListUtlis.getConfigAllKey(), new MyAction.One<List<SystemConfigBean>>() {
            @Override
            public void invoke(List<SystemConfigBean> arg) {
                SystemConfigBean bean = ConfigListUtlis.getSystemConfigBean(C.ConfigKey.CATEGORY_URL);
                if(bean!=null){
                    ShowWebActivity.start(getActivity(),bean.getSysValue(),"分类");
                }
            }
        });
    }

    private void getService() {
        ConfigModel.getInstance().getSystemCustomService((RxAppCompatActivity) getActivity())
                .subscribe(new DataObserver<String>() {
                    @Override
                    protected void onSuccess(String data) {
                        if (!TextUtils.isEmpty(data)) {
                            ShowWebActivity.start(getActivity(), data, getString(R.string.service));
                        }
                    }
                });
    }

    private void clickSysNoticeClose() {
        try {
            ArrayList<String> ids = new ArrayList<>();
            for (int i = 0; i < mSysNoticeData.size(); i++) {
                SysNoticeBean sysNoticeBean = mSysNoticeData.get(i);
                ids.add(sysNoticeBean.getId());
            }
            App.getACache().put(UserLocalData.getUser().getPhone() + C.sp.SysNoticeDataIds, ids);
            mRoomView.removeView(mHomeNoticeView);

        } catch (Exception e) {
            e.printStackTrace();
            //隐藏
            mRoomView.removeView(mHomeNoticeView);
        }

    }



    /**
     * 获取首页头部分类数据(超级分类）
     *
     * @param isCache 是否有缓存
     */
    public void getCategoryData(final boolean isCache) {

        RxHttp.getInstance().getGoodsService().getHomeSuerListData()
                .compose(RxUtils.<BaseResponse<List<GoodCategoryInfo>>>switchSchedulers())
                .compose(this.<BaseResponse<List<GoodCategoryInfo>>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (!isCache) {//没有缓存，设置tab
                            setupViewPager(mHomeColumns);
                        }
                    }
                })
                .subscribe(new DataObserver<List<GoodCategoryInfo>>() {

                    @Override
                    protected void onSuccess(List<GoodCategoryInfo> data) {
                        App.getACache().put(C.sp.homeFenleiData, (Serializable) data);
                        if (!isCache) {  // 没有缓存，设置数据
                            mHomeColumns.addAll(data);
                        }
                    }
                });
    }

    /**
     * 获取商品的分佣比例
     */
    public void getCommissionPercent(){
        ConfigModel.getInstance().getConfigForKey((RxAppCompatActivity) getActivity(), C.SysConfig.COMMISSION_PERCENT)
                .subscribe(new DataObserver<HotKeywords>() {
                    @Override
                    protected void onSuccess(HotKeywords data) {
                        String sysValue = data.getSysValue();
                        if (!TextUtils.isEmpty(sysValue)) {
                            C.SysConfig.COMMISSION_PERCENT_VALUE = sysValue;

                        }
                    }
                });
    }
    /**
     * 红包轮播
     */
    public void getTopRedPackage() {
        ConfigModel.getInstance().getConfigForKey((RxAppCompatActivity) getActivity(), C.SysConfig.RECEIVE_RED_PACKET)
                .subscribe(new DataObserver<HotKeywords>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        iv_hongbao.setVisibility(View.GONE);
                    }

                    @Override
                    protected void onSuccess(HotKeywords data) {
                        String sysValue = data.getSysValue();
                        HomeTopRedPagckageBean bean = (HomeTopRedPagckageBean) MyGsonUtils.jsonToBean(sysValue, HomeTopRedPagckageBean.class);
                        if (bean != null && bean.getStatus() == 1) {
                            iv_hongbao.setVisibility(View.VISIBLE);
                            Animation loadAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
                            iv_hongbao.startAnimation(loadAnimation);
                            if (!TextUtils.isEmpty(bean.getCode())) {
                                mRedpackagecode = bean.getCode();
                                AppUtil.coayTextPutNative(getActivity(), bean.getCode());
                            }
                        } else {
                            iv_hongbao.setVisibility(View.GONE);
                        }
                    }
                });
    }


    /**
     * 获取消息红点
     */
    public void getUserscore() {
        if (LoginUtil.checkIsLogin(getActivity(), false)) {
            RxHttp.getInstance().getCommonService().getNoreadCount()
                    .compose(RxUtils.<BaseResponse<UserscoreBean>>switchSchedulers())
                    .compose(this.<BaseResponse<UserscoreBean>>bindToLifecycle())
                    .subscribe(new DataObserver<UserscoreBean>() {

                        @Override
                        protected void onSuccess(UserscoreBean data) {
//                            mUserscoreBean = data;
                            getDot();
                        }
                    });
        }
    }

    /**
     * 获取系统提示消息
     */
//    public void getImportantNotice() {
//        RxHttp.getInstance().getCommonService().getImportantNotice()
//                .compose(RxUtils.<BaseResponse<List<SysNoticeBean>>>switchSchedulers())
//                .compose(this.<BaseResponse<List<SysNoticeBean>>>bindToLifecycle())
//                .subscribe(new DataObserver<List<SysNoticeBean>>(false) {
//
//                    @Override
//                    protected void onSuccess(List<SysNoticeBean> data) {
//                        ArrayList<SysNoticeBean> sysNoticeList = new ArrayList<>();
//                        ArrayList<String> ids = (ArrayList<String>) App.getACache().getAsObject(UserLocalData.getUser().getPhone() + C.sp.SysNoticeDataIds);
//                        for (int i = 0; i < data.size(); i++) {
//                            SysNoticeBean sysNoticeBean = data.get(i);
//                            if (ids == null) {
//                                sysNoticeList.addAll(data);
//                            } else {
//                                if (!ids.contains(sysNoticeBean.getId())) {
//                                    sysNoticeList.add(sysNoticeBean);
//                                }
//                            }
//                        }
//                        if (sysNoticeList.size() != 0) {
//                            mSysNoticeData = sysNoticeList;
//                            refreshSysNoticeView();
//                        }
//                    }
//                });
//    }
    private void getDot() {
//        if (mUserscoreBean != null) {
//            if ((mUserscoreBean.getIncomeCount() + mUserscoreBean.getFansCount() + mUserscoreBean.getSystemCount()) > 0) {
//                dot.setVisibility(View.VISIBLE);
//            } else {
//                dot.setVisibility(View.GONE);
//            }
//        } else {
//            dot.setVisibility(View.GONE);
//        }

    }


    @Subscribe  //订阅事件
    public void onEventMainThread(HomeSupeListRefreshEvent event) {
        if (mHomeColumns != null && mHomeColumns.size() == 1) {
            getSupeListData();
        }
    }

    @Subscribe  //订阅事件
    public void onEventMainThread(HomeScore event) {
        MyLog.i("test", "even");
//        getUserscore();
    }


    @Override
    protected void onVisible() {
        super.onVisible();
        if (isAdded() && currentViewPagerPosition == 0) { //条件是精选
            setTopBg(lastChangeBg, lastChangeColor, lastChangeTab);
        }
    }

    @Override
    protected void onInvisible() {
        super.onInvisible();
        //界面不可见，恢复默认白色
        if (isAdded()) {
            setTopBg(false, getResources().getColor(R.color.white), false);
        } else {
            if (null != home_msg) home_msg.setImageResource(R.drawable.icon_xiaoxi_black);
            if (null != btn_service)
                btn_service.setImageResource(R.drawable.icon_home_service_black);
        }
        isShowGuide = false;
    }

    /**
     * 修改顶部的背景
     *
     * @param ischange    false 为默认，true，随着轮播改变
     * @param changeColor
     */
    private void setTopBg(boolean ischange, int changeColor, boolean ischangeTab) {
        if (this == null){return;}
        lastChangeBg = ischange;
        if (sumDy > 300) {
            ischange = false;
            ischangeTab = false;
        }
        if (mArcBgView != null) {
            if (mArcBgView.getColor() != changeColor) {
                if(changeColor == getResources().getColor(R.color.white)){
                    perfectArcBg.setVisibility(View.GONE);
                    mArcBgView.setVisibility(View.GONE);
                }else{
                    perfectArcBg.setVisibility(View.VISIBLE);
                    mArcBgView.setVisibility(View.VISIBLE);
                }
                mArcBgView.setColor(changeColor, changeColor);
            }
        }
        if (ischangeTab != isStatusBarDark) {
            isStatusBarDark = ischangeTab;
            ImmersionBar.with(this)
                    .statusBarDarkFont(!ischange)
                    .init();
            setXTabLayoutColor(ischangeTab);
        }

//        if(ischangeTab){
//            search_rl.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.bg_input_white_trans_30dp));
//            searchTv.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
//            searchIconIv.setImageResource(R.drawable.icon_home_top_search);
//        }else{
//            search_rl.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.bg_input_white_round_30dp));
//            searchTv.setTextColor(ContextCompat.getColor(getActivity(),R.color.color_999999));
//            searchIconIv.setImageResource(R.drawable.icon_sousuo);
//        }


    }

    /**
     *
     * @param ischangeDefault default 是底色是白，文字是红，指示线也是红
     */
    private void setXTabLayoutColor(boolean ischangeDefault) {

        if (!ischangeDefault) {
            mXTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.color_FFD800));
            mXTabLayout.setTabTextColors(getResources().getColor(R.color.color_666666), getResources().getColor(R.color.color_333333));
            home_msg.setImageResource(R.drawable.icon_xiaoxi_black);
            btn_service.setImageResource(R.drawable.icon_home_service_black);
            mMoreCategoryBtn.setImageResource(R.drawable.icon_quanbu_yellow);
            search_rl.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.bg_input_white_round_30dp));
            searchTv.setTextColor(ContextCompat.getColor(getActivity(),R.color.color_999999));
            searchIconIv.setImageResource(R.drawable.icon_sousuo);
        } else {
            mXTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));
            mXTabLayout.setTabTextColors(getResources().getColor(R.color.white), getResources().getColor(R.color.white));
            home_msg.setImageResource(R.drawable.icon_xiaoxi);
            btn_service.setImageResource(R.drawable.icon_home_service);
            mMoreCategoryBtn.setImageResource(R.drawable.icon_quanbu);
            search_rl.setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.bg_input_white_trans_30dp));
            searchTv.setTextColor(ContextCompat.getColor(getActivity(),R.color.white));
            searchIconIv.setImageResource(R.drawable.icon_home_top_search);
        }

    }


    @Override
    public void updateColor(int color, boolean isWhite, int updateSumDy) {
        int changeColor = color;
        if (changeColor != 0 && isFragmentVisiable()) {
            sumDy = updateSumDy;
            setTopBg(!isWhite, changeColor, !isWhite);
            lastChangeColor = changeColor;
            if (updateSumDy >= 300) {
                lastChangeTab = false;
            } else {
                lastChangeTab = true;
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    public void setmGuideNextCallback(GuideNextCallback mGuideNextCallback) {
        this.mGuideNextCallback = mGuideNextCallback;
    }

    public void showGuideSearch() {
        if (null != search_rl) {
            GuideViewUtil.showGuideView(getActivity(), search_rl, GuideViewUtil.GUIDE_SEARCH, 0, this.mGuideNextCallback, null);
        }
    }

    private void addRecommendGoodsView(final ImageInfo imageInfo, final int index) {
        if(getActivity()==null){
            return;
        }
        View recommendGoodsView = LayoutInflater.from(getActivity()).inflate(R.layout.view_home_recommend_goods, null);
        rl_urgency_notifi.removeAllViews();
        rl_urgency_notifi.addView(recommendGoodsView);

        ImageView iv_picture = recommendGoodsView.findViewById(R.id.iv_picture);
        ImageView iv_icon = recommendGoodsView.findViewById(R.id.iv_icon);
        TextView tv_desc = recommendGoodsView.findViewById(R.id.tv_desc);
        TextView tv_title = recommendGoodsView.findViewById(R.id.tv_title);
        tv_desc.setText(imageInfo.getDesc());
        tv_title.setText(imageInfo.getTitle());
        LoadImgUtils.setImg(getActivity(), iv_picture, imageInfo.getPicture());
        LoadImgUtils.setImg(getActivity(), iv_icon, imageInfo.getIcon(), R.drawable.home_recommend_goods_dz);
        recommendGoodsView.findViewById(R.id.iv_to_below).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleseRecommendGoodsView(imageInfo, index);
            }
        });
        recommendGoodsView.findViewById(R.id.tv_clese).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleseRecommendGoodsView(imageInfo, 2);
            }
        });
        recommendGoodsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BannerInitiateUtils.gotoAction(getActivity(),imageInfo);
            }
        });
        if (mHandler == null) {
            mHandler = new Handler();
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                cleseRecommendGoodsView(imageInfo, index);
            }
        }, 5000);
    }

    private void cleseRecommendGoodsView(ImageInfo imageInfo, int index) {
        int id = imageInfo.getId();
        index++;
        App.getACache().put(C.sp.CLESE_RECOMMEND_GOODS + UserLocalData.getUser().getPhone() + id, index);
        if (rl_urgency_notifi != null)
            rl_urgency_notifi.removeAllViews();
        if(null != mHomeAdapter && fragments.size()>0){
            HomeRecommendFragment homeRecommendFragment =  fragments.get(0);
            homeRecommendFragment.setTopButtonPosition();
        }
    }

    public void cleseRecommendGoodsView() {
//        if (rl_urgency_notifi != null)
//            rl_urgency_notifi.removeAllViews();
        if(null != mHomeAdapter && fragments.size()>0){
            HomeRecommendFragment homeRecommendFragment =  fragments.get(0);
            homeRecommendFragment.setTopButtonPosition();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(!isVisibleToUser){
            if(null != mHomeAdapter && fragments.size()>0){
                MyLog.i("test","setUserVisibleHint");
                HomeRecommendFragment homeRecommendFragment =  fragments.get(0);
                homeRecommendFragment.setUserVisibleHint(isVisibleToUser);
            }
        }
    }


}
