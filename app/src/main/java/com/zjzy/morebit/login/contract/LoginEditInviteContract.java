package com.zjzy.morebit.login.contract;


import com.zjzy.morebit.mvp.base.base.BasePresenter;
import com.zjzy.morebit.pojo.login.InviteUserInfoBean;
import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * Created by fengrs
 * Data: 2018/8/7.
 */
public class LoginEditInviteContract {
    public interface View extends BaseLoginView {
        void setInviteUserInfo(InviteUserInfoBean data);
        void getInviteInfoFail(String msg);
    }

    public interface Present extends BasePresenter {
        void getInviteUserInfo(RxFragment activity, String invite);
    }
}
