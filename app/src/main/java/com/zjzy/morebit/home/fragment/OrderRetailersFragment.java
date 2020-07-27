package com.zjzy.morebit.home.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.trello.rxlifecycle2.components.support.RxFragment;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;

/**
 * 电商订单
 */
public class OrderRetailersFragment extends BaseMainFragmeng {


    ReUseListView mReUseListView;
    private int page = 1;
    private RetailersAdapter mAdapter;
    private int order_type=5;//全部  1待返佣  2已到账
    private int teamType=0;//全部订单
    private LinearLayout dateNullView;





    public static OrderRetailersFragment newInstance(int order_type,int orderstatus) {
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
        if (event.getAction().equals(EventBusAction.ORDERTYPE_ALL)) {
            getData(0);
        }else if (event.getAction().equals(EventBusAction.ORDERTYPE_JD)){
            getData(2);
        }else if (event.getAction().equals(EventBusAction.ORDERTYPE_PDD)){
            getData(4);
        }else if (event.getAction().equals(EventBusAction.ORDERTYPE_TAO)){
            getData(1);
        }else if (event.getAction().equals(EventBusAction.ORDERTYPE_WPH)){
            getData(6);
        }else if (event.getAction().equals(EventBusAction.ORDERTYPE_KAOLA)){
            getData(5);
        }
    }
    private void getData(int teamType) {
        getGoodsOrder(this, order_type,  teamType,page)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                }).subscribe(new DataObserver<List<ConsComGoodsInfo>>() {
            @Override
            protected void onDataListEmpty() {
                if (page==1){
                    mReUseListView.setVisibility(View.GONE);
                    dateNullView.setVisibility(View.VISIBLE);
                }
                mReUseListView.getListView().setNoMore(true);
            }

            @Override
            protected void onSuccess(List<ConsComGoodsInfo> data) {

                if (data!=null&&data.size()!=0){
                    dateNullView.setVisibility(View.GONE);
                    mReUseListView.setVisibility(View.VISIBLE);
                    if (page==1){
                        mAdapter.setData(data);
                    }else{
                        mAdapter.addData(data);
                        mReUseListView.getListView().setNoMore(true);
                    }

                }else{
                    dateNullView.setVisibility(View.VISIBLE);
                    mReUseListView.setVisibility(View.GONE);
                    mReUseListView.getListView().setNoMore(true);
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
            teamType=arguments.getInt(C.Extras.ORDERSTATUS);
        }
        mReUseListView=view.findViewById(R.id.mListView);
        dateNullView=view.findViewById(R.id.dateNullView);
        mAdapter = new RetailersAdapter(getActivity(),order_type);
        mReUseListView.getSwipeList().setOnRefreshListener(new com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                refreshData();
            }
        });
        mReUseListView.getListView().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!mReUseListView.getSwipeList().isRefreshing()) {
                    page++;
                   getData(teamType);
                }

            }
        });
        mReUseListView.setAdapter(mAdapter);
        getTime();

    }

    private void refreshData() {
        mReUseListView.getSwipeList().post(new Runnable() {

            @Override
            public void run() {
                mReUseListView.getSwipeList().setRefreshing(true);
            }
        });
        page = 1;
        mReUseListView.getListView().setNoMore(false);
        mReUseListView.getListView().setMarkermallNoMore(true);
        mReUseListView.getListView().setFootViewVisibility(View.VISIBLE);
        mReUseListView.getListView().setFooterViewHint("","仅支持查看近3个月订单","");
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
