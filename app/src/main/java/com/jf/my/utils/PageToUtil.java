package com.jf.my.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.jf.my.Activity.OneFragmentDefaultActivity;
import com.jf.my.Activity.ShowWebActivity;
import com.jf.my.info.ui.LoadUserInfoActivity;

/**
 * 页面跳转
 * Created by Administrator on 2017/12/22.
 */

public class PageToUtil {

    /**
     * 跳转到Fragment页面
     *
     * @param context
     * @param title
     * @param fragmentName
     */
    public static void goToSimpleFragment(Context context, String title, String fragmentName) {
        Intent it = new Intent(context, OneFragmentDefaultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("fragmentName", fragmentName);
        it.putExtras(bundle);
        context.startActivity(it);
    }

    /**
     * 跳转到个人
     *
     * @param context
     * @param title
     * @param fragmentName
     */
    public static void goToUserInfoSimpleFragment(Context context, String title, String fragmentName) {
        Intent it = new Intent(context, LoadUserInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("fragmentName", fragmentName);
        it.putExtras(bundle);
        context.startActivity(it);
    }

    public static void goToUserInfoSimpleFragment(Context context, Bundle bundle) {
        Intent it = new Intent(context, LoadUserInfoActivity.class);
        it.putExtras(bundle);
        context.startActivity(it);
    }

    /**
     * 跳转到Fragment页面
     *
     * @param context
     * @param bundle
     */
    public static void goToSimpleFragment(Context context, Bundle bundle) {
        Intent it = new Intent(context, OneFragmentDefaultActivity.class);
        it.putExtras(bundle);
        context.startActivity(it);
    }

    /**
     * 跳转到网页
     *
     * @param context
     * @param title
     * @param url
     */
    public static void goToWebview(Context context, String title, String url) {
        if (TextUtils.isEmpty(url)) return;
        ShowWebActivity.start((Activity) context,url,title);
//        Intent urlIt = new Intent(context, ShowWebActivity.class);
//        Bundle urlBundle = new Bundle();
//        urlBundle.putString("title", title);
//        urlBundle.putString("url", url);
//        urlIt.putExtras(urlBundle);
//        context.startActivity(urlIt);
    }

    /**
     * 跳转到微信App首页
     *
     * @param context
     */
    public static void goToWeixin(Context context) {
        oppeOtherApp(context, "com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
    }


    public static void oppeOtherApp(Context context, String pkg, String cls) {
        if (TextUtils.isEmpty(pkg) || TextUtils.isEmpty(cls)) return;
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            ComponentName cmp = new ComponentName(pkg, cls);

            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            context.startActivity(intent);
        } catch (Exception e) {
//            Toast.makeText(context, "检查到您手机没有安装微信，请安装后使用该功能", Toast.LENGTH_LONG).show();
        }
    }


}
