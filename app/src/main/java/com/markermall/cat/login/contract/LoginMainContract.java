package com.markermall.cat.login.contract;


import com.markermall.cat.mvp.base.base.BasePresenter;
import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * Created by fengrs
 * Data: 2018/8/7.
 */
public class LoginMainContract {
    public interface View extends BaseLoginView {
        void goToRegister();
    }

    public interface Present extends BasePresenter {
        void goToWXLogin(RxFragment activity);
    }
}
