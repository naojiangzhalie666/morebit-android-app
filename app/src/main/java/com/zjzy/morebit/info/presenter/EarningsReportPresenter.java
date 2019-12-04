package com.zjzy.morebit.info.presenter;

import com.zjzy.morebit.info.contract.EarningsReportContract;
import com.zjzy.morebit.info.model.InfoModel;
import com.zjzy.morebit.mvp.base.frame.MvpPresenter;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.DayEarnings;
import com.zjzy.morebit.pojo.MonthEarnings;
import com.trello.rxlifecycle2.components.support.RxFragment;

import io.reactivex.functions.Action;

/**
 - @Description:
 - @Author:  wuchaowen
 - @Time:  2019/8/31 13:30
 **/

public class EarningsReportPresenter extends MvpPresenter<InfoModel, EarningsReportContract.View> implements EarningsReportContract.Present {

    @Override
    public void getDayIncome(RxFragment rxFragment) {
        mModel.getDayIncome(rxFragment)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getIView().onIncomeFinally();
                    }
                }).subscribe(new DataObserver<DayEarnings>() {

            @Override
            protected void onSuccess(DayEarnings data) {
                getIView().onDayIncomeSuccessful(data);
            }
        });
    }

    @Override
    public void getMonthIncome(RxFragment rxFragment) {
        mModel.getMonthIncome(rxFragment)
             .subscribe(new DataObserver<MonthEarnings>() {
            @Override
            protected void onSuccess(MonthEarnings data) {
                getIView().onMonthIncomeSuccessful(data);
            }
        });
    }


}
