package com.zjzy.morebit.home.fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.alibaba.android.vlayout.layout.StickyLayoutHelper;
import com.blankj.utilcode.util.SPUtils;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.youth.banner.Banner;
import com.zjzy.morebit.Activity.ShowWebActivity;
import com.zjzy.morebit.App;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.HomeActivityAdapter;
import com.zjzy.morebit.adapter.HomeBaoAdapter;
import com.zjzy.morebit.adapter.HomeDouAdapter;
import com.zjzy.morebit.adapter.HomeGoodsAdapter;
import com.zjzy.morebit.adapter.HomeIconAdapter;
import com.zjzy.morebit.adapter.HomeLimiteAdapter;
import com.zjzy.morebit.adapter.HomeNewAdapter;
import com.zjzy.morebit.adapter.HomeOtherAdapter;
import com.zjzy.morebit.adapter.HomeSelectedAdapter;
import com.zjzy.morebit.fragment.PanicBuyFragment;
import com.zjzy.morebit.home.contract.HomeRecommentContract;
import com.zjzy.morebit.home.presenter.HomeRecommendPresenter;
import com.zjzy.morebit.interfaces.GuideNextCallback;
import com.zjzy.morebit.main.ui.myview.xtablayout.XTabLayout;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.FloorInfo;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.VideoClassBean;
import com.zjzy.morebit.pojo.goods.FloorBean;
import com.zjzy.morebit.pojo.goods.HandpickBean;
import com.zjzy.morebit.pojo.goods.NewRecommendBean;
import com.zjzy.morebit.pojo.request.RequestBannerBean;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.UI.BannerInitiateUtils;
import com.zjzy.morebit.view.AspectRatioView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeOtherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeOtherFragment extends MvpFragment<HomeRecommendPresenter> implements HomeRecommentContract.View {
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
    private List<String> title;
    private List<IconFragment> mFragments;
    private NewsPagerAdapter mAdapter;
    private XTabLayout xTablayout;
    private ViewPager icon_pager;


    public static HomeOtherFragment newInstance(String param1, String param2) {
        HomeOtherFragment fragment = new HomeOtherFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        initOtherView();//初始化
        initBanner();//banner
        initRefresh();
    }



    private void initOtherView() {
        home_rcy = mView.findViewById(R.id.home_rcy);
        status_bar = mView.findViewById(R.id.status_bar);
        swipeRefreshLayout=mView.findViewById(R.id.swipeRefreshLayout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //处理全屏显示
            ViewGroup.LayoutParams viewParams = status_bar.getLayoutParams();
            viewParams.height = ActivityStyleUtil.getStatusBarHeight(getActivity());
            status_bar.setLayoutParams(viewParams);
            // 设置状态栏颜色
            getActivity().getWindow().setStatusBarColor(Color.parseColor("#F05557"));

        }

        VirtualLayoutManager virtualLayoutManager = new VirtualLayoutManager(getActivity());
        home_rcy.setLayoutManager(virtualLayoutManager);
        DelegateAdapter delegateAdapter = new DelegateAdapter(virtualLayoutManager);

        HomeOtherAdapter otherAdapter = new HomeOtherAdapter(getActivity(), new SingleLayoutHelper()) {
            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                if (position == 0) {
                    banner = holder.itemView.findViewById(R.id.roll_view_pager);
                    mAsTitleBanner = holder.itemView.findViewById(R.id.ar_title_banner);
                }
            }
        };//通栏布局 banner

        HomeIconAdapter homeIconAdapter = new HomeIconAdapter(getActivity(), new SingleLayoutHelper()){
            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                if (position == 0) {
                    xTablayout=holder.itemView.findViewById(R.id.xTablayout);
                    icon_pager=holder.itemView.findViewById(R.id.icon_pager);
                    title=new ArrayList<>();
                    title.add("热门");
                    title.add("购物");
                    title.add("生活");
                    title.add("优选");
                    mFragments = new ArrayList<>();
                    for (int i = 0; i < title.size(); i++) {
                        IconFragment fragment = null;
                        fragment = new IconFragment();
                        mFragments.add(fragment.newInstance());
                    }

                    mAdapter = new NewsPagerAdapter(getActivity().getSupportFragmentManager(), mFragments,title);
                    icon_pager.setAdapter(mAdapter);
                    xTablayout.setupWithViewPager(icon_pager);
                    icon_pager.setOffscreenPageLimit(title.size());
                }
            }
        };// icon 金刚区
        HomeNewAdapter homeNewAdapter = new HomeNewAdapter(getActivity(), new SingleLayoutHelper());//新人0元购
        HomeActivityAdapter homeActivityAdapter = new HomeActivityAdapter(getActivity(), new SingleLayoutHelper());//精选活动
        HomeLimiteAdapter homeLimiteAdapter = new HomeLimiteAdapter(getActivity(), new SingleLayoutHelper());//限时秒杀
        HomeDouAdapter homeDouAdapter = new HomeDouAdapter(getActivity(), new SingleLayoutHelper());//抖货 定向高佣
        HomeBaoAdapter homeBaoAdapter = new HomeBaoAdapter(getActivity(), new SingleLayoutHelper());//爆款热销
        StickyLayoutHelper stickyLayoutHelper = new StickyLayoutHelper();
        stickyLayoutHelper.setStickyStart(true);
        stickyLayoutHelper.setItemCount(1);
        HomeSelectedAdapter homeSelectedAdapter = new HomeSelectedAdapter(getActivity(), stickyLayoutHelper);//精选分类导航栏
        HomeGoodsAdapter homeGoodsAdapter = new HomeGoodsAdapter(getActivity(), new LinearLayoutHelper());
        ArrayList<DelegateAdapter.Adapter> adapters = new ArrayList<>();
        adapters.add(otherAdapter);
        adapters.add(homeIconAdapter);
        adapters.add(homeNewAdapter);
        adapters.add(homeActivityAdapter);
        adapters.add(homeLimiteAdapter);
        adapters.add(homeDouAdapter);
        adapters.add(homeBaoAdapter);
        adapters.add(homeSelectedAdapter);
        adapters.add(homeGoodsAdapter);
        adapters.add(homeBaoAdapter);
        adapters.add(homeBaoAdapter);
        delegateAdapter.addAdapters(adapters);

        home_rcy.setAdapter(delegateAdapter);
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


    }

    private void onBrandBannerSuccess(List<ImageInfo> data, int back) {
        list = data;
        putBannerData((ArrayList) data, back);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View view) {


    }

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

    @Override
    public void onActivitySuccessFul(List<HandpickBean> data) {

    }

    @Override
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


    public class NewsPagerAdapter extends FragmentPagerAdapter {
        private List<IconFragment> mFragments;
        private List<String> title;
        public NewsPagerAdapter(FragmentManager fm, List<IconFragment> fragments, List<String> stitle) {
            super(fm);
            mFragments = fragments;
            title=stitle;
        }

        @Override
        public Fragment getItem(int position) {
            return IconFragment.newInstance();
        }

        @Override
        public int getCount() {
            return mFragments == null ? 0: mFragments.size();
        }


        //    为Tabayout设置主题名称
        @Override
        public CharSequence getPageTitle(int position) {
            return title == null ? "" + position : title.get(position);
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
        }
    }

}
