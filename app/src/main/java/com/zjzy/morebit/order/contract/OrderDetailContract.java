package com.zjzy.morebit.order.contract;

import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.address.AddressInfo;
import com.zjzy.morebit.mvp.base.base.BasePresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.order.OrderDetailInfo;
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
    }

    public interface Present extends BasePresenter {
        void getDefaultAddress(BaseActivity activity);

        void createOrderForVip(BaseActivity rxActivity, int addressId,
                               int goodsId,
                               int goodsNum,
                               String goodsPrice,
                               String totalPrice);
    }
}
