package com.zjzy.morebit.order.contract;

import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.address.AddressInfo;
import com.zjzy.morebit.mvp.base.base.BasePresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.order.OrderDetailInfo;
import com.zjzy.morebit.order.OrderSyncResult;
import com.zjzy.morebit.order.ResponseOrderInfo;

/**
 * 确认定单接口
 * Created by haiping.liu on 2019-12-12.
 */
public class OrderDetailContract {
    public interface View extends BaseView {


        /**
         * 取消订单成功

         */
        void onCancelOrderSuccess();

        /**
         * 取消订单失败
         */
        void onCancelOrderError();

        /**
         * 重新支付成功
         * @param orderInfo
         */
        void onRePaySuccess(ResponseOrderInfo orderInfo);

        /**
         * 重新支付失败
         */
        void onRePayError();

        /**
         * 获取订单详情信息
         * @param orderDetailInfo
         */
        void onOrderDetailSuccess(OrderDetailInfo orderDetailInfo);

        /**
         * 订单详情失败
         */
        void onOrderDetailError();

        /**
         * 支付结果同步成功
         */
        void onSyncPayResultSuccess(OrderSyncResult result);

        /**
         * 支付结果失败
         */
        void onSyncPayResultError();

    }

    public interface Present extends BasePresenter {

        /**
         * 取消订单
         */
        void cancelOrder(BaseActivity activity,String orderId);

        /**
         * 去支付未支付的订单
         */
        void payForOrder(BaseActivity activity,String orderId);

        /**
         * 获取订单详情
         * @param activity
         */
        void getOrderDetail(BaseActivity activity,String orderId);

        /**
         * 同步支付结果
         * @param rxActivity
         * @param orderId
         * @param payStatus
         */
        void syncPayResult(BaseActivity rxActivity,String orderId,int payStatus);

    }
}
