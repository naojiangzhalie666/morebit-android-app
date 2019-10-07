package com.jf.my.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import com.ali.auth.third.core.model.Session;
import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcPage;
import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.jf.my.Activity.ChannelWebActivity;
import com.jf.my.Activity.ShowWebActivity;
import com.jf.my.LocalData.UserLocalData;
import com.jf.my.Module.common.Activity.BaseActivity;
import com.jf.my.Module.common.Dialog.AuthorDialog;
import com.jf.my.network.BaseResponse;
import com.jf.my.network.RxHttp;
import com.jf.my.network.RxUtils;
import com.jf.my.network.observer.DataObserver;
import com.jf.my.pojo.HotKeywords;
import com.jf.my.pojo.request.RequestBaseBean;
import com.jf.my.pojo.requestbodybean.RequestKeyBean;
import com.jf.my.utils.fire.BuglyUtils;

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
                AlibcShowParams alibcShowParams = new AlibcShowParams(OpenType.Native, false);
                HashMap exParams = new HashMap<>();
                exParams.put("isv_code", "appisvcode");
                exParams.put("alibaba", "阿里巴巴");//自定义参数部分，可任意增删改
                AlibcTrade.show(activity, new AlibcPage(url), alibcShowParams, null, exParams, new DemoTradeCallback());
                BuglyUtils.setUserSceneTag(activity, C.SceneTag.goTaobaoTag);

            }
        } catch (Exception e) {
            e.printStackTrace();
            ShowWebActivity.start(activity, url, "粉丝福利购");
        }
    }

    public static void authTaobao(final Activity activity, final String client_id) {
        final AlibcLogin alibcLogin = AlibcLogin.getInstance();
        alibcLogin.showLogin(new AlibcLoginCallback() {

            @Override
            public void onSuccess(int i) {
                MyLog.i("test", "onSuccess: " + alibcLogin.getSession().toString());
                ChannelWebActivity.start(activity, "https://oauth.taobao.com/authorize?response_type=code&client_id=" + client_id + "&redirect_uri=http://127.0.0.1:12345/error&state=1212&view=wap");
            }

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
                public void onSuccess(int i) {

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
                                TaobaoUtil.authTaobao(activity, data);
                            }
                        });
            }
        });
        dialog.show();
    }
}
