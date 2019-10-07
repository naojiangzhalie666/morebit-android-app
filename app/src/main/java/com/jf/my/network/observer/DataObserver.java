package com.jf.my.network.observer;


import android.text.TextUtils;

import com.jf.my.App;
import com.jf.my.login.ui.LoginSinglePaneActivity;
import com.jf.my.network.BaseResponse;
import com.jf.my.network.base.BaseDataObserver;
import com.jf.my.utils.AppUtil;
import com.jf.my.utils.C;
import com.jf.my.utils.LoginUtil;
import com.jf.my.utils.ViewShowUtils;
import com.jf.my.utils.helper.ActivityLifeHelper;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by YangBoTian on 2018/7/4.
 */

public abstract class DataObserver<T> extends BaseDataObserver<T> {

    private Disposable d;

    public DataObserver(boolean isShowError) {
        this.isShowError = isShowError;
    }

    public DataObserver() {
    }


    /**
     * 成功回调
     *
     * @param data 结果
     */
    protected abstract void onSuccess(T data);

    /**
     * data == null
     */
    protected void onDataNull() {

    }

    /**
     * data 为列表时候返回空
     */
    protected void onDataListEmpty() {

    }

    @Override
    public void doOnSubscribe(Disposable d) {
        this.d = d;
    }

    @Override
    public void doOnError(String errorMsg) {
        onError(errorMsg, 100 + "");

    }

    @Override
    public void doOnNext(BaseResponse<T> data) {
        //对错误码统一处理

        switch (data.getCode()) {
            case C.requestCode.SUCCESS:

                if (data.getData() != null) {
                    if (data.getData() instanceof List) {
                        if (((List) data.getData()).size() != 0) {
                            onSuccess(data.getData());
                        } else {
                            onDataListEmpty();
                            onError(data.getMsg() + "", C.requestCode.dataListEmpty);
                        }
                    } else {
                        onSuccess(data.getData());
                    }
                } else {
                    onDataNull();
                    onError(data.getMsg() + "", C.requestCode.dataNull);
                }
                break;
            case C.requestCode.B10019://账号异常去登陆
                if (isShowError && !TextUtils.isEmpty(data.getMsg())) {
                    ViewShowUtils.showShortToast(App.getAppContext(), data.getMsg());
                }
                if (!ActivityLifeHelper.getInstance().isContainsActivity(LoginSinglePaneActivity.class)) {
                    if (!AppUtil.isFastClick(100)) {
                        LoginUtil.logout(true);
                    }
                }
                break;

            case C.requestCode.B10035://  不存在的邀请码
            case C.requestCode.B10003://密码不正确不做提示
            case C.requestCode.B10005://登录跳转不做提示
            case C.requestCode.B10010:
            case C.requestCode.B10031:
            case C.requestCode.B10051:
            case C.requestCode.B10310:
            case C.requestCode.B1000004:
            case C.requestCode.B30401:
                onError(data.getMsg() + "", data.getCode());
                break;
            default:
                onError(data.getMsg() + "", data.getCode());
                if (isShowError && !TextUtils.isEmpty(data.getMsg())) {
                    ViewShowUtils.showShortToast(App.getAppContext(), data.getMsg());
                }
                break;
        }
    }


    /**
     * 失败回调 java
     *
     * @param errorMsg 错误信息
     * @param errCode
     */
    protected void onError(String errorMsg, String errCode) {

    }


    @Override
    public void doOnCompleted() {
        d.dispose();
    }

}



