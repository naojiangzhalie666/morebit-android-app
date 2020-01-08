package com.zjzy.morebit.info.ui.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.zjzy.morebit.Activity.GoodsDetailActivity;
import com.zjzy.morebit.Activity.ShowWebActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.View.ReUseListView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.ConsComGoodsDetailAdapter;
import com.zjzy.morebit.info.contract.OrderListContract;
import com.zjzy.morebit.info.presenter.OrderListPresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.order.ui.NumberOrderDetailActivity;
import com.zjzy.morebit.pojo.ConsComGoodsInfo;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.event.OrderLoadDataEvent;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.OpenFragmentUtils;
import com.zjzy.morebit.utils.ViewShowUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by YangBoTian on 2018/9/12.
 */

public class OrderListFragment extends MvpFragment<OrderListPresenter> implements OrderListContract.View {
    private static final int REQUEST_COUNT = 10;

    @BindView(R.id.mListView)
    ReUseListView mReUseListView;
    @BindView(R.id.dateNullView)
    LinearLayout mDateNullView;
    @BindView(R.id.dateNullView_tips_tv)
    TextView mDateNullViewRecommend;
    private int mOrderType =1;
    private int page = 1;
    private ConsComGoodsDetailAdapter consComGoodsDetailAdapter;
    List<ConsComGoodsInfo> mListArray = new ArrayList<>();
    private int mTeamType;

    public static OrderListFragment newInstance(int order_type, int team_type) {
        Bundle args = new Bundle();
        args.putInt("order_type", order_type);
        args.putInt("team_type", team_type);
        OrderListFragment fragment = new OrderListFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initData() {
        initBundle();
        getData();
    }

    private void initBundle() {
        EventBus.getDefault().register(this);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mTeamType = bundle.getInt("team_type");
            mOrderType = bundle.getInt("order_type", 4);
        }
    }

    @Override
    protected void initView(View view) {
        consComGoodsDetailAdapter = new ConsComGoodsDetailAdapter(getActivity(), mListArray);
        mReUseListView.getSwipeList().setOnRefreshListener(new com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        mReUseListView.getListView().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!mReUseListView.getSwipeList().isRefreshing()) {
                    getData();
                }
            }
        });
        mReUseListView.setAdapter(consComGoodsDetailAdapter);

        mDateNullViewRecommend.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        mDateNullViewRecommend.getPaint().setAntiAlias(true);//抗锯齿
        consComGoodsDetailAdapter.setOnSelfOrderClickListener(new ConsComGoodsDetailAdapter.OnSelfOrderClickListener() {
            @Override
            public void onReceiveGoods(String orderId, int position) {
                //确认收货
                mPresenter.ConfirmReceiveGoods(OrderListFragment.this,orderId);
            }

            @Override
            public void onShip(String orderId, int position) {
                //调用查看物流接口
                String originUrl = mListArray.get(position).getShipUrl();
                if (!TextUtils.isEmpty(originUrl)){
                    originUrl = originUrl+"?orderId="+orderId;
                    ShowWebActivity.start(getActivity(),originUrl,"物流信息");
                }else{
                    MyLog.d("test","物流url为空");
                }
            }

            @Override
            public void onPay(String orderId, int position) {
                //去支付或者取消
                NumberOrderDetailActivity.startOrderDetailActivity(getActivity(),orderId);
            }
        });
        consComGoodsDetailAdapter.setOnAdapterClickListener(new ConsComGoodsDetailAdapter.OnAdapterClickListener() {
            @Override
            public void onItem(int position) {
                if (mTeamType == 1){
                    mPresenter.onCheckGoods(OrderListFragment.this, mListArray.get(position).getItemId());
                }else{
//                    ViewShowUtils.showShortToast(getActivity(),getString(R.string.order_no_look));
                    NumberOrderDetailActivity.startOrderDetailActivity(getActivity(),
                            mListArray.get(position).getOrderSn());
                }
            }


        });
    }

    public void refreshData() {
        page = 1;
        mReUseListView.getListView().setNoMore(false);
        mReUseListView.getSwipeList().setRefreshing(true);
        getData();
    }

    private void getData() {
        mPresenter.getGoodsOrder(this, mTeamType, mOrderType, page);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.framgent_order_list;
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }

    @OnClick({R.id.dateNullView_clickbox})
    public void Onclick(View v) {
        switch (v.getId()) {
            case R.id.dateNullView_clickbox://跳转到分享界面
                OpenFragmentUtils.goToSimpleFragment(getActivity(), ShareFriendsFragment.class.getName(), new Bundle());
                break;
        }
    }

    @Override
    public void onSuccessful(List<ConsComGoodsInfo> datas) {
        MyLog.i("test", "sn: " + datas.get(0).getOrderSn());
//        consComGoodsDetailAdapter.setTeamType(mTeamType);
        if (datas.size() == 0) {
            mDateNullView.setVisibility(View.VISIBLE);
            mReUseListView.setVisibility(View.GONE);
            return;
        }
        if (page == 1) {
            mListArray.clear();
            mListArray.addAll(datas);
            mDateNullView.setVisibility(View.GONE);
            mReUseListView.setVisibility(View.VISIBLE);
        } else {
            if (datas.size() == 0) {
                mReUseListView.getListView().setNoMore(true);
            } else {
                mListArray.addAll(datas);
            }

        }
        page++;
        consComGoodsDetailAdapter.setData(mListArray);
        mReUseListView.notifyDataSetChanged();
    }

    @Override
    public void onSearchSuccessful(ConsComGoodsInfo data) {

    }

    @Override
    public void onSearchSuccessful(List<ConsComGoodsInfo> data) {

    }

    @Override
    public void onFailure() {
        MyLog.i("test", "onFailure: " + this);
        if (page == 1) {
            mDateNullView.setVisibility(View.VISIBLE);
            mReUseListView.setVisibility(View.GONE);
        } else {
            mDateNullView.setVisibility(View.GONE);
            mReUseListView.setVisibility(View.VISIBLE);
        }
        mReUseListView.getListView().setNoMore(true);
    }

    @Override
    public void onFinally() {
        mReUseListView.getSwipeList().setRefreshing(false);
        mReUseListView.getListView().refreshComplete(REQUEST_COUNT);

    }

    @Override
    public void onCheckGoodsSuccessFul(ShopGoodInfo data) {
        GoodsDetailActivity.start(getActivity(), data);
    }

    @Override
    public void onReceiveGoodsSuccessFul(Boolean data) {
        ViewShowUtils.showShortToast(getActivity(),"确认收货成功！");
    }

    // 更新信息
    @Subscribe
    public void onEventMainThread(OrderLoadDataEvent event) {
        mTeamType = event.getType();
        refreshData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }
}
