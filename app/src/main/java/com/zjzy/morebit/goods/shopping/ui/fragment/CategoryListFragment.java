package com.zjzy.morebit.goods.shopping.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjzy.morebit.Module.common.Fragment.BaseFragment;
import com.zjzy.morebit.Module.common.View.ReEndlessGradListView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.GoodsAdapter;
import com.zjzy.morebit.adapter.ListTopFenleiAdapter;
import com.zjzy.morebit.adapter.ShoppingListAdapter;
import com.zjzy.morebit.contact.EventBusAction;
import com.zjzy.morebit.main.ui.fragment.GoodNewsFramgent;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.ShopGoodBean;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UI.BaseTitleTabBean;
import com.zjzy.morebit.pojo.event.LogoutEvent;
import com.zjzy.morebit.pojo.goods.Child2;
import com.zjzy.morebit.pojo.goods.Child3;
import com.zjzy.morebit.pojo.request.RequestSearchBean;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.DensityUtil;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.action.MyAction;
import com.zjzy.morebit.view.FixGridView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Action;


/**
 * 商品列表
 */
public class CategoryListFragment extends BaseFragment implements ReEndlessGradListView.OnReLoadListener {
    public static String CIDNAME = "cidName";
    public static String CATEGORY_ID = "cid";
    public static String TypeName = "TypeName";
    public static String title = "title";
    public static String keywords = "keywords";
    TextView textView;
    private ReEndlessGradListView mRecyclerView;
    private ShoppingListAdapter shoppingAoLeAdapter;
    private GoodsAdapter shoppingAoLeAdapter1;
    private List<ShopGoodInfo> listArray = new ArrayList<>();
    private int pageNum = 1;
    private static final int REQUEST_COUNT = 10;
    private ArrayList<BaseTitleTabBean> tabList = new ArrayList<>();

    private String descParms = "desc"; //倒序
    private String ascParms = "asc"; //升序
    private String typeName = "聚划算";
    private String inGoodType = "monly";

    private ImageView go_top;
    private View headView, searchNullTips_ly;
    private List<Child2> homeList;
    private List<Child3> fldateInfo = new ArrayList<>();
    private TabLayout mHeadTabLayout, mTabLayout;
    private LinearLayout tab_ly, gridView_ly;
    private int scrollHeight = 0;
    private FixGridView fixGridView;
    private int mSelectedPos;//当前被选中的pos
    View mRootView;
    // 三级分类用地  全网搜索
    private int mSearchType = 1;
    private String mMinId = "1";
    private String mPageString = "1";

    public static CategoryListFragment newInstance(String typeName) {
        CategoryListFragment shoppingAoLeFragment = new CategoryListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("TypeName", typeName);
        shoppingAoLeFragment.setArguments(bundle);
        return shoppingAoLeFragment;
    }

    public static CategoryListFragment newInstance(String typeName, List<Child2> shopGoodInfos) {

        CategoryListFragment shoppingAoLeFragment = new CategoryListFragment();
        //MyLog.i("test","newInstance: " +shoppingAoLeFragment);
        Bundle bundle = new Bundle();
        bundle.putString("TypeName", typeName);
        bundle.putSerializable("FlData", (Serializable) shopGoodInfos);
        shoppingAoLeFragment.setArguments(bundle);
        return shoppingAoLeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_aole, container, false);
            initBundle();
            inview(mRootView);
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    private void initBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            typeName = bundle.getString("TypeName", "");
            homeList = (List<Child2>) bundle.getSerializable("FlData");
        }
    }

    @Override
    public void onReload() {
        getFirstData();
    }

    @Override
    public void onLoadMore() {
        getMoreData();
    }


    public void inview(View view) {
        mRecyclerView = (ReEndlessGradListView) view.findViewById(R.id.listview_aole);

        shoppingAoLeAdapter = new ShoppingListAdapter(getActivity());

        shoppingAoLeAdapter1 = new GoodsAdapter(getActivity());
        tab_ly = (LinearLayout) view.findViewById(R.id.tab_ly);
        searchNullTips_ly = view.findViewById(R.id.searchNullTips_ly);

        headView = LayoutInflater.from(getActivity()).inflate(R.layout.item_good_list_topfenlei_headview, null);
        mHeadTabLayout = (TabLayout) headView.findViewById(R.id.head_tl_tab);
        gridView_ly = (LinearLayout) headView.findViewById(R.id.gridView_ly);
        mRecyclerView.setAdapterAndHeadView(headView, shoppingAoLeAdapter, shoppingAoLeAdapter1);
        fixGridView = (FixGridView) headView.findViewById(R.id.gridView);
        gridView_ly.setVisibility(View.VISIBLE);
        fldateInfo.clear();
        if (homeList != null) {
            for (int i = 0; i < homeList.size(); i++) {
                List<Child3> getDatas = homeList.get(i).getChild3();

                if (getDatas != null && getDatas.size() > 0) {
                    //设置最多三行数据
                    for (int j = 0; j < getDatas.size(); j++) {
                        if (fldateInfo.size() < 8) {
                            fldateInfo.add(getDatas.get(j));
                        }
                    }
                }
            }
        }
        ListTopFenleiAdapter adapter = new ListTopFenleiAdapter(getActivity(), fldateInfo, new MyAction.OnResult<Integer>() {
            @Override
            public void invoke(Integer arg) {
                Child3 child3 = fldateInfo.get(arg);
                if (child3 == null) return;
                ImageInfo imageInfo = new ImageInfo();
                imageInfo.categoryId = child3.getId() + "";
                imageInfo.setTitle(fldateInfo.get(arg).getName());
                imageInfo.categoryId =  fldateInfo.get(arg).getName()+ " " + typeName ;
                MyLog.i("test", "categoryId: " + imageInfo.categoryId);
                GoodNewsFramgent.start(getActivity(), imageInfo, C.GoodsListType.THREEGOODS);
            }

            @Override
            public void onError() {

            }
        });
        fixGridView.setAdapter(adapter);


        mRecyclerView.getSwipeList().setOnRefreshListener(new com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scrollHeight = 0;
                getFirstData();
            }
        });
        mRecyclerView.setOnReLoadListener(this);

        mTabLayout = (TabLayout) view.findViewById(R.id.tl_tab);
        tabList.add(new BaseTitleTabBean("综合", false, C.Setting.itemIndex));
        tabList.add(new BaseTitleTabBean("券后价", true, C.Setting.itemVoucherPrice));
//        tabList.add(new BaseTitleTabBean("优惠券", true, "coupon_price"));
        tabList.add(new BaseTitleTabBean("销量", true, C.Setting.saleMonth));
//        if (userInfo == null || userInfo.getPartner() == 0) {
//        } else {
////            tabList.add(new BaseTitleTabBean("奖励", true, "commission"));
//            tabList.add(new BaseTitleTabBean("销量", true, "m_sale"));
//        }

        tabList.add(new BaseTitleTabBean(true));
        initTab(mTabLayout);
        initTab(mHeadTabLayout);


        //默认选择第一个
//        if (mSerializable == null) {
        getFirstData();
//        } else {
//            mHeadTabLayout.setVisibility(View.GONE);
//            mTabLayout.setVisibility(View.GONE);
//            pageNum = pageNum + 1;
//            listArray.addAll((List<ShopGoodInfo>) mSerializable.getData());
//            shoppingAoLeAdapter.setData(listArray);
//            mRecyclerView.notifyDataSetChanged();
//        }
        tab_ly.setVisibility(View.GONE);
        //右下角置顶按钮
        go_top=  view.findViewById(R.id.go_top);
        go_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollHeight = 0;
                mRecyclerView.getListView().scrollToPosition(0);
            }
        });
        mRecyclerView.getListView().addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                try {
                    scrollHeight = scrollHeight + dy;
                    if (scrollHeight >= gridView_ly.getHeight()) {
                        tab_ly.setVisibility(View.VISIBLE);
                        go_top.setVisibility(View.VISIBLE);
                    } else {
                        tab_ly.setVisibility(View.GONE);
                        go_top.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
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

            TabLayout.Tab tab = tabLayout.newTab();
            if (bean.isSwitchItme) {
                tab.setCustomView(R.layout.tablayout_switch_itme);
                tab.setTag(i);
                tabLayout.addTab(tab);
                continue;
            }

            if (!bean.isSelect) {
                bean.order = descParms;
            } else {
                bean.order = ascParms;// 默认倒序  但是一次 升序  两次就 倒序
            }
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
        final boolean[] isTabReselected = {false};
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            /**
             * 新选
             * @param tab
             */
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                isTabReselected[0] = true;
                mSelectedPos = (int) tab.getTag();
                BaseTitleTabBean bean = tabList.get(mSelectedPos);
                if (bean.isSwitchItme) {
                    switchItmeStye(mTabLayout, mSelectedPos);
                    switchItmeStye(mHeadTabLayout, mSelectedPos);
                    mRecyclerView.switchAdapter(getActivity(), listArray.size());
                    return;
                }

                if (!bean.isSelect) {
                    bean.order = descParms;
                }
                if (tabLayout.hashCode() == mHeadTabLayout.hashCode()) {
                    mTabLayout.getTabAt(mSelectedPos).select();
                } else {
                    mHeadTabLayout.getTabAt(mSelectedPos).select();
                }
                switchTab(mTabLayout, mSelectedPos, bean, false);
                switchTab(mHeadTabLayout, mSelectedPos, bean, false);
                getFirstData();
            }

            /**
             * 当tab从 选择 ->未选择
             * @param tab
             */
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (isTabReselected[0]) {
                    isTabReselected[0] = false;
                    int pos = (int) tab.getTag();
                    BaseTitleTabBean bean = tabList.get(pos);
                    if (bean.isSwitchItme) return;
                    bean.order = ascParms;// 清空未选择的状态
                    switchTab(mHeadTabLayout, pos, bean, true);
                    switchTab(mTabLayout, pos, bean, true);

                }
            }

            /**
             * 复选
             * @param tab
             */
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int getPos = (int) tab.getTag();
                BaseTitleTabBean bean = tabList.get(getPos);
                if (bean.isSwitchItme) {
                    switchItmeStye(mTabLayout, mSelectedPos);
                    switchItmeStye(mHeadTabLayout, mSelectedPos);
                    mRecyclerView.switchAdapter(getActivity(), listArray.size());
                    return;
                }
                if (bean.isSelect) {
                    bean.order = ascParms.equals(bean.order) ? descParms : ascParms;
                    switchTab(mHeadTabLayout, getPos, bean, false);
                    switchTab(mTabLayout, getPos, bean, false);
                    getFirstData();
                }
            }


        });
    }

    private void switchItmeStye(TabLayout tab, int getPos) {
        scrollHeight = 0;
        MyLog.d("ShoppingListFragment2", "getPos " + getPos + "  rl_list.isGrad()   " + mRecyclerView.isGrad());

        ImageView iv_switch = (ImageView) tab.getTabAt(getPos).getCustomView().findViewById(R.id.iv_switch);
        if (mRecyclerView.isGrad() == true) {
            iv_switch.setSelected(false);
        } else {
            iv_switch.setSelected(true);
        }
    }

    private void switchTab(TabLayout tabLayout, int i, BaseTitleTabBean switchTop, boolean isInit) {
        ImageView textIcon1 = (ImageView) tabLayout.getTabAt(i).getCustomView().findViewById(R.id.class_icon_up);
        ImageView textIcon2 = (ImageView) tabLayout.getTabAt(i).getCustomView().findViewById(R.id.class_icon_down);
        if (isInit) {
            textIcon1.setBackgroundResource(R.drawable.icon_jiageshangxuanzhong);
            textIcon2.setBackgroundResource(R.drawable.icon_jiagexia);
        } else {
            if (ascParms.equals(switchTop.order)) {
                textIcon1.setBackgroundResource(R.drawable.icon_jiageshangxuanzhong);
                textIcon2.setBackgroundResource(R.drawable.icon_jiagexiaxuanzhong);
            } else {
                textIcon1.setBackgroundResource(R.drawable.icon_jiageshang);
                textIcon2.setBackgroundResource(R.drawable.icon_jiagexia);
            }
        }
    }

    /**
     * 第一次获取数据
     */
    public void getFirstData() {

        mRecyclerView.getSwipeList().setRefreshing(true);
        BaseTitleTabBean bean = tabList.get(mSelectedPos);
        getThreeGoods(typeName, bean, C.requestType.initData);
    }


    /**
     * 加载更多数据
     */
    public void getMoreData() {
        BaseTitleTabBean bean = tabList.get(mSelectedPos);
        getThreeGoods(typeName, bean, C.requestType.loadMore);
    }

    private void getThreeGoods(String keyword, BaseTitleTabBean bean, final int loadType) {
        if (loadType == C.requestType.initData) {
            mSearchType = 1;
            mMinId = "1";
            mPageString = "1";
        }

        RequestSearchBean requestBean = new RequestSearchBean();
        requestBean.setSort(bean.where);
        requestBean.setOrder(bean.order);
        requestBean.setPage(mPageString);
        requestBean.setCoupon("1");
        requestBean.setKeywords(keyword);
        requestBean.setMinId(mMinId);
        requestBean.setSearchType(mSearchType);
        RxHttp.getInstance().getGoodsService().getSearchGoodsList(requestBean)
                .compose(RxUtils.<BaseResponse<ShopGoodBean>>switchSchedulers())
                .compose(this.<BaseResponse<ShopGoodBean>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mRecyclerView.getListView().refreshComplete(REQUEST_COUNT);
                        mRecyclerView.getSwipeList().setRefreshing(false);
                    }
                })
                .subscribe(new DataObserver<ShopGoodBean>() {
                    @Override
                    protected void onDataListEmpty() {
                        if (loadType == C.requestType.initData) {
                            listArray.clear();
                            listArray.addAll(listArray);
                            shoppingAoLeAdapter.setData(listArray);
                            shoppingAoLeAdapter1.replace(listArray);
                            mRecyclerView.notifyDataSetChanged();
                            if (gridView_ly.getVisibility() == View.GONE)
                                searchNullTips_ly.setVisibility(View.VISIBLE);
                        } else {
                            mRecyclerView.getListView().setNoMore(true);
                        }
                    }

                    @Override
                    protected void onSuccess(ShopGoodBean data) {
                        List<ShopGoodInfo> goodsList = data.getData();
                        mMinId = data.getMinId();
                        mPageString = data.getPage();
                        mSearchType = data.getSearchType();
                        if (loadType == C.requestType.initData) {
                            if (goodsList != null && goodsList.size() != 0) {
                                if (gridView_ly.getVisibility() == View.GONE)
                                    searchNullTips_ly.setVisibility(View.GONE);
                                listArray.clear();
                                listArray.addAll(goodsList);
                                pageNum = pageNum + 1;
                                shoppingAoLeAdapter.setData(listArray);
                                shoppingAoLeAdapter1.replace(goodsList);
                                //设置是否是代理商
                                mRecyclerView.notifyDataSetChanged();
                            }

                        } else {
                            if (goodsList != null && goodsList.size() != 0) {
                                listArray.addAll(goodsList);
                                pageNum = pageNum + 1;
                                shoppingAoLeAdapter.setData(listArray);
                                shoppingAoLeAdapter1.add(goodsList);
                                mRecyclerView.notifyDataSetChanged();
                            }
                        }

                    }
                });
    }


    @Subscribe  //订阅事件
    public void onEventMainThread(MessageEvent event) {
        switch (event.getAction()) {
            case EventBusAction.LOGINA_SUCCEED:
                mRecyclerView.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    @Subscribe  //订阅事件
    public void onEventMainThread(LogoutEvent event) {
        mRecyclerView.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
