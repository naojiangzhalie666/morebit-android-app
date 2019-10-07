package com.jf.my.utils;

import android.text.TextUtils;

import com.jf.my.App;
import com.jf.my.BuildConfig;
import com.jf.my.LocalData.UserLocalData;
import com.jf.my.pojo.ImageInfo;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * @Author: wuchaowen
 * @Description:
 **/
public class WechatUtil {


    public static void openMiNi(ImageInfo imageInfo) {
        if (!AppUtil.isWeixinAvilible(App.getAppContext())) {
            ViewShowUtils.showShortToast(App.getAppContext(), "请先安装微信客户端");
            return;
        }
        if (imageInfo == null) return;
        String url = imageInfo.getUrl();
        String userName ="";
        if (!TextUtils.isEmpty(imageInfo.getDesc())) {
            userName =imageInfo.getDesc();
        } else {
            //默认蜜源优选
            userName ="gh_f8d561cd1942";
        }
        // 填Android应用AppId
        String appId = "wx92f654d146c13d44";
        final IWXAPI iwxapi = WXAPIFactory.createWXAPI(App.getAppContext(), appId);
        iwxapi.registerApp(appId);
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        if (!TextUtils.isEmpty(url)) {
            if (url.contains("?")) {
                url = url + "&inviteCode=" + UserLocalData.getUser().getInviteCode() + "&appVersion=" + C.Setting.app_version;                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页
            } else {
                url = url + "?inviteCode=" + UserLocalData.getUser().getInviteCode() + "&appVersion=" + C.Setting.app_version;                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页
            }
            req.path = url;
        }
        req.userName = userName;//小程序原始id
        req.miniprogramType = BuildConfig.ENV_TYPE == 4 ? WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE : WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW;
        iwxapi.sendReq(req);
    }
}
