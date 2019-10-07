package com.markermall.cat.network.base;


import com.markermall.cat.network.BaseResponse;
import com.markermall.cat.network.Exception.FxEception;
import com.markermall.cat.network.interfaces.IDataSubscriber;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by YangBoTian on 2018/7/4.
 */

public abstract class BaseDataObserver<T> implements Observer<BaseResponse<T>>, IDataSubscriber<T> {
   protected boolean isShowError = true;

    @Override
    public void onSubscribe(Disposable d) {
        doOnSubscribe(d);
    }

    @Override
    public void onNext(BaseResponse<T> baseData) {
        doOnNext(baseData);
    }

    @Override
    public void onError(Throwable e) {
       if (e!=null){
        String error = FxEception.handleException(e,isShowError).getMessage();
        setError(error == null ? "" : error);
       }
    }

    @Override
    public void onComplete() {
        doOnCompleted();
    }


    private void setError(String errorMsg) {
        doOnError(errorMsg);
    }
}
