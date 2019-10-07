package com.markermall.cat.login.presenter;


import com.markermall.cat.login.contract.ExclusiveWeChatContract;
import com.markermall.cat.login.model.LoginModel;
import com.markermall.cat.mvp.base.frame.MvpPresenter;
import com.markermall.cat.network.observer.DataObserver;
import com.markermall.cat.pojo.TeamInfo;
import com.trello.rxlifecycle2.components.support.RxFragment;

import io.reactivex.functions.Action;

/**
 * Created by YangBoTian on 2018/8/8
 */
public class ExclusiveWeChatPresenter extends MvpPresenter<LoginModel, ExclusiveWeChatContract.View> implements ExclusiveWeChatContract.Present {
    private int page = 1;


    @Override
    public void getServiceQrcode(RxFragment activity, String id) {
        mModel.getServiceQrcode(activity,id)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getIView().showFinally();
                    }
                }).subscribe(new DataObserver<TeamInfo>() {
            @Override
            protected void onSuccess(TeamInfo data) {
                getIView().showData(data);
            }

        });
    }
}
