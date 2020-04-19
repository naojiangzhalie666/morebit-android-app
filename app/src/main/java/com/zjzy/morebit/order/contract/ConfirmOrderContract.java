package com.zjzy.morebit.order.contract;

import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.pojo.address.AddressInfo;
import com.zjzy.morebit.mvp.base.base.BasePresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.pojo.order.OrderSyncResult;
import com.zjzy.morebit.pojo.order.ResponseOrderInfo;

/**
 * 确认定单接口
 * Created by haiping.liu on 2019-12-12.
 */
public class ConfirmOrderContract {
    public interface View extends BaseView {
        /**
         * 创建订单成功
         * @param orderInfo
         */
        void onCreateOrderSuccess(ResponseOrderInfo orderInfo);

        /**
         * 创建订单失败
         */
        void onCreateError(String msgError);

        /**
         * 获取默认地址成功
         */
        void onDefaultAddressSuccess(AddressInfo addressInfo);

        /**
         * 获取默认地址失败
         */
        void onDefaultAddressError();

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
        void getDefaultAddress(BaseActivity activity);

        void createOrderForVip(BaseActivity rxActivity,int addressId,
                               int goodsId,
                               int goodsNum,
                               String goodsPrice,
                               String totalPrice);

        void syncPayResult(BaseActivity rxActivity,String orderId,int payStatus);


    }
}
