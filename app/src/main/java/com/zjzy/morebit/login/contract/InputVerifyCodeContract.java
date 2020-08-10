package com.zjzy.morebit.login.contract;


import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.WeixinInfo;
import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * Created by YangBoTian on 2018/8/8.
 */
public class InputVerifyCodeContract {
    public interface View extends BaseLoginView {
        void showData(String msg);
        void showFinally();
        void showFailureMessage(String errorMsg);
        void showRegisterData(UserInfo userInfo);
        void showRegisterFinally();
        void showRegisterFailure(String errCode);
        void showLoginFailure(String errCode);
        void showLoginData(UserInfo userInfo);

    }

    public interface Present extends LoginBaseSendCodePresent {
        void register(RxFragment baseFragment,String phone, String verifyCode , String invitationCode,String areaCode,String oauthWx);
        void login(RxFragment baseFragment,String phone,String verifyCode);
        void weixinRegister(RxFragment baseFragment, String s, String phone, String verifyCode, WeixinInfo weixinInfo,String areaCode);
        void weixinLogin(RxFragment baseFragment, String phone, String verifyCode, WeixinInfo weixinInfo,String areaCode);
    }
}
