package com.jf.my.info.presenter;

import com.jf.my.info.contract.EarningsDetailContract;
import com.jf.my.info.model.InfoModel;
import com.jf.my.mvp.base.frame.MvpPresenter;
import com.jf.my.network.observer.DataObserver;
import com.jf.my.pojo.DayEarnings;
import com.jf.my.pojo.EarningExplainBean;
import com.jf.my.pojo.MonthEarnings;
import com.jf.my.utils.C;
import com.trello.rxlifecycle2.components.support.RxFragment;

import io.reactivex.functions.Action;

/**
 - @Description:
 - @Author:
 - @Time:  2019/9/6 11:15
 **/

public class EarningsDetailPresenter extends MvpPresenter<InfoModel, EarningsDetailContract.View> implements EarningsDetailContract.Present {





    @Override
    public void getPlatformDayIncome(RxFragment rxFragment, final int type) {
        if(type == C.OrderType.TAOBAO){
            mModel.getDayIncome(rxFragment)
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            getIView().onIncomeFinally();
                        }
                    }).subscribe(new DataObserver<DayEarnings>() {

                @Override
                protected void onSuccess(DayEarnings data) {
                    getIView().onPlatformDayIncomeSuccessful(data,type);
                }
            });
        }else{
            mModel.getPlatformDayIncome(rxFragment,type)
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            getIView().onIncomeFinally();
                        }
                    }).subscribe(new DataObserver<DayEarnings>() {

                @Override
                protected void onSuccess(DayEarnings data) {
                    getIView().onPlatformDayIncomeSuccessful(data,type);
                }
            });
        }

    }

    @Override
    public void getPlatformMonthIncome(RxFragment rxFragment, final int type) {
        if(type == C.OrderType.TAOBAO){
            mModel.getMonthIncome(rxFragment)
                    .subscribe(new DataObserver<MonthEarnings>() {
                        @Override
                        protected void onSuccess(MonthEarnings data) {
                            getIView().onPlatformMonthIncomeSuccessful(data,type);
                        }
                    });
        }else{
            mModel.getPlatformMonthIncome(rxFragment,type)
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            getIView().onIncomeFinally();
                        }
                    }).subscribe(new DataObserver<MonthEarnings>() {

                @Override
                protected void onSuccess(MonthEarnings data) {
                    getIView().onPlatformMonthIncomeSuccessful(data,type);
                }
            });
        }

    }

    @Override
    public void getEarningsExplain(RxFragment rxFragment) {
         mModel.getEarningsExplain(rxFragment)
                 .subscribe(new DataObserver<EarningExplainBean>() {
                     @Override
                     protected void onSuccess(EarningExplainBean data) {
                         getIView().onExplainSuccess(data);
                     }
                 });
    }
}
