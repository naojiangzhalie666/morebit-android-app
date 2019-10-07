package com.jf.my.info.contract;

import com.jf.my.mvp.base.base.BasePresenter;
import com.jf.my.mvp.base.base.BaseView;
import com.jf.my.pojo.DayEarnings;
import com.jf.my.pojo.EarningExplainBean;
import com.jf.my.pojo.MonthEarnings;
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
