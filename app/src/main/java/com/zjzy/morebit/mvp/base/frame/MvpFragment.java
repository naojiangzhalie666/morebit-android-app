package com.zjzy.morebit.mvp.base.frame;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zjzy.morebit.fragment.base.BaseMainFragmeng;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.utils.MyLog;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by fengrs
 * Data: 2018/8/4.
 */
public abstract class MvpFragment<P extends MvpPresenter> extends BaseMainFragmeng implements MvpView {
    public P mPresenter;
    protected View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getViewLayout(), container, false);
        initMvp();
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
    }

    protected abstract void initData();

    protected abstract void initView(View view);

    /**
     * 获取资源ID
     *
     * @return
     */
    protected abstract int getViewLayout();


    public void initMvp() {
        Mvp mvp = Mvp.getInstance();
        mvp.registerView(this.getClass(), this);
        mPresenter = (P) mvp.getPresenter(Mvp.getGenericType(this, 0), this);
        MyLog.i("MvpFragment", "mPresenter  " + mPresenter.hashCode());
        mPresenter.initPresenter(getBaseView());
    }

    public abstract BaseView getBaseView();

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        Mvp.getInstance().unRegister(this.getClass(), this);
        if (mPresenter != null) {
            mPresenter.destory();
        }
    }


}
