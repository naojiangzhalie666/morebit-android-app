package com.zjzy.morebit.info.contract;

import com.zjzy.morebit.mvp.base.base.BasePresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.zjzy.morebit.pojo.DayEarnings;
import com.zjzy.morebit.pojo.MonthEarnings;

/**
 * Created by YangBoTian on 2018/9/12.
 */

public class EarningsContract {
    public interface View extends BaseView {
        void onIncomeFinally();
        void checkWithdrawTime();
        void checkWithdrawTimeError(String errMsg,String errCode);

        void getMontnSuccess(MonthEarnings data);
        void getMonthError(String errMsg,String errCode);
        void getDaySuccess(DayEarnings data);
        void getDayError(String errMsg,String errCode);
    }

    public interface Present extends BasePresenter {
       void getAllianceAppKey(RxFragment rxFragment);
        void checkWithdrawTime(RxFragment rxFragment);
        void getMontMoney(RxFragment rxFragment);
        void getDayMoney(RxFragment rxFragment);

    }
}
