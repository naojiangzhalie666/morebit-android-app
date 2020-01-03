package com.zjzy.morebit.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;

import com.ali.auth.third.core.model.Session;
import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcDetailPage;
import com.alibaba.baichuan.trade.biz.AlibcConstants;
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams;
import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.zjzy.morebit.Activity.ChannelWebActivity;
import com.zjzy.morebit.Activity.ShowWebActivity;
import com.zjzy.morebit.Activity.WebViewActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.MainActivity;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Dialog.AuthorDialog;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.HotKeywords;
import com.zjzy.morebit.pojo.request.RequestBaseBean;
import com.zjzy.morebit.pojo.requestbodybean.RequestKeyBean;
import com.zjzy.morebit.utils.fire.BuglyUtils;

import java.sql.ClientInfoStatus;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/10/24.
 */

public class TaobaoUtil {

    /**
     * 打开指定链接
     */
    public static void showUrl(Activity activity, String url) {
        try {
            if (TextUtils.isEmpty(url)) {
                return;
            }
            // 为了淘宝安全,清空剪切板
            AppUtil.coayText(activity, "");
            if (!AppUtil.isTaobaoClientAvailable(activity)) {
                try {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.setAction("Android.intent.action.VIEW");
                    Uri uri = Uri.parse(url); // 商品地址
                    intent.setData(uri);
                    ComponentName cmp = new ComponentName("com.taobao.taobao", "com.taobao.browser.BrowserActivity");
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setComponent(cmp);
                    activity.startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();

                    ShowWebActivity.start(activity, url, "粉丝福利购");
                }
            } else {
                //阿里百川4.0升级修改---开始
                AlibcShowParams alibcShowParams = new AlibcShowParams();
                alibcShowParams.setOpenType(OpenType.Native);
                alibcShowParams.setClientType("taobao");
                alibcShowParams.setBackUrl("alisdk://");
                AlibcTaokeParams taokeParams = new AlibcTaokeParams("", "", "");

                //taokeParams.setPid("mm_112883640_11584347_72287650277");
                //taokeParams.setAdzoneid("72287650277");
                //adzoneid是需要taokeAppkey参数才可以转链成功&店铺页面需要卖家id（sellerId），具体设置方式如下：
                //taokeParams.extraParams.put("taokeAppkey", "xxxxx");
                //taokeParams.extraParams.put("sellerId", "xxxxx");
                HashMap exParams = new HashMap<>();
                exParams.put("isv_code", "appisvcode");
                exParams.put("alibaba", "阿里巴巴");//自定义参数部分，可任意增删改
                AlibcTrade.openByUrl(activity, "", url, null,
                        new WebViewClient(), new WebChromeClient(), alibcShowParams,
                        taokeParams, exParams, new DemoTradeCallback());
//                AlibcTrade.show(activity, new AlibcPage(url), alibcShowParams, null, exParams, new DemoTradeCallback());
                // 阿里百川4.0升级修改---结束
                BuglyUtils.setUserSceneTag(activity, C.SceneTag.goTaobaoTag);

            }
        } catch (Exception e) {
            e.printStackTrace();
            ShowWebActivity.start(activity, url, "粉丝福利购");
        }
    }

    public static void showByItemId(Activity activity,String itemId){
        AlibcBasePage page = new AlibcDetailPage(itemId);

        AlibcShowParams alibcShowParams = new AlibcShowParams();
        alibcShowParams.setOpenType(OpenType.Native);
        alibcShowParams.setClientType("taobao");
        alibcShowParams.setBackUrl("alisdk://");
        AlibcTaokeParams taokeParams = new AlibcTaokeParams("", "", "");
        taokeParams.setPid("mm_16333633_323700308_99561650135");
        taokeParams.setAdzoneid("99561650135");
        HashMap exParams = new HashMap<>();
        exParams.put(AlibcConstants.ISV_CODE, "appisvcode");
        exParams.put(AlibcConstants.TAOKE_APPKEY, "25606252");
        exParams.put("alibaba", "阿里巴巴");//自定义参数部分，可任意增删改
        AlibcTrade.openByBizCode(activity, page, null, new WebViewClient(),
                new WebChromeClient(), "detail", alibcShowParams, taokeParams,
                exParams, new DemoTradeCallback());
        BuglyUtils.setUserSceneTag(activity, C.SceneTag.goTaobaoTag);
    }

    public static void authTaobao(final Activity activity, final String client_id) {
        final AlibcLogin alibcLogin = AlibcLogin.getInstance();
        alibcLogin.showLogin(new AlibcLoginCallback() {
            @Override
            public void onSuccess(int loginResult, String openId, String userNick) {
                // 参数说明：
                // loginResult(0--登录初始化成功；1--登录初始化完成；2--登录成功)
                // openId：用户id
                // userNick: 用户昵称
                MyLog.d("test","loginresult:"+loginResult+"openId:"+openId+"userNick:"+userNick);
//                Intent intent = new Intent(activity, WebViewActivity.class);
//                intent.putExtra("url", "https://oauth.taobao.com/authorize?response_type=code&client_id=" + client_id + "&redirect_uri=http://127.0.0.1:12345/error&state=1212&view=wap");
//                activity.startActivity(intent);
                ChannelWebActivity.start(activity, "https://oauth.taobao.com/authorize?response_type=code&client_id=" + client_id + "&redirect_uri=http://127.0.0.1:12345/error&state=1212&view=wap");
//                ChannelWebActivity.start(activity, "https://oauth.taobao.com/authorize?response_type=code&client_id=" + client_id + "&redirect_uri=http://tkzs.jrsqrj.com/api/oauth&state=1212&view=wap");
//
            }

//            @Override
//            public void onSuccess(int i) {
//                MyLog.i("test", "onSuccess: " + alibcLogin.getSession().toString());
//                ChannelWebActivity.start(activity, "https://oauth.taobao.com/authorize?response_type=code&client_id=" + client_id + "&redirect_uri=http://127.0.0.1:12345/error&state=1212&view=wap");
//            }

            @Override
            public void onFailure(int code, String msg) {
                MyLog.i("test", "msg: " + msg);
            }
        });
    }


    /**
     * 是否需要授权  false 否,true是
     *
     * @return
     */
    public static boolean isAuth() {
        return UserLocalData.getUser().isNeedAuth();
    }


    /**
     * 淘宝授权登录
     */
    public static void authTaobao(AlibcLoginCallback alibcLoginCallback) {
        AlibcLogin alibcLogin = AlibcLogin.getInstance();
        alibcLogin.showLogin(alibcLoginCallback);
    }


    /**
     * 退出淘宝登录
     *
     * @param
     */
    public static void logoutTaobao(AlibcLoginCallback alibcLoginCallback) {

        AlibcLogin alibcLogin = AlibcLogin.getInstance();
        if (alibcLoginCallback == null) {
            alibcLogin.logout(new AlibcLoginCallback() {
                @Override
                public void onSuccess(int loginResult, String openId, String userNick) {
                    // 参数说明：
                    // loginResult(0--登录初始化成功；1--登录初始化完成；2--登录成功)
                    // openId：用户id
                    // userNick: 用户昵称
                }
                @Override
                public void onFailure(int i, String s) {

                }
            });
        } else { //回调自己处理
            alibcLogin.logout(alibcLoginCallback);
        }
    }

    /**
     * 渠道id授权
     *
     * @param activity
     */
    public static void getAllianceAppKey(final BaseActivity activity) {
        getAllianceAppKey(activity, true);
    }

    /**
     * 获取淘宝userid
     *
     * @return
     */
    public static String getTaobaoUserID() {
        Session session = AlibcLogin.getInstance().getSession();
        if (session == null) {
            return "";
        } else {
            return session.userid;
        }
    }

    public static void getAllianceAppKey(final BaseActivity activity, boolean isFastClick) {
        if (isFastClick && AppUtil.isFastClick(50)) return;
        AuthorDialog dialog = new AuthorDialog(activity);
        dialog.setmCancelListener(new AuthorDialog.OnCancelListner() {
            @Override
            public void onClick(View view) {
                RequestKeyBean requestKeyBean = new RequestKeyBean();
                requestKeyBean.setKey(C.SysConfig.TAOBAO_TO_GRANT_AUTHORIZATION);
                RxHttp.getInstance().getCommonService().getConfigForKey(requestKeyBean).
                        compose(RxUtils.<BaseResponse<HotKeywords>>switchSchedulers()).
                        compose(activity.<BaseResponse<HotKeywords>>bindToLifecycle()).
                        subscribe(new DataObserver<HotKeywords>() {
                            @Override
                            protected void onSuccess(HotKeywords data) {
                                if (!TextUtils.isEmpty(data.getSysValue())) {
                                    ShowWebActivity.start(activity, data.getSysValue(), "授权说明");
                                }

                            }
                        });
            }
        });
        dialog.setmOkListener(new AuthorDialog.OnOkListener() {
            @Override
            public void onClick(View view) {
                RequestBaseBean requestBean = new RequestBaseBean();
                RxHttp.getInstance().getCommonService().getAllianceAppKey(requestBean).
                        compose(RxUtils.<BaseResponse<String>>switchSchedulers()).
                        compose(activity.<BaseResponse<String>>bindToLifecycle()).
                        subscribe(new DataObserver<String>() {
                            @Override
                            protected void onSuccess(String data) {
//                                String data1 = "28224017";
                                TaobaoUtil.authTaobao(activity, data);
                            }
                        });
            }
        });
        dialog.show();
    }
}
