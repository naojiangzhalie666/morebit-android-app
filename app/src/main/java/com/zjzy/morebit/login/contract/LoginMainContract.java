package com.zjzy.morebit.login.contract;


import com.zjzy.morebit.mvp.base.base.BasePresenter;
import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * Created by fengrs
 * Data: 2018/8/7.
 */
public class LoginMainContract {
    public interface View extends BaseLoginView {
        void goToRegister();
        void goToWx();
    }

    public interface Present extends BasePresenter {
        void goToWXLogin(RxFragment activity);
        void goLocalWx(RxFragment activity);
    }
}
