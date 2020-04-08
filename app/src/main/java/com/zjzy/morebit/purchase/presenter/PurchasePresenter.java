package com.zjzy.morebit.purchase.presenter;

import android.annotation.SuppressLint;

import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.mvp.base.frame.MvpPresenter;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.VipUseInfoBean;
import com.zjzy.morebit.purchase.control.PurchaseControl;
import com.zjzy.morebit.purchase.model.PurchaseaModel;

import io.reactivex.functions.Action;

public class PurchasePresenter extends MvpPresenter<PurchaseaModel, PurchaseControl.View> implements PurchaseControl.Present {
    @Override
    public void freesheet(BaseActivity activity) {
       /* mModel.userInfo(activity)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getIView().onSuccess();
                    }
                })
                .subscribe(new DataObserver<VipUseInfoBean>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        getIView().onError();
                    }

                    @SuppressLint("CheckResult")
                    @Override
                    protected void onSuccess(VipUseInfoBean data) {
                        getIView().onSuccess();
                    }
                });*/
    }
}
