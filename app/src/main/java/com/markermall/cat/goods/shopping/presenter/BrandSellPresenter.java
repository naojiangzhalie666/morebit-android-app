package com.markermall.cat.goods.shopping.presenter;

import com.markermall.cat.goods.shopping.contract.BrandSellContract;
import com.markermall.cat.goods.shopping.model.ReturnCashModel;
import com.markermall.cat.goods.shopping.ui.fragment.BrandListFragment;
import com.markermall.cat.mvp.base.frame.MvpPresenter;
import com.markermall.cat.network.observer.DataObserver;
import com.markermall.cat.pojo.BrandSell;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

import io.reactivex.functions.Action;

/**
 * Created by YangBoTian on 2018/8/17.
 */

public class BrandSellPresenter extends MvpPresenter<ReturnCashModel, BrandSellContract.View> implements BrandSellContract.Present {

    @Override
    public void getBrandSellList(RxFragment fragment, int page) {
        mModel.getBrandSellList(fragment, page)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (getIView() != null && getIView() instanceof BrandListFragment) {
                            getIView().showFinally();
                        }
                    }
                })
                .subscribe(new DataObserver<List<BrandSell>>() {
                    @Override
                    protected void onSuccess(List<BrandSell> data) {
                        getIView().showSuccessful(data);
                    }
                });
    }

    @Override
    public void getBrandSellGoodsList(RxFragment fragment, int page) {
        mModel.getBrandSellGoodsList(fragment, page)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getIView().showFinally();
                    }
                }).subscribe(new DataObserver<List<BrandSell>>() {
            @Override
            protected void onDataListEmpty() {
                getIView().showError();
            }

            @Override
            protected void onSuccess(List<BrandSell> data) {
                getIView().getGoodsSuccessful(data);
            }
        });
    }
}
