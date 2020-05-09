package com.zjzy.morebit.login.contract;

import com.zjzy.morebit.mvp.base.base.BaseView;

/**
 * Created by fengrs on 2018/8/8.
 * 登录的Base
 */

public interface BaseLoginView extends BaseView{

    void loginError(String  code);

    void loginSucceed(String s);

    void sendCodeSuccess(String data);
    void sendCodeFail();
    void  goToRegister();
}
