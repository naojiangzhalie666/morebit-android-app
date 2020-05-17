package com.zjzy.morebit.main.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blankj.utilcode.util.SPUtils;
import com.flyco.tablayout.SlidingTabLayout;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.zjzy.morebit.Activity.SearchActivity;
import com.zjzy.morebit.Activity.ShowWebActivity;
import com.zjzy.morebit.App;
import com.zjzy.morebit.R;
import com.zjzy.morebit.fragment.PanicBuyFragment;
import com.zjzy.morebit.fragment.base.BaseMainFragmeng;
import com.zjzy.morebit.main.ui.myview.xtablayout.XTabLayout;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.RankingTitleBean;
import com.zjzy.morebit.pojo.goods.FloorBean;
import com.zjzy.morebit.pojo.goods.GoodCategoryInfo;
import com.zjzy.morebit.pojo.pddjd.PddJdTitleTypeItem;
import com.zjzy.morebit.pojo.request.RequestBannerBean;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.GlideImageLoader;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.SensorsDataUtil;
import com.zjzy.morebit.utils.SwipeDirectionDetector;
import com.zjzy.morebit.utils.UI.BannerInitiateUtils;
import com.zjzy.morebit.view.AspectRatioView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;


/**
 * 首页分类-拼多多商品列表
 */
public class PddChildFragment extends BaseMainFragmeng {

    @BindView(R.id.xTablayout)
    XTabLayout tablayout;
    @BindView(R.id.status_bar)
    View status_bar;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.iv_right_search)
    ImageView iv_right_search;

    boolean isUserHint =true;
    private View mView;
    private int mPushType;
    private ChannelAdapter mChannelAdapter;
    private SwipeDirectionDetector swipeDirectionDetector;
    private int currentViewPagerPosition = 0;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean canRefresh = true;
    private AppBarLayout mAppBarLt;
    private Banner banner;
    private AspectRatioView ar_title_banner;

    /**
     * 拼多多商品页面
     *
     * @param type
     * @return
     */
    public static PddChildFragment newInstance(int type) {
        PddChildFragment rankingChildFragment = new PddChildFragment();
        Bundle args = new Bundle();
        args.putInt(C.Extras.pushType, type);
        rankingChildFragment.setArguments(args);
        return rankingChildFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_pdd_child, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPushType = getArguments().getInt(C.Extras.pushType);
        initView(mView);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        MyLog.d("setUserVisibleHint", "CircleFragment  " + isVisibleToUser);
        if (isVisibleToUser && isUserHint && mView != null) {
            initView(mView);
            isUserHint = false;
        }

    }


    /**
     * 初始化界面
     */
    private void initView(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //处理全屏显示
            ViewGroup.LayoutParams viewParams = status_bar.getLayoutParams();
            viewParams.height = ActivityStyleUtil.getStatusBarHeight(getActivity());
            status_bar.setLayoutParams(viewParams);

        }

        banner=view.findViewById(R.id.banner);
        ar_title_banner=view.findViewById(R.id.ar_title_banner);

       // App.getACache().put(C.sp.PDD_CATEGORY, (ArrayList) initPddTitle());
      //  List<PddJdTitleTypeItem> data = (List<PddJdTitleTypeItem>) App.getACache().getAsObject(C.sp.PDD_CATEGORY);

        RxHttp.getInstance().getCommonService().getPddTitle()//获取拼多多栏目
                .compose(RxUtils.<BaseResponse<List<PddJdTitleTypeItem>>>switchSchedulers())
                .compose(this.<BaseResponse<List<PddJdTitleTypeItem>>>bindToLifecycle())
                .subscribe(new DataObserver<List<PddJdTitleTypeItem>>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {

                    }

                    @Override
                    protected void onSuccess(List<PddJdTitleTypeItem> data) {
                        if (data != null) {
                            if (data != null && data.size() != 0) {
                                initTab(data);
                            }

                        }
                    }
                });



        initBanner();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        iv_right_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });


        mAppBarLt = view.findViewById(R.id.app_bar_lt);
        swipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
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
                initBanner();
                Intent intent = new Intent();
                intent.setAction("action.refreshpdd");
                getActivity().sendBroadcast(intent);

            }
        });

//添加页面滑动监听,控制 SwipeRefreshLayout与ViewPager滑动冲突
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
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


    }

    private void initBanner() {
        RequestBannerBean requestBean = new RequestBannerBean();
        requestBean.setType(36);
        requestBean.setOs(1);
        RxHttp.getInstance().getCommonService().getBanner(requestBean)//获取京东banner
                .compose(RxUtils.<BaseResponse<List<ImageInfo>>>switchSchedulers())
                .compose(this.<BaseResponse<List<ImageInfo>>>bindToLifecycle())
                .subscribe(new DataObserver<List<ImageInfo>>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {

                    }

                    @Override
                    protected void onSuccess(List<ImageInfo> data) {
                        swipeRefreshLayout.setRefreshing(false);
                            if (data != null && data.size() != 0) {
                                BannerInitiateUtils.setJpBanner(getActivity(), data, banner, ar_title_banner);
                        }else{
                                ar_title_banner.setVisibility(View.GONE);
                            }
                    }
                });
    }

    private void setupViewPager(final List<PddJdTitleTypeItem> homeColumns) {

        mChannelAdapter.setHomeColumns(homeColumns);
        mChannelAdapter.notifyDataSetChanged();

        tablayout.setupWithViewPager(viewPager);


    }

    private void initTab(List<PddJdTitleTypeItem> data) {
        mChannelAdapter = new ChannelAdapter(getChildFragmentManager());
        swipeDirectionDetector = new SwipeDirectionDetector();
        viewPager.setAdapter(mChannelAdapter);
        setupViewPager(data);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
    }

    private class ChannelAdapter extends FragmentPagerAdapter {
        private List<PddJdTitleTypeItem> mHomeColumns = new ArrayList<>();

        public ChannelAdapter(FragmentManager fm) {
            super(fm);

        }
        public void setHomeColumns(List<PddJdTitleTypeItem> homeColumns) {
            mHomeColumns.clear();
            if (homeColumns == null && homeColumns.size() == 0) {
                return;
            }
            mHomeColumns.addAll(homeColumns);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            PddJdTitleTypeItem homeColumn = mHomeColumns.get(position);
            return homeColumn.getTabName();

        }

        @Override
        public Fragment getItem(int position) {
            return PddListFragment.newInstance(mHomeColumns.get(position));
        }

        @Override
        public int getCount() {
            return mHomeColumns.size();
        }
    }

}
