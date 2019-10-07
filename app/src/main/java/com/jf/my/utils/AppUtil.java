package com.jf.my.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

import com.jf.my.Activity.CashMoneyActivity;
import com.jf.my.Activity.ShowWebActivity;
import com.jf.my.App;
import com.jf.my.LocalData.UserLocalData;
import com.jf.my.MainActivity;
import com.jf.my.Module.common.Utils.LoadingView;
import com.jf.my.R;
import com.jf.my.utils.action.MyAction;
import com.jf.my.utils.fire.BuglyUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/10/17.
 */

public class AppUtil {

    private static int mTaobaoIconWidth;

    public static boolean isAppInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        List<String> pName = new ArrayList<String>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);
    }

    /**
     * get App versionCode
     *
     * @param context
     * @return
     */
    public static String getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        String versionCode = "";
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode + "";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * get App versionName
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        String versionName = "";
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * Apk安装
     *
     * @param context
     * @param file
     */
    public static void installApk(Context context, File file) {
        // 核心是下面几句代码
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return 语言列表
     */
    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return 手机IMEI
     */
    public static String getIMEI(Context ctx) {
        try {
            TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
            if (tm != null) {
                return tm.getDeviceId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取手机信息-上传记录用
     *
     * @return
     */
    public static String getPhoneSystemInfo(Context context) {
        String phoneInfo = "";
        try {
            phoneInfo = "手机厂商：" + getDeviceBrand();
            phoneInfo = phoneInfo + "," + "手机型号：" + getSystemModel();
            phoneInfo = phoneInfo + "," + "Android系统版本号：" + getSystemVersion();
            phoneInfo = phoneInfo + "," + "手机IMEI：" + getIMEI(context);
            phoneInfo = phoneInfo + "," + "App版本号：" + getVersionCode(context);
            phoneInfo = phoneInfo + "," + "App版本名称：" + getVersionName(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return phoneInfo;
    }

    /**
     * 获取application中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     */
    public static String getAppMetaData(Context ctx, String key) {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return resultData;
    }


    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }
            String mac = getMac(context);

            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getMac(Context context) {
        String mac = "";
        if (context == null) {
            return mac;
        }
        if (Build.VERSION.SDK_INT < 23) {
            mac = getMacBySystemInterface(context);
        } else {
            mac = getMacByJavaAPI();
            if (TextUtils.isEmpty(mac)) {
                mac = getMacBySystemInterface(context);
            }
        }
        return mac;

    }

    @TargetApi(9)
    private static String getMacByJavaAPI() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface netInterface = interfaces.nextElement();
                if ("wlan0".equals(netInterface.getName()) || "eth0".equals(netInterface.getName())) {
                    byte[] addr = netInterface.getHardwareAddress();
                    if (addr == null || addr.length == 0) {
                        return null;
                    }
                    StringBuilder buf = new StringBuilder();
                    for (byte b : addr) {
                        buf.append(String.format("%02X:", b));
                    }
                    if (buf.length() > 0) {
                        buf.deleteCharAt(buf.length() - 1);
                    }
                    return buf.toString().toLowerCase(Locale.getDefault());
                }
            }
        } catch (Throwable e) {
        }
        return null;
    }

    private static String getMacBySystemInterface(Context context) {
        if (context == null) {
            return "";
        }
        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (checkPermission(context, Manifest.permission.ACCESS_WIFI_STATE)) {
                WifiInfo info = wifi.getConnectionInfo();
                return info.getMacAddress();
            } else {
                return "";
            }
        } catch (Throwable e) {
            return "";
        }
    }

    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (context == null) {
            return result;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Throwable e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

    private static long lastClickTime;

    public static boolean isFastClick(int clickTime) {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= clickTime) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }

    private static long lastClickCashMoneyTime;

    public static boolean isFastCashMoneyClick(int clickTime) {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickCashMoneyTime) >= clickTime) {
            flag = false;
        }
        lastClickCashMoneyTime = currentClickTime;
        return flag;
    }

    private static long lastViewClickTime = 0;
    private static long lastViewId = 0; //view的Id
    private static long longTimeClick = 5000; //最长两次点击时间间隔

    /**
     * 判断两次点击时间间隔是否少于1秒
     *
     * @param viewId
     * @return
     */
    public static boolean isViewClickTimeCheck(int viewId) {
        boolean flag = false;
        long currentClickTime = System.currentTimeMillis();
        if (lastViewId == 0 || lastViewId != viewId) {
            lastViewClickTime = 0;
        }
        lastViewId = viewId;
        if ((currentClickTime - lastViewClickTime) >= longTimeClick) {
            lastViewClickTime = currentClickTime;
            flag = true;
        }

        return flag;
    }

    /**
     * 判断是否安装了微信
     *
     * @param context
     * @return
     */
    public static boolean isWeixinAvilible(Context context) {
        return isAvilibleApp(context, "com.tencent.mm");

    }

    private static boolean isAvilibleApp(Context context, String name) {
        boolean installed = false;
        try {
            final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
            List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
            if (pinfo != null) {
                for (int i = 0; i < pinfo.size(); i++) {
                    String pn = pinfo.get(i).packageName;
                    if (name.equals(pn)) {
                        return true;
                    }
                }
            }
            // 上面 可能获取不到
            PackageManager pm = context.getPackageManager();


            pm.getPackageInfo(name, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (Exception e) {
            e.printStackTrace();
            installed = false;
        }
        return installed;
    }

    /**
     * 判断是否安装了QQ
     *
     * @param context
     * @return
     */
    public static boolean isQQClientAvailable(Context context) {
        return isAvilibleApp(context, "com.tencent.mobileqq");

    }

    /**
     * 判断是否安装了微博
     *
     * @param context
     * @return
     */
    public static boolean isWeiboClientAvailable(Context context) {
        return isAvilibleApp(context, "com.sina.weibo");
    }

    /**
     * 判断是否安装了淘宝
     *
     * @param context
     * @return
     */
    public static boolean isTaobaoClientAvailable(Context context) {
        return isAvilibleApp(context, "com.taobao.taobao");
    }

    /**
     * 检测图片地址是否有效
     *
     * @param path
     * @param callback
     */
    public static void isCheckImgUrlTrue(final Activity activity, final String path, final MyAction.OnResult<Integer> callback) {
        LoadingView.showDialog(activity, "");
        new Thread() {
            public void run() {
                try {
                    //连接服务器，获取图片
                    URL url = new URL(path);
                    //发出http请求
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    //连接超时的时间
                    connection.setConnectTimeout(3000);
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        //返回成功
                        if (callback != null) {
                            callback.invoke(0);
                        }
                    } else {
                        //返回失败
                        if (callback != null) {
                            callback.onError();
                        }
                    }
                    LoadingView.dismissDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                    //返回失败
                    if (callback != null) {
                        callback.onError();
                    }
                    LoadingView.dismissDialog();
                }
            }

            ;
        }.start();
    }

    public static void coayText(final Activity activity, String text) {
        ClipboardManager cma = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        cma.setText(text);
        SharedPreferencesUtils.put(activity, C.sp.SearchText, AppUtil.replaceText(text));
    }

    public static void coayTextPutNative(final Activity activity, String text) {
        SharedPreferencesUtils.put(activity, C.sp.SearchText, AppUtil.replaceText(text));
        ClipboardManager cma = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        cma.setText(text);


    }

    public static String replaceText(String text) {
        try {
            return text.replace("\n", "").replace(" ", "").replace("\t", "").replace("\r", "");
        } catch (Exception e) {
            e.printStackTrace();
            return text;
        }


    }


    /**
     * 设置界面
     *
     * @param activity
     */
    public static void goSetting(final Activity activity) {
        try {
            ViewShowUtils.showShortToast(activity, "请开启权限,才能使用此功能哦");
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + activity.getPackageName()));
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            BuglyUtils.e("requestOne", "permission state  == goSetting  error ");
        }
    }

    public static String StringEncode(String text) {
        try {
            return URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return text;
        }

    }

    public static String StringDecoder(String text) {
        try {
            return URLDecoder.decode(text, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public static boolean isNotificationEnabled(Activity activity) {

        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        AppOpsManager mAppOps = (AppOpsManager) activity.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = activity.getApplicationInfo();
        String pkg = activity.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;
     /* Context.APP_OPS_MANAGER */
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                appOpsClass = Class.forName(AppOpsManager.class.getName());
            }
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    public static void gotoSet(Activity activity) {

        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= 26) {
            // android 8.0引导
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("android.provider.extra.APP_PACKAGE", activity.getPackageName());
        } else if (Build.VERSION.SDK_INT >= 21) {
            // android 5.0-7.0
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", activity.getPackageName());
            intent.putExtra("app_uid", activity.getApplicationInfo().uid);
        } else {
            // 其他
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);

    }


    //友盟获取渠道名称
    public static String getChannelName(Context context) {
        String channelName = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                //注意此处为ApplicationInfo 而不是 ActivityInfo,因为友盟设置的meta-data是在application标签中，而不是某activity标签中，所以用ApplicationInfo
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        channelName = applicationInfo.metaData.getString("UMENG_CHANNEL");
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return context.getString(R.string.app_name);
        }
        if (TextUtils.isEmpty(channelName)) {
            return context.getString(R.string.app_name);
        } else {
            return channelName;
        }
    }

    /**
     * 去掉标签
     *
     * @param content
     * @return
     */
    public static String delHTMLTag(String content) {
        String REGEX_HTML = "<[^>]+>";
        Pattern p_html = Pattern.compile(REGEX_HTML, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(content);
        content = m_html.replaceAll("");
        return content.trim();
    }

    /**
     * 添加https
     *
     * @param shopIcon
     * @return
     */
    @NonNull
    public static String jointUrl(String shopIcon) {
        if (!shopIcon.startsWith("http")) {
            shopIcon = "http:" + shopIcon;
        }
        return shopIcon;
    }

    /**
     * 去掉url中的路径，留下请求参数部分
     *
     * @param strURL url地址
     * @return url请求参数部分
     */
    private static String TruncateUrlPage(String strURL) {
        String strAllParam = null;
        String[] arrSplit = null;

        strURL = strURL.trim();

        arrSplit = strURL.split("[?]");
        if (strURL.length() > 1) {
            if (arrSplit.length > 1) {
                if (arrSplit[1] != null) {
                    strAllParam = arrSplit[1];
                }
            }
        }

        return strAllParam;
    }

    /**
     * 解析出url参数中的键值对
     * 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
     *
     * @param URL url地址
     * @return url请求参数部分
     */
    public static Map<String, String> URLRequest(String URL) {
        Map<String, String> mapRequest = new HashMap<String, String>();

        String[] arrSplit = null;

        String strUrlParam = TruncateUrlPage(URL);
        if (strUrlParam == null) {
            return mapRequest;
        }
        //每个键值为一组 www.2cto.com
        arrSplit = strUrlParam.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");

            //解析出键值
            if (arrSplitEqual.length > 1) {
                //正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);

            } else {
                if (arrSplitEqual[0] != "") {
                    //只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }


    /**
     * 获取URL中的参数,格式為4&type=2,首页的二级模块
     *
     * @param url
     * @return
     */
    public static Map<String, String> getUrlParms(String url) {
        Map<String, String> map = null;
        if (url != null && url.indexOf("&") > -1 && url.indexOf("=") > -1) {
            map = new HashMap<String, String>();
            String[] arrTemp = url.split("&");
            for (String str : arrTemp) {
                if (!str.contains("=")) {
                    //因为接口第一个参数没有=符号,加个id标识第一个参数,用于自用
                    map.put("id", str);
                } else {
                    //因为这str可能有两个=号，只取第一个
                    int p = str.indexOf("=");
                    String key = str.substring(0, p);
                    String value = str.substring(p + 1, str.length());
                    // String[] qs = str.split("=");
                    map.put(key, value);
                }

            }
        }
        return map;
    }


    /**
     * 获取淘宝图片宽度
     */
    public static int getTaobaoIconWidth() {
        if (mTaobaoIconWidth != 0) return mTaobaoIconWidth;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(App.getAppContext().getResources(), R.drawable.taobao, opts);
        opts.inSampleSize = 1;
        opts.inJustDecodeBounds = false;
        mTaobaoIconWidth = opts.outWidth;
        return mTaobaoIconWidth;
    }

    /**
     * 去提现
     */
    public static void gotoCashMoney(Activity activity, String totalMoney) {

        try {
            if (TextUtils.isEmpty(totalMoney)) {
                ViewShowUtils.showShortToast(activity, "您暂时没有余额,不能提现");
                return;
            }
            if (Double.parseDouble(totalMoney) <= 0) {
                ViewShowUtils.showShortToast(activity, "您暂时没有余额,不能提现");
                return;
            }

            if (Double.parseDouble(totalMoney) < 1) {
                ViewShowUtils.showShortToast(activity, "您的余额少于1元,不能提现");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        //没绑定支付宝跳到支付宝绑定界面
        if (TextUtils.isEmpty(UserLocalData.getUser(activity).getAliPayNumber())) {
            PageToUtil.goToSimpleFragment(activity, "绑定支付宝", "BindZhiFuBaoFragment");
            return;
        }

        Intent it = new Intent(activity, CashMoneyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("inType", 1);
        bundle.putString("totalMoney", totalMoney);
        it.putExtras(bundle);
        activity.startActivity(it);

    }


    public static InputFilter getTrimInputFilter() {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                // 判断是否输入空格
                if (" ".equals(source)) {
                    return "";
                }
                return null;
            }
        };
        return filter;
    }

    public static boolean isCorrectPwd(Context context, String pwd) {


        boolean isDigit = Pattern.compile("^[0-9]*$").matcher(pwd).matches();// 数字

        boolean matches1 = Pattern.compile("^.{8,16}$").matcher(pwd).matches();

        if (isDigit || !matches1) {
//            ViewShowUtils.showShortToast(context, R.string.edit_pwd_error);
            return false;
        }else{
            return true;
        }
    }
    public static String listToString(List<String> list) {
        String str = "";
        for (int i = 0; i < list.size(); i++) {
            String s = list.get(i);
            if (!TextUtils.isEmpty(s)) {
                if (TextUtils.isEmpty(str)) {
                    str = str + list.get(i);
                } else {
                    str = str + "," + list.get(i);
                }
            }
        }
        return str;

    }
    public static int getWindowWidth(Activity activity){
        if (activity == null)return  0;
         return activity.getWindowManager().getDefaultDisplay().getWidth() ;
    }


    /**
     * web打开app页面
     * @param uri
     * @param activity
     */
    public static  void sendWebOpenApp(Uri uri,Activity activity) {
        //这里是h5打开app
        //判断有没有登录
        if (null == uri) return;
        if (!LoginUtil.checkIsLogin(activity)) {
            return;
        }

        String scheme = uri.getScheme();
        String host = uri.getHost();
        String path = uri.getPath();
        if (!TextUtils.isEmpty(scheme) && scheme.equals("scheme") && !TextUtils.isEmpty(host) && host.equals("miyuan")) {
            if (!TextUtils.isEmpty(path) && path.equals("/showweb")) {
                //商学院
                String uriString = uri.toString();
                if (uriString.indexOf("url=") != -1) {
                    uriString = uriString.substring(uriString.indexOf("url") + 4, uriString.length());
                }
                // String h5Url = uri.getQueryParameter("url");
                if (!TextUtils.isEmpty(uriString)) {
                    ShowWebActivity.start(activity, uriString, "");
                }
            }
        }

    }

}
