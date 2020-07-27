package com.zjzy.morebit.info.contract;

import com.zjzy.morebit.mvp.base.base.BasePresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.pojo.UserIncomeDetail;
import com.zjzy.morebit.pojo.MonthEarnings;
import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * Created by YangBoTian on 2018/9/12.
 */

public class EarningsReportContract {
    public interface View extends BaseView {
        void onDayIncomeSuccessful(UserIncomeDetail data);
        void onMonthIncomeSuccessful(MonthEarnings data);
        void onIncomeFinally();
    }

    public interface Present extends BasePresenter {
       void getDayIncome(RxFragment rxFragment);
       void getMonthIncome(RxFragment rxFragment);
    }
}
