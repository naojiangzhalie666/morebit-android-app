package com.zjzy.morebit.home.fragment;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.github.jdsjlzx.ItemDecoration.SpaceItemDecoration;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.Activity.ShowWebActivity;
import com.zjzy.morebit.App;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.View.ReUseStaggeredView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.FloorAdapter;
import com.zjzy.morebit.adapter.HomeMenuAdapter;
import com.zjzy.morebit.adapter.HomeRecommendAdapter;
import com.zjzy.morebit.adapter.SecondModuleAdapter;
import com.zjzy.morebit.contact.EventBusAction;
import com.zjzy.morebit.fragment.PanicBuyFragment;
import com.zjzy.morebit.goodsvideo.VideoClassActivity;
import com.zjzy.morebit.home.adpater.ActivityAdapter;
import com.zjzy.morebit.home.adpater.ShakeGoodsAdapter;
import com.zjzy.morebit.home.contract.HomeRecommentContract;
import com.zjzy.morebit.home.presenter.HomeRecommendPresenter;
import com.zjzy.morebit.interfaces.UpdateColorCallback;
import com.zjzy.morebit.main.ui.NoticeActivity;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.FloorInfo;
import com.zjzy.morebit.pojo.HomeRecommendGoods;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.PanicBuyTiemBean;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.SystemConfigBean;
import com.zjzy.morebit.pojo.UserFeedback;
import com.zjzy.morebit.pojo.event.HomeSupeListRefreshEvent;
import com.zjzy.morebit.pojo.event.LogoutEvent;
import com.zjzy.morebit.pojo.goods.FloorBean;
import com.zjzy.morebit.pojo.goods.GoodCategoryInfo;
import com.zjzy.morebit.pojo.goods.HandpickBean;
import com.zjzy.morebit.pojo.goods.NewRecommendBean;
import com.zjzy.morebit.pojo.goods.VideoBean;
import com.zjzy.morebit.pojo.request.RequestPanicBuyTabBean;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.ConfigListUtlis;
import com.zjzy.morebit.utils.DateTimeUtils;
import com.zjzy.morebit.utils.DensityUtil;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.OpenFragmentUtils;
import com.zjzy.morebit.utils.SensorsDataUtil;
import com.zjzy.morebit.utils.SpaceItemDecorationUtils;
import com.zjzy.morebit.utils.StringsUtils;
import com.zjzy.morebit.utils.UI.BannerInitiateUtils;
import com.zjzy.morebit.utils.UIUtils;
import com.zjzy.morebit.utils.action.MyAction;
import com.zjzy.morebit.view.AspectRatioView;
import com.zjzy.morebit.view.GridSpacingItemDecoration;
import com.zjzy.morebit.view.UPMarqueeView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.youth.banner.Banner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by liuhaiping on 2019/1/05.
 * <p>
 * 首页精选
 */

public class HomeRecommendFragment extends MvpFragment<HomeRecommendPresenter> implements HomeRecommentContract.View, View.OnClickListener {
    private static final int REQUEST_COUNT = 10;
    ReUseStaggeredView mReUseListView;
    private RecyclerView mActivityRecyclerView;
    private ActivityAdapter mActivityAdapter;
    private RecyclerView mSecondModuleRv;
    private SecondModuleAdapter mSecondModuleAdapter;
    private Banner mMakeMoneyBanner;
    private Banner mTitleBanner;
    private Banner mOfficialBanner;
    private AspectRatioView mAsMakeMoneyBanner;
    private AspectRatioView mAsTitleBanner;
    private AspectRatioView mAsOfficialBanner;
    private RecyclerView mFloorRv;
    private ImageView bannerSpace;
    //    private View status_bar;
    private LinearLayout mLlActivity;
    private List<ImageInfo> mCategorys = new ArrayList<>();
    View mHeadView;
    private HomeRecommendAdapter mAdapter;
    private int mMinNum;
    UPMarqueeView mUpview;
    private ImageView mMoreNoticeIv;
    private int page = 1;
    List<View> mHotviews = new ArrayList<>();
    private List<GoodCategoryInfo> mGetArrGcInfo;
    private RecyclerView mViewpger_category;
    private View bar_line;
    private FrameLayout scrollbarLayout;
    private HomeMenuAdapter menuAdapter;
    private LinearLayout secondLayout;
    private View officalFrameLayout;
    private LinearLayout floorRootLayout, litmited_time;
    private TextView recommendTitleTv;
    private ImageView go_recommod, img_limted2, img_limted1;
    private ImageView go_top;
    private ImageView floatIv;
    private RelativeLayout parentNotify;
    List<Integer> topBannerColors = new ArrayList<>();

    //一个结束颜色
    int endColor;
    int sumDy = 0; //用于recycleview滑动的计算
    int recycleStartColor;
    int currentBannerPosition = 0;
    int recycleEndColor;
    UpdateColorCallback mUpdateColorCallback;
    boolean bannerVisiable = true;
    private FloorAdapter mFloorAdapter;
    private int mType;
    float endX = 0;
    private boolean isLoadData = false;
    private boolean isSetBannerColor = false;
    private int moveMarginHeight = 0;
    private int originMarginHeight = 0;
    private int originTopMarginHeight = 0;
    private int mScreenWidth;

    private boolean isNearEadge = false;

    private RecyclerView rcy_shakegoods;
    private ImageInfo mImageInfo;
    private TextView shake_more;
    private TextView tv_limitime;
    private List<PanicBuyTiemBean> mPanicBuyTiemBean;
    private CountDownTimer mCountDownTiemr;

    public static HomeRecommendFragment newInstance() {
        Bundle args = new Bundle();
        HomeRecommendFragment fragment = new HomeRecommendFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setUpdateColorCallback(UpdateColorCallback updateColorCallback) {
        this.mUpdateColorCallback = updateColorCallback;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            super.onCreateView(inflater, container, savedInstanceState);
            mHeadView = LayoutInflater.from(getActivity()).inflate(R.layout.home_recommend_head, null);
            init();
        }
        ViewGroup parent = (ViewGroup) mView.getParent();
        if (parent != null) {
            parent.removeView(mView);
        }
        return mView;
    }


    private void init() {
        initOtherView();
        initBanner();
        initRecyclerView();
        getCache();
        setBigTag(false);
//        refreshData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            MyLog.i("test", "setBigTag");
            setBigTag(false);
        }
    }

    /**
     * 获取缓存
     */
    private void getCache() {
        mGetArrGcInfo = (List<GoodCategoryInfo>) App.getACache().getAsObject(C.sp.homeFenleiData);
        getBannerCache();
        //设置 聚划算活动图
        List<HandpickBean> activityBeanDatas = (List<HandpickBean>) App.getACache().getAsObject(C.sp.homeActivityData);
        if (activityBeanDatas != null) {
            mActivityAdapter.replace(activityBeanDatas);
            mActivityAdapter.notifyDataSetChanged();
        }
        List<ImageInfo> secondCacheData = (List<ImageInfo>) App.getACache().getAsObject(C.sp.UIShow + C.UIShowType.HomeSecond);
        if (null != secondCacheData) {
            setSecondModule(secondCacheData);
        }
        List<HomeRecommendGoods> goodsList = (List<HomeRecommendGoods>) App.getACache().getAsObject(C.sp.homeListData);

        if (goodsList != null) {
            mAdapter.setNewData(goodsList);
//            mAdapter.notifyDataSetChanged();
        }


        List<FloorInfo> mFloorCacheData = (List<FloorInfo>) App.getACache().getAsObject(C.sp.FLOOR_CACHE);
        if (null != mFloorCacheData) {
            setFloorLayout(mFloorCacheData);
        }
    }

    /**
     * 获取所有广告缓存
     */
    private void getBannerCache() {
        List<ImageInfo> hots = (List<ImageInfo>) App.getACache().getAsObject(C.sp.UIShow + C.UIShowType.Noticebar);//最火标题滑动
        List<ImageInfo> makeMoney = (List<ImageInfo>) App.getACache().getAsObject(C.sp.UIShow + C.UIShowType.Makemoney); //赚钱计划
        List<ImageInfo> title = (List<ImageInfo>) App.getACache().getAsObject(C.sp.UIShow + C.UIShowType.HomeBanner);  //顶部
        List<ImageInfo> officals = (List<ImageInfo>) App.getACache().getAsObject(C.sp.UIShow + C.UIShowType.Official);  //
        List<ImageInfo> homeIcon = (List<ImageInfo>) App.getACache().getAsObject(C.sp.UIShow + C.UIShowType.HomeIcon);  //
        setCategorys(homeIcon);
        setHotView(hots);
        setBannerColors(title);
        BannerInitiateUtils.setBrandBanner(getActivity(), makeMoney, mMakeMoneyBanner, mAsMakeMoneyBanner);
        BannerInitiateUtils.setBrandBanner(getActivity(), title, mTitleBanner, mAsTitleBanner);
        BannerInitiateUtils.setBrandBanner(getActivity(), officals, mOfficialBanner, mAsOfficialBanner);
        MyLog.i("test", "off: " + officals);
    }


    private void refreshData() {
        setBannerData();
        mPresenter.getActivities(this, 1);
        mMinNum = 0;
        mType = 0;
        page = 1;
        isNearEadge = false;
        setRecommendData();
        if (isLoadData) initTitleUI();
        //mPresenter.getOfficalList(this);
    }

    /**
     * 设置轮播图数据
     */
    private void setBannerData() {
//        mPresenter.getBanner(this, C.UIShowType.Brandsale);
//        mPresenter.getBanner(this, C.UIShowType.Taobao_v2);
        mPresenter.getBanner(this, C.UIShowType.Noticebar);
        mPresenter.getBanner(this, C.UIShowType.Makemoney);
        mPresenter.getBanner(this, C.UIShowType.Official);
        mPresenter.getBanner(this, C.UIShowType.HomeBanner);
        mPresenter.getBanner(this, C.UIShowType.HomeIcon);
        //二级模块
        mPresenter.getBanner(this, C.UIShowType.HomeSecond);
        //楼层管理
        mPresenter.getFloor(this);
        //首页悬浮窗
        mPresenter.getBanner(this, C.UIShowType.FLOAT_AD);
        mPresenter.getVideo(this);//首页抖货商品
    }

    private void setRecommendData() {
        mPresenter.getNewRecommend(this, page, mMinNum, mType);
    }

    /**
     * 初始化一些view
     */
    private void initOtherView() {
        mUpview = (UPMarqueeView) mHeadView.findViewById(R.id.upview);
        mMoreNoticeIv = (ImageView) mHeadView.findViewById(R.id.moreNoticeIv);
        mAsMakeMoneyBanner = (AspectRatioView) mHeadView.findViewById(R.id.as_banner_make_money);
        mAsOfficialBanner = mHeadView.findViewById(R.id.as_banner_offical);
        mLlActivity = (LinearLayout) mHeadView.findViewById(R.id.ll_activity);
        mMoreNoticeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NoticeActivity.actionStart(getActivity());
            }
        });
        setModuleView();

        //楼层
        initFloorView();
    }

    private void initTitleUI() {
        ConfigListUtlis.getConfigListCacheNet((RxAppCompatActivity) getActivity(), ConfigListUtlis.getConfigAllKey(), new MyAction.One<List<SystemConfigBean>>() {
            @Override
            public void invoke(List<SystemConfigBean> arg) {
                SystemConfigBean noticeBean = ConfigListUtlis.getSystemConfigBean(C.ConfigKey.NOTICE);
                SystemConfigBean recommendBean = ConfigListUtlis.getSystemConfigBean(C.ConfigKey.HOME_RECOMMENDED_TITLE);
                if (noticeBean != null && noticeBean.getSysValue().equals("1")) {
                    mMoreNoticeIv.setVisibility(View.VISIBLE);
                }
                if (null != recommendBean && !TextUtils.isEmpty(recommendBean.getSysValue())) {
                    recommendTitleTv.setText(recommendBean.getSysValue());
                }
            }
        });
    }

    /**
     * 初始化楼层
     */
    private void initFloorView() {
        mFloorRv = mHeadView.findViewById(R.id.vlayout_rv);
        floorRootLayout = mHeadView.findViewById(R.id.floorRootLayout);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mFloorRv.setLayoutManager(linearLayoutManager);
        mFloorAdapter = new FloorAdapter(getActivity(), getChildFragmentManager());
        mFloorRv.setAdapter(mFloorAdapter);

        img_limted2 = mHeadView.findViewById(R.id.img_limted2);//新手教程
        img_limted1 = mHeadView.findViewById(R.id.img_limted1);//限时抢购
        litmited_time = mHeadView.findViewById(R.id.litmited_time);//限时抢购模块


        mImageInfo = new ImageInfo();
        mImageInfo.setClassId(4);
        mImageInfo.setId(363);
        mImageInfo.setSplicePid("0");
        mImageInfo.setType("4");
        mImageInfo.setTitle("限时抢购");

        RxHttp.getInstance().getSysteService()//获取楼层活动数据
                .getFloorPicture()
                .compose(RxUtils.<BaseResponse<FloorBean>>switchSchedulers())
                .compose(this.<BaseResponse<FloorBean>>bindToLifecycle())
                .subscribe(new DataObserver<FloorBean>() {
                    @Override
                    protected void onSuccess(final FloorBean data) {
                        if (data != null) {
                            LoadImgUtils.setImg(getActivity(), img_limted1, data.getFlashSalePic());
                            LoadImgUtils.setImg(getActivity(), img_limted2, data.getNoviceTutorialPic());
                            img_limted1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // BannerInitiateUtils.gotoMenu(getActivity(),4,mImageInfo);
                                    PanicBuyFragment.start(getActivity(), mImageInfo);//跳限时秒杀
                                }
                            });
                            img_limted2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ShowWebActivity.start(getActivity(),data.getNoviceTutorial(),"新手教程");//跳新手教程
                                }
                            });
                            litmited_time.setVisibility(View.VISIBLE);
                        } else {
                            litmited_time.setVisibility(View.GONE);
                        }

                    }
                });


        shake_more=mHeadView.findViewById(R.id.shake_more);
        tv_limitime=mHeadView.findViewById(R.id.tv_limitime);//限时秒杀时间
        shake_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), VideoClassActivity.class));
            }
        });

        getGet_taoqianggou_time(this)
                .subscribe(new DataObserver<List<PanicBuyTiemBean>>() {
                    @Override
                    protected void onSuccess(List<PanicBuyTiemBean> bean) {
                        mPanicBuyTiemBean = bean;

                        for (int i = 0; i < bean.size(); i++) {
                            PanicBuyTiemBean itme = bean.get(i);
                            if ("1".equals(itme.getType())) {
                                long l = System.currentTimeMillis();
                                int endTiem = 0;
                                try {
                                    endTiem = Integer.valueOf(itme.getEndTime());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                final long firstCreateIndexDate = DateTimeUtils.getFirstCreateIndexDate(endTiem);
                                long downTiem = firstCreateIndexDate - l;
                                if (mCountDownTiemr != null) {
                                    mCountDownTiemr.cancel();
                                }
                                mCountDownTiemr = new CountDownTimer(downTiem, 1000) {
                                    public void onTick(long millisUntilFinished) {

                                        String hms = DateTimeUtils.getCountTimeByLong(millisUntilFinished);
                                        if (!TextUtils.isEmpty(hms))
                                            tv_limitime.setText(hms);
                                    }

                                    public void onFinish() {
                                       // getTabDeta(activity);
                                    }
                                }.start();
                            }
                        }
                    }
                });


    }

    /*
    * 秒杀时间
    * */
    private Observable<BaseResponse<List<PanicBuyTiemBean>>> getGet_taoqianggou_time(RxFragment fragment) {
        RequestPanicBuyTabBean requestBean = new RequestPanicBuyTabBean();
        requestBean.setType(0);

        return RxHttp.getInstance().getSysteService()
                .getPanicBuyTabData(requestBean)
                .compose(RxUtils.<BaseResponse<List<PanicBuyTiemBean>>>switchSchedulers())
                .compose(fragment.<BaseResponse<List<PanicBuyTiemBean>>>bindToLifecycle());
    }
    private void setFloorLayout(List<FloorInfo> data) {
        if (null == data || data.isEmpty()) {
            return;
        }
        App.getACache().put(C.sp.FLOOR_CACHE, (Serializable) data);
        mFloorAdapter.setData(data);
    }

    /***
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        rcy_shakegoods = mHeadView.findViewById(R.id.rcy_shakegoods);//抖货recycleview
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 3);
        //设置图标的间距
        SpaceItemDecorationUtils spaceItemDecorationUtils = new SpaceItemDecorationUtils(20, 3);
        rcy_shakegoods.addItemDecoration(spaceItemDecorationUtils);
        rcy_shakegoods.setLayoutManager(manager);


        recommendTitleTv = mHeadView.findViewById(R.id.recommendTitleTv);
        mActivityRecyclerView = (RecyclerView) mHeadView.findViewById(R.id.recyclerview_activity);
        mReUseListView = (ReUseStaggeredView) mView.findViewById(R.id.mListView);
        go_recommod = mView.findViewById(R.id.go_recommod);
        go_top = mView.findViewById(R.id.go_top);
        floatIv = mView.findViewById(R.id.floatIv);
        mViewpger_category = (RecyclerView) mHeadView.findViewById(R.id.viewpger_category);
        bar_line = mHeadView.findViewById(R.id.main_line);
        scrollbarLayout = mHeadView.findViewById(R.id.scrollbarLayout);
        int windowWidth = AppUtil.getWindowWidth(getActivity());
        int defpadding = DensityUtil.dip2px(getActivity(), 30);
        float menuItmeWidth = 0.0F;
        if (windowWidth != 0) {
            int padding = windowWidth * defpadding / 1086;
            menuItmeWidth = ((windowWidth - padding) / 5) + 0.0f;
        }
        double horizontalSpacing = defpadding;
        double itmeWidth = DensityUtil.dip2px(getActivity(), 42);
        if (menuItmeWidth != 0) {
            horizontalSpacing = menuItmeWidth * 0.36;
            itmeWidth = menuItmeWidth * 0.67;
        }
        menuAdapter = new HomeMenuAdapter(getActivity(), (int) itmeWidth);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.HORIZONTAL, false);
        mViewpger_category.setLayoutManager(layoutManager);
        GridSpacingItemDecoration gridSpacingItemDecoration = GridSpacingItemDecoration.newGridItemDecoration(layoutManager, DensityUtil.dip2px(getActivity(), 12.5f), (int) horizontalSpacing, true);
        if (mViewpger_category.getItemDecorationCount() == 0) {
            mViewpger_category.addItemDecoration(gridSpacingItemDecoration);
        }
        mViewpger_category.setAdapter(menuAdapter);
        setScrollBar();
        //精选活动
        LinearLayoutManager activityLinearLayoutManager = new LinearLayoutManager(getActivity());
        activityLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mActivityRecyclerView.setLayoutManager(activityLinearLayoutManager);
        mActivityAdapter = new ActivityAdapter(getActivity());
        mActivityRecyclerView.setAdapter(mActivityAdapter);


//        //精品推荐（上下刷新列表）
//        mReUseListView.getSwipeList().setOnRefreshListener(new com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//            }
//        });
        mAdapter = new HomeRecommendAdapter(getActivity(), new ArrayList<HomeRecommendGoods>());
        mAdapter.addHeaderView(mHeadView);
//        mReUseListView.setLayoutManager(layoutManager1);
        mAdapter.setPreLoadNumber(3);
        mReUseListView.setAdapter(mAdapter, new ReUseStaggeredView.RefreshAndLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
//                if (!mReUseListView.getSwipeList().isRefreshing())
                MyLog.i("test", "setRecommendData1");
                setRecommendData();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isLoadData = true;
                isSetBannerColor = false;
                page = 1;
                mAdapter.setEnableLoadMore(false);
                MyLog.i("test", "refreshData");
                refreshData();
                if (mGetArrGcInfo == null || mGetArrGcInfo.size() == 0) {
                    EventBus.getDefault().post(new HomeSupeListRefreshEvent());
                }
            }
        });
//        mReUseListView.getListView().setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//
//            }
//        });
//        mReUseListView.setAdapterAndHeadView(mHeadView, mAdapter);
        mReUseListView.setOnExternalScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        moveNormal();
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        moveNearEdge();
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:

                        break;

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                if (null != mAdapter && mAdapter.getData().isEmpty()) {
                    return;
                }
                if (sumDy < 0) sumDy = 0;
                sumDy += dy;
                //开始色值
                if (sumDy == 0) {
                    if (null != topBannerColors && topBannerColors.size() > 0) {
                        recycleStartColor = topBannerColors.get(currentBannerPosition);
                    }
                    updateColor(0, recycleStartColor, recycleEndColor, false);
                    bannerVisiable = true;
                } else if (sumDy >= 300) {
                    //最终色值
                    recycleEndColor = getResources().getColor(R.color.white);
                    updateColor(1, recycleStartColor, recycleEndColor, true);
                    if (!bannerVisiable) {
                        go_recommod.setVisibility(View.VISIBLE);
                    }
                } else {
                    //渐变色值,伴随手指移动,移动的越多颜色变化的就越多
                    bannerVisiable = true;
                    float caculateDy = sumDy / 300.0f;

                    updateColor(caculateDy, recycleStartColor, recycleEndColor, sumDy < 150 ? false : true);
                    if (null != mTitleBanner && mTitleBanner.getGlobalVisibleRect(new Rect())) {
                        if (sumDy > 0 && sumDy < 500) {
                            go_recommod.setVisibility(View.GONE);
                        } else if (sumDy > 500) {
                            go_recommod.setVisibility(View.VISIBLE);
                        }
                    }
                }
//                setBigData();
                if (null != recommendTitleTv && recommendTitleTv.getGlobalVisibleRect(new Rect())) {
                    go_recommod.setVisibility(View.GONE);
                    go_top.setVisibility(View.VISIBLE);
                } else {
                    go_top.setVisibility(View.GONE);
                }
                if (null != mTitleBanner && !mTitleBanner.getGlobalVisibleRect(new Rect())) {
                    bannerVisiable = false;
                }


            }
        });


        go_recommod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((StaggeredGridLayoutManager) mReUseListView.getRecyclerView().getLayoutManager()).scrollToPositionWithOffset(1, 100);
            }
        });

        go_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sumDy = 0;
                go_top.setVisibility(View.GONE);
                ((StaggeredGridLayoutManager) mReUseListView.getRecyclerView().getLayoutManager()).scrollToPositionWithOffset(0, 0);
            }
        });


        //二级模块
        secondLayout = mHeadView.findViewById(R.id.secondLayout);
        officalFrameLayout = mHeadView.findViewById(R.id.officalFrameLayout);
        GridLayoutManager secondGridLayoutManager = new GridLayoutManager(getActivity(), 6, GridLayoutManager.VERTICAL, false);
        mSecondModuleRv = mHeadView.findViewById(R.id.rv_secondModule);
        mSecondModuleRv.setLayoutManager(secondGridLayoutManager);
        mSecondModuleAdapter = new SecondModuleAdapter(getActivity());
        mSecondModuleRv.addItemDecoration(new SpaceItemDecoration(DensityUtil.dip2px(getActivity(), 3)));
        mSecondModuleRv.setAdapter(mSecondModuleAdapter);
        secondGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int spanSize = 1;
                if (mSecondModuleAdapter.getItemViewType(position) == SecondModuleAdapter.DISPLAY_HORIZONTAL) {
                    spanSize = 3;//跨2列
                } else if (mSecondModuleAdapter.getItemViewType(position) == SecondModuleAdapter.DISPLAY_VERTICAL) {
                    spanSize = 2;//跨1列

                }
                return spanSize;

            }
        });

        //获取父类fragment的登录提示控件高度，把推荐按钮和置顶按钮放在它之上
        parentNotify = getParentFragment().getView().findViewById(R.id.rl_urgency_notifi);
    }


    private void setBigTag(boolean tag) {
        if (mAsTitleBanner != null) {
            mAsTitleBanner.setTag(tag);
        }
        if (mAsOfficialBanner != null) {
            mAsOfficialBanner.setTag(tag);
        }
        if (mAsMakeMoneyBanner != null) {
            mAsMakeMoneyBanner.setTag(tag);
        }
        if (scrollbarLayout != null) {
            scrollbarLayout.setTag(tag);
        }
        if (floorRootLayout != null) {
            floorRootLayout.setTag(tag);
        }
        if (recommendTitleTv != null) {
            recommendTitleTv.setTag(tag);
        }
        if (mLlActivity != null) {
            mLlActivity.setTag(tag);
        }
    }

    /**
     * 大数据收集
     **/
    private void setBigData() {
        Rect rectAsTitleBanner = new Rect();
        Rect rectAsOfficialBanner = new Rect();
        Rect rectAsMakeMoneyBanner = new Rect();
        Rect rectScrollbarLayout = new Rect();
        Rect rectFloorRootLayout = new Rect();
        Rect rectLlActivity = new Rect();
        Rect rectRecommendTitleTv = new Rect();
        if (mAsTitleBanner != null && mAsTitleBanner.getLocalVisibleRect(rectAsTitleBanner) && !(boolean) mAsTitleBanner.getTag()) {
            if (rectAsTitleBanner.top == 0) {
            } else {
                MyLog.i("test", "A1");
                mAsTitleBanner.setTag(true);
                SensorsDataUtil.getInstance().exposureViewTrack("A");
            }


        }
        if (mAsOfficialBanner != null && mAsOfficialBanner.getLocalVisibleRect(rectAsOfficialBanner) && !(boolean) mAsOfficialBanner.getTag()) {
            if (rectAsOfficialBanner.top == 0) {
            } else {
                MyLog.i("test", "B");
                mAsOfficialBanner.setTag(true);
                SensorsDataUtil.getInstance().exposureViewTrack("B");
            }

        }
        if (mAsMakeMoneyBanner != null && mAsMakeMoneyBanner.getLocalVisibleRect(rectAsMakeMoneyBanner) && !(boolean) mAsMakeMoneyBanner.getTag()) {
            if (rectAsMakeMoneyBanner.top == 0) {
            } else {
                MyLog.i("test", "C");
                mAsMakeMoneyBanner.setTag(true);
                SensorsDataUtil.getInstance().exposureViewTrack("B");
            }

        }
        if (scrollbarLayout != null && scrollbarLayout.getLocalVisibleRect(rectScrollbarLayout) && !(boolean) scrollbarLayout.getTag()) {
            if (rectScrollbarLayout.top == 0) {
            } else {
                MyLog.i("test", "二级分类");
                scrollbarLayout.setTag(true);
                SensorsDataUtil.getInstance().exposureViewTrack("二级分类");
            }

        }
        if (floorRootLayout != null && floorRootLayout.getLocalVisibleRect(rectFloorRootLayout) && !(boolean) floorRootLayout.getTag()) {
            if (rectFloorRootLayout.top == 0) {
            } else {
                MyLog.i("test", "楼层管理");
                floorRootLayout.setTag(true);
                SensorsDataUtil.getInstance().exposureViewTrack("楼层管理");
            }

        }
        if (mLlActivity != null && mLlActivity.getLocalVisibleRect(rectLlActivity) && !(boolean) mLlActivity.getTag()) {
            if (rectLlActivity.top == 0) {
            } else {
                MyLog.i("test", "精选活动");
                mLlActivity.setTag(true);
                SensorsDataUtil.getInstance().exposureViewTrack("精选活动");
            }

        }
        if (null != recommendTitleTv && recommendTitleTv.getLocalVisibleRect(rectRecommendTitleTv) && !(boolean) recommendTitleTv.getTag()) {
            if (rectRecommendTitleTv.top == 0) {
            } else {
                MyLog.i("test", "多点优选推荐");
                recommendTitleTv.setTag(true);
                SensorsDataUtil.getInstance().exposureViewTrack(getString(R.string.markermall_recommend));
            }

        }
    }

    private void setScrollBar() {
        mViewpger_category.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //整体的总宽度，注意是整体，包括在显示区域之外的。
                int range = mViewpger_category.computeHorizontalScrollRange();
                float density = ScreenUtils.getScreenDensity();
                //计算出溢出部分的宽度，即屏幕外剩下的宽度
                float maxEndX = range + (10 * density) + 5 - ScreenUtils.getScreenWidth();
                //滑动的距离
                endX += dx;
                if (endX > maxEndX) {
                    endX = maxEndX;
                }
                if (endX < 5.0) {
                    endX = 5.0f;
                }

                //计算比例
                float proportion = endX / maxEndX;

                //计算滚动条宽度
                int transMaxRange = ((ViewGroup) bar_line.getParent()).getWidth() - bar_line.getWidth();
                //设置滚动条移动
                bar_line.setTranslationX(transMaxRange * proportion);

            }

        });

    }


    private void updateColor(float caculateDy, int startColor, int endColor, boolean iswhile) {
        if (!bannerVisiable) {
            return;
        }
        ArgbEvaluator evaluator = new ArgbEvaluator();
        int changeBannerColor = (int) evaluator.evaluate(caculateDy, startColor, endColor);
        if (null != this.mUpdateColorCallback) {
            this.mUpdateColorCallback.updateColor(changeBannerColor, iswhile, sumDy);
        }
    }

    /**
     * 初始化轮播图view
     */
    private void initBanner() {
        mMakeMoneyBanner = (Banner) mHeadView.findViewById(R.id.banner_make_money);
        mOfficialBanner = mHeadView.findViewById(R.id.banner_offical);
    }


    /**
     * 品牌特卖等等模块
     */
    private void setModuleView() {
        mTitleBanner = (Banner) mHeadView.findViewById(R.id.roll_view_pager);
        mTitleBanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                /**
                 *  position 页签[0 ~ 3]
                 *  positionOffset 页百分比偏移[0F ~ 1F]
                 *  positionOffsetPixels 页像素偏移[0 ~ ViewPager的宽度]
                 */
                if (null != topBannerColors && topBannerColors.size() > 0 && sumDy == 0 && isResumed()) {
                    //颜色估值器
                    ArgbEvaluator evaluator = new ArgbEvaluator();

                    //计算不同页面的结束颜色，最后一张的颜色是第一个颜色，其他的分别加一
                    endColor = position == topBannerColors.size() - 1 ? topBannerColors.get(0) : topBannerColors.get(position + 1);
                    //根据positionOffset得到渐变色，因为positionOffset本身为0~1之间的小数所以无需多做处理了
                    //传两个颜色,起始颜色和结束颜色
                    int evaluate = (int) evaluator.evaluate(positionOffset, topBannerColors.get(position), endColor);

                    if (null != mUpdateColorCallback) {
                        mUpdateColorCallback.updateColor(evaluate, false, 0);
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                currentBannerPosition = position;
                if (null != topBannerColors && topBannerColors.size() > 0) {
                    recycleStartColor = topBannerColors.get(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        //获取高度，设置背景占位图的为它的一半
        bannerSpace = mHeadView.findViewById(R.id.bannerSpace);
        mTitleBanner.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int height = mTitleBanner.getHeight();
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) bannerSpace.getLayoutParams();
                params.height = height / 2;
                bannerSpace.setLayoutParams(params);
                mTitleBanner.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        mAsTitleBanner = (AspectRatioView) mHeadView.findViewById(R.id.ar_title_banner);
    }

    private void setHotView(final List<ImageInfo> datas) {
        //多点优选热门
        if (datas == null || datas.size() == 0) {
            return;
        }
        mHotviews.clear();
        for (int i = 0; i < datas.size(); i++) {
            //设置滚动的单个布局
            LinearLayout moreView = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.item_hot_text, null);

            TextView tv = (TextView) moreView.findViewById(R.id.title);

            //进行对控件赋值
            tv.setText(datas.get(i).getTitle());
            //添加到循环滚动数组里面去
            mHotviews.add(moreView);
        }
        mUpview.setViews(mHotviews);
        mUpview.setOnItemClickListener(new UPMarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                //sendSearchStatistics(datas.get(position).getId()+"");
                SensorsDataUtil.getInstance().hotClick(datas.get(position).getTitle());
                BannerInitiateUtils.gotoAction(getActivity(), datas.get(position));
            }

            /**
             * 滚动监听
             *
             * @param tag
             * @param view
             */
            @Override
            public void onAnimationListener(Object tag, View view) {

            }
        });
    }

    private void setCategorys(final List<ImageInfo> datas) {
        if (datas == null || datas.size() == 0) {
            return;
        }
        menuAdapter.setData(datas);
        mViewpger_category.scrollToPosition(0);
        endX = 0.0f;
        bar_line.setTranslationX(0);
        App.mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (UIUtils.isRecyclerScrollable(mViewpger_category)) {
                    scrollbarLayout.setVisibility(View.VISIBLE);
                } else {
                    scrollbarLayout.setVisibility(View.GONE);
                }
            }
        }, 500);

    }


    private void setSecondModule(final List<ImageInfo> datas) {

        if (datas == null || datas.size() == 0) {
            return;
        }
        String defaultColor = datas.get(0).getColorValue();
        GradientDrawable background = (GradientDrawable) secondLayout.getBackground();
        GradientDrawable officalBg = (GradientDrawable) officalFrameLayout.getBackground();
        if (!TextUtils.isEmpty(defaultColor) && StringsUtils.checkColor(defaultColor.trim())) {
            background.setColor(Color.parseColor(defaultColor.trim()));
            officalBg.setColor(Color.parseColor(defaultColor.trim()));
        } else {
            background.setColor(getActivity().getResources().getColor(R.color.white));
            officalBg.setColor(getActivity().getResources().getColor(R.color.white));
        }
        mSecondModuleAdapter.setData(fillSecondData(datas));

    }


    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void onInvisible() {
        super.onInvisible();
        if (isAdded()) {
            mTitleBanner.stopAutoPlay();
        }
    }


    @Override
    protected void onVisible() {
        super.onVisible();
        if (isAdded()) {
            mTitleBanner.startAutoPlay();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setTopButtonPosition();
        setBigTag(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe  //订阅事件
    public void onEventMainThread(MessageEvent event) {
        switch (event.getAction()) {
            case EventBusAction.LOGINA_SUCCEED:
                UserLocalData.isPartner = true;
                MyLog.i("test", "refreshData");
                refreshData();
                break;

        }
    }

    @Subscribe  //订阅事件
    public void onEventMainThread(LogoutEvent event) {
        UserLocalData.isPartner = true;
        MyLog.i("test", "refreshData");
        refreshData();

    }


    @Override
    protected int getViewLayout() {
        return R.layout.fragment_home_recommend;
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }

    @Override
    public void onRecommendSuccessful(NewRecommendBean recommendBean) {
        mReUseListView.getSwipeList().setRefreshing(false);
        mAdapter.setEnableLoadMore(true);

        if (recommendBean == null) {
            mAdapter.loadMoreEnd();
            return;
        }
        List<ShopGoodInfo> goods = recommendBean.getItemList();
        if (goods == null || goods.size() == 0) {
            mAdapter.loadMoreEnd();
            return;
        }
        mAdapter.loadMoreComplete();
        List<HomeRecommendGoods> list = addRecommendData(recommendBean);
        if (page == 1) {
            if (list.size() % 2 != 0) {   //第一页也可能有奇数
                list.remove(list.size() - 1);
            }
            mAdapter.setNewData(list);
            App.getACache().put(C.sp.homeListData, (Serializable) list);
        } else {
            List<HomeRecommendGoods> newList = new ArrayList<>();
            newList.addAll(removeDuplicate(list));
            mAdapter.addData(newList);
        }
        mMinNum = recommendBean.getMinNum();
        mType = recommendBean.getType();
        page++;
//        App.getACache().put(C.sp.homeListData, (Serializable) recommendBean.getList());
    }

    private List<HomeRecommendGoods> addRecommendData(NewRecommendBean recommendBean) {
        List<HomeRecommendGoods> list = new ArrayList<>();
        List<ImageInfo> imageInofs = recommendBean.getAd();
        List<ShopGoodInfo> goods = recommendBean.getItemList();
        List<HomeRecommendGoods> nullList = new ArrayList<>();
        if (goods != null) {
            for (int i = 0; i < goods.size(); i++) {
                HomeRecommendGoods homeRecommendGoods = new HomeRecommendGoods(HomeRecommendGoods.TYPE_GOODS);
                homeRecommendGoods.setShopGoodInfo(goods.get(i));
                list.add(homeRecommendGoods);
            }
        }
        if (imageInofs != null) {
            Comparator<ImageInfo> comparator = new Comparator<ImageInfo>() {
                public int compare(ImageInfo s1, ImageInfo s2) {
                    return s1.getSort() - s2.getSort();
                }
            };
            //先把广告按sort排序
            Collections.sort(imageInofs, comparator);
            //因为商品列表和广告列表加起来是一页10个，所以先创建几个新的补够10个
            for (ImageInfo imageInfo : imageInofs) {
                HomeRecommendGoods homeRecommendGoods = new HomeRecommendGoods(HomeRecommendGoods.TYPE_AD);
                nullList.add(homeRecommendGoods);
            }
            //补够一页10个
            list.addAll(nullList);
        }
        if (imageInofs != null) {
            //插入广告
            for (int i = 0; i < imageInofs.size(); i++) {
                HomeRecommendGoods homeRecommendGoods = new HomeRecommendGoods(HomeRecommendGoods.TYPE_AD);
                homeRecommendGoods.setImageInfo(imageInofs.get(i));
                int sort = imageInofs.get(i).getSort();
                if (sort > 0 && sort - 1 < list.size()) {
                    list.add(imageInofs.get(i).getSort() - 1, homeRecommendGoods);
                }
            }
        }
        //去掉之前新建补够的个数
        list.removeAll(nullList);
        return list;
    }

    @Override
    public void onRecommendFailure(String errorMsg, String errCode) {
        mReUseListView.getSwipeList().setRefreshing(false);
        if (StringsUtils.isDataEmpty(errCode)) {
            mAdapter.loadMoreEnd();
        }


    }

    @Override
    public void onBrandBanner(List<ImageInfo> datas, int back) {
        switch (back) {
            case C.UIShowType.Brandsale: //品牌特卖轮播
                putBannerData((ArrayList) datas, back);
                break;
            case C.UIShowType.Taobao_v2:  //设置活动图轮播
                putBannerData((ArrayList) datas, back);

                break;
            case C.UIShowType.Noticebar:   //最火标题滑动
                setHotView(datas);
                putBannerData((ArrayList) datas, back);
                break;

            case C.UIShowType.Official:   //官方推荐
                mAsOfficialBanner.setVisibility(View.VISIBLE);
                BannerInitiateUtils.setBrandBanner(getActivity(), datas, mOfficialBanner, mAsOfficialBanner);
                putBannerData((ArrayList) datas, back);
                break;

            case C.UIShowType.Makemoney:       //赚钱计划
                mAsMakeMoneyBanner.setVisibility(View.VISIBLE);
                BannerInitiateUtils.setBrandBanner(getActivity(), datas, mMakeMoneyBanner, mAsMakeMoneyBanner);
                putBannerData((ArrayList) datas, back);
                break;
            case C.UIShowType.HomeBanner:         //顶部
                setBannerColors(datas);
                BannerInitiateUtils.setBrandBanner(getActivity(), datas, mTitleBanner, mAsTitleBanner);
                putBannerData((ArrayList) datas, back);
                break;
            case C.UIShowType.HomeIcon:         // 首页ICON配置
                setCategorys(datas);
                putBannerData((ArrayList) datas, back);
                break;
            case C.UIShowType.HomeSecond:  //二级
                secondLayout.setVisibility(View.VISIBLE);
                setSecondModule(datas);
                putBannerData((ArrayList) datas, back);
                break;
            case C.UIShowType.FLOAT_AD: //首页悬浮窗
                setFloatADView(datas);
                break;

            default:
                break;
        }


    }

    private void setBannerColors(List<ImageInfo> datas) {
        if (isSetBannerColor) return;
        if (null != datas && datas.size() > 0) {
            topBannerColors.clear();
            for (ImageInfo info : datas) {
                String itemColor = info.getColorValue();
                if (!TextUtils.isEmpty(itemColor) && StringsUtils.checkColor(itemColor.trim())) {
                    topBannerColors.add(Color.parseColor(itemColor.trim()));
                } else {
                    //如果接口给的是null或者格式不对,就要设置一个默认值了，这里设置一个白色
                    topBannerColors.add(getActivity().getResources().getColor(R.color.black));
                }
            }
            if (null != topBannerColors && topBannerColors.size() > 0) {
                //这里开始取到数据后,要设置每一个颜色，不然会有白屏
                recycleStartColor = topBannerColors.get(0);
                recycleEndColor = getResources().getColor(R.color.white);
                ArgbEvaluator evaluator = new ArgbEvaluator();
                int changeBannerColor = (int) evaluator.evaluate(0, recycleStartColor, recycleEndColor);
                if (null != mUpdateColorCallback) {
                    isSetBannerColor = true;
                    mUpdateColorCallback.updateColor(changeBannerColor, false, 0);
                }

            }
        }
    }

    private void putBannerData(ArrayList datas, int back) {
        App.getACache().put(C.sp.UIShow + back, datas);
    }

    @Override
    public void onBannerFailure(String errorMsg, int back) {
        switch (back) {
            case C.UIShowType.Official:
                mAsOfficialBanner.setVisibility(View.GONE);
                break;
            case C.UIShowType.Makemoney:
                mAsMakeMoneyBanner.setVisibility(View.GONE);
                break;
            case C.UIShowType.HomeSecond:
                //清空二级缓存
                App.getACache().remove(C.sp.UIShow + back);
                mSecondModuleAdapter.clearData();
                secondLayout.setVisibility(View.GONE);
                break;
            case C.UIShowType.FLOAT_AD:
                floatIv.setVisibility(View.GONE);
                break;
            default:
                break;
        }

    }


    @Override
    public void onActivitySuccessFul(List<HandpickBean> data) {
        mLlActivity.setVisibility(View.VISIBLE);
        mActivityAdapter.replace(data);
        mActivityAdapter.notifyDataSetChanged();
        App.getACache().put(C.sp.homeActivityData, (Serializable) data);
    }

    @Override
    public void onActivityFailure() {
        MyLog.i("test", "errorMsg");
        List<HandpickBean> items = mActivityAdapter.getItems();
        mLlActivity.setVisibility(View.GONE);
    }

    @Override
    public void onFloorSuccess(List<FloorInfo> data) {
        //假的数据

        setFloorLayout(data);
    }

    @Override
    public void onFloorFailure() {
        floorRootLayout.setVisibility(View.GONE);
    }

    @Override
    public void onVideoSuccess(List<ShopGoodInfo> videoBean) {//抖货商品获取成功
        List<ShopGoodInfo> data = new ArrayList<>();
        data.addAll(videoBean);
        ShakeGoodsAdapter shakeadapter = new ShakeGoodsAdapter(getActivity(), data);
        rcy_shakegoods.setAdapter(shakeadapter);
    }

    @Override
    public void onVideoFailure() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }

    /***
     * 去掉重复数据
     * @param list
     * @return
     */
    public List<HomeRecommendGoods> removeDuplicate(List<HomeRecommendGoods> list) {
        List<Integer> indexList = new ArrayList<>();
        for (int i = 0; i < mAdapter.getData().size(); i++) {
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).getShopGoodInfo() != null && mAdapter.getData().get(i).getShopGoodInfo() != null) {
                    if (list.get(j).getShopGoodInfo().getItemSourceId().equals(mAdapter.getData().get(i).getShopGoodInfo())) {
                        indexList.add(j);
                    }
                }
            }
        }
        for (int i = 0; i < indexList.size(); i++) {
            list.remove(indexList.get(i));
        }

        if (list.size() % 2 != 0) {
            list.remove(list.size() - 1);
        }
        return list;
    }

    /**
     * 处理二级模块的数据填充
     *
     * @param datas
     * @return
     */
    public List<ImageInfo> fillSecondData(List<ImageInfo> datas) {
        List<ImageInfo> newDatas = new ArrayList<>();
        List<ImageInfo> horizonalImages = new ArrayList<>();
        List<ImageInfo> verticalImages = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            ImageInfo imageInfo = datas.get(i);
            if (0 != imageInfo.getId() && imageInfo.getDisplayStyle() == SecondModuleAdapter.DISPLAY_HORIZONTAL) {
                horizonalImages.add(imageInfo);
            } else {
                verticalImages.add(imageInfo);
            }
        }
        if (horizonalImages.size() > 0) {
            if (horizonalImages.size() % 2 != 0) {
                //填充一条横图,达到跨列,使得竖图在下一行展示
                ImageInfo emptyInfo = new ImageInfo();
                emptyInfo.setId(-1);
                horizonalImages.add(emptyInfo);
            }
        }
        newDatas.addAll(horizonalImages);
        newDatas.addAll(verticalImages);
        return newDatas;
    }

    public void setTopButtonPosition() {
        if (null != parentNotify) {
            parentNotify.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    RelativeLayout.LayoutParams margin = (RelativeLayout.LayoutParams) go_recommod.getLayoutParams();
                    RelativeLayout.LayoutParams topMargin = (RelativeLayout.LayoutParams) go_top.getLayoutParams();
                    if (originMarginHeight == 0) {
                        originMarginHeight = margin.bottomMargin;
                    }
                    if (originTopMarginHeight == 0) {
                        originTopMarginHeight = topMargin.bottomMargin;
                    }
                    int height = parentNotify.getHeight();
                    if (height > 0) {
                        moveMarginHeight = height / 2 + 50;
                    } else {
                        moveMarginHeight = 0;
                    }
                    margin.setMargins(margin.leftMargin, margin.topMargin, margin.rightMargin, moveMarginHeight);
                    go_recommod.setLayoutParams(margin);
                    topMargin.setMargins(topMargin.leftMargin, topMargin.topMargin, topMargin.rightMargin, moveMarginHeight);
                    go_top.setLayoutParams(topMargin);
                    setFloatAdMargin();
                    parentNotify.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }

    }


    private void setFloatADView(final List<ImageInfo> datas) {
        if (null != datas && datas.size() > 0) {
            final ImageInfo adInfo = datas.get(0);
            if (!TextUtils.isEmpty(adInfo.getPicture())) {
                floatIv.setVisibility(View.VISIBLE);
                LoadImgUtils.setImg(getActivity(), floatIv, adInfo.getPicture(), false);
                floatIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BannerInitiateUtils.gotoAction(getActivity(), adInfo);
                    }
                });
                setFloatAdMargin();
            } else {
                floatIv.setVisibility(View.GONE);
            }
        }
    }

    private void setFloatAdMargin() {
        floatIv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (floatIv.getHeight() > 0) {
                    RelativeLayout.LayoutParams floatMargin = (RelativeLayout.LayoutParams) floatIv.getLayoutParams();
                    floatMargin.setMargins(floatMargin.leftMargin, floatMargin.topMargin, floatMargin.rightMargin, moveMarginHeight + 200);
                    floatMargin.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                    floatIv.setLayoutParams(floatMargin);
                    floatIv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

    }


    /**
     * 移到边沿
     */
    private void moveNearEdge() {
        if (!isNearEadge) {
            isNearEadge = true;
            App.mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScreenWidth = DensityUtil.getPhoneWidth(getActivity());
                    int floatleft = mScreenWidth - floatIv.getWidth() - DensityUtil.dip2px(getActivity(), 15);
                    int targetLeft = mScreenWidth - 60;


                    ValueAnimator valueAnimator = ValueAnimator.ofInt(floatleft, targetLeft);
                    valueAnimator.setDuration(300);
                    valueAnimator.setRepeatCount(0);
                    valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            int moveleft = (int) animation.getAnimatedValue();
                            floatIv.setLayoutParams(createLayoutParams(moveleft, floatIv.getTop(), moveleft + floatIv.getWidth(), moveMarginHeight + 200));
                        }
                    });
                    valueAnimator.start();
                }
            }, 200);


        }


    }

    private void moveNormal() {
        if (isNearEadge) {
            isNearEadge = false;
            App.mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScreenWidth = DensityUtil.getPhoneWidth(getActivity());
                    int targetLeft = mScreenWidth - 60;
                    int floatleft = mScreenWidth - floatIv.getWidth() - DensityUtil.dip2px(getActivity(), 15);

                    ValueAnimator valueAnimator = ValueAnimator.ofInt(targetLeft, floatleft);
                    valueAnimator.setDuration(300);
                    valueAnimator.setRepeatCount(0);
                    valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            int moveleft = (int) animation.getAnimatedValue();
                            floatIv.setLayoutParams(createLayoutParams(moveleft, floatIv.getTop(), moveleft - floatIv.getWidth(), moveMarginHeight + 200));
                        }
                    });
                    valueAnimator.start();
                }
            }, 200);


        }

    }

    private RelativeLayout.LayoutParams createLayoutParams(int left, int top, int right, int bottom) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(DensityUtil.dip2px(getActivity(), 60), DensityUtil.dip2px(getActivity(), 60));
        layoutParams.setMargins(left, top, right, bottom);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        return layoutParams;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mCountDownTiemr != null) mCountDownTiemr.cancel();
    }
}
