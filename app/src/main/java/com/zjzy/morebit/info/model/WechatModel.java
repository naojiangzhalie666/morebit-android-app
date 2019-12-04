package com.zjzy.morebit.info.model;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.CallBackObserver;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.RxWXHttp;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.WeixinInfo;
import com.zjzy.morebit.pojo.request.RequestBindWxBean;
import com.zjzy.morebit.utils.LogUtils;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.action.MyAction;
import com.zjzy.morebit.utils.encrypt.EncryptUtlis;
import com.mob.tools.utils.UIHandler;

import java.util.HashMap;

import javax.net.ssl.SSLHandshakeException;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.utils.WechatClientNotExistException;
import cn.sharesdk.wechat.utils.WechatFavoriteNotSupportedException;
import cn.sharesdk.wechat.utils.WechatTimelineNotSupportedException;
import io.reactivex.functions.Action;

/**
 * @author fengrs
 * @date 2019/9/4
 */
public class WechatModel implements Handler.Callback, PlatformActionListener {

    private static final int MSG_USERID_FOUND = 1;
    private static final int MSG_LOGIN = 2;
    private static final int MSG_AUTH_CANCEL = 3;
    private static final int MSG_AUTH_ERROR = 4;
    private static final int MSG_AUTH_COMPLETE = 5;
    private final MyAction.OnResult<String> mAction;
    private BaseActivity mActivity;

    public WechatModel(BaseActivity activity, MyAction.OnResult<String> action) {
        this.mActivity = activity;
        this.mAction = action;
    }

    public void authorize() {
        Wechat plat = new Wechat();
        plat.removeAccount(true);
        if (plat.isAuthValid()) {
            String userId = plat.getDb().getUserId();
            if (!TextUtils.isEmpty(userId)) {
                Message msg = new Message();
                msg.what = MSG_USERID_FOUND;
                msg.obj = plat;
                UIHandler.sendMessage(msg, this);
                //                login(plat.getName(), userId, null);
                return;
            }
        }
        plat.setPlatformActionListener(this);
        plat.SSOSetting(true);
        plat.showUser(null);
    }


    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_USERID_FOUND: {
                //                Toast.makeText(this, R.string.userid_found, Toast.LENGTH_SHORT).show();
                LogUtils.Log("ThirdLogin:", "userid_found");
                //已获取到用户信息--直接登录
                Platform comPlatform = (Platform) msg.obj;
                getWeixinInfo(comPlatform);
            }
            break;
            case MSG_LOGIN: {

                LogUtils.Log("ThirdLogin:", "logining");
            }
            break;
            case MSG_AUTH_CANCEL: {
                LogUtils.Log("ThirdLogin:", "MSG_AUTH_CANCEL");
            }
            break;
            case MSG_AUTH_ERROR: {
                LogUtils.Log("ThirdLogin:", "MSG_AUTH_ERROR");
            }
            break;
            case MSG_AUTH_COMPLETE: {
                LogUtils.Log("ThirdLogin:", "MSG_AUTH_COMPLETE");
                //帐号密码登录微信成功，返回信息--直接调用登录接口
                Platform comPlatform = (Platform) msg.obj;
                getWeixinInfo(comPlatform);

            }
            break;
            default:
        }
        return false;
    }

    private void getWeixinInfo(final Platform platform) {
        LoadingView.showDialog(mActivity, "");
        RxWXHttp.getInstance().getService("https://api.weixin.qq.com").getWxUserInfo(platform.getDb().getToken(), platform.getDb().getUserId())
                .compose(RxUtils.<String>switchSchedulers())
                .compose(mActivity.<String>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                })
                .subscribe(new CallBackObserver<String>() {
                    @Override
                    public void onNext(String baseResponse) {
                        super.onNext(baseResponse);
                        WeixinInfo weixinInfo = JSON.parseObject(baseResponse, WeixinInfo.class);
                        weixinLogic(weixinInfo);

                    }

                });
    }

    @Override
    public void onComplete(Platform platform, int action,
                           HashMap<String, Object> res) {
        if (action == Platform.ACTION_USER_INFOR) {
            Message msg = new Message();
            msg.what = MSG_AUTH_COMPLETE;
            msg.obj = platform;
            UIHandler.sendMessage(msg, this);
        }
        LogUtils.Log("ThirdLogin:", platform.getDb().getUserName());
        LogUtils.Log("ThirdLogin:", platform.getDb().getUserId());
        LogUtils.Log("ThirdLogin:", platform.getDb().getUserIcon());

    }

    @Override
    public void onError(Platform platform, int action, Throwable t) {
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_ERROR, this);
        }

        String msg = "微信授权失败，请稍后再试";
        String type = "";
        if (platform.getName().equalsIgnoreCase(Wechat.NAME)) {
            type = "微信";
        }
        if (t instanceof SSLHandshakeException) {
            msg = type + "授权失败，请检查您的网络状态";
        } else if (t instanceof WechatClientNotExistException
                || t instanceof WechatFavoriteNotSupportedException
                || t instanceof WechatTimelineNotSupportedException) {
            msg = type + "授权失败，请先安装微信客户端";
        }
        final String finalMsg = msg;
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ViewShowUtils.showShortToast(mActivity, finalMsg);
            }
        });

//        t.printStackTrace();
    }

    @Override
    public void onCancel(Platform platform, int action) {
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_CANCEL, this);
        }
    }

    /**
     * 绑定微信
     *
     * @param
     */
    public void weixinLogic(WeixinInfo weixinInfo) {
        UserInfo userInfo = UserLocalData.getUser(mActivity);
        if (userInfo == null) return;
        LoadingView.showDialog(mActivity, "提交中...");
        final String openid = weixinInfo.getOpenid();
        String nickname = weixinInfo.getNickname();
        String sex = weixinInfo.getSex() + "";
        String headImg = weixinInfo.getHeadimgurl();
        String country = weixinInfo.getCountry();
        String province = weixinInfo.getProvince();
        String city = weixinInfo.getCity();


        RequestBindWxBean requestBean = new RequestBindWxBean();
        requestBean.setOauthWx(openid);
        requestBean.setNickname(nickname);
        requestBean.setSex(sex);
        requestBean.setHeadImg(headImg);
        requestBean.setCountry(country);
        requestBean.setProvince(province);
        requestBean.setCity(city);
        String sign = EncryptUtlis.getSign2(requestBean);
        requestBean.setSign(sign);
        RxHttp.getInstance().getUsersService()
                .setWechatNew(requestBean)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(mActivity.<BaseResponse<String>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                })

                .subscribe(new DataObserver<String>() {
                    @Override
                    protected void onDataNull() {
                        onSuccess("");
                    }

                    @Override
                    protected void onSuccess(String data) {
                        UserLocalData.getUser().setOauthWx(openid);
                        if (mAction != null) {
                            mAction.invoke(openid);
                        }
                    }
                });


    }
}
