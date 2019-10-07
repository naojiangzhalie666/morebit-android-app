package com.markermall.cat.info.contract;

import com.markermall.cat.mvp.base.base.BasePresenter;
import com.markermall.cat.mvp.base.base.BaseView;
import com.markermall.cat.pojo.DayEarnings;
import com.markermall.cat.pojo.EarningExplainBean;
import com.markermall.cat.pojo.MonthEarnings;
import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 - @Description:
 - @Author:  wuchaowen
 - @Time:  2019/9/6 11:14
 **/

public class EarningsDetailContract {
    public interface View extends BaseView {
        void onIncomeFinally();
        void onPlatformDayIncomeSuccessful(DayEarnings data, int type);
        void onPlatformMonthIncomeSuccessful(MonthEarnings data, int type);
        void onExplainSuccess(EarningExplainBean data);
    }

    public interface Present extends BasePresenter {
        void getPlatformDayIncome(RxFragment rxFragment, int type);
        void getPlatformMonthIncome(RxFragment rxFragment, int type);
        void getEarningsExplain(RxFragment rxFragment);
    }
}
