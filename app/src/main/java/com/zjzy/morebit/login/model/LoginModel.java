package com.zjzy.morebit.login.model;


import com.zjzy.morebit.mvp.base.frame.MvpModel;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.pojo.AreaCodeBean;
import com.zjzy.morebit.pojo.TeamInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.WeixinInfo;
import com.zjzy.morebit.pojo.request.RequestLoginCodeBean;
import com.zjzy.morebit.pojo.request.RequestRegisterAndBindWeChatBean;
import com.zjzy.morebit.pojo.request.RequestRegisterloginBean;
import com.zjzy.morebit.pojo.request.RequestSetPasswordBean;
import com.zjzy.morebit.pojo.requestbodybean.RequestInviteCodeBean;
import com.zjzy.morebit.utils.encrypt.EncryptUtlis;
import com.zjzy.morebit.utils.encrypt.MD5Utils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by fengrs
 * Data: 2018/8/3.
 */
public class LoginModel extends MvpModel {

    public Observable<BaseResponse<UserInfo>> register(RxFragment baseFragment, String phone, String verifyCode, String invitationCode,String areaCode) {

        RequestRegisterloginBean requestBean = new RequestRegisterloginBean();
        requestBean.setPhone(phone);
        requestBean.setVerCode(verifyCode);
        requestBean.setYqmCodeOrPhone(invitationCode);
        requestBean.setAreaCode(areaCode);
        requestBean.setSign(EncryptUtlis.getSign2(requestBean));

        return RxHttp.getInstance().getUsersService()
                .getRegisterlogin(requestBean)
                .compose(RxUtils.<BaseResponse<UserInfo>>switchSchedulers())
                .compose(baseFragment.<BaseResponse<UserInfo>>bindToLifecycle());

    }

    public Observable<BaseResponse<TeamInfo>> getServiceQrcode(RxFragment activity, String id) {

        return RxHttp.getInstance().getUsersService().getServiceQrcode(new RequestInviteCodeBean().setWxShowType(2))
                .compose(RxUtils.<BaseResponse<TeamInfo>>switchSchedulers())
                .compose(activity.<BaseResponse<TeamInfo>>bindToLifecycle());

    }

    public Observable<BaseResponse<String>> setPassword(RxFragment activity, String password) {
        String pwdMD5 = MD5Utils.getPsdMD5(password);

        RequestSetPasswordBean requestBean = new RequestSetPasswordBean();
        requestBean.setPassword(pwdMD5);
        requestBean.setSign(password);
        String sign = EncryptUtlis.getSign2(requestBean);
        requestBean.setSign(sign);
        return RxHttp.getInstance().getUsersService()
//                .getRegistrationPassword( pwdMD5 ,password)
                .getRegistrationPassword(requestBean)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(activity.<BaseResponse<String>>bindToLifecycle());
    }

    public Observable<BaseResponse<UserInfo>> login(RxFragment fragment, String phone, String verifyCode) {

        RequestLoginCodeBean requestBean = new RequestLoginCodeBean();
        requestBean.setPhone(phone);
        requestBean.setVerCode(verifyCode);
        String sign = EncryptUtlis.getSign2(requestBean);
        requestBean.setSign(sign);
        return RxHttp.getInstance().getUsersService()
//                .getLoginCode( phone, verifyCode,"" )
                .getLoginCode(requestBean)
                .compose(RxUtils.<BaseResponse<UserInfo>>switchSchedulers())
                .compose(fragment.<BaseResponse<UserInfo>>bindToLifecycle());

    }

    public Observable<BaseResponse<UserInfo>> getWeixinRegister(RxFragment baseFragment, String phone, String yqm_code, String verifyCode, WeixinInfo weixinInfo) {

        RequestRegisterAndBindWeChatBean requestBean = new RequestRegisterAndBindWeChatBean();
        requestBean.setPhone(phone);
        requestBean.setVerCode(verifyCode);
        requestBean.setOauthWx(weixinInfo.getOpenid());
        requestBean.setYqmCodeOrPhone(yqm_code);
        requestBean.setNickname(weixinInfo.getNickname());
        requestBean.setSex(weixinInfo.getSex() + "");
        requestBean.setHeadImg(weixinInfo.getHeadimgurl());
        requestBean.setCountry(weixinInfo.getCountry());
        requestBean.setProvince(weixinInfo.getProvince());
        requestBean.setCity(weixinInfo.getCity());
        requestBean.setSign(EncryptUtlis.getSign2(requestBean));


        return RxHttp.getInstance().getCommonService().registerAndBindWeixin(
                requestBean
//                phone,
//                verifyCode,
//                weixinInfo.getOpenid(),
//                yqm_code,
//                weixinInfo.getNickName(),
//                weixinInfo.getSex() + "",
//                weixinInfo.getHeadimgurl(),
//                weixinInfo.getCountry(),
//                weixinInfo.getProvince(),
//                weixinInfo.getCity(),
//                ""
        )
                .compose(RxUtils.<BaseResponse<UserInfo>>switchSchedulers())
                .compose(baseFragment.<BaseResponse<UserInfo>>bindToLifecycle());
    }


    public Observable<BaseResponse<List<AreaCodeBean>>> getCountryList(RxAppCompatActivity baseFragment) {
        return RxHttp.getInstance().getUsersService()
                .getCountryList()
                .compose(RxUtils.<BaseResponse<List<AreaCodeBean>>>switchSchedulers())
                .compose(baseFragment.<BaseResponse<List<AreaCodeBean>>>bindToLifecycle());

    }
}
