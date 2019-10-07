package com.jf.my.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jf.my.LocalData.UserLocalData;
import com.jf.my.Module.common.Fragment.BaseFragment;
import com.jf.my.Module.common.Utils.LoadingView;
import com.jf.my.R;
import com.jf.my.contact.EventBusAction;
import com.jf.my.network.BaseResponse;
import com.jf.my.network.CallBackObserver;
import com.jf.my.network.RxHttp;
import com.jf.my.network.RxUtils;
import com.jf.my.network.RxWXHttp;
import com.jf.my.network.observer.DataObserver;
import com.jf.my.pojo.MessageEvent;
import com.jf.my.pojo.UserInfo;
import com.jf.my.pojo.WeixinInfo;
import com.jf.my.pojo.request.RequestAuthCodeBean;
import com.jf.my.pojo.request.RequestBindWxBean;
import com.jf.my.utils.ActivityStyleUtil;
import com.jf.my.utils.AppUtil;
import com.jf.my.utils.C;
import com.jf.my.utils.LogUtils;
import com.jf.my.utils.LoginUtil;
import com.jf.my.utils.ViewShowUtils;
import com.jf.my.utils.encrypt.EncryptUtlis;
import com.mob.tools.utils.UIHandler;

import org.greenrobot.eventbus.EventBus;

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
 * 绑定微信-界面
 */
public class BindWeixinFragment extends BaseFragment implements View.OnClickListener, Handler.Callback,
        PlatformActionListener {

    private static final int MSG_USERID_FOUND = 1;
    private static final int MSG_LOGIN = 2;
    private static final int MSG_AUTH_CANCEL = 3;
    private static final int MSG_AUTH_ERROR = 4;
    private static final int MSG_AUTH_COMPLETE = 5;
    private EditText zhifubaoagin_et, zhifubao_et, edt_yanzhengma;
    private TextView tv_yanzhengma, phone;
    private long first = 0;
    private static final int SECONDS = 60; // 秒
    private WeixinInfo weixinInfo;
    private UserInfo mUsInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_setting_bindweixin, container, false);
        inview(view);
        initData();
        return view;
    }

    public void inview(View view) {
        ActivityStyleUtil.initSystemBar(getActivity(), R.color.color_757575); //设置标题栏颜色值
        zhifubaoagin_et = (EditText) view.findViewById(R.id.zhifubaoagin_et);
        zhifubao_et = (EditText) view.findViewById(R.id.zhifubao_et);
        phone = (TextView) view.findViewById(R.id.phone);
        tv_yanzhengma = (TextView) view.findViewById(R.id.tv_yanzhengma);
        edt_yanzhengma = (EditText) view.findViewById(R.id.edt_yanzhengma);
        tv_yanzhengma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppUtil.isFastClick(500)) {
                    return;
                }


                if ("获取验证码".equals(tv_yanzhengma.getText().toString().trim()) || "重新获取".equals(tv_yanzhengma.getText().toString().trim())) {
                    getPublicTimeline();
                }
            }
        });
        view.findViewById(R.id.submit).setOnClickListener(this);
    }

    /**
     * 初始化界面数据
     */
    private void initData() {
        mUsInfo = UserLocalData.getUser(getActivity());
        if (mUsInfo == null) {
            return;
        }
        phone.setText(LoginUtil.hideNumber(LoginUtil.hideNumber(mUsInfo.getPhone())));
        UserInfo info = UserLocalData.getUser(getActivity());
        if (info.getTealName() != null && !"".equals(info.getTealName())) {
            zhifubaoagin_et.setText(info.getTealName());
        }
        if (info.getAliPayNumber() != null && !"".equals(info.getAliPayNumber())) {
            zhifubao_et.setText(info.getAliPayNumber());
        }

    }


    class Counter extends CountDownTimer {

        public Counter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            try {
                tv_yanzhengma.setEnabled(true);
                tv_yanzhengma.setText("重新获取");
                tv_yanzhengma.setBackgroundResource(R.drawable.bg_ffe10a_rightround_2dp);
                tv_yanzhengma.setTextColor(getResources().getColor(R.color.color_000000));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // 获取当前时间总秒数
            first = millisUntilFinished / 1000;
            if (first <= SECONDS) { // 小于一分钟 只显示秒
                tv_yanzhengma.setText((first < 10 ? "0" + first : first) + " 秒后再试");
            }

        }

    }

    /**
     * 获取短信
     */
    public void getPublicTimeline() {
        UserInfo usInfo = UserLocalData.getUser(getActivity());
        if (usInfo == null || TextUtils.isEmpty(usInfo.getPhone())) {
            return;
        }

        LoadingView.showDialog(getActivity(), "请求中...");

        RequestAuthCodeBean requestBean = new RequestAuthCodeBean();
        requestBean.setPhone(usInfo.getPhone());
        requestBean.setType(C.sendCodeType.BINDWEIXIN);

        RxHttp.getInstance().getUsersService()
                //                .sendAuthCode(usInfo.getPhone(), C.sendCodeType.BINDWEIXIN)
                .sendAuthCode(requestBean)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(this.<BaseResponse<String>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                })
                .subscribe(new DataObserver<String>() {
                    @Override
                    protected void onError(String errorMsg, String errCode) {
                        tv_yanzhengma.setEnabled(true);
                        tv_yanzhengma.setText("获取验证码");
                    }

                    @Override
                    protected void onDataNull() {
                        onSuccess("");
                    }

                    /**
                     * 成功回调
                     *
                     * @param data 结果
                     */
                    @Override
                    protected void onSuccess(String data) {
                        ViewShowUtils.showLongToast(getActivity(), "验证码发送成功");
                        tv_yanzhengma.setEnabled(false);
                        Counter counter = new Counter(60 * 1000, 1000); // 第一个参数是倒计时时间，后者为计时间隔，单位毫秒，这里是倒计时
                        counter.start();
                    }
                });
    }

    /**
     * 绑定微信
     */
    public void weixinLogic(final Platform plat) {

        if (plat == null) {
            ViewShowUtils.showShortToast(getActivity(), "获取微信用户信息失败");
            return;
        }
        UserInfo userInfo = UserLocalData.getUser(getActivity());
        if (userInfo == null) return;

        LoadingView.showDialog(getActivity(), "提交中...");
        String verCode = edt_yanzhengma.getText().toString().trim();
        String openid = weixinInfo.getOpenid();
        String nickname = weixinInfo.getNickname();
        String sex = weixinInfo.getSex() + "";
        String headImg = weixinInfo.getHeadimgurl();
        String country = weixinInfo.getCountry();
        String province = weixinInfo.getProvince();
        String city = weixinInfo.getCity();


        RequestBindWxBean requestBean = new RequestBindWxBean();
        requestBean.setVerCode(verCode);
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
                .bindWx(requestBean)
                .compose(RxUtils.<BaseResponse<String>>switchSchedulers())
                .compose(this.<BaseResponse<String>>bindToLifecycle())
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
                        ViewShowUtils.showShortToast(getActivity(), "绑定成功");
                        //更新个人数据
                        mUsInfo.setOauthWx(weixinInfo.getOpenid());
                        EventBus.getDefault().post(new MessageEvent(EventBusAction.MAINPAGE_MYCENTER_REFRESH_DATA));
                        getActivity().finish();
                    }
                });


    }

    private void authorize(Platform plat) {
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

    public void onComplete(Platform platform, int action,
                           HashMap<String, Object> res) {
        if (action == Platform.ACTION_USER_INFOR) {
            Message msg = new Message();
            msg.what = MSG_AUTH_COMPLETE;
            msg.obj = platform;
            UIHandler.sendMessage(msg, this);
            //            login(platform.getName(), platform.getDb().getUserId(), res);
        }
        LogUtils.Log("ThirdLogin:", platform.getDb().getUserName());
        LogUtils.Log("ThirdLogin:", platform.getDb().getUserId());
        LogUtils.Log("ThirdLogin:", platform.getDb().getUserIcon());

        //        System.out.println(res);
        //        System.out.println("------User Name ---------" + platform.getDb().getUserName());
        //        System.out.println("------User ID ---------" + platform.getDb().getUserId());
    }

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
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ViewShowUtils.showShortToast(getActivity(), finalMsg);
            }
        });

//        t.printStackTrace();
    }

    public void onCancel(Platform platform, int action) {
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_CANCEL, this);
        }
    }

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

                //              String text = getString(R.string.logining, msg.obj);
                //              Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                LogUtils.Log("ThirdLogin:", "logining");

                //				Builder builder = new Builder(this);
                //				builder.setTitle(R.string.if_register_needed);
                //				builder.setMessage(R.string.after_auth);
                //				builder.setPositiveButton(R.string.ok, null);
                //				builder.create().show();
            }
            break;
            case MSG_AUTH_CANCEL: {
                //                Toast.makeText(this, R.string.auth_cancel, Toast.LENGTH_SHORT).show();
                LogUtils.Log("ThirdLogin:", "MSG_AUTH_CANCEL");
                //                System.out.println("-------MSG_AUTH_CANCEL--------");
            }
            break;
            case MSG_AUTH_ERROR: {
                //                Toast.makeText(this, R.string.auth_error, Toast.LENGTH_SHORT).show();
                //                System.out.println("-------MSG_AUTH_ERROR--------");
                LogUtils.Log("ThirdLogin:", "MSG_AUTH_ERROR");
            }
            break;
            case MSG_AUTH_COMPLETE: {
                //                Toast.makeText(this, R.string.auth_complete, Toast.LENGTH_SHORT).show();
                //                System.out.println("--------MSG_AUTH_COMPLETE-------");
                LogUtils.Log("ThirdLogin:", "MSG_AUTH_COMPLETE");
                //帐号密码登录微信成功，返回信息--直接调用登录接口
                Platform comPlatform = (Platform) msg.obj;
                getWeixinInfo(comPlatform);

            }
            break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit: //提交数据
                if (TextUtils.isEmpty(edt_yanzhengma.getText().toString().trim())) {
                    ViewShowUtils.showLongToast(getActivity(), "请填写验证码");
                    return;
                }
                Wechat plat = new Wechat();
                plat.removeAccount(true);
                authorize(plat);
                break;
            default:
                break;

        }
    }

    private void getWeixinInfo(final Platform platform) {
        LoadingView.showDialog(getActivity(), "");


        RxWXHttp.getInstance().getService("https://api.weixin.qq.com").getWxUserInfo(platform.getDb().getToken(), platform.getDb().getUserId())
                .compose(RxUtils.<String>switchSchedulers())
                .compose(this.<String>bindToLifecycle())
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
                        weixinInfo = JSON.parseObject(baseResponse, WeixinInfo.class);
                        weixinLogic(platform);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                });
    }


}
