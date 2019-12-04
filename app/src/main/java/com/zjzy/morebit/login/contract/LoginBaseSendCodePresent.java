package com.zjzy.morebit.login.contract;


import com.zjzy.morebit.mvp.base.base.BasePresenter;
import com.trello.rxlifecycle2.components.support.RxFragment;

public interface LoginBaseSendCodePresent extends BasePresenter {
        void getsendCode(RxFragment activity, String phone,int type,String areaCode);

    /**
     * 校验手机号码
     * @param activity
     * @param phone
     */
        void checkoutPhone(RxFragment activity, String phone,int type,String areaCode);
    }