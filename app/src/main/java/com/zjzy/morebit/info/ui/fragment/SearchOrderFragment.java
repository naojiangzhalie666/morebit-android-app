package com.zjzy.morebit.info.ui.fragment;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.zjzy.morebit.Activity.GoodsDetailActivity;
import com.zjzy.morebit.Activity.GoodsDetailForJdActivity;
import com.zjzy.morebit.Activity.GoodsDetailForKoalaActivity;
import com.zjzy.morebit.Activity.GoodsDetailForPddActivity;
import com.zjzy.morebit.Activity.GoodsDetailForWphActivity;
import com.zjzy.morebit.Activity.NumberGoodsDetailsActivity;
import com.zjzy.morebit.Activity.ShowWebActivity;
import com.zjzy.morebit.Module.common.View.ReUseListView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.ConsComGoodsDetailAdapter;
import com.zjzy.morebit.fragment.PanicBuyFragment;
import com.zjzy.morebit.info.contract.OrderListContract;
import com.zjzy.morebit.info.presenter.OrderListPresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpFragment;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.order.ui.NumberOrderDetailActivity;
import com.zjzy.morebit.pojo.ConsComGoodsInfo;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.goods.FloorBean;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.OpenFragmentUtils;
import com.zjzy.morebit.utils.SoftInputUtil;
import com.zjzy.morebit.utils.ViewShowUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by YangBoTian on 2018/9/12.
 */

public class SearchOrderFragment extends MvpFragment<OrderListPresenter> implements OrderListContract.View {

    private static final int REQUEST_COUNT = 10;
    @BindView(R.id.mListView)
    ReUseListView mReUseListView;
    @BindView(R.id.dateNullView)
    LinearLayout mDateNullView;
    @BindView(R.id.dateNullView_tips_tv)
    TextView mDateNullViewRecommend;
    @BindView(R.id.search_et)
    EditText search_et;
    private int type;
    private boolean isRefresh = false;
    private ConsComGoodsDetailAdapter consComGoodsDetailAdapter;
    List<ConsComGoodsInfo> mListArray = new ArrayList<>();


    @Override
    public void onSuccessful(List<ConsComGoodsInfo> datas) {

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
    public void onSearchSuccessful(ConsComGoodsInfo data) {
        //        List<ConsComGoodsInfo> consComGoodsInfos = new ArrayList<ConsComGoodsInfo>();
        //        consComGoodsInfos.add(data);
        //        mDateNullView.setVisibility(View.GONE);
        //        mReUseListView.setVisibility(View.VISIBLE);
        //        mListArray.clear();
        //        mListArray.addAll(consComGoodsInfos);
        //        consComGoodsDetailAdapter.setData(mListArray);
        //        mReUseListView.notifyDataSetChanged();
    }

    @Override
    public void onSearchSuccessful(List<ConsComGoodsInfo> data) {
        if (mPage == 1) {
            mDateNullView.setVisibility(View.GONE);
            mReUseListView.setVisibility(View.VISIBLE);
            mListArray.clear();
            mListArray.addAll(data);
        } else {
            mListArray.addAll(data);
        }
        mPage++;
        consComGoodsDetailAdapter.setData(mListArray);
        mReUseListView.notifyDataSetChanged();
        //搜索成功隐藏虚拟键盘
        SoftInputUtil.hideSoftInputAt(search_et);
    }

    @Override
    public void onFailure() {
        if (mPage == 1) {
            mDateNullView.setVisibility(View.VISIBLE);
            mReUseListView.setVisibility(View.GONE);
        } else {
            mReUseListView.getListView().setNoMore(true);
        }
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

    }

    @Override
    public void showDetailsView(ShopGoodInfo data, boolean seavDao) {

        if (data != null) {
            if ("1".equals(data.getItemSource())) {
                data.setItemSource("1");
                GoodsDetailForJdActivity.start(getActivity(), data);
            } else if ("2".equals(data.getItemSource())) {
                data.setItemSource("2");
                GoodsDetailForPddActivity.start(getActivity(), data);
            }

        } else {
            ViewShowUtils.showShortToast(getActivity(), "商品已下架");
        }

    }

    @Override
    public void OngetDetailDataFinally() {

    }

    @Override
    protected void initData() {
        //虚拟键盘回车键监听
        search_et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    MyLog.i("test", "refreshData");
                    refreshData(true);
                }
                return false;
            }
        });
    }

    @Override
    protected void initView(View view) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getInt(C.Extras.order_type, 1);
        }
        if (1 != type && 10 != type && 4 != type && 5 != type && 6 != type) {
            type = 3;
        }
        consComGoodsDetailAdapter = new ConsComGoodsDetailAdapter(getActivity(), mListArray);
        mReUseListView.getSwipeList().setOnRefreshListener(new com.zjzy.morebit.Module.common.widget.SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MyLog.i("test", "refreshData");
                refreshData(true);
            }
        });

        mReUseListView.getListView().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!mReUseListView.getSwipeList().isRefreshing()) {
                    MyLog.i("test", "refreshData");
                    refreshData(false);
                }

            }
        });
        mReUseListView.setAdapter(consComGoodsDetailAdapter);
        mDateNullViewRecommend.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        mDateNullViewRecommend.getPaint().setAntiAlias(true);//抗锯齿
        consComGoodsDetailAdapter.setOnAdapterClickListener(new ConsComGoodsDetailAdapter.OnAdapterClickListener() {
            @Override
            public void onItem(int position) {
                if (type == 1) {
                    mPresenter.onCheckGoods(SearchOrderFragment.this, mListArray.get(position).getItemId());
                } else if (type == 3) {
                    ShopGoodInfo mGoodsInfo = new ShopGoodInfo();
                    mGoodsInfo.setGoodsId(Long.valueOf(mListArray.get(position).getItemId()));
                    mPresenter.getDetailDataForJd(SearchOrderFragment.this, mGoodsInfo);
                } else if (type == 4) {
                    ShopGoodInfo mGoodsInfo = new ShopGoodInfo();
                    mGoodsInfo.setGoodsId(Long.valueOf(mListArray.get(position).getItemId()));
                    mPresenter.getDetailDataForPdd(SearchOrderFragment.this, mGoodsInfo);
                } else if (type == 5) {
                    GoodsDetailForKoalaActivity.start(getActivity(), mListArray.get(position).getItemId());
                }  else if (type == 6) {
                    GoodsDetailForWphActivity.start(getActivity(), mListArray.get(position).getItemId());
                }else if (type == 10) {
                    if (mListArray.get(position).isSelf()) {//进订单
                        NumberOrderDetailActivity.startOrderDetailActivity(getActivity(), String.valueOf(mListArray.get(position).isOnSale()),
                                mListArray.get(position).getOrderSn());
                    } else {//进商品
                        NumberGoodsDetailsActivity.start(getActivity(), mListArray.get(position).getItemId());
                    }
                } else {
                    ViewShowUtils.showShortToast(getActivity(), getString(R.string.order_no_look));
                }
            }
        });

        consComGoodsDetailAdapter.setOnSelfOrderClickListener(new ConsComGoodsDetailAdapter.OnSelfOrderClickListener() {
            @Override
            public void onReceiveGoods(String orderId, int position) {
                //确认收货
                mPresenter.ConfirmReceiveGoods(SearchOrderFragment.this, orderId);
            }

            @Override
            public void onShip(String orderId, int position) {
                //调用查看物流接口
                String originUrl = mListArray.get(position).getShipUrl();
                if (!TextUtils.isEmpty(originUrl)) {
                    originUrl = originUrl + "?orderId=" + orderId;
                    ShowWebActivity.start(getActivity(), originUrl, "物流信息");
                } else {
                    MyLog.d("test", "物流url为空");
                }
            }

            @Override
            public void onPay(String orderId, int position) {
                //去支付或者取消
                NumberOrderDetailActivity.startOrderDetailActivity(getActivity(), String.valueOf(mListArray.get(position).isOnSale()), orderId);
            }
        });
    }

    @OnClick({R.id.search, R.id.iv_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search:
                refreshData(true);
                break;
            case R.id.iv_back:
                getActivity().finish();
                break;
        }
    }


    private int mPage = 1;

    private void refreshData(boolean isRefresh) {
        if (isRefresh) {
            mPage = 1;
        }
        mPresenter.searchGoodsOrder(this, search_et.getText().toString().trim(), type, mPage);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.fragment_search_order;
    }

    @Override
    public BaseView getBaseView() {
        return this;
    }
}
