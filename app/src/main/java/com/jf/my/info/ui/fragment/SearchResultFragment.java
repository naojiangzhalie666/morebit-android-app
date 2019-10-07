package com.jf.my.info.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.jf.my.LocalData.UserLocalData;
import com.jf.my.Module.common.Fragment.BaseFragment;
import com.jf.my.Module.common.View.ReUseListView;
import com.jf.my.R;
import com.jf.my.adapter.ShoppingListAdapter;
import com.jf.my.network.BaseResponse;
import com.jf.my.network.RxHttp;
import com.jf.my.network.RxUtils;
import com.jf.my.network.observer.DataObserver;
import com.jf.my.pojo.ShopGoodBean;
import com.jf.my.pojo.ShopGoodInfo;
import com.jf.my.pojo.UI.BaseTitleTabBean;
import com.jf.my.pojo.UserInfo;
import com.jf.my.pojo.request.RequestSearchBean;
import com.jf.my.utils.C;
import com.jf.my.utils.DensityUtil;
import com.jf.my.utils.ViewShowUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.functions.Action;

/**
 * Created by fengrs on 2019/1/18.
 * 备注:
 */

public class SearchResultFragment extends BaseFragment {
    @BindView(R.id.dataList_ly)
    LinearLayout dataList_ly;
    @BindView(R.id.searchNullTips_ly)
    LinearLayout searchNullTips_ly;
    @BindView(R.id.listview)
    ReUseListView mRecyclerView;

    private static final int REQUEST_COUNT = 10;

    private TabLayout mTabLayout;
    private boolean isCouponShowOff = false;
    private String mMinId = "1";
    private String mPage = "1";
    private boolean isOneSwitchConpon = true; // 是否第一次切换优惠卷
    private ShoppingListAdapter mAdapter;
    private List<ShopGoodInfo> listArray = new ArrayList<>();
    private List<ShopGoodInfo> withinArray = new ArrayList<>(); //内网
    private List<ShopGoodInfo> outerArray = new ArrayList<>(); // 全网

    private ArrayList<BaseTitleTabBean> tabList = new ArrayList<>();
    private int mSelectedPos;

    private String mKeyWord = "";
    private int mSearchType = 1;


    public static SearchResultFragment newInstance(String keyWord) {
        Bundle args = new Bundle();
        SearchResultFragment fragment = new SearchResultFragment();
        args.putString("keyWord", keyWord);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_result, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initBundle();
        initView(view);
        initData();
    }

    private void initData() {

    }

    private void initView(View view) {

        mAdapter = new ShoppingListAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.getSwipeList().setOnRefreshListener(new com.jf.my.Module.common.widget.SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstData(mKeyWord);
            }
        });

        mRecyclerView.getListView().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!mRecyclerView.getSwipeList().isRefreshing())
                    getMoreData();
            }
        });
        mTabLayout = (TabLayout) view.findViewById(R.id.tl_tab);
//        "综合", "券后价", "销量", "奖励"
        tabList.add(new BaseTitleTabBean("综合", false, C.Setting.itemIndex));
        tabList.add(new BaseTitleTabBean("券后价", true, C.Setting.itemVoucherPrice));
        tabList.add(new BaseTitleTabBean("销量", true, C.Setting.saleMonth));
        if (!C.UserType.member.equals(UserLocalData.getUser(getActivity()).getPartner())) {
            tabList.add(new BaseTitleTabBean("奖励", false, C.Setting.commission));
        }
        initTab(mTabLayout);

        //默认选择第一个
        reLoadData();
    }

    private void initBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mKeyWord = bundle.getString("keyWord");
        }
    }

    /**
     * 重新加载数据
     */
    private void reLoadData() {
        mRecyclerView.getSwipeList().post(new Runnable() {
            @Override
            public void run() {
                getFirstData(mKeyWord);
            }
        });
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
                getFirstData(mKeyWord);
            }

            /**
             * 当tab从 选择 ->未选择
             * @param tab
             */
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int pos = (int) tab.getTag();
                BaseTitleTabBean bean = tabList.get(pos);
                bean.order = C.Setting.descParms;// 清空未选择的状态
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
                    bean.order = C.Setting.ascParms.equals(bean.order) ? C.Setting.descParms : C.Setting.ascParms;
                    switchTab(tabLayout, getPos, bean, false);
                    getFirstData(mKeyWord);
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
            if (C.Setting.ascParms.equals(switchTop.order)) {
                textIcon1.setImageResource(R.drawable.icon_jiageshangxuanzhong);
                textIcon2.setImageResource(R.drawable.icon_jiagexiaxuanzhong);
            } else {
                textIcon1.setImageResource(R.drawable.icon_jiageshang);
                textIcon2.setImageResource(R.drawable.icon_jiagexia);
            }
        }
    }

    /**
     * 修改优惠卷状态
     */
    public void clickCouponSwitch(ImageView im_coupon_switch) {
        if (isCouponShowOff) {
            isCouponShowOff = false;
            im_coupon_switch.setImageResource(R.drawable.switch_bg_close);
            mRecyclerView.getSwipeList().setRefreshing(true);
            getFirstData(mKeyWord);
            //重新读取数据
        } else {
            isCouponShowOff = true;
            im_coupon_switch.setImageResource(R.drawable.switch_bg_open);
            mRecyclerView.getSwipeList().setRefreshing(true);
            getFirstData(mKeyWord);
            //重新读取数据
        }
    }

    /*
     * 第一次获取数据
     */
    public void getFirstData(String keyWord) {
        this.mKeyWord = keyWord;
        //这里调用搜索方法
        searchNullTips_ly.setVisibility(View.GONE);
        dataList_ly.setVisibility(View.VISIBLE);


        if (TextUtils.isEmpty(keyWord)) {
            mRecyclerView.getSwipeList().setRefreshing(false);
            ViewShowUtils.showShortToast(getActivity(), "请输入搜索内容");
            return;
        }
        mPage = "1";
        mMinId = "1";
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

//        return RxHttp.getInstance().getGoodsService().getSearchGoodsList(
//                bean.where,
//                bean.order,
//                mPage,
//                coupon,
//                keywords,
//                min_id + "")
//                .compose(RxUtils.<BaseResponse<ShopGoodBean>>switchSchedulers())
//                .compose(this.<BaseResponse<ShopGoodBean>>bindToLifecycle());
    }

    /**
     * 加载更多数据
     */
    public void getMoreData() {

        String coupon = (isCouponShowOff == true ? 1 : 0) + "";
        getMoreSearch(coupon, mKeyWord, mMinId);
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

}
