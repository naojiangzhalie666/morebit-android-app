package com.markermall.cat.info.contract;

import com.markermall.cat.mvp.base.base.BasePresenter;
import com.markermall.cat.mvp.base.base.BaseView;
import com.markermall.cat.pojo.DayEarnings;
import com.markermall.cat.pojo.MonthEarnings;
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
