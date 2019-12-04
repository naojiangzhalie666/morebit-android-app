package com.zjzy.morebit.login.presenter;


import android.app.Activity;
import android.text.TextUtils;

import com.zjzy.morebit.login.contract.BaseLoginView;
import com.zjzy.morebit.mvp.base.frame.MvpModel;
import com.zjzy.morebit.mvp.base.frame.MvpPresenter;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MyGsonUtils;


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
