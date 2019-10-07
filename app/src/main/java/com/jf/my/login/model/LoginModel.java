package com.jf.my.login.model;


import com.jf.my.mvp.base.frame.MvpModel;
import com.jf.my.network.BaseResponse;
import com.jf.my.network.RxHttp;
import com.jf.my.network.RxUtils;
import com.jf.my.pojo.AreaCodeBean;
import com.jf.my.pojo.TeamInfo;
import com.jf.my.pojo.UserInfo;
import com.jf.my.pojo.WeixinInfo;
import com.jf.my.pojo.request.RequestLoginCodeBean;
import com.jf.my.pojo.request.RequestRegisterAndBindWeChatBean;
import com.jf.my.pojo.request.RequestRegisterloginBean;
import com.jf.my.pojo.request.RequestSetPasswordBean;
import com.jf.my.pojo.requestbodybean.RequestInviteCodeBean;
import com.jf.my.utils.encrypt.EncryptUtlis;
import com.jf.my.utils.encrypt.MD5Utils;
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
