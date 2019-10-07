package com.jf.my.login.presenter;

import com.jf.my.Module.common.Utils.LoadingView;
import com.jf.my.login.contract.AreaCodeContract;
import com.jf.my.login.model.LoginModel;
import com.jf.my.mvp.base.frame.MvpPresenter;
import com.jf.my.network.observer.DataObserver;
import com.jf.my.pojo.AreaCodeBean;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.List;

import io.reactivex.functions.Action;

/**
 * @Author: wuchaowen
 * @Description:
 **/
public class AreaCodePresenter extends MvpPresenter<LoginModel, AreaCodeContract.View> implements AreaCodeContract.Present{
    @Override
    public void getCountryList(RxAppCompatActivity activity) {
        LoadingView.showDialog(activity, "请求中...");
        mModel.getCountryList(activity)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                        getIView().showFinally();
                    }
                })
                .subscribe(new DataObserver<List<AreaCodeBean>>() {

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        super.onError(errorMsg, errCode);
                    }

                    @Override
                    protected void onDataNull() {
                        onSuccess(null);
                    }

                    @Override
                    protected void onSuccess(List<AreaCodeBean> data) {
                          getIView().updateAreaCode(data);
                    }

                });
    }
}
