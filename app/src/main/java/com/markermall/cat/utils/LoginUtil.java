package com.markermall.cat.utils;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;

import com.markermall.cat.App;
import com.markermall.cat.LocalData.UserLocalData;
import com.markermall.cat.Module.common.Utils.LoadingView;
import com.markermall.cat.Module.push.PushAction;
import com.markermall.cat.contact.EventBusAction;
import com.markermall.cat.fragment.MineFragment;
import com.markermall.cat.login.ui.LoginMainFragment;
import com.markermall.cat.network.BaseResponse;
import com.markermall.cat.network.RxHttp;
import com.markermall.cat.network.RxUtils;
import com.markermall.cat.network.observer.DataObserver;
import com.markermall.cat.pojo.MessageEvent;
import com.markermall.cat.pojo.ProtocolRuleBean;
import com.markermall.cat.pojo.UserInfo;
import com.markermall.cat.pojo.event.HomeScore;
import com.markermall.cat.pojo.event.LoginSucceedEvent;
import com.markermall.cat.pojo.event.LogoutEvent;
import com.markermall.cat.pojo.requestbodybean.RequestSystemStaticPage;
import com.markermall.cat.utils.action.MyAction;
import com.markermall.cat.utils.fire.DeviceIDUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.functions.Action;

/**
 * Created by Administrator on 2017/12/22.
 */

public class LoginUtil {
    public static int REQUEST_CODE = 0x123;
    /**
     * 跳转到登录界面
     */
//    public static void goToLogin(Activity activity) {
//        activity.startActivity(new Intent(activity, LoginActivity.class));
//    }

    /**
     * 跳转到密码登录界面
     */
    public static void goToPasswordLogin(Activity activity) {
//        activity.startActivity(new Intent(activity, LoginPasswordActivity.class));
        LoginMainFragment.start(activity);
    }

    /**
     * 判断是否已经登录了,没有跳转到登录界面
     *
     * @param activity
     * @return
     */
    public static boolean checkIsLogin(Activity activity, boolean isToLogin) {
        UserInfo userInfo1 = UserLocalData.getUser(activity);
        if (userInfo1 == null || TextUtils.isEmpty(UserLocalData.getToken())) {
            if (isToLogin)
                goToPasswordLogin(activity);
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断是否已经登录了,没有跳转到登录界面
     *
     * @param activity
     * @return
     */
    public static boolean checkIsLogin(Activity activity) {
        return checkIsLogin(activity, true);
    }


    /**
     * 获取个人信息
     *
     * @param activity
     * @param isShowLoading 是否显示转圈
     */
    public static void getUserInfo(final RxAppCompatActivity activity, final boolean isShowLoading) {
        getUserInfo(activity, isShowLoading, null);
    }

    /**
     * 获取用户信息返回 回调 Action
     *
     * @param activity
     * @param isShowLoading
     * @param action
     */
    public static void getUserInfo(final RxAppCompatActivity activity, final boolean isShowLoading, final MyAction.OnResult<UserInfo> action) {
        getUserInfoService(activity, isShowLoading)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (action instanceof MyAction.OnResultFinally) {
                            ((MyAction.OnResultFinally) action).onFinally();
                        }
                    }
                }).subscribe(new DataObserver<UserInfo>() {
            @Override
            protected void onError(String errorMsg, String errCode) {
                UserInfo user = UserLocalData.getUser(activity);
                if (!TextUtils.isEmpty(user.getInviteCode())) {
                    if (action != null) {
                        action.invoke(user);
                    }
                } else {
                    if (action != null) {
                        action.onError();
                    }
                }
            }

            @Override
            protected void onSuccess(UserInfo data) {
                UserLocalData.setUser(activity, data);
                //通知刷新合伙人首页数据
                MessageEvent messageEvent4 = new MessageEvent();
                messageEvent4.setAction(EventBusAction.MAINPAGE_MYCENTER_REFRESH_DATA);
                messageEvent4.setMsg("");
                EventBus.getDefault().post(messageEvent4);
                if (action != null) {
                    action.invoke(data);
                }
            }
        });
    }


    /**
     * 获取用户信息 的 Observable 对象
     *
     * @param activity
     * @param isShowLoading
     * @return
     */
    public static Observable<BaseResponse<UserInfo>> getUserInfoService(RxAppCompatActivity activity, final boolean isShowLoading) {
        if (isShowLoading) {
            LoadingView.showDialog(activity, "请求中...");
        }
        return getUserInfoObservable(isShowLoading, activity);
    }

    public static Observable<BaseResponse<UserInfo>> getUserInfoObservable(final boolean isShowLoading, RxAppCompatActivity activity) {
        return RxHttp.getInstance().getUsersService().getUserInfo()
                .compose(RxUtils.<BaseResponse<UserInfo>>switchSchedulers())
                .compose(RxUtils.<BaseResponse<UserInfo>>switchSchedulers())
                .compose(activity.<BaseResponse<UserInfo>>bindToLifecycle())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (isShowLoading) {
                            LoadingView.dismissDialog();
                        }
                    }
                });
    }

    /**
     * 获取用户协议
     *
     * @param activity
     */
    public static void getUserProtocol(final RxAppCompatActivity activity) {

        LoadingView.showDialog(activity, "请求中...");
        getSystemStaticPage(activity, C.ProtocolType.agreement)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                })
                .subscribe(new DataObserver<List<ProtocolRuleBean>>() {
                    /**
                     * 成功回调
                     *
                     * @param data 结果
                     */
                    @Override
                    protected void onSuccess(List<ProtocolRuleBean> data) {
                        PageToUtil.goToWebview(activity, "用户协议 ", data.get(0).getHtmlUrl());
                    }
                });
    }


    /**
     * 获取隐私政策
     *
     * @param activity
     */
    public static void getPrivateProtocol(final RxAppCompatActivity activity) {

        LoadingView.showDialog(activity, "请求中...");
        getSystemStaticPage(activity, C.ProtocolType.privateProtocol)
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        LoadingView.dismissDialog();
                    }
                })
                .subscribe(new DataObserver<List<ProtocolRuleBean>>() {
                    /**
                     * 成功回调
                     *
                     * @param data 结果
                     */
                    @Override
                    protected void onSuccess(List<ProtocolRuleBean> data) {
                        PageToUtil.goToWebview(activity, "隐私政策 ", data.get(0).getHtmlUrl());
                    }
                });
    }

    /**
     * 获取html页面链接 list
     *
     * @param activity
     * @param type
     * @return
     */
    public static Observable<BaseResponse<List<ProtocolRuleBean>>> getSystemStaticPage(RxAppCompatActivity activity, int type) {
        return getSystemStaticPage(activity, type, null);
    }

    public static Observable<BaseResponse<List<ProtocolRuleBean>>> getSystemStaticPage(RxAppCompatActivity activity, int type, String page) {

        return RxHttp.getInstance().getSysteService().getSystemStaticPage(new RequestSystemStaticPage().setScope(type).setPage(page))
                .compose(RxUtils.<BaseResponse<List<ProtocolRuleBean>>>switchSchedulers())
                .compose(activity.<BaseResponse<List<ProtocolRuleBean>>>bindToLifecycle());

    }

    /**
     * 验证手机格式
     */
    public static boolean isMobile(String phone) {
        return true;
//        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
//        if (phone.length() != 11) {
//            return false;
//        } else {
//            Pattern p = Pattern.compile(regex);
//            Matcher m = p.matcher(phone);
//            boolean isMatch = m.matches();
//            return isMatch;
//        }
    }

    public static boolean isPhone(String phone) {
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (phone.length() != 11) {
            return false;
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            boolean isMatch = m.matches();
            return isMatch;
        }
    }

    /**
     * 验证手机格式
     */
    public static boolean isSeekMobile(String number) {

    /*
    移动：134、135、136、137、138、139、150、151、152、157(TD)、158、159、178(新)、182、184、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、170、173、177、180、181、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String num = "[1][3456789]\\d{9}";//"[1]"代表第1位为数字1，"[34578]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。

        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return number.matches(num);
        }
    }

    /**
     * 隐藏手机中间4位
     *
     * @param s
     * @return
     */
    public static String hideNumber(String s) {

        if (TextUtils.isEmpty(s)) {
            return "";
        }
        if (s.length() != 11) {
            return s;
        }
        StringBuilder sb = new StringBuilder(s);
        sb.replace(3, 7, "****");
        sb.substring(7, 11);
        return sb.toString();
    }

    /**
     * 获取拷贝文字 是邀请码和手机号码就返回
     *
     * @param activity
     * @return
     */
    public static String getCoyeTextIsInvite(Activity activity) {
        String text = "";
        try {
            ClipboardManager mClipboardManager = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
            String getClipText = "";

            getClipText = mClipboardManager.getPrimaryClip().getItemAt(0).getText().toString();
            //判断验证码
            Pattern p = Pattern.compile("[a-zA-Z0-9]{6}$");
            Matcher m = p.matcher(getClipText);
//                Pattern p2 = Pattern.compile("", Pattern.CASE_INSENSITIVE);
//                Matcher m2 = p2.matcher(getClipText);
            if (/*m2.matches() || */m.matches()) {
                text = getClipText;
            }
            if (isSeekMobile(getClipText)) {
                text = getClipText;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return text;
    }

    /**
     * 判断是否
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }


    public static void LoginSuccess(UserInfo userInfo, Activity activity) {
        UserLocalData.setUser(activity, userInfo);
        //注册推送
        UserLocalData.setToken(userInfo.getToken());
//        OneApmConfig.setLoginUserName(userInfo.getPhone());
        if (userInfo != null && !TextUtils.isEmpty(userInfo.getPhone())) {
            PushAction.bindAccount(App.getAppContext(), userInfo.getPhone());
        }
        EventBus.getDefault().post(new LoginSucceedEvent());
        App.mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MessageEvent messageEvent = new MessageEvent();
                messageEvent.setAction(EventBusAction.MAINPAGE_MYCENTER_REFRESH_DATA);
                EventBus.getDefault().post(messageEvent);
                MessageEvent loginSucceedEvent = new MessageEvent();
                loginSucceedEvent.setAction(EventBusAction.LOGINA_SUCCEED);

                EventBus.getDefault().post(loginSucceedEvent);
                HomeScore homeScore = new HomeScore();
                EventBus.getDefault().postSticky(homeScore);
            }
        }, 300);
    }

    public static void logout( ) {
        logout(false);

    }
    public static void logout(boolean isPutError) {
        PushAction.bindAccount(App.getAppContext(), "");
        String token = UserLocalData.getToken();
        String phone = UserLocalData.getUser().getPhone();
        String deviceId = DeviceIDUtils.getDeviceId(App.getAppContext());
        LogoutEvent logoutEvent = new LogoutEvent();
        logoutEvent.setPutError(isPutError);
        // 添加错误日志过去
        String spToken = (String) SharedPreferencesUtils.get(App.getAppContext(), C.sp.token, "");
        String acacheToken = App.getACache().getAsString(C.sp.token);
        logoutEvent.setLogoutErrorMsg("os  1 " +
                "  token  " + token +
                "  acacheToken  " + acacheToken +
                "  spToken  " + spToken +
                "  phone  " + phone +
                "  deviceId   " + deviceId);
        UserLocalData.cloerUser();
        EventBus.getDefault().post(logoutEvent);
//        OneApmConfig.clearLoginUserName();
        MineFragment.mMonthEarnings = null;
        MineFragment.mDayEarnings = null;
        TaobaoUtil.logoutTaobao(null);
    }
}
