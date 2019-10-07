package com.jf.my.login.presenter;


import com.jf.my.Module.common.Utils.LoadingView;
import com.jf.my.login.contract.LoginEditInviteContract;
import com.jf.my.mvp.base.frame.MvpModel;
import com.jf.my.network.BaseResponse;
import com.jf.my.network.RxHttp;
import com.jf.my.network.RxUtils;
import com.jf.my.network.observer.DataObserver;
import com.jf.my.pojo.login.InviteUserInfoBean;
import com.jf.my.pojo.request.RequestUserInfoBean;
import com.trello.rxlifecycle2.components.support.RxFragment;

import io.reactivex.functions.Action;


/**
 * Created by fengrs
 * Data: 2018/8/3.
 */
public class LoginEditInvitePresenter extends BaseLoginPresenter<MvpModel, LoginEditInviteContract.View> implements LoginEditInviteContract.Present {


    @Override
    public void getInviteUserInfo(RxFragment fragment, String invite) {
        //LoadingView.showDialog(fragment.getActivity());

        RequestUserInfoBean requestBean = new RequestUserInfoBean();
        requestBean.setInviteCodeOrPhone(invite);

        RxHttp.getInstance().getUsersService()
//                .getInviteUserInfo(invite)
                .getInviteUserInfo(requestBean)
                .compose(RxUtils.<BaseResponse<InviteUserInfoBean>>switchSchedulers())
                .compose(fragment.<BaseResponse<InviteUserInfoBean>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                }).subscribe(new DataObserver<InviteUserInfoBean>() {
            @Override
            protected void onError(String errorMsg, String errCode) {
                getIView().getInviteInfoFail(errorMsg);
            }

            @Override
            protected void onSuccess(InviteUserInfoBean data) {
                getIView().setInviteUserInfo(data);
            }
        });

    }

}
