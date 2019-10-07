package com.jf.my.network;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by YangBoTian on 2018/6/7 14:39
 */

public class RxUtils {



    /**
     * 对RESTful返回结果做预处理，对逻辑错误抛出异常
     *
     * @param <T>
     * @return
     */
    public static <T> Function<BaseResponse<T>, T> handleRESTFulResult() {
        return  new Function<BaseResponse<T>, T>() {
            @Override
            public T apply(@NonNull BaseResponse<T> tResponse) throws Exception {
//                Log.i("test","tResponse: "+tResponse);
//                Log.i("test","tResponse.getData(): "+tResponse.getData());
                return tResponse.getData();
            }
        };
    }

    /**
     * @param <T> 泛型
     * @return 返回Observable
     */
    public static <T> ObservableTransformer<T, T> switchSchedulers() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(@NonNull Disposable disposable) throws Exception {

                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

}
