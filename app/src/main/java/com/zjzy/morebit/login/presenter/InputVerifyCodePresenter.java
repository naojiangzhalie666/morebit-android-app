package com.zjzy.morebit.login.presenter;


import com.zjzy.morebit.login.contract.InputVerifyCodeContract;
import com.zjzy.morebit.login.model.LoginModel;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.WeixinInfo;
import com.zjzy.morebit.utils.AppUtil;
import com.trello.rxlifecycle2.components.support.RxFragment;

import io.reactivex.functions.Action;

/**
 * Created by YangBoTian on 2018/8/8
 */
public class InputVerifyCodePresenter extends BaseSendCodePresenter<LoginModel, InputVerifyCodeContract.View> implements InputVerifyCodeContract.Present {
    private int page = 1;


    @Override
    public void register(RxFragment baseFragment, String phone, String verifyCode, String invitationCode, String areaCode,String oauthWx) {
        if (AppUtil.isFastCashMoneyClick(500)) {
            return;
        }
        mModel.register(baseFragment, phone, verifyCode, invitationCode, areaCode,oauthWx)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getIView().showRegisterFinally();
                    }
                }).subscribe(new DataObserver<UserInfo>() {
            @Override
            protected void onSuccess(UserInfo data) {
                getIView().showRegisterData(data);
            }

            @Override
            protected void onError(String errorMsg, String errCode) {
                getIView().showRegisterFailure(errCode);
            }
        });
    }

    @Override
    public void login(RxFragment baseFragment, String phone, String verifyCode) {
        mModel.login(baseFragment, phone, verifyCode)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getIView().showFinally();
                    }
                })
                .subscribe(getLoginDataObserver(baseFragment.getActivity()));
    }

    @Override
    public void weixinRegister(final RxFragment baseFragment, final String phone, String yqm_code, String verifyCode, WeixinInfo weixinInfo) {
        if (AppUtil.isFastCashMoneyClick(500)) {
            return;
        }

        mModel.getWeixinRegister(baseFragment, phone, yqm_code, verifyCode, weixinInfo)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getIView().showFinally();
                    }
                })
                .subscribe(new DataObserver<UserInfo>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        getIView().showRegisterFailure(errCode);
                    }

                    @Override
                    protected void onSuccess(UserInfo data) {
                        getIView().showRegisterData(data);
                    }
                });

    }

    @Override
    public void weixinLogin(RxFragment baseFragment, String phone, String verifyCode, WeixinInfo weixinInfo) {
        if (AppUtil.isFastCashMoneyClick(500)) {
            return;
        }
        mModel.getWeixinLogin(baseFragment, phone, verifyCode, weixinInfo)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getIView().showFinally();
                    }
                })
                .subscribe(new DataObserver<UserInfo>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        getIView().showLoginFailure(errCode);
                    }

                    @Override
                    protected void onSuccess(UserInfo data) {
                        getIView().showLoginData(data);
                    }
                });
    }
}
