package com.zjzy.morebit.login.presenter;


import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.login.contract.LoginEditInviteContract;
import com.zjzy.morebit.mvp.base.frame.MvpModel;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.login.InviteUserInfoBean;
import com.zjzy.morebit.pojo.request.RequestUserInfoBean;
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
