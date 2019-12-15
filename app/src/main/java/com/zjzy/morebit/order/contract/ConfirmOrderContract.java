package com.zjzy.morebit.order.contract;

import com.trello.rxlifecycle2.components.RxActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.address.AddressInfo;
import com.zjzy.morebit.mvp.base.base.BasePresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.order.ResponseOrderInfo;
import com.zjzy.morebit.pojo.BrandSell;

import java.util.List;

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
        void onCreateError();

        /**
         * 获取默认地址成功
         */
        void onDefaultAddressSuccess(AddressInfo addressInfo);

        /**
         * 获取默认地址失败
         */
        void onDefaultAddressError();

    }

    public interface Present extends BasePresenter {
        void getDefaultAddress(BaseActivity activity);

        void createOrderForVip(BaseActivity rxActivity,int addressId,
                               int goodsId,
                               int goodsNum,
                               String goodsPrice,
                               String totalPrice);
    }
}
