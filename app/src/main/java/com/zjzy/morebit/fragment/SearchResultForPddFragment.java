package com.zjzy.morebit.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.View.ReUseListView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.ShoppingListForPddAdapter;
import com.zjzy.morebit.fragment.base.BaseMainFragmeng;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UI.BaseTitleTabBean;
import com.zjzy.morebit.pojo.event.SearchGoodsForPddEvent;
import com.zjzy.morebit.pojo.event.SearchGoodsForTaobaoEvent;
import com.zjzy.morebit.pojo.request.RequestSearchForPddBean;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.DensityUtil;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.ViewShowUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.reactivex.Observable;
import io.reactivex.functions.Action;

/**
 * 拼多多的搜索结果展示
 */

public class SearchResultForPddFragment extends BaseMainFragmeng {

    private ShoppingListForPddAdapter mAdapter;
    private List<ShopGoodInfo> listArray = new ArrayList<>();
    private String keyWord = "";

    private static final int REQUEST_COUNT = 10;

    private TabLayout mTabLayout;
    private View mView;


    private int mPage = 1;
    private boolean isCouponShowOff = false;

    private ArrayList<BaseTitleTabBean> tabList = new ArrayList<>();
    private int mSelectedPos;
    @BindView(R.id.searchNullTips_ly)
    LinearLayout searchNullTips_ly;
    @BindView(R.id.listview)
    ReUseListView mRecyclerView;
    @BindView(R.id.dataList_ly)
    LinearLayout dataList_ly;
    @BindView(R.id.couponIv)
    ImageView couponIv;
    @BindView(R.id.couponTv)
    TextView couponTv;
    boolean isUserHint =true;
    private int mPushType;





    public static SearchResultForPddFragment newInstance(int type) {
        SearchResultForPddFragment searchResultForPddFragment = new SearchResultForPddFragment();
        Bundle args = new Bundle();
        args.putInt(C.Extras.pushType, type);
        searchResultForPddFragment.setArguments(args);
        return searchResultForPddFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mView = inflater.inflate(R.layout.fragment_searchcommodity_pdd, container, false);
        return mView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        MyLog.d("setUserVisibleHint", "CircleFragment  " + isVisibleToUser);
        if (isVisibleToUser && isUserHint && mView != null&&mPushType == 3) {
            initView();
            isUserHint = false;
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPushType = getArguments().getInt(C.Extras.pushType);
        if (mPushType == 3) {
            initView();
        }
        initBundle();
        mTabLayout = (TabLayout) getActivity().findViewById(R.id.tl_pdd_tab);
//        "综合", "券后价", "销量", "奖励"
        tabList.add(new BaseTitleTabBean("综合", false, ""));
        tabList.add(new BaseTitleTabBean("佣金比例", true, C.Setting.sort_commissionShare));
        tabList.add(new BaseTitleTabBean("销量", true, C.Setting.sort_inOrderCount30Days));
        tabList.add(new BaseTitleTabBean("价格", true, C.Setting.sort_price));
        initTab(mTabLayout);

    }
    private Bundle bundle;
    private void initBundle() {
        bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            keyWord = getActivity().getIntent().getStringExtra("keyWord");
            //mSearchType = getIntent().getIntExtra(KEY_SEARCH_TYPE,SEARCH_APP);
            //fromSearch = getIntent().getIntExtra(KEY_SEARCH_FROM,FROM_SELF);
        }
    }



    /**
     * 重新加载数据
     */
    private void reLoadData() {
        getFirstData(keyWord);
    }

    public void initView() {
        ActivityStyleUtil.initSystemBar(getActivity(), R.color.white); //设置标题栏颜色值
        mAdapter = new ShoppingListForPddAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.getSwipeList().setOnRefreshListener(new com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstData(keyWord);
            }
        });

        mRecyclerView.getListView().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!mRecyclerView.getSwipeList().isRefreshing())
                    getMoreData();
            }
        });

        //默认选择第一个
        reLoadData();
    }


    /**
     * 初始化TabLayout
     */
    private void initTab(final TabLayout tabLayout) {
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabTextColors(getResources().getColor(R.color.color_666666), getResources().getColor(R.color.color_666666));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.top_head));
        tabLayout.setSelectedTabIndicatorHeight(DensityUtil.dip2px(getActivity(), 0));

        //填充数据
        for (int i = 0; i < tabList.size(); i++) {
            BaseTitleTabBean bean = tabList.get(i);
            if (i == 0 ){
                bean.order = "";
            }else{
                bean.order = C.Setting.descParms;
            }

            TabLayout.Tab tab = tabLayout.newTab();
            tab.setCustomView(R.layout.tablayout_donw_up_item_tv);
            TextView textView = (TextView) tab.getCustomView().findViewById(R.id.class_tv);

            textView.setText(bean.name);//设置tab上的文字
            tab.setTag(i);
            tabLayout.addTab(tab);
            //初始化上下按钮
            LinearLayout linearLayout = (LinearLayout) tab.getCustomView().findViewById(R.id.class_icon_ly);

            if (bean.isSelect) {
                linearLayout.setVisibility(View.VISIBLE);
                switchTab(tabLayout, i, bean, true);
            } else {
                linearLayout.setVisibility(View.INVISIBLE);
            }

        }

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            /**
             * 新选
             * @param tab
             */
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mSelectedPos = (int) tab.getTag();
                BaseTitleTabBean bean = tabList.get(mSelectedPos);
                switchTab(tabLayout, mSelectedPos, bean, false);
                getFirstData(keyWord);
            }

            /**
             * 当tab从 选择 ->未选择
             * @param tab
             */
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int pos = (int) tab.getTag();
                BaseTitleTabBean bean = tabList.get(pos);
                bean.order =  C.Setting.descParms;// 清空未选择的状态
                switchTab(tabLayout, pos, bean, true);
            }

            /**
             * 复选
             * @param tab
             */
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int getPos = (int) tab.getTag();
                BaseTitleTabBean bean = tabList.get(getPos);
                if (bean.isSelect) {
                    bean.order =  C.Setting.ascParms.equals(bean.order) ?  C.Setting.descParms : C.Setting. ascParms;
                    switchTab(tabLayout, getPos, bean, false);
                    getFirstData(keyWord);
                }
            }
        });
    }

    private void switchTab(TabLayout tabLayout, int i, BaseTitleTabBean switchTop, boolean isInit) {
        ImageView textIcon1 = (ImageView) tabLayout.getTabAt(i).getCustomView().findViewById(R.id.class_icon_up);
     //   ImageView textIcon2 = (ImageView) tabLayout.getTabAt(i).getCustomView().findViewById(R.id.class_icon_down);
        if (isInit) {
            textIcon1.setImageResource(R.drawable.icon_jiage_no);
        //    textIcon2.setImageResource(R.drawable.icon_jiagexia);
        } else {
            if ( C.Setting.ascParms.equals(switchTop.order)) {
                textIcon1.setImageResource(R.drawable.icon_jiage_down);
             //   textIcon2.setImageResource(R.drawable.icon_jiagexiaxuanzhong);
            } else {
                textIcon1.setImageResource(R.drawable.icon_jiage_up);
             //   textIcon2.setImageResource(R.drawable.icon_jiagexia);
            }
        }
    }
    @OnClick({R.id.couponLayout})
    public void Onclick(View v) {
        switch (v.getId()) {
            case R.id.couponLayout:
                clickCouponSwitch();
                break;
            default:
                break;
        }
    }

    /**
     * 修改优惠卷状态
     */
    private void clickCouponSwitch() {
        Log.e("isCouponShowOff",isCouponShowOff+"");
        if (isCouponShowOff) {
            isCouponShowOff = false;
            couponIv.setImageResource(R.drawable.check_no);
            couponTv.setTextColor(ContextCompat.getColor(getActivity(),R.color.tv_tablay_text));
            mRecyclerView.getSwipeList().setRefreshing(true);
            getFirstData(keyWord);
            //重新读取数据
        } else {
            isCouponShowOff = true;
            couponIv.setImageResource(R.drawable.check_yes);
            couponTv.setTextColor(ContextCompat.getColor(getActivity(),R.color.color_333333));
            mRecyclerView.getSwipeList().setRefreshing(true);
            getFirstData(keyWord);
            //重新读取数据
        }
    }
    /*
     * 第一次获取数据
     */
    public void getFirstData(String keyWords) {

        if (TextUtils.isEmpty(keyWords)) {
            mRecyclerView.getSwipeList().setRefreshing(false);
            if (!isUserHint){
                ViewShowUtils.showShortToast(getActivity(), "请输入搜索内容");
            }
            return;
        }
        keyWord = keyWords;
        mPage = 1;
        mRecyclerView.getListView().setNoMore(false);
        mRecyclerView.getSwipeList().setRefreshing(true);
        fristSearch(keyWord);
    }

    private void fristSearch(String keywords) {
        if (tabList!=null&&tabList.size()>0){
            BaseTitleTabBean bean = tabList.get(mSelectedPos);
            mRecyclerView.getSwipeList().setRefreshing(true);
            mRecyclerView.getListView().setNoMore(false);
            getObservable( keywords, bean)
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            mRecyclerView.getSwipeList().setRefreshing(false);
                        }
                    })
                    .subscribe(new DataObserver<List<ShopGoodInfo> >() {
                        @Override
                        protected void onError(String errorMsg, String errCode) {
                            searchNullTips_ly.setVisibility(View.VISIBLE);

                        }

                        @Override
                        protected void onSuccess(List<ShopGoodInfo> data) {
                            mRecyclerView.getSwipeList().setRefreshing(false);
                            dataList_ly.setVisibility(View.VISIBLE);
                            if (data != null && data.size() != 0) {
                                searchNullTips_ly.setVisibility(View.GONE);
                                listArray.clear();
                                listArray.addAll(data);
                                mAdapter.replace(listArray);

                            } else {
                                searchNullTips_ly.setVisibility(View.VISIBLE);
                            }
                            mRecyclerView.notifyDataSetChanged();
                            if (data != null && data.size() == 0){
                                mRecyclerView.getListView().setNoMore(true);
                            }
                        }
                    });
        }


    }

    private Observable<BaseResponse<List<ShopGoodInfo>>> getObservable(String keywords, BaseTitleTabBean bean) {
        RequestSearchForPddBean requestBean = new RequestSearchForPddBean();
        requestBean.setSort(bean.where);
        requestBean.setOrder(bean.order);
        requestBean.setPage(mPage);
        requestBean.setKeyword(keywords);
        return RxHttp.getInstance().getGoodsService().getSearchGoodsListForPdd(requestBean)
                .compose(RxUtils.<BaseResponse<List<ShopGoodInfo>>>switchSchedulers())
                .compose(this.<BaseResponse<List<ShopGoodInfo>>>bindToLifecycle());
    }

    /**
     * 加载更多数据
     */
    public void getMoreData() {
        getMoreSearch(keyWord);
    }

    private void getMoreSearch( String keywords) {
        BaseTitleTabBean bean = tabList.get(mSelectedPos);
        getObservable(keywords, bean)
                .subscribe(new DataObserver<List<ShopGoodInfo>>() {
                    @Override
                    protected void onDataListEmpty() {
                        mRecyclerView.getListView().refreshComplete(REQUEST_COUNT);
                        mRecyclerView.getListView().setNoMore(true);
                    }

                    @Override
                    protected void onSuccess(List<ShopGoodInfo>  data) {
                        mRecyclerView.getListView().refreshComplete(REQUEST_COUNT);
                        mPage++;
                        if (data != null) {
                            if (data.size() > 0) {
                                listArray.addAll(data);
                                mAdapter.replace(listArray);
                               // mRecyclerView.notifyDataSetChanged();

                            } else {
                                mRecyclerView.getListView().setNoMore(true);
                            }
                        }
                    }
                });
    }
    @Subscribe  //订阅事件
    public void onEventMainThread(SearchGoodsForPddEvent event) {
        getFirstData(event.getKeyword());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
