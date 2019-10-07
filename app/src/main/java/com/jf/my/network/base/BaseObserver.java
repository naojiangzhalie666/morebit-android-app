package com.jf.my.network.base;



import com.jf.my.network.Exception.FxEception;
import com.jf.my.network.interfaces.ISubscriber;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by YangBoTian on 2018/7/3.
 */

public abstract class BaseObserver<T> implements Observer<T>, ISubscriber<T> {


    @Override
    public void onSubscribe(@NonNull Disposable d) {
        doOnSubscribe(d);
    }

    @Override
    public void onNext(@NonNull T t) {
        doOnNext(t);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        String error = FxEception.handleException(e, true).getMessage();
        setError(error);
    }


    @Override
    public void onComplete() {
        doOnCompleted();
    }


    private void setError(String errorMsg) {
        doOnError(errorMsg);
    }
}
