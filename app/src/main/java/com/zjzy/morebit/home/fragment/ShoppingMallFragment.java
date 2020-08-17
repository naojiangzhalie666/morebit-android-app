package com.zjzy.morebit.home.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.Activity.ShopCarActivity;
import com.zjzy.morebit.Activity.ShowWebActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.SelectGoodsAdapter;
import com.zjzy.morebit.adapter.SubNumberAdapter;
import com.zjzy.morebit.contact.EventBusAction;
import com.zjzy.morebit.fragment.base.BaseMainFragmeng;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.ShopCarNumBean;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.UserZeroInfoBean;
import com.zjzy.morebit.pojo.goods.NewRecommendBean;
import com.zjzy.morebit.pojo.number.NumberGoods;
import com.zjzy.morebit.pojo.number.NumberGoodsList;
import com.zjzy.morebit.pojo.request.RequestRecommendBean;
import com.zjzy.morebit.pojo.requestbodybean.RequestNumberGoodsList;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.StringsUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;

/**
 * 优选商城
 */
public class ShoppingMallFragment extends BaseMainFragmeng {

    private RecyclerView rcy_goods;

    private List<ShopGoodInfo> list = new ArrayList<>();
    private LinearLayout searchNullTips_ly;
    private int mMinNum = 0;
    private int page = 1;
    private int mType = 0;
    private SelectGoodsAdapter selectGoodsAdapter;
    private List<ShopGoodInfo> goods;
    private NestedScrollView netscrollview;

    private boolean isRun = true;
    private TextView daysTv, hoursTv, minutesTv, secondsTv, tv_title, tv_icon;
    private LinearLayout new_goods;
    private ImageView img;
    private SubNumberAdapter mAdapter;
    private SmartRefreshLayout swipeList;
    private RelativeLayout shop_car;
    private ImageView top_rcy;
    private TextView shopnum;


    public static ShoppingMallFragment newInstance() {
        ShoppingMallFragment fragment = new ShoppingMallFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopmall, container, false);
        getData();
        getTime();
        initView(view);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        return view;
    }

    private void getTime() {


    }

    @Override
    public void onResume() {
        super.onResume();
        getShopCarNum();
    }
    private void getData() {
        getNumberGoodsList(this, page)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {


                    }
                })
                .subscribe(new DataObserver<NumberGoodsList>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
//                        super.onError(errorMsg, errCode);
                        showError(errCode, errorMsg);
                    }

                    @Override
                    protected void onDataListEmpty() {
                        //                if (mReUseListView.getSwipeList() != null) {
                        //                    mReUseListView.getSwipeList().setRefreshing(false);
                        //                }
                        swipeList.finishLoadMore();

                    }

                    @Override
                    protected void onSuccess(NumberGoodsList data) {
                        showSuccessful(data);
                    }
                });
    }

    private void showSuccessful(NumberGoodsList data) {
        List<NumberGoods> list = data.getList();
        if (list == null || (list != null && list.size() == 0)) {
            swipeList.finishLoadMore();
            return;
        }
        if (page == 1) {
            mAdapter.addData(list);
        } else {
            mAdapter.setData(list);
            swipeList.finishLoadMore();

        }
    }

    private void showError(String errCode, String errorMsg) {

    }


    private void initView(View view) {


        Bundle arguments = getArguments();
        if (arguments != null) {
        }
        shopnum=view.findViewById(R.id.shopnum);
        top_rcy=view.findViewById(R.id.top_rcy);
        mAdapter = new SubNumberAdapter(getActivity());
        swipeList=view.findViewById(R.id.swipeList);
        rcy_goods = view.findViewById(R.id.rcy_goods);

        final LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        rcy_goods.setLayoutManager(manager);
        rcy_goods.setAdapter(mAdapter);
        swipeList.setEnableRefresh(false);

        swipeList.setEnableLoadMore(true);
        swipeList.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                getData();
            }
        });
        shop_car=view.findViewById(R.id.shop_car);
        shop_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ShopCarActivity.class));
            }
        });
        top_rcy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.scrollToPositionWithOffset(0, 0);
                manager.setStackFromEnd(true);
            }
        });



    }

    private void getShopCarNum() {

        shopCarNum(this)
                .subscribe(new DataObserver<ShopCarNumBean>(false) {
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
                    protected void onSuccess(ShopCarNumBean data) {
                        int goodsNum = data.getGoodsNum();
                        if (goodsNum!=0){
                            shopnum.setVisibility(View.VISIBLE);
                            shopnum.setText(goodsNum+"");
                        }else{
                            shopnum.setVisibility(View.GONE);
                        }

                    }
                });
    }

    private void onActivityFailure() {

    }


    //获取优选商品
    public Observable<BaseResponse<NumberGoodsList>> getNumberGoodsList(RxFragment fragment, int page) {
        RequestNumberGoodsList bean = new RequestNumberGoodsList();
        bean.setPage(page);
        bean.setLimit(10);
        return RxHttp.getInstance().getGoodsService().getNumberGoodsList(bean)
                .compose(RxUtils.<BaseResponse<NumberGoodsList>>switchSchedulers())
                .compose(fragment.<BaseResponse<NumberGoodsList>>bindToLifecycle());
    }
    @Subscribe  //订阅事件
    public void onEventMainThread(MessageEvent event) {
        if (event.getAction().equals(EventBusAction.ACTION_REFRSH)) {
            getData();
            getShopCarNum();

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }



    //购物车数量
    public Observable<BaseResponse<ShopCarNumBean>> shopCarNum(RxFragment fragment) {

        return RxHttp.getInstance().getSysteService().getShopCarNum()
                .compose(RxUtils.<BaseResponse<ShopCarNumBean>>switchSchedulers())
                .compose(fragment.<BaseResponse<ShopCarNumBean>>bindToLifecycle());
    }




}
