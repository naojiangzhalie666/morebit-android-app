package com.markermall.cat.utils;

import android.text.TextUtils;

import com.markermall.cat.Module.common.Activity.BaseActivity;
import com.markermall.cat.Module.common.Utils.LoadingView;
import com.markermall.cat.network.BaseResponse;
import com.markermall.cat.network.RxHttp;
import com.markermall.cat.network.RxUtils;
import com.markermall.cat.network.observer.DataObserver;
import com.markermall.cat.pojo.request.RequestPutErrorBean;

import io.reactivex.functions.Action;

/**
 * Created by fengrs on 2018/7/7.
 * 上传错误日志error
 */

public class PutErrorUtils extends Exception {

    public static void putMyServerLog(BaseActivity activity,String errorString) {
        if (TextUtils.isEmpty(errorString))return;
        RequestPutErrorBean requestBean = new RequestPutErrorBean();
        requestBean.setInfo(errorString);
        RxHttp.getInstance().getSysteService()
                .sendMyServerLog(requestBean)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(activity.<BaseResponse<String>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                })
                .subscribe(new DataObserver<String>(false) {
                    /**
                     * 成功回调
                     *
                     * @param data 结果
                     */
                    @Override
                    protected void onSuccess(String data) {

                    }

                });
    }


}
