package com.zjzy.morebit.order.presenter;

import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.mvp.base.frame.MvpPresenter;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.order.OrderDetailInfo;
import com.zjzy.morebit.pojo.order.OrderSyncResult;
import com.zjzy.morebit.pojo.order.ResponseOrderInfo;
import com.zjzy.morebit.order.contract.OrderDetailContract;
import com.zjzy.morebit.order.model.OrderDetailModel;

/**
 * Created by fengrs on 2018/11/3.
 */

public class OrderDetailPresenter extends MvpPresenter<OrderDetailModel, OrderDetailContract.View> implements OrderDetailContract.Present {


    /**
     * 取消订单
     * @param activity
     * @param orderId
     */
    @Override
    public void cancelOrder(BaseActivity activity, String orderId) {
        mModel.cancelOrder(activity,orderId)
                .subscribe(new DataObserver<Boolean>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        getIView().onCancelOrderError();
                    }

                    @Override
                    protected void onSuccess(Boolean result) {
                        getIView().onCancelOrderSuccess();
                    }
                });
    }

    /**
     * 去支付
     * @param activity
     * @param orderId
     */
    @Override
    public void payForOrder(BaseActivity activity, String orderId) {
        mModel.rePayByOrderId(activity,orderId)
                .subscribe(new DataObserver<ResponseOrderInfo>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        getIView().onRePayError();
                    }

                    @Override
                    protected void onSuccess(ResponseOrderInfo data) {
                        getIView().onRePaySuccess(data);
                    }
                });
    }

    /**
     * 订单详情
     * @param activity
     * @param orderId
     */
    @Override
    public void getOrderDetail(BaseActivity activity, String orderId) {
        mModel.getOrderDetail(activity,orderId)
                .subscribe(new DataObserver<OrderDetailInfo>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        getIView().onOrderDetailError();
                    }

                    @Override
                    protected void onSuccess(OrderDetailInfo data) {
                        getIView().onOrderDetailSuccess(data);
                    }
                });
    }

    @Override
    public void syncPayResult(BaseActivity rxActivity, String orderId, int payStatus) {
        mModel.syncPayStatus(rxActivity,orderId,payStatus)
                .subscribe(new DataObserver<OrderSyncResult>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        getIView().onSyncPayResultError();
                    }

                    @Override
                    protected void onSuccess(OrderSyncResult data) {
                        getIView().onSyncPayResultSuccess(data);
                    }
                });
    }

}
