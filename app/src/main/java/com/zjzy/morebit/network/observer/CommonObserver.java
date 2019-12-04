package com.zjzy.morebit.network.observer;


import com.zjzy.morebit.network.base.BaseObserver;

import io.reactivex.disposables.Disposable;

/**
 * Created by YangBoTian on 2018/7/3.
 *     通用的Observer
 *     可以根据自己需求自定义自己的类继承BaseObserver<T>即可
 */

public abstract class CommonObserver<T> extends BaseObserver<T> {
    private Disposable d;

    public CommonObserver() {
    }


    /**
     * 失败回调
     *
     * @param errorMsg
     */
    protected abstract void onError(String errorMsg);

    /**
     * 成功回调
     *
     * @param t
     */
    protected abstract void onSuccess(T t);



    @Override
    public void doOnSubscribe(Disposable d) {
        this.d = d;
    }


    @Override
    public void doOnError(String errorMsg) {

        onError(errorMsg);
    }

    @Override
    public void doOnNext(T t) {
        onSuccess(t);
    }

    @Override
    public void doOnCompleted() {
        d.dispose();
    }
}
