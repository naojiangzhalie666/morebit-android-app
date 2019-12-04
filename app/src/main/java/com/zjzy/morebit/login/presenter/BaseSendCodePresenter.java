package com.zjzy.morebit.login.presenter;


import android.text.TextUtils;

import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.login.contract.BaseLoginView;
import com.zjzy.morebit.login.contract.LoginEditPhoneContract;
import com.zjzy.morebit.mvp.base.frame.MvpModel;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.request.RequestAuthCodeBean;
import com.zjzy.morebit.pojo.request.RequestCheckOutPhoneBean;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.trello.rxlifecycle2.components.support.RxFragment;

import io.reactivex.Observable;
import io.reactivex.functions.Action;


/**
 * Created by fengrs
 * Data: 2018/8/3.
 */
public class BaseSendCodePresenter<M extends MvpModel, V extends BaseLoginView> extends BaseLoginPresenter<M, V> implements LoginEditPhoneContract.Present {


    @Override
    public void getsendCode(final RxFragment activity, String phone, int type,String areaCode) {
        if (type == C.sendCodeType.WEIXINREGISTER) {
            type = C.sendCodeType.REGISTER;
        }
        LoadingView.showDialog(activity.getActivity(), "请求中...");
        if (!LoginUtil.isMobile(phone)) {
            ViewShowUtils.showShortToast(activity.getActivity(), "请输入正确的手机号");
            return;
        }

        RequestAuthCodeBean requestBean = new RequestAuthCodeBean();
        requestBean.setPhone(phone);
        requestBean.setType(type);

        //这三种类型要传地区  1：登录 2.修改密码 6 。注册
        if(type == C.sendCodeType.LOGIN || type == C.sendCodeType.REGISTER || type == C.sendCodeType.REVAMPPWD){
           if(!TextUtils.isEmpty(areaCode)) requestBean.setAreaCode(areaCode);
        }

        RxHttp.getInstance().getUsersService()
//                .sendAuthCode(phone, type)
                .sendAuthCode(requestBean)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(activity.<BaseResponse<String>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                })
                .subscribe(new DataObserver<String>() {
                    @Override
                    protected void onDataNull() {
                        onSuccess("");
                    }

                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        getIView().sendCodeFail();
                    }

                    @Override
                    protected void onSuccess(String data) {
                        getIView().sendCodeSuccess("发送成功");
                    }
                });
    }

    /**
     * 校验手机号码
     *
     * @param activity
     * @param phone
     */
    @Override
    public void checkoutPhone(final RxFragment activity, final String phone, final int type,final String areaCode) {
        LoadingView.showDialog(activity.getActivity(), "请求中...");
        getCheckoutPhoneObservable(activity, phone, type,areaCode)

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
                        getsendCode(activity, phone, type,areaCode);
                    }
                });
    }

    public Observable<BaseResponse<String>> getCheckoutPhoneObservable(final RxFragment activity, final String phone, int type, String areaCode) {
        if (type == C.sendCodeType.WEIXINREGISTER) {
            type = C.sendCodeType.REGISTER;
        }

        RequestCheckOutPhoneBean requestBean = new RequestCheckOutPhoneBean();
        requestBean.setPhone(phone);
        requestBean.setType(type);
        if(TextUtils.isEmpty(areaCode)){
            areaCode = "86";
        }
        requestBean.setAreaCode(areaCode);

        return RxHttp.getInstance().getUsersService()
                .checkoutPhone(requestBean)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(activity.<BaseResponse<String>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                });
    }
}
