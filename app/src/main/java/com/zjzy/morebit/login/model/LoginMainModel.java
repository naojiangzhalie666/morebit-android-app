package com.zjzy.morebit.login.model;


import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.mvp.base.frame.MvpModel;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.RxWXHttp;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.WeixinInfo;
import com.zjzy.morebit.pojo.request.RequestWeixiLoginBean;
import com.zjzy.morebit.utils.encrypt.EncryptUtlis;
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
