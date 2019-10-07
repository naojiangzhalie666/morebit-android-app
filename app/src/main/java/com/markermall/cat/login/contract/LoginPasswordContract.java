package com.markermall.cat.login.contract;


import com.markermall.cat.mvp.base.base.BasePresenter;
import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * Created by fengrs
 * Data: 2018/8/8.
 */
public class LoginPasswordContract {
    public interface View extends BaseLoginView {
    }

    public interface Present extends BasePresenter {
        void Passwordlogin(RxFragment rxFragment, String phone, String pwd,String areaCode);
    }
}
