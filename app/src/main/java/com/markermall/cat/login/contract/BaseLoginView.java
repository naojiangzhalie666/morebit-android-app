package com.markermall.cat.login.contract;

import com.markermall.cat.mvp.base.base.BaseView;

/**
 * Created by fengrs on 2018/8/8.
 * 登录的Base
 */

public interface BaseLoginView extends BaseView{

    void loginError(String  code);

    void loginSucceed(String s);

    void sendCodeSuccess(String data);
    void sendCodeFail();
}
