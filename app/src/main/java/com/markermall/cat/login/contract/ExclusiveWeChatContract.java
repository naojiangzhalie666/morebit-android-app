package com.markermall.cat.login.contract;


import com.markermall.cat.mvp.base.base.BasePresenter;
import com.markermall.cat.mvp.base.base.BaseRcView;
import com.markermall.cat.pojo.TeamInfo;
import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * Created by YangBoTian on 2018/8/7.
 */
public class ExclusiveWeChatContract {
    public interface View extends BaseRcView {
        void showData(TeamInfo exclusiveWechat);
        void showFailureMessage(String errorMsg);
    }

    public interface Present extends BasePresenter {
        void getServiceQrcode(RxFragment activity, String id);

    }
}
