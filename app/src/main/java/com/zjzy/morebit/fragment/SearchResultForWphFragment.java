package com.zjzy.morebit.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.JdListAdapter;
import com.zjzy.morebit.adapter.WphListAdapter;
import com.zjzy.morebit.main.contract.PddContract;
import com.zjzy.morebit.main.presenter.PddListPresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.pojo.ProgramCatItemBean;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.event.SearchGoodsForJdEvent;
import com.zjzy.morebit.pojo.event.SearchGoodsForWphEvent;
import com.zjzy.morebit.pojo.request.ProgramWphBean;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.ViewShowUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 京东的搜索结果展示
 */

public class SearchResultForWphFragment extends MvpFragment<PddListPresenter> implements PddContract.View, View.OnClickListener {

    private WphListAdapter mAdapter;
    private String keyWord = "";

    private static final int REQUEST_COUNT = 10;

    private View mView;
    @BindView(R.id.searchNullTips_ly)
    LinearLayout searchNullTips_ly;
    @BindView(R.id.listview)
    RecyclerView mRecyclerView;
    @BindView(R.id.dataList_ly)
    LinearLayout dataList_ly;

    boolean isUserHint = true;
    private int mPushType;
    private List<ShopGoodInfo> listArray = new ArrayList<>();


    //券后价
    private LinearLayout mTitlePostCouponPriceLl;
    private ImageView mTitlePostCouponPriceIv;
    private TextView mTitlePostCouponPriceTv;
    //佣金
    private LinearLayout mTitleCommissionLl;
    private ImageView mTitleCommissionIv;
    private TextView mTitleCommissionTv;

    private TextView title_comprehensive_tv;
    //排序方向
    private int eSortDirection = C.OrderType.E_UPLIMIT_SORT_DOWN;// 0降序  1升序
    //排序类型
    private long mSortType = 0;//排序类型 0 综合排序 2 销量排序 3 价格排序 4 奖励排序
    private int page=1;
    private SmartRefreshLayout mSwipeList;



    public static SearchResultForWphFragment newInstance(int type) {
        SearchResultForWphFragment searchResultForjdFragment = new SearchResultForWphFragment();
        Bundle args = new Bundle();
        args.putInt(C.Extras.pushType, type);
        searchResultForjdFragment.setArguments(args);
        return searchResultForjdFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_searchcommodity_wph, container, false);
        return mView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        MyLog.d("setUserVisibleHint", "CircleFragment  " + isVisibleToUser);
        if (isVisibleToUser && isUserHint && mView != null && mPushType ==4) {
            initView(mView);
            isUserHint = false;
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPushType = getArguments().getInt(C.Extras.pushType);
        if (mPushType == 4) {
          initView(mView);
        }
        initBundle();

    }

    @Override
    protected void initData() {

    }

    private Bundle bundle;

    private void initBundle() {
        bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            keyWord = getActivity().getIntent().getStringExtra("keyWord");
        }
    }


    public void initView(View view) {
        title_comprehensive_tv = view.findViewById(R.id.title_comprehensive_tv);//综合


        mTitlePostCouponPriceLl = view.findViewById(R.id.title_post_coupon_price__ll);//价格
        mTitlePostCouponPriceTv = view.findViewById(R.id.title_post_coupon_price_tv);
        mTitlePostCouponPriceIv = view.findViewById(R.id.title_post_coupon_price_iv);

        mTitleCommissionLl = view.findViewById(R.id.title_commission_ll);//奖励
        mTitleCommissionTv = view.findViewById(R.id.title_commission_tv);
        mTitleCommissionIv = view.findViewById(R.id.title_commission_iv);



        mTitlePostCouponPriceLl.setOnClickListener(this);
        mTitleCommissionLl.setOnClickListener(this);
        title_comprehensive_tv.setOnClickListener(this);
        getFirstData(keyWord);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        //设置图标的间距
        mRecyclerView.setLayoutManager(manager);
        mSwipeList=view.findViewById(R.id.swipeList);
        mSwipeList.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page=1;
                onReload();
                refreshLayout.finishRefresh(true);//刷新完成
            }
        });
        mSwipeList.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                onReload();
            }
        });

    }

    @Override
    protected int getViewLayout() {
        return R.layout.fragment_searchcommodity_jd;
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }


    /*
     * 第一次获取数据
     */
    public void getFirstData(String keyWords) {

        if (TextUtils.isEmpty(keyWords)) {
            if (!isUserHint){
                ViewShowUtils.showShortToast(getActivity(), "请输入搜索内容");
            }
            return;
        }
        keyWord = keyWords;
        page=1;
        onReload();

    }

    private void fristSearch(String keywords) {

    }


    @Subscribe  //订阅事件
    public void onEventMainThread(SearchGoodsForWphEvent event) {
        getFirstData(event.getKeyword());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        page=1;
        switch (v.getId()) {
            case R.id.title_comprehensive_tv://综合
                requestClickRadar(null, title_comprehensive_tv, 0);
                onReload();
                break;
            case R.id.title_post_coupon_price__ll://价格
                requestClickRadar(mTitlePostCouponPriceIv, mTitlePostCouponPriceTv, 1);
                onReload();
                break;
            case R.id.title_commission_ll://奖励
                requestClickRadar(mTitleCommissionIv, mTitleCommissionTv, 2);
                onReload();
                break;
        }
    }

    private void requestClickRadar(ImageView clickIv, TextView textView, int orderType) {
        if (orderType == 0) {
            //综合只有降序
            eSortDirection = C.OrderType.E_UPLIMIT_SORT_DOWN;
        } else {
            if (!textView.isSelected()) {
                eSortDirection = C.OrderType.E_UPLIMIT_SORT_DOWN;
            } else {
                eSortDirection = eSortDirection == C.OrderType.E_UPLIMIT_SORT_DOWN ? C.OrderType.E_UPLIMIT_SORT_UP : C.OrderType.E_UPLIMIT_SORT_DOWN;
            }
        }
        mSortType = orderType;
        resetTitleRankDrawable(clickIv, textView, eSortDirection);

//        mLoadMoreHelper.loadData();
    }

    private void resetTitleRankDrawable(ImageView clickIv, TextView textView, int eSortDir) {
        //对点击的重置图

        mTitlePostCouponPriceIv.setImageResource(R.drawable.icon_jiage_no);
        mTitleCommissionIv.setImageResource(R.drawable.icon_jiage_no);

        mTitlePostCouponPriceTv.setSelected(false);
        mTitleCommissionTv.setSelected(false);
        title_comprehensive_tv.setTextColor(Color.parseColor("#999999"));
        mTitlePostCouponPriceTv.setTextColor(Color.parseColor("#999999"));
        mTitleCommissionTv.setTextColor(Color.parseColor("#999999"));
        //对点击的设置 图片
        if (clickIv != null) {
            clickIv.setImageResource(getDrawableIdBySortDir(eSortDir));
        }
        if (textView != null) {
            textView.setSelected(true);
            textView.setTextColor(Color.parseColor("#FF645B"));
        }
    }

    private int getDrawableIdBySortDir(int sortDir) {
        int res = R.drawable.icon_jiage_no;
        switch (sortDir) {
            case 0:
                res = R.drawable.icon_jiage_down;
                break;
            case 1:
                res = R.drawable.icon_jiage_up;
                break;
        }
        return res;
    }




    public void onReload() {
        ProgramWphBean programCatItemBean = new ProgramWphBean();
        programCatItemBean.setPage(page);
        programCatItemBean.setKeyword(keyWord);
        if (eSortDirection == 0) {
            programCatItemBean.setOrder("0");
        } else {
            programCatItemBean.setOrder("1");
        }
        if (mSortType==0){
            programCatItemBean.setFieldName("");
        }else  if (mSortType==1){
            programCatItemBean.setFieldName("PRICE");
        }else  if (mSortType==2){
            programCatItemBean.setFieldName("COMMISSION");
        }
        mPresenter.getWphGoodList(this, programCatItemBean, C.requestType.initData);
    }




    @Override
    public void setPdd(List<ShopGoodInfo> data, int loadType) {

    }

    @Override
    public void setPddError(int loadType) {

    }

    @Override
    public void setJd(List<ShopGoodInfo> data, int loadType) {

    }

    @Override
    public void setJdError(int loadType) {


    }

    @Override
    public void setWph(List<ShopGoodInfo> data, int loadType) {
        if (page==1) {
            //mData.clear();
            mAdapter = new WphListAdapter(getActivity(), data);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            // rl_list.getListView().refreshComplete(10);
            mAdapter.setData(data);
            mSwipeList.finishLoadMore(false);
        }
    }

    @Override
    public void setWphError(int loadType) {
        if (page==1){
            mAdapter = new WphListAdapter(getActivity(), listArray);
            mRecyclerView.setAdapter(mAdapter);
        }else{
            mSwipeList.finishLoadMore(false);
        }
    }

    @Override
    public void showFinally() {
    }
}
