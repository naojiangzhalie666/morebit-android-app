package com.zjzy.morebit.home.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.MainActivity;
import com.zjzy.morebit.Module.common.View.ReUseListView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.RetailersAdapter;
import com.zjzy.morebit.contact.EventBusAction;
import com.zjzy.morebit.fragment.base.BaseMainFragmeng;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ConsComGoodsInfo;
import com.zjzy.morebit.pojo.MessageEvent;
import com.zjzy.morebit.pojo.request.RequestGoodsOrderBean;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.helper.ActivityLifeHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;

/**
 * 电商订单
 */
public class OrderRetailersFragment extends BaseMainFragmeng {


    RecyclerView mReUseListView;
    private int page = 1;
    private RetailersAdapter mAdapter;
    private int order_type = 5;//全部  1待返佣  2已到账
    private int teamType = 0;//全部订单
    private LinearLayout dateNullView;
    private TextView btn_invite;
    private SmartRefreshLayout refreshLayout;


    public static OrderRetailersFragment newInstance(int order_type, int orderstatus) {
        OrderRetailersFragment fragment = new OrderRetailersFragment();
        Bundle args = new Bundle();
        args.putInt(C.Extras.ORDERTEAM, order_type);
        args.putInt(C.Extras.ORDERSTATUS, orderstatus);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_retailerst, container, false);
        initView(view);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        return view;
    }

    private void getTime() {
        refreshData();

    }


    @Subscribe  //订阅事件
    public void onEventMainThread(MessageEvent event) {
        page = 1;
        if (event.getAction().equals(EventBusAction.ORDERTYPE_ALL)) {
            teamType = 0;
            getData(0);
        } else if (event.getAction().equals(EventBusAction.ORDERTYPE_JD)) {
            teamType = 2;
            getData(2);
        } else if (event.getAction().equals(EventBusAction.ORDERTYPE_PDD)) {
            teamType = 4;
            getData(4);
        } else if (event.getAction().equals(EventBusAction.ORDERTYPE_TAO)) {
            teamType = 1;
            getData(1);
        } else if (event.getAction().equals(EventBusAction.ORDERTYPE_WPH)) {
            teamType = 6;
            getData(6);
        } else if (event.getAction().equals(EventBusAction.ORDERTYPE_KAOLA)) {
            teamType = 5;
            getData(5);
        }
    }

    private void getData(int teamType) {
        getGoodsOrder(this, order_type, teamType, page)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                }).subscribe(new DataObserver<List<ConsComGoodsInfo>>() {
            @Override
            protected void onDataListEmpty() {
                if (page == 1) {
                    mReUseListView.setVisibility(View.GONE);
                    dateNullView.setVisibility(View.VISIBLE);
                }
                refreshLayout.finishLoadMore(true);
            }

            @Override
            protected void onSuccess(List<ConsComGoodsInfo> data) {

                if (data != null && data.size() != 0) {
                    dateNullView.setVisibility(View.GONE);
                    mReUseListView.setVisibility(View.VISIBLE);
                    if (page == 1) {
                        mAdapter.setData(data);
                    } else {
                        mAdapter.addData(data);
                        refreshLayout.finishLoadMore(true);
                    }

                } else {
                    dateNullView.setVisibility(View.VISIBLE);
                    mReUseListView.setVisibility(View.GONE);
                    refreshLayout.finishRefresh();
                }


            }
        });
    }


    private void showError(String errCode, String errorMsg) {

    }


    private void initView(View view) {


        Bundle arguments = getArguments();
        if (arguments != null) {
            order_type = arguments.getInt(C.Extras.ORDERTEAM);
            teamType = arguments.getInt(C.Extras.ORDERSTATUS);
        }
        refreshLayout = view.findViewById(R.id.refreshLayout);
        mReUseListView = view.findViewById(R.id.mListView);
        dateNullView = view.findViewById(R.id.dateNullView);
        btn_invite = view.findViewById(R.id.btn_invite);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mReUseListView.setLayoutManager(manager);
        mAdapter = new RetailersAdapter(getActivity(), order_type);
        mReUseListView.setAdapter(mAdapter);
        getTime();

        btn_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityLifeHelper.getInstance().finishActivity(MainActivity.class);
                EventBus.getDefault().post(new MessageEvent(EventBusAction.ACTION_HOME));
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                getData(teamType);
                refreshLayout.finishRefresh(true);//刷新完成
            }
        });
        refreshLayout.setOnLoadMoreListener(new com.scwang.smartrefresh.layout.listener.OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                getData(teamType);
            }
        });


        mReUseListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int minLeftItemCount = 5;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int itemCount = layoutManager.getItemCount();
                    int lastPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                    Log.i("minLeftItemCount", "【总数】" + itemCount + "【位置】" + lastPosition);
                    if (lastPosition == layoutManager.getItemCount() - 5) {
                        loadMore();//加载更多
                    } else {
                        if (itemCount > minLeftItemCount) {
                            if (lastPosition == itemCount - minLeftItemCount) {
                                loadMore();//加载更多
                            }
                        } else {
                            loadMore();//加载更多
                        }
                    }
                }
            }


        });


    }

    private void loadMore() {
        page++;
        getData(teamType);
    }

    private void refreshData() {

        getData(teamType);
    }


    /**
     * 获取订单列表
     *
     * @param rxFragment
     * @param orderType
     */
    public Observable<BaseResponse<List<ConsComGoodsInfo>>> getGoodsOrder(RxFragment rxFragment, int orderType, int teamType, int page) {
        RequestGoodsOrderBean requestBean = new RequestGoodsOrderBean();
        requestBean.setOrderStatus(orderType);
        requestBean.setType(teamType);
        requestBean.setPage(page);

        return RxHttp.getInstance().getUsersService()
                .getGoodsOrder(requestBean)
                .compose(RxUtils.<BaseResponse<List<ConsComGoodsInfo>>>switchSchedulers())
                .compose(rxFragment.<BaseResponse<List<ConsComGoodsInfo>>>bindToLifecycle());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
