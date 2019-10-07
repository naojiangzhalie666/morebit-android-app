package com.markermall.cat.login.presenter;


import android.app.Activity;
import android.text.TextUtils;

import com.markermall.cat.login.contract.BaseLoginView;
import com.markermall.cat.mvp.base.frame.MvpModel;
import com.markermall.cat.mvp.base.frame.MvpPresenter;
import com.markermall.cat.network.observer.DataObserver;
import com.markermall.cat.pojo.UserInfo;
import com.markermall.cat.utils.LoginUtil;
import com.markermall.cat.utils.MyGsonUtils;


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
