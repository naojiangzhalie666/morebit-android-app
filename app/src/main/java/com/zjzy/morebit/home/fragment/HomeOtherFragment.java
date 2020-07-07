package com.zjzy.morebit.home.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.alibaba.android.vlayout.layout.StickyLayoutHelper;
import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.jdsjlzx.ItemDecoration.SpaceItemDecoration;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.youth.banner.Banner;
import com.zjzy.morebit.Activity.SearchActivity;
import com.zjzy.morebit.Activity.ShowWebActivity;
import com.zjzy.morebit.App;
import com.zjzy.morebit.HomeFragment;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.ActivityBaoAdapter;
import com.zjzy.morebit.adapter.ActivityDouAdapter;
import com.zjzy.morebit.adapter.ActivityFloorAdapter1;
import com.zjzy.morebit.adapter.ActivityFloorAdapter2;
import com.zjzy.morebit.adapter.ActivityFloorAdapter3;
import com.zjzy.morebit.adapter.HomeActivityAdapter;
import com.zjzy.morebit.adapter.HomeBaoAdapter;
import com.zjzy.morebit.adapter.HomeDouAdapter;
import com.zjzy.morebit.adapter.HomeGoodsAdapter;
import com.zjzy.morebit.adapter.HomeHaoAdapter;
import com.zjzy.morebit.adapter.HomeIconAdapter;
import com.zjzy.morebit.adapter.HomeLimiteAdapter;
import com.zjzy.morebit.adapter.HomeNewAdapter;
import com.zjzy.morebit.adapter.HomeOtherAdapter;
import com.zjzy.morebit.adapter.HomeSelectedAdapter;
import com.zjzy.morebit.adapter.NewItemAdapter;
import com.zjzy.morebit.fragment.PanicBuyFragment;
import com.zjzy.morebit.goods.shopping.ui.fragment.CategoryListFragment;
import com.zjzy.morebit.goods.shopping.ui.fragment.CategoryListFragment2;
import com.zjzy.morebit.goodsvideo.VideoFragment;
import com.zjzy.morebit.home.contract.HomeRecommentContract;
import com.zjzy.morebit.home.presenter.HomeRecommendPresenter;
import com.zjzy.morebit.info.ui.fragment.MsgFragment;
import com.zjzy.morebit.interfaces.GuideNextCallback;
import com.zjzy.morebit.main.ui.fragment.GoodNewsFramgent;
import com.zjzy.morebit.main.ui.fragment.GuessLikeFragment;
import com.zjzy.morebit.main.ui.fragment.ShoppingListFragment2;
import com.zjzy.morebit.main.ui.myview.xtablayout.XTabLayout;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.DoorGodCategoryBean;
import com.zjzy.morebit.pojo.FloorBean2;
import com.zjzy.morebit.pojo.FloorInfo;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.PanicBuyingListBean;
import com.zjzy.morebit.pojo.QueryDhAndGyBean;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UserZeroInfoBean;
import com.zjzy.morebit.pojo.VideoClassBean;
import com.zjzy.morebit.pojo.goods.Child2;
import com.zjzy.morebit.pojo.goods.FloorBean;
import com.zjzy.morebit.pojo.goods.GoodCategoryInfo;
import com.zjzy.morebit.pojo.goods.HandpickBean;
import com.zjzy.morebit.pojo.goods.NewRecommendBean;
import com.zjzy.morebit.pojo.request.RequestBannerBean;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.AutoInterceptViewGroup;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.CountTimeView;
import com.zjzy.morebit.utils.DensityUtil;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.OpenFragmentUtils;
import com.zjzy.morebit.utils.SensorsDataUtil;
import com.zjzy.morebit.utils.SwipeDirectionDetector;
import com.zjzy.morebit.utils.UI.BannerInitiateUtils;
import com.zjzy.morebit.utils.UI.SpaceItemRightUtils;
import com.zjzy.morebit.view.AspectRatioView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import me.relex.circleindicator.CircleIndicator;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeOtherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeOtherFragment extends MvpFragment<HomeRecommendPresenter> implements HomeRecommentContract.View, View.OnClickListener, AppBarLayout.OnOffsetChangedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView home_rcy;
    private View status_bar;
    private List<ImageInfo> list = new ArrayList<>();
    private View mView;
    private Banner banner;
    private AspectRatioView mAsTitleBanner;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<String> title = new ArrayList<>();
    ;
    private List<IconFragment> mFragments = new ArrayList<>();
    ;
    private NewsPagerAdapter mAdapter;
    private XTabLayout xTablayout, xablayout, litmited_tab;
    private ViewPager icon_pager, mViewPager, litmited_pager;
    private CircleIndicator circle_indicator_view;
    private LinearLayout ll_bg;
    private RecyclerView activity_rcy1, activity_rcy2, new_rcy, activity_hao, dou_rcy, activity_rcy;
    private ImageView activity_img;
    private AppBarLayout mAppBarLt;
    private SwipeDirectionDetector swipeDirectionDetector;
    private int currentViewPagerPosition = 0;
    private boolean canRefresh = true;
    HomeAdapter mHomeAdapter;
    List<GoodCategoryInfo> mHomeColumns = new ArrayList<>();
    private List<SelectGoodsFragment> fragments = new ArrayList<>();
    private boolean isAnimatorEnd;
    private ImageView img_bao;
    private List<String> title_time = new ArrayList<>();
    private List<String> title_tv = new ArrayList<>();
    private List<LimiteFragment> mfragment = new ArrayList<>();
    private LimitePagerAdapter limiteAdapter;
    private LinearLayout linear1, linear2, linear3, time_bg1, time_bg2, time_bg3, limitskill;
    private TextView tv_time1, tv_time2, tv_time3, tv_title1, tv_title2, tv_title3, tv1, tv2;
    private boolean isAutoPlay = true;
    private AutoInterceptViewGroup autoView;
    private LinearLayout limited;
    private ImageInfo mImageInfo;
    private TextView searchTv;
    private ImageView img_go;
    private ImageView home_msg,shareImageView;
    private List<PanicBuyingListBean.TimeListBean> timeList;
    private int scroll=0;
    private CountTimeView count_time_view,count_time_view2;
    private DataSetObserver mObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            //倒计时结束
            initLimit();
        }
    };
    private DataSetObserver mObserver2 = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            //倒计时结束
           // initLimit();
        }
    };
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1001:
                    int index = litmited_pager.getCurrentItem();

                    litmited_pager.setCurrentItem(index + 1);

                    handler.sendEmptyMessageDelayed(1001, 3000);
                    break;
            }
        }

    };

    public static HomeOtherFragment newInstance(String param1, String param2) {
        HomeOtherFragment fragment = new HomeOtherFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void autoPlay() {
        isAutoPlay = true;
        handler.removeMessages(1001);
        handler.sendEmptyMessageDelayed(1001, 3000);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (mView == null) {
            mView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home_other, null);
            init();
        }
        ViewGroup parent = (ViewGroup) mView.getParent();
        if (parent != null) {
            parent.removeView(mView);
        }
        return mView;
    }

    private void init() {
        initBanner();//banner
        mImageInfo = new ImageInfo();
        mImageInfo.setClassId(4);
        mImageInfo.setId(363);
        mImageInfo.setSplicePid("0");
        mImageInfo.setType("4");
        mImageInfo.setTitle("限时抢购");
        GoodCategoryInfo good = new GoodCategoryInfo();
        good.setName(getString(R.string.choiceness));
        mHomeColumns.add(good);
        GoodCategoryInfo goodWhatLink = new GoodCategoryInfo();
        goodWhatLink.setName(getString(R.string.what_like));
        mHomeColumns.add(goodWhatLink);
        initOtherView();//初始化
        initRefresh();
        autoPlay();


    }


    private void initOtherView() {
        // home_rcy = mView.findViewById(R.id.home_rcy);
        img_bao = mView.findViewById(R.id.img_bao);
        swipeRefreshLayout = mView.findViewById(R.id.swipeRefreshLayout);
        status_bar = mView.findViewById(R.id.status_bar);
        mAppBarLt = mView.findViewById(R.id.app_bar_lt);
        mViewPager = mView.findViewById(R.id.viewPager);
        xablayout = mView.findViewById(R.id.xablayout);
        shareImageView=mView.findViewById(R.id.shareImageView);

        litmited_pager = mView.findViewById(R.id.litmited_pager);
        limited = mView.findViewById(R.id.limited);
        limited.setOnClickListener(this);
        litmited_pager.setOnClickListener(this);

        tv_time1 = mView.findViewById(R.id.tv_time1);
        tv_time2 = mView.findViewById(R.id.tv_time2);
        tv_time3 = mView.findViewById(R.id.tv_time3);
        tv_title1 = mView.findViewById(R.id.tv_title1);
        tv_title2 = mView.findViewById(R.id.tv_title2);
        tv_title3 = mView.findViewById(R.id.tv_title3);
        linear1 = mView.findViewById(R.id.linear1);
        linear2 = mView.findViewById(R.id.linear2);
        linear3 = mView.findViewById(R.id.linear3);

        img_go = mView.findViewById(R.id.img_go);
        Glide.with(this).asGif().load(R.drawable.must_go).into(img_go);
        Glide.with(this).asGif().load(R.drawable.new_hongbao).into(shareImageView);

        home_msg = mView.findViewById(R.id.home_msg);
        home_msg.setOnClickListener(this);
        linear1.setOnClickListener(this);
        linear2.setOnClickListener(this);
        linear3.setOnClickListener(this);


        searchTv = mView.findViewById(R.id.searchTv);
        searchTv.setOnClickListener(this);
        autoView = mView.findViewById(R.id.autoView);


        count_time_view = mView.findViewById(R.id.count_time_view);
        count_time_view.registerDataSetObserver(mObserver);


        count_time_view2=mView.findViewById(R.id.count_time_view2);
        count_time_view2.registerDataSetObserver(mObserver2);
        initBar();



        litmited_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                swipeDirectionDetector.onPageSelected(position);
                currentViewPagerPosition = position;
                if (position % 3 == 0) {
                    tv_time1.setTextColor(Color.parseColor("#FFFF005E"));
                    tv_title1.setBackgroundResource(R.drawable.background_f05557_radius_10dp);
                    tv_title1.setTextColor(Color.parseColor("#FFFFFF"));
                    tv_time2.setTextColor(Color.parseColor("#FF999999"));
                    tv_title2.setTextColor(Color.parseColor("#FF999999"));
                    tv_title2.setBackgroundResource(R.drawable.bg_ffffff_8dp);
                    tv_time3.setTextColor(Color.parseColor("#FF999999"));
                    tv_title3.setTextColor(Color.parseColor("#FF999999"));
                    tv_title3.setBackgroundResource(R.drawable.bg_ffffff_8dp);
                } else if (position % 3 == 1) {
                    tv_time1.setTextColor(Color.parseColor("#FF999999"));
                    tv_title1.setBackgroundResource(R.drawable.bg_ffffff_8dp);
                    tv_title1.setTextColor(Color.parseColor("#FF999999"));
                    tv_time2.setTextColor(Color.parseColor("#FFFF005E"));
                    tv_title2.setTextColor(Color.parseColor("#FFFFFF"));
                    tv_title2.setBackgroundResource(R.drawable.background_f05557_radius_10dp);
                    tv_time3.setTextColor(Color.parseColor("#FF999999"));
                    tv_title3.setTextColor(Color.parseColor("#FF999999"));
                    tv_title3.setBackgroundResource(R.drawable.bg_ffffff_8dp);
                } else {
                    tv_time1.setTextColor(Color.parseColor("#FF999999"));
                    tv_title1.setBackgroundResource(R.drawable.bg_ffffff_8dp);
                    tv_title1.setTextColor(Color.parseColor("#FF999999"));
                    tv_time2.setTextColor(Color.parseColor("#FF999999"));
                    tv_title2.setTextColor(Color.parseColor("#FF999999"));
                    tv_title2.setBackgroundResource(R.drawable.bg_ffffff_8dp);
                    tv_time3.setTextColor(Color.parseColor("#FFFF005E"));
                    tv_title3.setTextColor(Color.parseColor("#FFFFFF"));
                    tv_title3.setBackgroundResource(R.drawable.background_f05557_radius_10dp);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 1) {
                    swipeRefreshLayout.setEnabled(false);//设置不可触发
                } else if (state == 2 && canRefresh) {
                    swipeRefreshLayout.setEnabled(true);//设置可触发
                }
            }
        });

        banner = mView.findViewById(R.id.roll_view_pager);
        mAsTitleBanner = mView.findViewById(R.id.ar_title_banner);
        xTablayout = mView.findViewById(R.id.xTablayout);
        icon_pager = mView.findViewById(R.id.icon_pager);
        circle_indicator_view = mView.findViewById(R.id.circle_indicator_view);
        swipeDirectionDetector = new SwipeDirectionDetector();
        icon_pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (state == 1) {
                    swipeRefreshLayout.setEnabled(false);//设置不可触发
                } else if (state == 2 && canRefresh) {
                    swipeRefreshLayout.setEnabled(true);//设置可触发
                }
            }
        });
        mAppBarLt.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (i < -150 && canRefresh) {
                    swipeRefreshLayout.setEnabled(false);//设置可触发
                    canRefresh = false;
                } else if (i > -150 && !canRefresh) {
                    canRefresh = true;
                    swipeRefreshLayout.setEnabled(true);
                }
            }

        });


        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 1) {
                    swipeRefreshLayout.setEnabled(false);//设置不可触发
                } else if (state == 2 && canRefresh) {
                    swipeRefreshLayout.setEnabled(true);//设置可触发
                }
            }
        });
        icon_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                swipeDirectionDetector.onPageScrolled(position, positionOffset, positionOffsetPixels);

            }

            @Override
            public void onPageSelected(int position) {
                swipeDirectionDetector.onPageSelected(position);
                currentViewPagerPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                swipeDirectionDetector.onPageScrollStateChanged(state);
                MyLog.d("addOnPageChangeListener", " onPageScrollStateChanged  state = " + state);
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                if (state == 1) {
                    swipeRefreshLayout.setEnabled(false);//设置不可触发
                } else if (state == 2 && canRefresh) {
                    swipeRefreshLayout.setEnabled(true);//设置可触发
                }
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                swipeDirectionDetector.onPageScrolled(position, positionOffset, positionOffsetPixels);

            }

            @Override
            public void onPageSelected(int position) {
                swipeDirectionDetector.onPageSelected(position);
                currentViewPagerPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                swipeDirectionDetector.onPageScrollStateChanged(state);
                MyLog.d("addOnPageChangeListener", " onPageScrollStateChanged  state = " + state);
            }
        });
        new_rcy = mView.findViewById(R.id.new_rcy);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 3);
        //设置图标的间距
        if (new_rcy.getItemDecorationCount() == 0) {//防止每一次刷新recyclerview都会使间隔增大一倍 重复调用addItemDecoration方法
            SpaceItemRightUtils spaceItemDecorationUtils = new SpaceItemRightUtils(16, 3);
            new_rcy.addItemDecoration(spaceItemDecorationUtils);
        }

        new_rcy.setLayoutManager(manager);

        activity_rcy1 = mView.findViewById(R.id.activity_rcy1);//横版列表
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        activity_rcy1.setLayoutManager(linearLayoutManager);


        activity_hao = mView.findViewById(R.id.activity_hao);  //好单预告
        GridLayoutManager manager4 = new GridLayoutManager(getActivity(), 2);
        activity_hao.setLayoutManager(manager4);
        if (activity_hao.getItemDecorationCount() == 0) {//防止每一次刷新recyclerview都会使间隔增大一倍 重复调用addItemDecoration方法
            activity_hao.addItemDecoration(new SpaceItemDecoration(DensityUtil.dip2px(getActivity(), 3)));
        }


        dou_rcy = mView.findViewById(R.id.dou_rcy);//抖货
        GridLayoutManager manager5 = new GridLayoutManager(getActivity(), 2);
        dou_rcy.setLayoutManager(manager5);
        if (dou_rcy.getItemDecorationCount() == 0) {//防止每一次刷新recyclerview都会使间隔增大一倍 重复调用addItemDecoration方法
            dou_rcy.addItemDecoration(new SpaceItemDecoration(DensityUtil.dip2px(getActivity(), 3)));
        }


        activity_rcy = mView.findViewById(R.id.activity_rcy);
        LinearLayoutManager manager6 = new LinearLayoutManager(getActivity());
        manager6.setOrientation(LinearLayout.HORIZONTAL);
        //设置图标的间距
        if (activity_rcy.getItemDecorationCount() == 0) {//防止每一次刷新recyclerview都会使间隔增大一倍 重复调用addItemDecoration方法
            SpaceItemRightUtils spaceItemDecorationUtils = new SpaceItemRightUtils(20, 3);
            activity_rcy.addItemDecoration(spaceItemDecorationUtils);
        }
        activity_rcy.setLayoutManager(manager6);
        limited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PanicBuyFragment.start(getActivity(), mImageInfo);//跳限时秒杀
            }
        });
        autoView.setOnsClickListener(new AutoInterceptViewGroup.OnClickListener() {
            @Override
            public void paly() {
                isAutoPlay = true;
                handler.removeMessages(1001);
                handler.sendEmptyMessageDelayed(1001, 3000);
            }

            @Override
            public void stop() {
                isAutoPlay = false;
                handler.removeMessages(1001);
                PanicBuyFragment.start(getActivity(), mImageInfo);//跳限时秒杀
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        initBar();
        mAppBarLt.addOnOffsetChangedListener(this);
    }


    @Override
    public void onPause() {
        super.onPause();
        mAppBarLt.removeOnOffsetChangedListener(this);

    }

    private void initBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //处理全屏显示
            ViewGroup.LayoutParams viewParams = status_bar.getLayoutParams();
            viewParams.height = ActivityStyleUtil.getStatusBarHeight(getActivity());
            status_bar.setLayoutParams(viewParams);
            // 设置状态栏颜色
            getActivity().getWindow().setStatusBarColor(Color.parseColor("#F05557"));

        }
    }

    private void initRefresh() {
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setNestedScrollingEnabled(true);
        //设置进度View下拉的起始点和结束点，scale 是指设置是否需要放大或者缩小动画
        swipeRefreshLayout.setProgressViewOffset(true, -0, 100);
        //设置进度View下拉的结束点，scale 是指设置是否需要放大或者缩小动画
        swipeRefreshLayout.setProgressViewEndTarget(true, 180);
        //设置进度View的组合颜色，在手指上下滑时使用第一个颜色，在刷新中，会一个个颜色进行切换
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#FF645B"));
        //设置触发刷新的距离
        swipeRefreshLayout.setDistanceToTriggerSync(200);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshData();
                initData();
                initBanner();
                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }

    private void refreshData() {

    }

    private void initBanner() {

        getBanner(this, 0)
                .subscribe(new DataObserver<List<ImageInfo>>(false) {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        onBannerFailure(errorMsg, 0);

                    }


                    @Override
                    protected void onSuccess(List<ImageInfo> data) {
                        BannerInitiateUtils.setBrandBanner(getActivity(), data, banner, mAsTitleBanner);
                        onBrandBannerSuccess(data, 0);
                    }
                });

        getBanner(this, 22)
                .subscribe(new DataObserver<List<ImageInfo>>(false) {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        onBannerFailure(errorMsg, 22);

                    }


                    @Override
                    protected void onSuccess(List<ImageInfo> data) {
                        if (data != null && data.size() != 0) {
                            onBrandBannerSuccess(data, 22);
                            ActivityFloorAdapter3 floorAdapter = new ActivityFloorAdapter3(getActivity(), data);
                            activity_hao.setAdapter(floorAdapter);
                        }


                    }
                });
        getActivities(this)
                .subscribe(new DataObserver<List<HandpickBean>>(false) {
                    @Override
                    protected void onDataListEmpty() {
                        onActivityFailure();
                    }

                    @Override
                    protected void onDataNull() {
                        onActivityFailure();
                    }

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        onActivityFailure();
                    }

                    @Override
                    protected void onSuccess(List<HandpickBean> data) {
                        onActivitySuccessFul(data);
                    }
                });


        initLimit();


        getUserZeroInfo(this)
                .subscribe(new DataObserver<UserZeroInfoBean>(false) {
                    @Override
                    protected void onDataListEmpty() {
                        onActivityFailure();
                    }

                    @Override
                    protected void onDataNull() {
                        onActivityFailure();
                    }

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        onActivityFailure();
                    }

                    @Override
                    protected void onSuccess(UserZeroInfoBean data) {
                        onGetUserZeroInfo(data);
                    }
                });

        getDoorGodCategory(this)
                .subscribe(new DataObserver<DoorGodCategoryBean>(false) {
                    @Override
                    protected void onDataListEmpty() {
                        onActivityFailure();
                    }

                    @Override
                    protected void onDataNull() {
                        onActivityFailure();
                    }

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        onActivityFailure();
                    }

                    @Override
                    protected void onSuccess(DoorGodCategoryBean data) {
                        onGetDoorGodCategory(data);
                    }
                });

        getQueryDhAndGy(this)
                .subscribe(new DataObserver<QueryDhAndGyBean>(false) {
                    @Override
                    protected void onDataListEmpty() {
                        onActivityFailure();
                    }

                    @Override
                    protected void onDataNull() {
                        onActivityFailure();
                    }

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        onActivityFailure();
                    }

                    @Override
                    protected void onSuccess(QueryDhAndGyBean data) {
                        onGetQueryDhAndGy(data);
                    }
                });


        getListGraphicInfoSorting(this)
                .subscribe(new DataObserver<FloorBean2>(false) {
            @Override
            protected void onDataListEmpty() {
                onActivityFailure();
            }

            @Override
            protected void onDataNull() {
                onActivityFailure();
            }

            @Override
            protected void onError(String errorMsg, String errCode) {
                onActivityFailure();
            }

            @Override
            protected void onSuccess(FloorBean2 data) {
                onGetListGraphicInfoSorting(data);
            }
        });



        getCategoryData();

    }

    private void initLimit() {
        getpainBuyinglist(this)
                .subscribe(new DataObserver<PanicBuyingListBean>(false) {
                    @Override
                    protected void onDataListEmpty() {
                        onActivityFailure();
                    }

                    @Override
                    protected void onDataNull() {
                        onActivityFailure();
                    }

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        onActivityFailure();
                    }

                    @Override
                    protected void onSuccess(PanicBuyingListBean data) {
                        onGetLitmitSkill(data);
                    }
                });


    }

    private void onGetListGraphicInfoSorting(FloorBean2 data) {
        List<FloorBean2.ListDataBean> listData = data.getListData();
        ActivityFloorAdapter1 floorAdapter1 = new ActivityFloorAdapter1(getActivity(),listData,data.getExt());
        activity_rcy1.setAdapter(floorAdapter1);
    }

    private void onGetQueryDhAndGy(QueryDhAndGyBean data) {
        ActivityDouAdapter douAdapter = new ActivityDouAdapter(getActivity(),data);
        dou_rcy.setAdapter(douAdapter);
    }

    private void onGetDoorGodCategory(DoorGodCategoryBean data) {
        if (data != null) {
            mFragments.clear();
            for (int i = 0; i < data.getResultList().size(); i++) {
                IconFragment fragment = null;
                fragment = new IconFragment();
                mFragments.add(fragment.newInstance(data.getResultList().get(i).getWheelChartDisplayVo()));
            }

            mAdapter = new NewsPagerAdapter(getActivity().getSupportFragmentManager(), mFragments, data.getResultList());
            icon_pager.setAdapter(mAdapter);
            xTablayout.setupWithViewPager(icon_pager);
            for (int i = 0; i < data.getResultList().size(); i++) {
                XTabLayout.Tab tab = xTablayout.getTabAt(i);//获得每一个tab
                tab.setCustomView(R.layout.tab_text);//给每一个tab设置view
                if (i == 0) {
                    // 设置第一个tab的TextView是被选择的样式
                    tab.getCustomView().findViewById(R.id.ll_icon).setSelected(true);//第一个tab被选中
                    tab.getCustomView().findViewById(R.id.icon_bg).setVisibility(View.VISIBLE);

                }
                TextView textView = (TextView) tab.getCustomView().findViewById(R.id.tv_name);
                textView.getPaint().setFakeBoldText(true);
                textView.setText(data.getResultList().get(i).getCategoryName());//设置tab上的文字
            }
            icon_pager.setOffscreenPageLimit(data.getResultList().size());

            circle_indicator_view.setViewPager(icon_pager);//设置指示器
            mAdapter.registerDataSetObserver(circle_indicator_view.getDataSetObserver());
            xTablayout.setOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(XTabLayout.Tab tab) {
                    tab.getCustomView().findViewById(R.id.ll_icon).setSelected(true);
                    if (tab.getPosition() == 0) {
                        tab.getCustomView().findViewById(R.id.icon_bg).setVisibility(View.VISIBLE);
                    } else {
                        tab.getCustomView().findViewById(R.id.icon_bg).setVisibility(View.GONE);
                    }

                    icon_pager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(XTabLayout.Tab tab) {
                    tab.getCustomView().findViewById(R.id.tv_name).setSelected(false);
                    tab.getCustomView().findViewById(R.id.icon_bg).setVisibility(View.GONE);
                }

                @Override
                public void onTabReselected(XTabLayout.Tab tab) {

                }
            });
        }


    }

    private void onGetUserZeroInfo(UserZeroInfoBean data) {
        List<UserZeroInfoBean.ItemListBean> itemList = data.getItemList();
        NewItemAdapter newItemAdapter = new NewItemAdapter(getActivity(), itemList);//新人
        new_rcy.setAdapter(newItemAdapter);
        count_time_view2.startLimitedTime(Long.parseLong(data.getTime()));
    }

    private void onGetLitmitSkill(PanicBuyingListBean data) {

        timeList = data.getTimeList();
        if (timeList.size() != 0 && timeList.size() > 1) {
            tv_time1.setText(timeList.get(0).getTitle());
            tv_time2.setText(timeList.get(1).getTitle());
            tv_time3.setText(timeList.get(2).getTitle());
            tv_title1.setText(timeList.get(0).getSubTitle());
            tv_title2.setText(timeList.get(1).getSubTitle());
            tv_title3.setText(timeList.get(2).getSubTitle());
        }

        for (int i = 0; i < timeList.size(); i++) {
            LimiteFragment fragment = null;
            fragment = new LimiteFragment();
            mfragment.add(fragment.newInstance(timeList.get(i).getItemList()));
        }
        limiteAdapter = new LimitePagerAdapter(getActivity().getSupportFragmentManager(), mfragment, timeList);
        litmited_pager.setAdapter(limiteAdapter);
        litmited_pager.setOffscreenPageLimit(title_time.size());
      // count_time_view.startLimitedTime(data.getTimeList());
    }


    /**
     * 获取首页头部分类数据(超级分类）
     */
    public void getCategoryData() {

        RxHttp.getInstance().getGoodsService().getHomeSuerListData()
                .compose(RxUtils.<BaseResponse<List<GoodCategoryInfo>>>switchSchedulers())
                .compose(this.<BaseResponse<List<GoodCategoryInfo>>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                })
                .subscribe(new DataObserver<List<GoodCategoryInfo>>() {

                    @Override
                    protected void onSuccess(List<GoodCategoryInfo> data) {
                        App.getACache().put(C.sp.homeFenleiData, (Serializable) data);
                        mHomeColumns.addAll(data);
                        mHomeAdapter = new HomeAdapter(getChildFragmentManager(), mHomeColumns);
                        swipeDirectionDetector = new SwipeDirectionDetector();
                        mViewPager.setAdapter(mHomeAdapter);
                        xablayout.setupWithViewPager(mViewPager);
                    }
                });
    }

    private void onBrandBannerSuccess(List<ImageInfo> data, int back) {
        list = data;

        switch (back) {
            case C.UIShowType.Brandsale: //品牌特卖轮播
                //  putBannerData((ArrayList) datas, back);
                break;
            case C.UIShowType.Taobao_v2:  //设置活动图轮播
                //   putBannerData((ArrayList) datas, back);

                break;
            case C.UIShowType.Noticebar:   //最火标题滑动
                //    setHotView(datas);
                //    putBannerData((ArrayList) datas, back);
                break;

            case C.UIShowType.Official:   //官方推荐
                //  mAsOfficialBanner.setVisibility(View.VISIBLE);
                // BannerInitiateUtils.setBrandBanner(getActivity(), datas, mOfficialBanner, mAsOfficialBanner);
                //   putBannerData((ArrayList) datas, back);
                break;

            case C.UIShowType.Makemoney:       //赚钱计划
                //   mAsMakeMoneyBanner.setVisibility(View.VISIBLE);
                //   BannerInitiateUtils.setBrandBanner(getActivity(), datas, mMakeMoneyBanner, mAsMakeMoneyBanner);
                //  putBannerData((ArrayList) datas, back);
                break;
            case C.UIShowType.HomeBanner:         //顶部
                // BannerInitiateUtils.setBrandBanner(getActivity(), datas, mTitleBanner, mAsTitleBanner);
                putBannerData((ArrayList) data, back);

                break;
            case C.UIShowType.HomeIcon:         // 首页ICON配置
//                setCategorys(datas);
//                putBannerData((ArrayList) datas, back);
                break;
            case C.UIShowType.HomeSecond:  //二级
//                secondLayout.setVisibility(View.VISIBLE);
//                setSecondModule(datas);
                putBannerData((ArrayList) data, back);
                break;
            case C.UIShowType.FLOAT_AD: //首页悬浮窗
                //  setFloatADView(datas);
                break;

            default:
                break;
        }

    }

    @Override
    protected void initData() {

    }

//    private void getSupeListData() {
//        getCategoryData(false);
////        List<GoodCategoryInfo> getArrGcInfo = (List<GoodCategoryInfo>) App.getACache().getAsObject(C.sp.homeFenleiData);
////        if (getArrGcInfo != null) {  //有缓存
////            mHomeColumns.addAll(getArrGcInfo);
////            setupViewPager(mHomeColumns);
////            getCategoryData(true);
////        } else {
////
////        }
//    }

    @Override
    protected void initView(View view) {


    }

//    private void setupViewPager(final List<GoodCategoryInfo> homeColumns) {
//        //MyLog.i("test","setupViewPager");
//        mHomeAdapter.setHomeColumns(homeColumns);
//        mHomeAdapter.notifyDataSetChanged();
//        xablayout.setupWithViewPager(mViewPager);
//        // mXTabLayout.setViewPager(mViewPager);
//
//    }

    @Override
    protected int getViewLayout() {
        return R.layout.fragment_home_other;
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }

    public void cleseRecommendGoodsView() {
//        if (rl_urgency_notifi != null)
//            rl_urgency_notifi.removeAllViews();
//        if (null != mHomeAdapter && fragments.size() > 0) {
//            HomeRecommendFragment homeRecommendFragment = fragments.get(0);
//            homeRecommendFragment.setTopButtonPosition();
//        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        if (!isVisibleToUser) {
//            if (null != mHomeAdapter && fragments.size() > 0) {
//                MyLog.i("test", "setUserVisibleHint");
//                HomeRecommendFragment homeRecommendFragment = fragments.get(0);
//                homeRecommendFragment.setUserVisibleHint(isVisibleToUser);
//            }
//        }
    }

    public void setmGuideNextCallback(GuideNextCallback mGuideNextCallback) {
        //  this.mGuideNextCallback = mGuideNextCallback;
    }

    public void getLoginView() {

    }

    public void selectFirst() {
//        try {
//            if (mViewPager != null) {
//                mViewPager.setCurrentItem(0);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


    public void setSysNotificationData(List<ImageInfo> data) {
//        boolean isLogin = LoginUtil.checkIsLogin(getActivity(), false);
//        if (isLogin) {
//            if (data != null && data.size() != 0) {
//                for (int i = 0; i < data.size(); i++) {
//                    ImageInfo imageInfo = data.get(i);
//                    if (imageInfo.getDisplayPage() == 1 && imageInfo.getMediaType() == 3) {//首页
//                        Integer index = (Integer) App.getACache().getAsObject(C.sp.CLESE_RECOMMEND_GOODS + UserLocalData.getUser().getPhone() + imageInfo.getId());
//                        index = index == null ? 0 : index;
//                        if (index != null && index < 2) {
//                            addRecommendGoodsView(imageInfo, index);
//                        }
//                        return;
//                    }
//                }
//            }
//
//        }
    }

    @Override
    public void onRecommendSuccessful(NewRecommendBean recommendBean) {

    }

    @Override
    public void onRecommendFailure(String errorMsg, String errCode) {

    }

    @Override
    public void onBrandBanner(List<ImageInfo> datas, int back) {
        list = datas;
        switch (back) {
            case C.UIShowType.Brandsale: //品牌特卖轮播
                //  putBannerData((ArrayList) datas, back);
                break;
            case C.UIShowType.Taobao_v2:  //设置活动图轮播
                //   putBannerData((ArrayList) datas, back);

                break;
            case C.UIShowType.Noticebar:   //最火标题滑动
                //    setHotView(datas);
                //    putBannerData((ArrayList) datas, back);
                break;

            case C.UIShowType.Official:   //官方推荐
                //  mAsOfficialBanner.setVisibility(View.VISIBLE);
                // BannerInitiateUtils.setBrandBanner(getActivity(), datas, mOfficialBanner, mAsOfficialBanner);
                //   putBannerData((ArrayList) datas, back);
                break;

            case C.UIShowType.Makemoney:       //赚钱计划
                //   mAsMakeMoneyBanner.setVisibility(View.VISIBLE);
                //   BannerInitiateUtils.setBrandBanner(getActivity(), datas, mMakeMoneyBanner, mAsMakeMoneyBanner);
                //  putBannerData((ArrayList) datas, back);
                break;
            case C.UIShowType.HomeBanner:         //顶部
                // BannerInitiateUtils.setBrandBanner(getActivity(), datas, mTitleBanner, mAsTitleBanner);


                break;
            case C.UIShowType.HomeIcon:         // 首页ICON配置
//                setCategorys(datas);
//                putBannerData((ArrayList) datas, back);
                break;
            case C.UIShowType.HomeSecond:  //二级
//                secondLayout.setVisibility(View.VISIBLE);
//                setSecondModule(datas);
//                putBannerData((ArrayList) datas, back);
                break;
            case C.UIShowType.FLOAT_AD: //首页悬浮窗
                //  setFloatADView(datas);
                break;

            default:
                break;
        }

    }

    @Override
    public void onBannerFailure(String errorMsg, int back) {

    }


    public void onActivitySuccessFul(List<HandpickBean> data) {
        if (data != null && data.size() != 0) {
            final HandpickBean handpickBean = data.get(0);
            ActivityBaoAdapter activityBaoAdapter = new ActivityBaoAdapter(getActivity(), handpickBean.getItems());
            activity_rcy.setAdapter(activityBaoAdapter);
            if (!TextUtils.isEmpty(handpickBean.getPicture())) {
                LoadImgUtils.loadingCornerTop(getActivity(), img_bao, handpickBean.getPicture(), 5);
            }


            img_bao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageInfo imageInfo = new ImageInfo();
                    imageInfo.setId(handpickBean.getId());
                    imageInfo.setTitle(handpickBean.getTitle());
                    imageInfo.setPicture(handpickBean.getPicture());
                    if (!TextUtils.isEmpty(handpickBean.getBackgroundImage())) {
                        imageInfo.setBackgroundImage(handpickBean.getBackgroundImage());
                    } else {
                        imageInfo.setBackgroundImage(handpickBean.getPicture());
                    }
                    SensorsDataUtil.getInstance().setAcitivityClickTrack("", +handpickBean.getId() + "");
                    GoodNewsFramgent.start(getActivity(), imageInfo);
//                BannerInitiateUtils.gotoAction((Activity) mContext, activityBean);
                }
            });
        }

    }

    public void onActivityFailure() {

    }

    @Override
    public void onFloorSuccess(List<FloorInfo> data) {

    }

    @Override
    public void onFloorFailure() {

    }

    @Override
    public void onVideoSuccess(List<ShopGoodInfo> videoBean) {

    }

    @Override
    public void onVideoFailure() {

    }

    @Override
    public void onCommissionuccess(List<ShopGoodInfo> data) {

    }

    @Override
    public void onCommissionFailure() {

    }


    private void putBannerData(ArrayList datas, int back) {
        App.getACache().put(C.sp.UIShow + back, datas);
    }


    private void showShareImage() {
        float translationX = shareImageView.getTranslationX();
        ObjectAnimator animator = ObjectAnimator.ofFloat(shareImageView, "translationX", 0);
        animator.setDuration(0);
        if (!isAnimatorEnd) {
            animator.setStartDelay(1200);
        }
        animator.start();
    }

    private void hideShareImage() {
        isAnimatorEnd = false;
        float translationX = shareImageView.getTranslationX();
        ObjectAnimator animator = ObjectAnimator.ofFloat(shareImageView, "translationX", 100);
        animator.setDuration(0);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimatorEnd = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }


    //获取banner
    public Observable<BaseResponse<List<ImageInfo>>> getBanner(RxFragment fragment, int back) {
        RequestBannerBean requestBean = new RequestBannerBean();
        requestBean.setType(back);
        return RxHttp.getInstance().getSysteService()
//                .getBanner(back,2)
                .getBanner(requestBean)
                .compose(RxUtils.<BaseResponse<List<ImageInfo>>>switchSchedulers())
                .compose(fragment.<BaseResponse<List<ImageInfo>>>bindToLifecycle());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear1:
                tv_time1.setTextColor(Color.parseColor("#FFFF005E"));
                tv_title1.setBackgroundResource(R.drawable.background_f05557_radius_10dp);
                tv_title1.setTextColor(Color.parseColor("#FFFFFF"));
                tv_time2.setTextColor(Color.parseColor("#FF999999"));
                tv_title2.setTextColor(Color.parseColor("#FF999999"));
                tv_title2.setBackgroundResource(R.drawable.bg_ffffff_8dp);
                tv_time3.setTextColor(Color.parseColor("#FF999999"));
                tv_title3.setTextColor(Color.parseColor("#FF999999"));
                tv_title3.setBackgroundResource(R.drawable.bg_ffffff_8dp);
                litmited_pager.setCurrentItem(auplay(0));
                break;
            case R.id.linear2:
                tv_time1.setTextColor(Color.parseColor("#FF999999"));
                tv_title1.setBackgroundResource(R.drawable.bg_ffffff_8dp);
                tv_title1.setTextColor(Color.parseColor("#FF999999"));
                tv_time2.setTextColor(Color.parseColor("#FFFF005E"));
                tv_title2.setTextColor(Color.parseColor("#FFFFFF"));
                tv_title2.setBackgroundResource(R.drawable.background_f05557_radius_10dp);
                tv_time3.setTextColor(Color.parseColor("#FF999999"));
                tv_title3.setTextColor(Color.parseColor("#FF999999"));
                tv_title3.setBackgroundResource(R.drawable.bg_ffffff_8dp);
                litmited_pager.setCurrentItem(auplay(1));
                break;
            case R.id.linear3:
                tv_time1.setTextColor(Color.parseColor("#FF999999"));
                tv_title1.setBackgroundResource(R.drawable.bg_ffffff_8dp);
                tv_title1.setTextColor(Color.parseColor("#FF999999"));
                tv_time2.setTextColor(Color.parseColor("#FF999999"));
                tv_title2.setTextColor(Color.parseColor("#FF999999"));
                tv_title2.setBackgroundResource(R.drawable.bg_ffffff_8dp);
                tv_time3.setTextColor(Color.parseColor("#FFFF005E"));
                tv_title3.setTextColor(Color.parseColor("#FFFFFF"));
                tv_title3.setBackgroundResource(R.drawable.background_f05557_radius_10dp);
                litmited_pager.setCurrentItem(auplay(2));
                break;
            case R.id.searchTv:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                break;

            case R.id.home_msg:
                if (!LoginUtil.checkIsLogin(getActivity())) {
                    return;
                }
                //消息
                OpenFragmentUtils.goToSimpleFragment(getActivity(), MsgFragment.class.getName(), new Bundle());


                break;

        }
    }


    public int auplay(int currt) {
        int index = litmited_pager.getCurrentItem() % 3;

        if (currt == 1) {
            if (index == 1) {
                return litmited_pager.getCurrentItem();
            } else if (index == 2) {
                return litmited_pager.getCurrentItem() - 1;
            } else {
                return litmited_pager.getCurrentItem() + 1;
            }

        } else if (currt == 2) {
            if (index == 1) {
                return litmited_pager.getCurrentItem() + 1;
            } else if (index == 2) {
                return litmited_pager.getCurrentItem();
            } else {
                return litmited_pager.getCurrentItem() + 2;
            }
        } else {
            if (index == 1) {
                return litmited_pager.getCurrentItem() - 1;
            } else if (index == 2) {
                return litmited_pager.getCurrentItem() - 2;
            } else {
                return litmited_pager.getCurrentItem();
            }
        }

    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        Log.e("oooooo","滑动了："+verticalOffset);
        if (scroll==verticalOffset){
            showShareImage();
        }else{
            hideShareImage();
            scroll=verticalOffset;
        }


    }

    public class NewsPagerAdapter extends FragmentPagerAdapter {
        private List<IconFragment> mFragments;
        private List<DoorGodCategoryBean.ResultListBean> mresultList;

        public NewsPagerAdapter(FragmentManager fm, List<IconFragment> fragments, List<DoorGodCategoryBean.ResultListBean> resultList) {
            super(fm);
            mFragments = fragments;
            mresultList = resultList;

        }

        @Override
        public Fragment getItem(int position) {
            return IconFragment.newInstance(mresultList.get(position).getWheelChartDisplayVo());
        }

        @Override
        public int getCount() {
            return mFragments == null ? 0 : mFragments.size();
        }


        //    为Tabayout设置主题名称
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return title == null ? "" + position : title.get(position);
//        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {

        }
    }

    public class LimitePagerAdapter extends FragmentPagerAdapter {
        private List<LimiteFragment> mFragments;
        private List<PanicBuyingListBean.TimeListBean> timeList;


        public LimitePagerAdapter(FragmentManager fm, List<LimiteFragment> fragments, List<PanicBuyingListBean.TimeListBean> timeList) {
            super(fm);
            mFragments = fragments;
            this.timeList = timeList;

        }

        @Override
        public Fragment getItem(int position) {
            int mpostion = position % 3;
            return LimiteFragment.newInstance(timeList.get(mpostion).getItemList());
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }


        //    为Tabayout设置主题名称
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return title == null ? "" + position : title.get(position);
//        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {

        }
    }

    private class HomeAdapter extends FragmentPagerAdapter {
        private List<GoodCategoryInfo> mHomeColumns;

        public HomeAdapter(FragmentManager fragmentManager, List<GoodCategoryInfo> mHomeColumns) {
            super(fragmentManager);
            this.mHomeColumns = mHomeColumns;
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
                SelectGoodsFragment goodsFragment = SelectGoodsFragment.newInstance();
                //  HomeRecommendFragment homeRecommendFragment = HomeRecommendFragment.newInstance();
                //  homeRecommendFragment.setUpdateColorCallback(HomeOtherFragment.this);
                fragments.add(goodsFragment);
                return goodsFragment;
            } else if (getString(R.string.what_like).equals(homeColumn.getName())) {
                GuessLikeFragment whatLikeFragment = GuessLikeFragment.newInstance();
                return whatLikeFragment;
            } else {
                List<Child2> childs = homeColumn.getChild2();
                return CategoryListFragment2.newInstance(homeColumn.getName(), childs);

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

    //爆款热销
    public Observable<BaseResponse<List<HandpickBean>>> getActivities(RxFragment fragment) {
        return RxHttp.getInstance().getSysteService().getActivities()
                .compose(RxUtils.<BaseResponse<List<HandpickBean>>>switchSchedulers())
                .compose(fragment.<BaseResponse<List<HandpickBean>>>bindToLifecycle());
    }

    //限时秒杀
    public Observable<BaseResponse<PanicBuyingListBean>> getpainBuyinglist(RxFragment fragment) {
        return RxHttp.getInstance().getSysteService().getpanicBuyingList()
                .compose(RxUtils.<BaseResponse<PanicBuyingListBean>>switchSchedulers())
                .compose(fragment.<BaseResponse<PanicBuyingListBean>>bindToLifecycle());
    }

    //0元购信息
    public Observable<BaseResponse<UserZeroInfoBean>> getUserZeroInfo(RxFragment fragment) {
        return RxHttp.getInstance().getSysteService().getUserZeroInfo()
                .compose(RxUtils.<BaseResponse<UserZeroInfoBean>>switchSchedulers())
                .compose(fragment.<BaseResponse<UserZeroInfoBean>>bindToLifecycle());
    }

    //获取金刚位
    public Observable<BaseResponse<DoorGodCategoryBean>> getDoorGodCategory(RxFragment fragment) {
        RequestBannerBean requestBannerBean = new RequestBannerBean();
        requestBannerBean.setType(C.UIShowType.HomeIcon);
        return RxHttp.getInstance().getSysteService().getDoorGodCategory(requestBannerBean)
                .compose(RxUtils.<BaseResponse<DoorGodCategoryBean>>switchSchedulers())
                .compose(fragment.<BaseResponse<DoorGodCategoryBean>>bindToLifecycle());
    }


    //查询抖货和高佣专区接口
    public Observable<BaseResponse<QueryDhAndGyBean>> getQueryDhAndGy(RxFragment fragment) {
        return RxHttp.getInstance().getSysteService().getQueryDhAndGy()
                .compose(RxUtils.<BaseResponse<QueryDhAndGyBean>>switchSchedulers())
                .compose(fragment.<BaseResponse<QueryDhAndGyBean>>bindToLifecycle());
    }

    //新楼层查询
    public Observable<BaseResponse<FloorBean2>> getListGraphicInfoSorting(RxFragment fragment) {
        return RxHttp.getInstance().getSysteService().getListGraphicInfoSorting()
                .compose(RxUtils.<BaseResponse<FloorBean2>>switchSchedulers())
                .compose(fragment.<BaseResponse<FloorBean2>>bindToLifecycle());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (count_time_view!=null&&mObserver!=null){
            count_time_view.unregisterDataSetObserver(mObserver);
        }

        if (count_time_view2!=null&&mObserver2!=null){
            count_time_view2.unregisterDataSetObserver(mObserver2);
        }
    }
}
