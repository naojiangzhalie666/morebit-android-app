package com.zjzy.morebit.login.presenter;

import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.login.contract.AreaCodeContract;
import com.zjzy.morebit.login.model.LoginModel;
import com.zjzy.morebit.mvp.base.frame.MvpPresenter;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.AreaCodeBean;
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
