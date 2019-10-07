package com.markermall.cat.mvp.base.frame;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.markermall.cat.Module.common.Activity.BaseActivity;
import com.markermall.cat.mvp.base.base.BaseView;


/**
 * Created by fengrs
 * Data: 2018/8/4.
 */
public abstract class MvpActivity<P extends MvpPresenter> extends BaseActivity implements MvpView {
    public P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getViewLayout());
        initMvp();
    }


    public void initMvp() {
        Mvp mvp = Mvp.getInstance();
        mvp.registerView(this.getClass(), this);
        mPresenter = (P) mvp.getPresenter(Mvp.getGenericType(this, 0), this);
        mPresenter.initPresenter(getBaseView());
    }

    /**
     * 确定IView类型
     *
     * @return
     */
    public abstract BaseView getBaseView();

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Mvp.getInstance().unRegister(this.getClass(),this);
        mPresenter.destory();
    }

    protected abstract int getViewLayout();
}
