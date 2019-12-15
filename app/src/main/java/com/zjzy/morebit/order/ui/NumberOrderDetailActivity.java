package com.zjzy.morebit.order.ui;

import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpActivity;
import com.zjzy.morebit.order.OrderDetailInfo;
import com.zjzy.morebit.order.ResponseOrderInfo;
import com.zjzy.morebit.order.contract.OrderDetailContract;
import com.zjzy.morebit.order.presenter.OrderDetailPresenter;

/**
 * 订单详情页面
 * Created by haiping.liu on 2019-12-13.
 */
public class NumberOrderDetailActivity extends MvpActivity<OrderDetailPresenter> implements OrderDetailContract.View {

    @Override
    public BaseView getBaseView() {
        return this;
    }

    @Override
    protected int getViewLayout() {
        return 0;
    }

    @Override
    public void onCancelOrderSuccess() {

    }

    @Override
    public void onCancelOrderError() {

    }

    @Override
    public void onRePaySuccess(ResponseOrderInfo orderInfo) {

    }

    @Override
    public void onRePayError() {

    }

    @Override
    public void onOrderDetailSuccess(OrderDetailInfo orderDetailInfo) {

    }

    @Override
    public void onOrderDetailError() {

    }
}
