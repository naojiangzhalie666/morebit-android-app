package com.zjzy.morebit.network;

import android.app.Activity;
import android.view.View;

import com.zjzy.morebit.R;
import com.zjzy.morebit.interfaces.EmptyView;
import com.zjzy.morebit.utils.MyLog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by liuhaiping on 2019/12/25.
 */

public class CommonEmpty {
    @BindView(R.id.empty_view)
    View mEmptyView;
    @BindView(R.id.content)
    View mContentView;
    private boolean mFirstComplete = false;

    class EmptyTransformer<T> implements ObservableTransformer<T, T>,
            SingleTransformer<T, T> {


        @Override
        public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
            return upstream
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doAfterNext(new Consumer<T>() {
                        @Override
                        public void accept(@NonNull T t) throws Exception {
                            BaseResponse b = (BaseResponse) t;
                            doAfterLoad(t);
                        }
                    }).doOnError(new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {
                            MyLog.i("test", "doOnError: " + throwable);
                            doOnError(throwable);
                        }
                    });
        }

        @Override
        public SingleSource<T> apply(@NonNull Single<T> upstream) {
            return upstream
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doAfterSuccess(new Consumer<T>() {
                        @Override
                        public void accept(@NonNull T t) throws Exception {
                            MyLog.i("test", "SingleSourcet: " + t);
                            doAfterLoad(t);
                        }
                    }).doOnError(new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {
                            MyLog.i("test", "SingleSource: " + throwable);
                            doOnError(throwable);
                        }
                    });
        }


        private void doAfterLoad(@NonNull T t) {
            if (!mFirstComplete && (isEmptyData(t))) {
                hide(mContentView).show(mEmptyView);
            } else {
                hide(mEmptyView).show(mContentView);
                mFirstComplete = true;
            }
        }

        private void doOnError(@NonNull Throwable throwable) {
            if (!mFirstComplete) {
                hide(mContentView).show(mEmptyView);
                if (mEmptyView instanceof EmptyView) {
                    ((EmptyView) mEmptyView).onError(throwable);
                }
            }
        }
    }


    protected <T> boolean isEmptyData(@NonNull T t) {
        if (t instanceof BaseResponse) {
            if(((BaseResponse) t).getData()==null){
                return  true;
            } else if (((BaseResponse) t).getData() instanceof List) {
                return ((List) ((BaseResponse) t).getData()).size() == 0;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public CommonEmpty(View view, String message, int drawableId) {

        ButterKnife.bind(this, view);
        if (mEmptyView instanceof EmptyView) {
            ((EmptyView) mEmptyView).setIcon(drawableId);
            ((EmptyView) mEmptyView).setMessage(message);
        }


    }

    public CommonEmpty(Activity activity, String message, int drawableId) {
        ButterKnife.bind(this, activity);
        if (mEmptyView instanceof EmptyView) {
            ((EmptyView) mEmptyView).setIcon(drawableId);
            ((EmptyView) mEmptyView).setMessage(message);
        }

    }


    private CommonEmpty hide(View view) {
        if (view != null) {
            if (view.getVisibility() != View.GONE) {
                view.setVisibility(View.GONE);
            }
        }
        return this;
    }

    private CommonEmpty show(View view) {
        if (view != null) {
            if (view.getVisibility() != View.VISIBLE) {
                view.setVisibility(View.VISIBLE);
            }
        }
        return this;
    }


    public <T> EmptyTransformer<T> bind() {
        return new EmptyTransformer<>();
    }

    public boolean ismFirstComplete() {
        return mFirstComplete;
    }

    public void setmFirstComplete(boolean mFirstComplete) {
        this.mFirstComplete = mFirstComplete;
    }


}
