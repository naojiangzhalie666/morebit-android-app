package com.zjzy.morebit.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.widget.Toast;

import com.zjzy.morebit.App;
import com.zjzy.morebit.BuildConfig;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.pojo.ImageInfo;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.File;

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
            //默认多点优选优选
            userName ="gh_f8d561cd1942";
        }
        // 填Android应用AppId
        String appId = "wx0d185820ca66c15b";
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
        req.miniprogramType = BuildConfig.ENV_TYPE == 3 ? WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE : WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW;
        iwxapi.sendReq(req);
    }

}
