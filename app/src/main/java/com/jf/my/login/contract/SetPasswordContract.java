package com.jf.my.login.contract;

import com.jf.my.mvp.base.base.BasePresenter;
import com.jf.my.mvp.base.base.BaseRcView;
import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * Created by YangBoTian on 2018/8/8.
 */

public class SetPasswordContract {
    public interface View extends BaseRcView {
        void showData(String data);
        void showFailureMessage(String errorMsg);
    }

    public interface Present extends BasePresenter {
        void setPassword(RxFragment activity, String id, String password, String password2);

    }
}
