package com.zjzy.morebit.order.presenter;

import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.address.AddressInfo;
import com.zjzy.morebit.mvp.base.frame.MvpPresenter;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.order.ResponseOrderInfo;
import com.zjzy.morebit.order.contract.ConfirmOrderContract;
import com.zjzy.morebit.order.model.ConfirmOrderModel;

/**
 * Created by fengrs on 2018/11/3.
 */

public class OrderDetailPresenter extends MvpPresenter<ConfirmOrderModel, ConfirmOrderContract.View> implements ConfirmOrderContract.Present {

    @Override
    public void getDefaultAddress(BaseActivity activity) {
        mModel.getdefaultAddress(activity)
                .subscribe(new DataObserver<AddressInfo>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        getIView().onDefaultAddressError();
                    }

                    @Override
                    protected void onSuccess(AddressInfo data) {
                        getIView().onDefaultAddressSuccess(data);
                    }
                });
    }

    @Override
    public void createOrderForVip(BaseActivity rxActivity, int addressId, int goodsId, int goodsNum, String goodsPrice, String totalPrice) {
        mModel.createOrderForVip(rxActivity,addressId,goodsId,goodsNum,goodsPrice,totalPrice)
                .subscribe(new DataObserver<ResponseOrderInfo>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        getIView().onCreateError();
                    }

                    @Override
                    protected void onSuccess(ResponseOrderInfo data) {
                        getIView().onCreateOrderSuccess(data);
                    }
                });
    }




}
