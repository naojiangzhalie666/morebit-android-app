package com.markermall.cat.login.presenter;


import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.markermall.cat.Module.common.Utils.LoadingView;
import com.markermall.cat.R;
import com.markermall.cat.login.contract.LoginMainContract;
import com.markermall.cat.login.model.LoginMainModel;
import com.markermall.cat.network.BaseResponse;
import com.markermall.cat.network.CallBackObserver;
import com.markermall.cat.network.RxHttp;
import com.markermall.cat.network.RxUtils;
import com.markermall.cat.network.observer.DataObserver;
import com.markermall.cat.pojo.WeixinInfo;
import com.markermall.cat.pojo.request.RequestCheckOutPhoneBean;
import com.markermall.cat.utils.C;
import com.markermall.cat.utils.LogUtils;
import com.markermall.cat.utils.ViewShowUtils;
import com.mob.tools.utils.UIHandler;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.wechat.friends.Wechat;
import io.reactivex.Observable;
import io.reactivex.functions.Action;


/**
 * Created by fengrs
 * Data: 2018/8/3.
 */
public class LoginMainPresenter extends BaseLoginPresenter<LoginMainModel, LoginMainContract.View> implements LoginMainContract.Present, Handler.Callback, PlatformActionListener {

    private static final int MSG_USERID_FOUND = 1;
    private static final int MSG_LOGIN = 2;
    private static final int MSG_AUTH_CANCEL = 3;
    private static final int MSG_AUTH_ERROR = 4;
    private static final int MSG_AUTH_COMPLETE = 5;
    private RxFragment mRxFragment;
    private WeixinInfo mWeixinInfo;

    @Override
    public void goToWXLogin(RxFragment rxFragment) {
        LoadingView.showDialog(rxFragment.getActivity(), "");
        this.mRxFragment = rxFragment;
        Wechat plat = new Wechat();
        plat.removeAccount(true);
        authorize(plat, rxFragment);
    }

    private void authorize(Platform plat, RxFragment rxFragment) {

        if (plat.isAuthValid()) {
            String userId = plat.getDb().getUserId();
            if (!TextUtils.isEmpty(userId)) {
                Message msg = new Message();
                msg.what = MSG_USERID_FOUND;
                msg.obj = plat;
                UIHandler.sendMessage(msg, this);
//                login(plat.getName(), userId, null);
                return;
            }
        }
        plat.setPlatformActionListener(this);
        plat.SSOSetting(true);
        plat.showUser(null);
    }

    public void onComplete(Platform platform, int action,
                           HashMap<String, Object> res) {
        if (action == Platform.ACTION_USER_INFOR) {
            Message msg = new Message();
            msg.what = MSG_AUTH_COMPLETE;
            msg.obj = platform;
            UIHandler.sendMessage(msg, this);
//            login(platform.getName(), platform.getDb().getUserId(), res);
        }
        LogUtils.Log("ThirdLogin:", platform.getDb().getUserName());
        LogUtils.Log("ThirdLogin:", platform.getDb().getUserId());
        LogUtils.Log("ThirdLogin:", platform.getDb().getUserIcon());

//        System.out.println(res);
//        System.out.println("------User Name ---------" + platform.getDb().getUserName());
//        System.out.println("------User ID ---------" + platform.getDb().getUserId());
    }

    public void onError(Platform platform, int action, Throwable t) {
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_ERROR, this);
        }
        t.printStackTrace();
    }

    public void onCancel(Platform platform, int action) {
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_CANCEL, this);
        }
    }

    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_USERID_FOUND: {

                LogUtils.Log("ThirdLogin:", "userid_found");
                //已获取到用户信息--直接登录
                Platform comPlatform = (Platform) msg.obj;
                getWeixinInfo(comPlatform);
            }
            break;
            case MSG_AUTH_CANCEL: {//取消
                ViewShowUtils.showShortToast(mRxFragment.getActivity(),mRxFragment.getString(R.string.login_error));
                LoadingView.dismissDialog();
//                Toast.makeText(this, R.string.auth_cancel, Toast.LENGTH_SHORT).show();
                LogUtils.Log("ThirdLogin:", "MSG_AUTH_CANCEL");
//                System.out.println("-------MSG_AUTH_CANCEL--------");
            }
            break;
            case MSG_AUTH_ERROR: {
                ViewShowUtils.showShortToast(mRxFragment.getActivity(),mRxFragment.getString(R.string.login_error));
                LoadingView.dismissDialog();
                ViewShowUtils.showLongToast(mRxFragment.getActivity(),"获取微信息失败");
                LogUtils.Log("ThirdLogin:", "MSG_AUTH_ERROR");
            }
            break;
            case MSG_AUTH_COMPLETE: {
//                Toast.makeText(this, R.string.auth_complete, Toast.LENGTH_SHORT).show();
//                System.out.println("--------MSG_AUTH_COMPLETE-------");
                LogUtils.Log("ThirdLogin:", "MSG_AUTH_COMPLETE");
                //帐号密码登录微信成功，返回信息--直接调用登录接口
                Platform comPlatform = (Platform) msg.obj;
                getWeixinInfo(comPlatform);

            }
            break;
        }
        return false;
    }

    /**
     * 获取微信信息
     *
     * @param platform
     */
    private void getWeixinInfo(final Platform platform) {

        LoadingView.showDialog(mRxFragment.getActivity(), "");
        mModel.getWXUserObservable(mRxFragment, platform)
                .subscribe(new CallBackObserver<String>() {
                    @Override
                    public void onNext(String baseResponse) {
                        super.onNext(baseResponse);
                        WeixinInfo weixinInfo = JSON.parseObject(baseResponse, WeixinInfo.class);
                        weixinLogic(weixinInfo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ViewShowUtils.showShortToast(mRxFragment.getActivity(),mRxFragment.getString(R.string.login_error));
                        LoadingView.dismissDialog();
                    }

                });
    }

    /**
     * 微信登录
     *
     * @param weixinInfo
     */
    public void weixinLogic(final WeixinInfo weixinInfo) {
        if (weixinInfo == null) {
            return;
        }
        mWeixinInfo = weixinInfo ;
        mModel.getWXLoginObservable(mRxFragment, weixinInfo)
                .subscribe(getLoginDataObserver(mRxFragment.getActivity()));

    }

    public WeixinInfo getWeixinInfo() {
        return mWeixinInfo;
    }



    /**
     * 校验手机号码
     *
     * @param activity
     * @param phone
     */

    public void checkoutPhone(final RxFragment activity, final String phone, final int type,final String areacode) {
        LoadingView.showDialog(activity.getActivity(), "请求中...");
        getCheckoutPhoneObservable(activity, phone, type,areacode)

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
                        getIView().goToRegister();
                    }
                });
    }


    public Observable<BaseResponse<String>> getCheckoutPhoneObservable(final RxFragment activity, final String phone, int type,String areaCode) {
        LoadingView.showDialog(activity.getActivity(), "请求中...");
        if (type == C.sendCodeType.WEIXINREGISTER) {
            type = C.sendCodeType.REGISTER;
        }

        RequestCheckOutPhoneBean requestBean = new RequestCheckOutPhoneBean();
        requestBean.setPhone(phone);
        requestBean.setType(type);
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
