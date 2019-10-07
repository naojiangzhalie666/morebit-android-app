package com.jf.my.login.contract;


import com.jf.my.mvp.base.base.BasePresenter;
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
