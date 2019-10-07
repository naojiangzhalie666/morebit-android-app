package com.markermall.cat.network.interfaces;


import com.markermall.cat.network.BaseResponse;

import io.reactivex.disposables.Disposable;

/**
 * Created by YangBoTian on 2018/7/4.
 */

public interface  IDataSubscriber<T>  {
    /**
     * doOnSubscribe 回调
     *
     * @param d Disposable
     */
    void doOnSubscribe(Disposable d);

    /**
     * 错误回调
     *
     * @param errorMsg 错误信息
     */
    void doOnError(String errorMsg);

    /**
     * 成功回调
     *
     * @param baseData 基础泛型
     */
    void doOnNext(BaseResponse<T> baseData);

    /**
     * 请求完成回调
     */
    void doOnCompleted();
}
