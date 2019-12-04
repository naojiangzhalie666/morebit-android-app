package com.zjzy.morebit.utils;

import android.text.TextUtils;

import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.request.RequestPutErrorBean;

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
