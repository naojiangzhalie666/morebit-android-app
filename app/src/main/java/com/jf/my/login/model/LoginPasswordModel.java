package com.jf.my.login.model;


import com.jf.my.Module.common.Utils.LoadingView;
import com.jf.my.mvp.base.frame.MvpModel;
import com.jf.my.network.BaseResponse;
import com.jf.my.network.RxHttp;
import com.jf.my.network.RxUtils;
import com.jf.my.pojo.UserInfo;
import com.jf.my.pojo.request.RequestLoginBean;
import com.jf.my.utils.encrypt.EncryptUtlis;
import com.jf.my.utils.encrypt.MD5Utils;
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
