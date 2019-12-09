package com.zjzy.morebit.circle.ui;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.zjzy.morebit.Activity.ShowWebActivity;
import com.zjzy.morebit.App;
import com.zjzy.morebit.Module.common.Dialog.HintDialog;
import com.zjzy.morebit.Module.common.View.CommonRecyclerView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.circle.adapter.CollegeHomeAdapter;
import com.zjzy.morebit.circle.adapter.StudyRankAdapter;
import com.zjzy.morebit.circle.contract.CommercialCollegeContract;
import com.zjzy.morebit.circle.presenter.CommercialCollegePresenter;
import com.zjzy.morebit.contact.EventBusAction;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.pojo.CollegeHome;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.MarkermallInformation;
import com.zjzy.morebit.pojo.StudyRank;
import com.zjzy.morebit.pojo.request.RequestListBody;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.UI.BannerInitiateUtils;
import com.zjzy.morebit.utils.UIUtils;
import com.zjzy.morebit.utils.UploadingOnclickUtils;
import com.zjzy.morebit.view.UPMarqueeView;
import com.youth.banner.Banner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * Created by YangBoTian on 2018/12/24.
 * 商学院
 */

public class CommercialCollegeFragment extends MvpFragment<CommercialCollegePresenter> implements CommercialCollegeContract.View {
    private static final int REQUEST_COUNT = 10;
    private View mHeadView;
    CommonRecyclerView mReUseListView;
    RecyclerView viewpger_category;
    //    private LinearLayout search;
    private ImageView iv_hot;
    private Banner mBanner;
    private int page;
    //    View status_bar;
    private CollegeHomeAdapter mArticleAdapter;
    private StudyRankAdapter mStudyRankAdapter;
    private HintDialog mDialog;
    UPMarqueeView mUpview;
    private FrameLayout scrollbarLayout;
    private View bar_line;
    private LinearLayout ll_information_more;
    private  List<MarkermallInformation> mMarkermallInformationList = new ArrayList<>();
    float endX = 0;
    List<View> mHotviews = new ArrayList<>();

    public static CommercialCollegeFragment newInstance() {
        Bundle args = new Bundle();
        CommercialCollegeFragment fragment = new CommercialCollegeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    //    @Override
    //    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    //        MyLog.i("test","onCreateView");
    //        if (mView == null) {
    //            super.onCreateView(inflater, container, savedInstanceState);
    //            mHeadView = LayoutInflater.from(getActivity()).inflate(R.layout.item_commercial_college_headview, null);
    //            init();
    //        }
    //        ViewGroup parent = (ViewGroup) mView.getParent();
    //        if (parent != null) {
    //            parent.removeView(mView);
    //        }
    //        return mView;
    //    }

    private void init() {
        initHeadView();
        initRecyclerView();
        refreshData();
    }

    private void initRecyclerView() {
        mReUseListView =mView.findViewById(R.id.mListView);
//        mReUseListView.getSwipeList().setOnRefreshListener(new com.markermall.cat.Module.common.widget.SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//               if(mReUseListView.getSwipeList().isRefreshing()){
//                   return;
//               }
//
//            }
//        });
        mReUseListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mArticleAdapter = new CollegeHomeAdapter(getActivity(), new ArrayList<CollegeHome>(),this);
        mArticleAdapter.setEnableLoadMore(false);
        mArticleAdapter.addHeaderView(mHeadView);

        mReUseListView.setAdapter(mArticleAdapter, new CommonRecyclerView.RefreshAndLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {

            }

            @Override
            public void onRefresh() {
                refreshData();
            }
        });
//        mReUseListView.setAdapterAndHeadView(mHeadView, mArticleAdapter);
//        mReUseListView.getListView().setMiyuanNoMore(true);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initView(View view) {
        mHeadView = LayoutInflater.from(getActivity()).inflate(R.layout.item_commercial_college_headview, null);
//        status_bar = view.findViewById(R.id.status_bar);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            //处理全屏显示
//            ViewGroup.LayoutParams viewParams = status_bar.getLayoutParams();
//            viewParams.height = ActivityStyleUtil.getStatusBarHeight(getActivity());
//            status_bar.setLayoutParams(viewParams);
//
//        }
        init();
    }

    private void initHeadView() {
        mBanner = (Banner) mHeadView.findViewById(R.id.banner);
        iv_hot = mHeadView.findViewById(R.id.iv_hot);
        mUpview = (UPMarqueeView) mHeadView.findViewById(R.id.upview);
        ll_information_more =  mHeadView.findViewById(R.id.ll_information_more);
//        mHeadView.findViewById(R.id.moreNoticeIv).setVisibility(View.VISIBLE);

        mHeadView.findViewById(R.id.ll_hote_text).setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
//        search = (LinearLayout) mHeadView.findViewById(R.id.search);
        initStudyView();
//        search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                OpenFragmentUtils.goToSimpleFragment(getActivity(), SearchArticleListResultFragment.class.getName(), new Bundle());
//            }
//        });
//        mHeadView.findViewById(R.id.ll_more).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                RecommendListActivity.start(getActivity(), RecommendListActivity.ARTICLE_RECOMMEND);
//            }
//        });
        ll_information_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarkermallInformationFragment.start(getActivity(), mMarkermallInformationList);
            }
        });
        iv_hot.setImageResource(R.drawable.icon_college_information);
    }

    private void initStudyView() {
        viewpger_category = (RecyclerView) mHeadView.findViewById(R.id.viewpger_category);
        bar_line = mHeadView.findViewById(R.id.main_line);
        scrollbarLayout = mHeadView.findViewById(R.id.scrollbarLayout);
        int windowWidth = AppUtil.getWindowWidth(getActivity());
//        MyLog.i("test","windowWidth: " +windowWidth);
//        int left = ConvertUtils.dp2px(27);
//        int right = ConvertUtils.dp2px(51);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        CollegeCategoryItemDecoration itemDecoration = new CollegeCategoryItemDecoration(0,0,0,13);
//        viewpger_category.addItemDecoration(itemDecoration);
        viewpger_category.setLayoutManager(linearLayoutManager);
        mStudyRankAdapter = new StudyRankAdapter(getActivity(), windowWidth / 4);
        viewpger_category.setAdapter(mStudyRankAdapter);
        setScrollBar();
    }

    private void setCategorys(List<StudyRank> datas) {
        if (datas == null || datas.size() == 0) {
            return;
        }
        mStudyRankAdapter.replace(datas);
        mStudyRankAdapter.notifyDataSetChanged();
        viewpger_category.scrollToPosition(0);
        endX = 0.0f;
        bar_line.setTranslationX(0);
        App.mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (UIUtils.isRecyclerScrollable(viewpger_category)) {
                    scrollbarLayout.setVisibility(View.VISIBLE);
                } else {
                    scrollbarLayout.setVisibility(View.GONE);
                }
            }
        }, 500);

    }

    private void refreshData() {
        page = 1;
        mPresenter.getCarousel(this);
        mPresenter.getCommercialBanner(this); //获取轮播图
        mPresenter.getStudyRank(this);

        RequestListBody pageBean = new RequestListBody();
//        pageBean.setPage(page);

        mPresenter.getPageAggregationList(this, pageBean);
    }


    @Override
    protected int getViewLayout() {
        return R.layout.fragment_commercial_college;
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }

    @Override
    public void onBannerSuccessful(List<ImageInfo> data) {
        BannerInitiateUtils.setBrandBanner(getActivity(), data, mBanner);
    }

    @Override
    public void onStudyRankSuccessful(List<StudyRank> data) {
        setCategorys(data);
//        mStudyRankAdapter.replace(data);

    }

    @Override
    public void onStudyRankEmpty() {
        viewpger_category.setVisibility(View.GONE);
    }

    @Override
    public void onArticleSuccessful(List<CollegeHome> data) {
        for(CollegeHome collegeHome:data){
            if(collegeHome.getModelId()==0&&data.size()>4){
                data.get(3).setTypeset(CollegeHome.TYPE_RECOMMEND);
            }
        }
        mArticleAdapter.setNewData(data);
        mArticleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onArticleEmpty() {

    }


    @Override
    public void onArticleFinally() {
        mReUseListView.getRefreshLayout().finishRefresh();
        mArticleAdapter.loadMoreEnd();
        mArticleAdapter.notifyDataSetChanged();
//        mReUseListView.getListView().refreshComplete(REQUEST_COUNT);
    }

    @Override
    public void onCarouselSuc(List<MarkermallInformation> data) {
        mMarkermallInformationList.clear();
        mMarkermallInformationList.addAll(data);
        setHotView(data);
    }


    private void setScrollBar() {
        viewpger_category.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //整体的总宽度，注意是整体，包括在显示区域之外的。
                int range = viewpger_category.computeHorizontalScrollRange();
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


    @Subscribe  //订阅事件
    public void onEventMainThread(MessageEvent event) {
        switch (event.getAction()) {
            case EventBusAction.LOGINA_SUCCEED:
                refreshData();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @OnClick({R.id.btn_search})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                SearchArticleListActitivty.start(getActivity());
                break;
        }
    }


    private void setHotView(final List<MarkermallInformation> datas) {
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
                if(datas.get(position).getOpen()==3){
                    ShowWebActivity.start(getActivity(), datas.get(position).getUrl(), datas.get(position).getTitle());
                  UploadingOnclickUtils.updateThemeClicks(datas.get(position).getId(),1);
                } else if(datas.get(position).getOpen()==9){
                    BannerInitiateUtils.goToArticle(getActivity(),datas.get(position).getUrl(),datas.get(position).getTitle());
                    UploadingOnclickUtils.mp4Browse(datas.get(position).getId(),1);
                }
                //sendSearchStatistics(datas.get(position).getId()+"");
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



}
