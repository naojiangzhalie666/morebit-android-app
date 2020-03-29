package com.zjzy.morebit.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.View.ReUseListView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.ShoppingListAdapter;
import com.zjzy.morebit.fragment.base.BaseMainFragmeng;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ShopGoodBean;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UI.BaseTitleTabBean;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.event.SearchGoodsForTaobaoEvent;
import com.zjzy.morebit.pojo.request.RequestSearchBean;
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
 * 搜索结果展示
 */

public class SearchResultForTaobaoFragment extends BaseMainFragmeng {
    public static final String KEY_SEARCH_TYPE = "searchType";
    public static final String KEY_SEARCH_FROM = "searchFrom";
    public  static final int SEARCH_APP = 1; //搜索APP
    public static final int SEARCH_NETWORK = 0; //搜索全网
    public  static final int FROM_SELF = 2; //来自搜索界面
    public static final int FROM_EXTERNAL = 3; //来自外部全局的搜索
    private ShoppingListAdapter mAdapter;
    private List<ShopGoodInfo> listArray = new ArrayList<>();
    private List<ShopGoodInfo> withinArray = new ArrayList<>(); //内网
    private List<ShopGoodInfo> outerArray = new ArrayList<>(); // 全网
    private String keyWord = "";
    private Bundle bundle;

    private static final int REQUEST_COUNT = 10;

    private TabLayout mTabLayout;
    private boolean isCouponShowOff = false;
    private String mMinId = "1";
    private String mPage = "1";
    private boolean isOneSwitchConpon = true; // 是否第一次切换优惠卷
    private ArrayList<BaseTitleTabBean> tabList = new ArrayList<>();
    private int mSelectedPos;
    @BindView(R.id.searchNullTips_ly)
    LinearLayout searchNullTips_ly;
    @BindView(R.id.listview)
    ReUseListView mRecyclerView;
//    @BindView(R.id.search_et)
//    EditText etSearch;
    @BindView(R.id.dataList_ly)
    LinearLayout dataList_ly;
//    @BindView(R.id.list_serach_key)
//    ListView mListView;
//    @BindView(R.id.iv_clear)
//    ImageView iv_clear;
    @BindView(R.id.couponIv)
    ImageView couponIv;
    @BindView(R.id.couponTv)
    TextView couponTv;
//    private ArrayList<String> mTaobaoSearchData = new ArrayList<>();
//    private SearchHotAdapter mTaobaoSearchAdapter;
    boolean isClserSearch = false;
    private boolean isFrist = true;
    private int mSearchType =1;
    private int fromSearch = FROM_SELF;
    private boolean isLoadData = false;
    private View mView;
    boolean isUserHint =true;
    private int mPushType;


    public static SearchResultForTaobaoFragment newInstance(int type) {
        SearchResultForTaobaoFragment searchResultForTaobaoFragment = new SearchResultForTaobaoFragment();
        Bundle args = new Bundle();
        args.putInt(C.Extras.pushType, type);
        searchResultForTaobaoFragment.setArguments(args);
        return searchResultForTaobaoFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_searchcommodity, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPushType = getArguments().getInt(C.Extras.pushType);
        if (mPushType == 1) {
            initBundle();
            initView();
        }



    }

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
        mAdapter = new ShoppingListAdapter(getActivity());
        mAdapter.setShowHotTag(true);
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

        mTabLayout = (TabLayout) getActivity().findViewById(R.id.tl_tab);
//        "综合", "券后价", "销量", "奖励"
        tabList.add(new BaseTitleTabBean("综合", false, C.Setting.itemIndex));
        tabList.add(new BaseTitleTabBean("券后价", true, C.Setting.itemVoucherPrice));
        tabList.add(new BaseTitleTabBean("销量", true, C.Setting.saleMonth));
        tabList.add(new BaseTitleTabBean("奖励", false, C.Setting.commission));

        initTab(mTabLayout);

        //默认选择第一个
        reLoadData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        MyLog.d("setUserVisibleHint", "taobaoFragment  " + isVisibleToUser);
        if (isVisibleToUser && isUserHint && mView != null&&mPushType == 2) {
            initView();
            isUserHint = false;
        }

    }

//    @OnTextChanged(value = R.id.search_et, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
//    void afterTextChanged(Editable s) {
//        //判断是否显示删除按钮
//        if (TextUtils.isEmpty(etSearch.getText().toString().trim())) {
//            if (iv_clear.getVisibility() != View.GONE) {
//                iv_clear.setVisibility(View.GONE);
//            }
//        } else {
//            if (iv_clear.getVisibility() != View.VISIBLE) {
//                iv_clear.setVisibility(View.VISIBLE);
//            }
//        }
//        MyLog.i("test", "isFrist: " + isFrist);
//        MyLog.i("test", "editable: " + s.toString());
//        if (!s.toString().equals(keyWord)) {
//            try {
//                if (!TextUtils.isEmpty(s.toString())) {
//                    getGoodsData(s.toString());
//                    isClserSearch = false;
//                } else {
//                    isClserSearch = true;
//                    clserSearchAdapter();
//                }
//            } catch (Exception e) {
//            }
//        }
//    }

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
            bean.order = C.Setting.descParms;
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
        ImageView textIcon2 = (ImageView) tabLayout.getTabAt(i).getCustomView().findViewById(R.id.class_icon_down);
        if (isInit) {
            textIcon1.setImageResource(R.drawable.icon_jiageshangxuanzhong);
            textIcon2.setImageResource(R.drawable.icon_jiagexia);
        } else {
            if ( C.Setting.ascParms.equals(switchTop.order)) {
                textIcon1.setImageResource(R.drawable.icon_jiageshangxuanzhong);
                textIcon2.setImageResource(R.drawable.icon_jiagexiaxuanzhong);
            } else {
                textIcon1.setImageResource(R.drawable.icon_jiageshang);
                textIcon2.setImageResource(R.drawable.icon_jiagexia);
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
            couponIv.setImageResource(R.drawable.icon_search_coupon_unselect);
            couponTv.setTextColor(ContextCompat.getColor(getActivity(),R.color.tv_tablay_text));
            mRecyclerView.getSwipeList().setRefreshing(true);
            getFirstData(keyWord);
            //重新读取数据
        } else {
            isCouponShowOff = true;
            couponIv.setImageResource(R.drawable.icon_search_coupon_select);
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
        keyWord = keyWords;
        if (TextUtils.isEmpty(keyWord)) {
            mRecyclerView.getSwipeList().setRefreshing(false);
            ViewShowUtils.showShortToast(getActivity(), "请输入搜索内容");
            return;
        }

        mPage = "1";
        mMinId = "1";
        mSearchType =1;
        String coupon = (isCouponShowOff == false ? 0 : 1) + "";
        mRecyclerView.getListView().setNoMore(false);
        mRecyclerView.getSwipeList().setRefreshing(true);
        fristSearch(coupon, keyWord, mMinId);
    }

    private void fristSearch(String coupon, String keywords, String minId) {
        BaseTitleTabBean bean = tabList.get(mSelectedPos);
        mRecyclerView.getSwipeList().setRefreshing(true);
        getObservable(coupon, keywords, minId, bean)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mRecyclerView.getSwipeList().setRefreshing(false);

                    }
                })
                .subscribe(new DataObserver<ShopGoodBean>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        listArray.clear();
                        mAdapter.setData(listArray);
                        mRecyclerView.notifyDataSetChanged();
                        if (isOneSwitchConpon) {
                            isOneSwitchConpon = false;
                        } else {
                            searchNullTips_ly.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    protected void onSuccess(ShopGoodBean data) {
                        mRecyclerView.getSwipeList().setRefreshing(false);
                        dataList_ly.setVisibility(View.VISIBLE);

                        List<ShopGoodInfo> goodsList = data.getData();
                        if (goodsList != null && goodsList.size() != 0) {
                            mMinId = data.getMinId();
                            mPage = data.getPage();
                            mSearchType = data.getSearchType();
                            searchNullTips_ly.setVisibility(View.GONE);
                            listArray.clear();
                            List<ShopGoodInfo> shopGoodInfos = partList(goodsList);
                            mAdapter.setData(shopGoodInfos);
                            //设置是否是代理商
                            UserInfo userInfo = UserLocalData.getUser(getActivity());
                            mRecyclerView.notifyDataSetChanged();
                        } else {
                            listArray.clear();
                            mAdapter.setData(listArray);
                            mRecyclerView.notifyDataSetChanged();
                            if (isOneSwitchConpon) {
                                isOneSwitchConpon = false;
                            } else {
                                searchNullTips_ly.setVisibility(View.VISIBLE);
                            }
                        }
                        if (data.getIsPageEnd() == 1){
                            mRecyclerView.getListView().setNoMore(true);
                        }
                    }
                });
    }

    private Observable<BaseResponse<ShopGoodBean>> getObservable(String coupon, String keywords, String min_id, BaseTitleTabBean bean) {
        RequestSearchBean requestBean = new RequestSearchBean();
        requestBean.setSort(bean.where);
        requestBean.setOrder(bean.order);
        requestBean.setPage(mPage);
        requestBean.setCoupon(coupon);
        requestBean.setKeywords(keywords);
        requestBean.setMinId(min_id + "");
        requestBean.setSearchType(mSearchType);
        return RxHttp.getInstance().getGoodsService().getSearchGoodsList(requestBean)
                .compose(RxUtils.<BaseResponse<ShopGoodBean>>switchSchedulers())
                .compose(this.<BaseResponse<ShopGoodBean>>bindToLifecycle());

    }

    /**
     * 加载更多数据
     */
    public void getMoreData() {
//        keyWord = etSearch.getText().toString().trim();
        String coupon = (isCouponShowOff == true ? 1 : 0) + "";
        getMoreSearch(coupon, keyWord, mMinId);
    }

    private void getMoreSearch(String coupon, String keywords, String minId) {
        BaseTitleTabBean bean = tabList.get(mSelectedPos);
        getObservable(coupon, keywords, minId, bean)
                .subscribe(new DataObserver<ShopGoodBean>() {
                    @Override
                    protected void onDataListEmpty() {
                        mRecyclerView.getListView().refreshComplete(REQUEST_COUNT);
                        mRecyclerView.getListView().setNoMore(true);
                    }

                    @Override
                    protected void onSuccess(ShopGoodBean data) {
                        mRecyclerView.getListView().refreshComplete(REQUEST_COUNT);
                        List<ShopGoodInfo> goodsList = data.getData();
                        if (goodsList != null) {
                            if (goodsList.size() > 0) {
                                mMinId = data.getMinId();
                                mPage = data.getPage();
                                mSearchType = data.getSearchType();
                                List<ShopGoodInfo> shopGoodInfos = partList(goodsList);
                                mAdapter.setData(shopGoodInfos);
                                //设置是否是代理商
                                mRecyclerView.notifyDataSetChanged();
                            } else {
                                mRecyclerView.getListView().setNoMore(true);
                            }
                        }
                        if (data.getIsPageEnd() == 1){
                            mRecyclerView.getListView().setNoMore(true);
                        }
                    }
                });
    }

    /**
     * 分离全网 和内网
     *
     * @param goodsList
     */
    private List<ShopGoodInfo> partList(List<ShopGoodInfo> goodsList) {
        listArray.addAll(goodsList);
        outerArray.clear();
        withinArray.clear();
        for (int i = 0; i < listArray.size(); i++) {
            ShopGoodInfo shopGoodInfo = listArray.get(i);
            if (shopGoodInfo.isSearchType == 1) { // 排除分割
                continue;
            }
            if ("0".equals(shopGoodInfo.getItemFrom())) {//全给
                outerArray.add(shopGoodInfo);
            } else {
                withinArray.add(shopGoodInfo);
            }
        }
        listArray.clear();
        listArray.addAll(withinArray);
        if (outerArray != null && outerArray.size() != 0) {
            listArray.add(new ShopGoodInfo(1));
            listArray.addAll(outerArray);
        }
        return listArray;
    }








    @Subscribe  //订阅事件
    public void onEventMainThread(SearchGoodsForTaobaoEvent event) {
        getFirstData(event.getKeyword());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
