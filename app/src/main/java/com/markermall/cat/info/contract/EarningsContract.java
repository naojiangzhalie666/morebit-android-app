package com.markermall.cat.info.contract;

import com.markermall.cat.mvp.base.base.BasePresenter;
import com.markermall.cat.mvp.base.base.BaseView;
import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * Created by YangBoTian on 2018/9/12.
 */

public class EarningsContract {
    public interface View extends BaseView {
        void onIncomeFinally();
        void checkWithdrawTime();
        void checkWithdrawTimeError(String errMsg,String errCode);
    }

    public interface Present extends BasePresenter {
       void getAllianceAppKey(RxFragment rxFragment);
        void checkWithdrawTime(RxFragment rxFragment);
    }
}
