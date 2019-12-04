package com.zjzy.morebit.network;

import com.zjzy.morebit.App;
import com.zjzy.morebit.utils.ViewShowUtils;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by YangBoTian on 2018/6/7 17:28
 */

public abstract class CallBackObserver<T> implements Observer<T> {
    private Disposable d;
    private boolean isShowError = true;

    public CallBackObserver() {

    }

    public CallBackObserver(boolean isShowError) {
        this.isShowError = isShowError;
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.d = d;
    }


    @Override
    public void onNext(@NonNull T t) {

    }

    @Override//异常信息统一处理
    public void onError(Throwable e) {
        //中断请求
        d.dispose();
        //Http异常
        if (isShowError) {
            ViewShowUtils.showShortToast(App.getAppContext(), "获取失败数据,请重试");
        }
        e.printStackTrace();
    }

    //请求完成
    @Override
    public void onComplete() {
        d.dispose();
    }

}
