package com.zjzy.morebit.goods.shopping.presenter;

import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.fragment.NumberFragment;
import com.zjzy.morebit.goods.shopping.contract.NumberGoodsListContract;
import com.zjzy.morebit.goods.shopping.model.NumberGoodsModel;
import com.zjzy.morebit.goods.shopping.ui.fragment.BrandListFragment;
import com.zjzy.morebit.mvp.base.frame.MvpPresenter;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.myInfo.UpdateInfoBean;
import com.zjzy.morebit.pojo.number.NumberGoods;
import com.zjzy.morebit.pojo.number.NumberGoodsList;
import com.zjzy.morebit.utils.appDownload.update_app.UpdateAppBean;

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
                        if (getIView() != null && getIView() instanceof NumberFragment) {
                            getIView().showFinally();
                        }
                    }
                })
                .subscribe(new DataObserver<NumberGoodsList>() {
                    @Override
                    protected void onDataListEmpty() {
                        getIView().showError();
                    }
                    @Override
                    protected void onSuccess(NumberGoodsList data) {
                        getIView().showSuccessful(data);
                    }
                });
    }

    @Override
    public void updateGrade(RxFragment fragment,int userType) {
        mModel.updateUserGrade(fragment, userType)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (getIView() != null && getIView() instanceof NumberFragment) {
                            getIView().showFinally();
                        }
                    }
                })
                .subscribe(new DataObserver<UpdateInfoBean>() {
                    @Override
                    protected void onDataListEmpty() {
                        getIView().showError();
                    }
                    @Override
                    protected void onSuccess(UpdateInfoBean data) {
                        getIView().onGradeSuccess(data);
                    }
                });
    }


}
