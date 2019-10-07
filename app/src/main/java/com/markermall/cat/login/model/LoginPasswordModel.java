package com.markermall.cat.login.model;


import com.markermall.cat.Module.common.Utils.LoadingView;
import com.markermall.cat.mvp.base.frame.MvpModel;
import com.markermall.cat.network.BaseResponse;
import com.markermall.cat.network.RxHttp;
import com.markermall.cat.network.RxUtils;
import com.markermall.cat.pojo.UserInfo;
import com.markermall.cat.pojo.request.RequestLoginBean;
import com.markermall.cat.utils.encrypt.EncryptUtlis;
import com.markermall.cat.utils.encrypt.MD5Utils;
import com.trello.rxlifecycle2.components.support.RxFragment;

import io.reactivex.Observable;
import io.reactivex.functions.Action;

/**
 * Created by fengrs
 * Data: 2018/8/7.
 */
public class LoginPasswordModel extends MvpModel {

    public Observable getPwdLoginObservable(RxFragment rxFragment, String phone, String pwd,String areaCode) {
        String pwdMD5 = MD5Utils.getPsdMD5(pwd);

        RequestLoginBean requestBean = new RequestLoginBean();
        requestBean.setPhone(phone);
        requestBean.setPassword(pwdMD5);
        requestBean.setSign(EncryptUtlis.getSign2(requestBean));
        requestBean.setAreaCode(areaCode);

        return RxHttp.getInstance().getUsersService()
                .getLogin(requestBean)
                .compose(RxUtils.<BaseResponse<UserInfo>>switchSchedulers())
                .compose(rxFragment.<BaseResponse<UserInfo>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                });
    }
}
