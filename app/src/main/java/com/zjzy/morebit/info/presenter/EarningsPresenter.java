package com.zjzy.morebit.info.presenter;

import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.info.contract.EarningsContract;
import com.zjzy.morebit.info.model.InfoModel;
import com.zjzy.morebit.mvp.base.frame.MvpPresenter;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.UserIncomeDetail;
import com.zjzy.morebit.pojo.EarningExplainBean;
import com.zjzy.morebit.pojo.MonthEarnings;
import com.zjzy.morebit.utils.TaobaoUtil;
import com.trello.rxlifecycle2.components.support.RxFragment;

import io.reactivex.functions.Action;

/**
 * Created by YangBoTian on 2018/9/12.
 */

public class EarningsPresenter extends MvpPresenter<InfoModel, EarningsContract.View> implements EarningsContract.Present {


    @Override
    public void getAllianceAppKey(RxFragment rxFragment) {
        TaobaoUtil.getAllianceAppKey((BaseActivity) rxFragment.getActivity());
    }

    @Override
    public void checkWithdrawTime(RxFragment rxFragment) {
        mModel.checkWithdrawTime(rxFragment)
                .subscribe(new DataObserver<String>(false) {
                    @Override
                    protected void onSuccess(String data) {
                        getIView().checkWithdrawTime();
                    }

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        getIView().checkWithdrawTimeError(errorMsg, errCode);
                    }
                });
    }



    @Override
    public void getUserIncomeDetail(RxFragment rxFragment) {
        mModel.getUserIncomeDetail(rxFragment)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getIView().onIncomeFinally();
                    }
                }).subscribe(new DataObserver<UserIncomeDetail>() {

            @Override
            protected void onSuccess(UserIncomeDetail data) {
                getIView().getDaySuccess(data);
            }
        });
    }



}
