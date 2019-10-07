package com.jf.my.login.presenter;


import com.jf.my.Module.common.Utils.LoadingView;
import com.jf.my.login.contract.LoginPasswordContract;
import com.jf.my.login.model.LoginPasswordModel;
import com.jf.my.network.observer.DataObserver;
import com.jf.my.utils.C;
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
