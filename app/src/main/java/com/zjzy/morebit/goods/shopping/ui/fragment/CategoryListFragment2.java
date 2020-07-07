package com.zjzy.morebit.goods.shopping.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.ItemDecoration.SpaceItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.zjzy.morebit.Module.common.Fragment.BaseFragment;
import com.zjzy.morebit.Module.common.View.ReEndlessGradListView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.GoodsAdapter;
import com.zjzy.morebit.adapter.ListTopFenleiAdapter;
import com.zjzy.morebit.adapter.SelectGoodsAdapter2;
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
public class CategoryListFragment2 extends BaseFragment implements ReEndlessGradListView.OnReLoadListener {
    public static String CIDNAME = "cidName";
    public static String CATEGORY_ID = "cid";
    public static String TypeName = "TypeName";
    public static String title = "title";
    public static String keywords = "keywords";
    TextView textView;
    private RecyclerView mRecyclerView;
    private ShoppingListAdapter shoppingAoLeAdapter;
    private SelectGoodsAdapter2 shoppingAoLeAdapter1;
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
    private    SmartRefreshLayout swipeList;
    View mRootView;
    // 三级分类用地  全网搜索
    private int mSearchType = 1;
    private String mMinId = "1";
    private String mPageString = "1";

    public static CategoryListFragment2 newInstance(String typeName) {
        CategoryListFragment2 shoppingAoLeFragment = new CategoryListFragment2();
        Bundle bundle = new Bundle();
        bundle.putString("TypeName", typeName);
        shoppingAoLeFragment.setArguments(bundle);
        return shoppingAoLeFragment;
    }

    public static CategoryListFragment2 newInstance(String typeName, List<Child2> shopGoodInfos) {

        CategoryListFragment2 shoppingAoLeFragment = new CategoryListFragment2();
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
            mRootView = inflater.inflate(R.layout.fragment_aole2, container, false);
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
         swipeList=view.findViewById(R.id.swipeList);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.listview_aole);
        GridLayoutManager manager3 = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(manager3);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(DensityUtil.dip2px(getActivity(), 8)));

          shoppingAoLeAdapter1=new SelectGoodsAdapter2(getActivity());


        searchNullTips_ly = view.findViewById(R.id.searchNullTips_ly);

        mRecyclerView.setAdapter(shoppingAoLeAdapter1);
        swipeList.finishRefresh(false);
        swipeList.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getMoreData();
            }
        });


        getFirstData();
        //右下角置顶按钮
        go_top=  view.findViewById(R.id.go_top);
        go_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollHeight = 0;
                mRecyclerView.scrollToPosition(0);
            }
        });

    }




    /**
     * 第一次获取数据
     */
    public void getFirstData() {

      //  mRecyclerView.getSwipeList().setRefreshing(false);
        getThreeGoods(typeName, C.requestType.initData);
    }


    /**
     * 加载更多数据
     */
    public void getMoreData() {

        getThreeGoods(typeName, C.requestType.loadMore);
    }

    private void getThreeGoods(String keyword, final int loadType) {
        if (loadType == C.requestType.initData) {
            mSearchType = 1;
            mMinId = "1";
            mPageString = "1";
        }

        RequestSearchBean requestBean = new RequestSearchBean();

        requestBean.setOrder("desc");
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
//                        mRecyclerView.getListView().refreshComplete(REQUEST_COUNT);
//                        mRecyclerView.getSwipeList().setRefreshing(false);
                    }
                })
                .subscribe(new DataObserver<ShopGoodBean>() {
                    @Override
                    protected void onDataListEmpty() {
                        if (loadType == C.requestType.initData) {
                           listArray.clear();
                            listArray.addAll(listArray);
                            shoppingAoLeAdapter1.setData(listArray);
                          //  mRecyclerView.notifyDataSetChanged();
//                            if (gridView_ly.getVisibility() == View.GONE)
//                                searchNullTips_ly.setVisibility(View.VISIBLE);
                        } else {
                          //  mRecyclerView.getListView().setNoMore(true);
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
//                              0  if (gridView_ly.getVisibility() == View.GONE)
//                                    searchNullTips_ly.setVisibility(View.GONE);
                                listArray.clear();
                                listArray.addAll(goodsList);
                                pageNum = pageNum + 1;
                                shoppingAoLeAdapter1.setData(goodsList);
                                //设置是否是代理商
                              //  mRecyclerView.notifyDataSetChanged();
                            }

                        } else {
                            if (goodsList != null && goodsList.size() != 0) {
                                listArray.addAll(goodsList);
                                pageNum = pageNum + 1;
                                shoppingAoLeAdapter1.setData(goodsList);
                                swipeList.finishLoadMore(false);
                               // mRecyclerView.notifyDataSetChanged();
                            }
                        }

                    }
                });
    }




    @Subscribe  //订阅事件
    public void onEventMainThread(MessageEvent event) {
        switch (event.getAction()) {
            case EventBusAction.LOGINA_SUCCEED:
               // mRecyclerView.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    @Subscribe  //订阅事件
    public void onEventMainThread(LogoutEvent event) {
     //   mRecyclerView.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
