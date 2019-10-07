package com.jf.my.login.model;


import com.jf.my.Module.common.Utils.LoadingView;
import com.jf.my.mvp.base.frame.MvpModel;
import com.jf.my.network.BaseResponse;
import com.jf.my.network.RxHttp;
import com.jf.my.network.RxUtils;
import com.jf.my.network.RxWXHttp;
import com.jf.my.pojo.UserInfo;
import com.jf.my.pojo.WeixinInfo;
import com.jf.my.pojo.request.RequestWeixiLoginBean;
import com.jf.my.utils.encrypt.EncryptUtlis;
import com.trello.rxlifecycle2.components.support.RxFragment;

import cn.sharesdk.framework.Platform;
import io.reactivex.Observable;
import io.reactivex.functions.Action;

/**
 * Created by fengrs
 * Data: 2018/8/7.
 */
public class LoginMainModel extends MvpModel {
//    http://192.168.1.186/miyuan/miyuan-android-app.git

    public Observable<String> getWXUserObservable(RxFragment rxFragment, Platform platform) {
        return RxWXHttp.getInstance().getService("https://api.weixin.qq.com").getWxUserInfo(platform.getDb().getToken(), platform.getDb().getUserId())
                .compose(RxUtils.<String>switchSchedulers())
                .compose(rxFragment.<String>bindToLifecycle());
    }

    public Observable<BaseResponse<UserInfo>> getWXLoginObservable(RxFragment rxFragment, WeixinInfo weixinInfo) {

        RequestWeixiLoginBean requestBean = new RequestWeixiLoginBean();
        requestBean.setOauthWx(weixinInfo.getOpenid());
        String sign = EncryptUtlis.getSign2(requestBean);
        requestBean.setSign(sign);
        return RxHttp.getInstance().getUsersService()
//                .getWeixiLogin(weixinInfo.getOpenid(),"")
                .getWeixiLogin(requestBean)
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
