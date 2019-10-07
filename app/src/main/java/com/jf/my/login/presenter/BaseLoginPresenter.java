package com.jf.my.login.presenter;


import android.app.Activity;
import android.text.TextUtils;

import com.jf.my.login.contract.BaseLoginView;
import com.jf.my.mvp.base.frame.MvpModel;
import com.jf.my.mvp.base.frame.MvpPresenter;
import com.jf.my.network.observer.DataObserver;
import com.jf.my.pojo.UserInfo;
import com.jf.my.utils.LoginUtil;
import com.jf.my.utils.MyGsonUtils;


/**
 * Created by fengrs
 * Data: 2018/8/3.
 */
public abstract class BaseLoginPresenter<M extends MvpModel, V extends BaseLoginView> extends MvpPresenter<M, V> {

    public DataObserver getLoginDataObserver(final Activity activity) {

        return new DataObserver<UserInfo>() {
            @Override
            protected void onError(String errorMsg, String errCode) {
                getIView().loginError(errCode);

            }

            @Override
            protected void onSuccess(UserInfo data) {
                String s = MyGsonUtils.beanToJson(data);
                if (TextUtils.isEmpty(s)) {
                    return;
                }

                LoginUtil.LoginSuccess(data, activity);
                getIView().loginSucceed(s);
            }
        };
    }


}
