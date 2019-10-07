package com.markermall.cat.login.presenter;


import com.markermall.cat.Module.common.Utils.LoadingView;
import com.markermall.cat.login.contract.LoginPasswordContract;
import com.markermall.cat.login.model.LoginPasswordModel;
import com.markermall.cat.network.observer.DataObserver;
import com.markermall.cat.utils.C;
import com.trello.rxlifecycle2.components.support.RxFragment;


/**
 * Created by fengrs
 * Data: 2018/8/8.
 */
public class LoginPasswordPresenter extends BaseSendCodePresenter<LoginPasswordModel, LoginPasswordContract.View> implements LoginPasswordContract.Present {

    @Override
    public void Passwordlogin(final RxFragment rxFragment, final String phone, final String pwd,final String areacode) {
        LoadingView.showDialog(rxFragment.getActivity(), "请求中...");
        getCheckoutPhoneObservable(rxFragment, phone, C.sendCodeType.LOGIN,areacode)
                .subscribe(new DataObserver<String>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        getIView().loginError(errCode);
                    }

                    @Override
                    protected void onDataNull() {
                        onSuccess("");
                    }

                    @Override
                    protected void onSuccess(String data) {
                        mModel.getPwdLoginObservable(rxFragment, phone, pwd,areacode)
                                .subscribe(getLoginDataObserver(rxFragment.getActivity()));
                    }
                });
    }


}
