package com.zjzy.morebit.purchase.presenter;

import android.annotation.SuppressLint;

import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.mvp.base.frame.MvpPresenter;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.CommonEmpty;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ShopGoodBean;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.VipUseInfoBean;
import com.zjzy.morebit.purchase.control.PurchaseControl;
import com.zjzy.morebit.purchase.model.PurchaseaModel;

import java.util.List;

import io.reactivex.functions.Action;

public class PurchasePresenter extends MvpPresenter<PurchaseaModel, PurchaseControl.View> implements PurchaseControl.Present {

    @Override
    public void getPurchase(BaseActivity activity, int page) {

        mModel.getPurchaseGoods(activity, page)
                .compose(RxUtils.<BaseResponse<List<ShopGoodInfo>>>switchSchedulers())
                .compose(activity.<BaseResponse<List<ShopGoodInfo>>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                })
                .subscribe(new DataObserver<List<ShopGoodInfo>>() {

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        getIView().onError(errorMsg);
                    }

                    @Override
                    protected void onSuccess(List<ShopGoodInfo> data) {
                        getIView().onSuccess(data);

                    }

                });
    }

    @Override
    public void getProduct(BaseActivity activity) {
        mModel.getProductGoods(activity)
                .compose(RxUtils.<BaseResponse<List<ShopGoodInfo>>>switchSchedulers())
                .compose(activity.<BaseResponse<List<ShopGoodInfo>>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                })
                .subscribe(new DataObserver<List<ShopGoodInfo>>() {

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        getIView().onProductError(errorMsg);
                    }

                    @Override
                    protected void onSuccess(List<ShopGoodInfo> data) {
                        getIView().onProductSuccess(data);

                    }

                });
    }


}
