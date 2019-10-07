package com.jf.my.info.contract;

import com.jf.my.mvp.base.base.BasePresenter;
import com.jf.my.mvp.base.base.BaseView;
import com.jf.my.pojo.DayEarnings;
import com.jf.my.pojo.MonthEarnings;
import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * Created by YangBoTian on 2018/9/12.
 */

public class EarningsReportContract {
    public interface View extends BaseView {
        void onDayIncomeSuccessful(DayEarnings data);
        void onMonthIncomeSuccessful(MonthEarnings data);
        void onIncomeFinally();
    }

    public interface Present extends BasePresenter {
       void getDayIncome(RxFragment rxFragment);
       void getMonthIncome(RxFragment rxFragment);
    }
}
