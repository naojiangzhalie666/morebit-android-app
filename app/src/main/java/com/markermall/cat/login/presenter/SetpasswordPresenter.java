package com.markermall.cat.login.presenter;

import com.markermall.cat.login.contract.SetPasswordContract;
import com.markermall.cat.login.model.LoginModel;
import com.markermall.cat.mvp.base.frame.MvpPresenter;
import com.markermall.cat.network.observer.DataObserver;
import com.trello.rxlifecycle2.components.support.RxFragment;

import io.reactivex.functions.Action;

/**
 * Created by YangBoTian on 2018/8/8.
 */

public class SetpasswordPresenter extends MvpPresenter<LoginModel, SetPasswordContract.View> implements SetPasswordContract.Present {
    @Override
    public void setPassword(RxFragment activity, String id, String password, String password2) {

        mModel.setPassword(activity, password)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getIView().showFinally();
                    }
                })
                .subscribe(new DataObserver<String>() {
                    @Override
                    protected void onDataNull() {
                        onSuccess("");
                    }

                    @Override
                    protected void onSuccess(String data) {
                        getIView().showData("设置成功");
                    }

                });
    }




}
