package com.zjzy.morebit.goods.shopping.presenter;

import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.goods.shopping.contract.NumberGoodsListContract;
import com.zjzy.morebit.goods.shopping.model.NumberGoodsModel;
import com.zjzy.morebit.goods.shopping.ui.fragment.BrandListFragment;
import com.zjzy.morebit.mvp.base.frame.MvpPresenter;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.number.NumberGoods;

import java.util.List;

import io.reactivex.functions.Action;

/**
 * Created by haiping.liu on 2019-12-11.
 */
public class NumberGoodsListPresenter extends MvpPresenter<NumberGoodsModel, NumberGoodsListContract.View> implements NumberGoodsListContract.Present {

    @Override
    public void getNumberGoodsList(RxFragment fragment, int page) {
        mModel.getNumberGoodsList(fragment, page)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (getIView() != null && getIView() instanceof BrandListFragment) {
                            getIView().showFinally();
                        }
                    }
                })
                .subscribe(new DataObserver<List<NumberGoods>>() {
                    @Override
                    protected void onDataListEmpty() {
                        getIView().showError();
                    }
                    @Override
                    protected void onSuccess(List<NumberGoods> data) {
                        getIView().showSuccessful(data);
                    }
                });
    }
}
