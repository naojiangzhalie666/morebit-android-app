package com.zjzy.morebit.info.contract;

import com.zjzy.morebit.mvp.base.base.BasePresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.pojo.UserIncomeDetail;
import com.zjzy.morebit.pojo.EarningExplainBean;
import com.zjzy.morebit.pojo.MonthEarnings;
import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 - @Description:
 - @Author:  wuchaowen
 - @Time:  2019/9/6 11:14
 **/

public class EarningsDetailContract {
    public interface View extends BaseView {
        void onIncomeFinally();
        void onPlatformDayIncomeSuccessful(UserIncomeDetail data, int type);
        void onPlatformMonthIncomeSuccessful(MonthEarnings data, int type);
        void onExplainSuccess(EarningExplainBean data);
    }

    public interface Present extends BasePresenter {
        void getPlatformDayIncome(RxFragment rxFragment, int type);
        void getPlatformMonthIncome(RxFragment rxFragment, int type);
        void getEarningsExplain(RxFragment rxFragment);
    }
}
