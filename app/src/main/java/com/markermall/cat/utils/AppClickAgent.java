package com.markermall.cat.utils;

import android.content.Context;

/**
 * 点击统计
 */
public class AppClickAgent {


    public static void onResume(Context context) {
//        MobclickAgent.onResume(context);
    }

    public static void onPause(Context context) {
//        MobclickAgent.onPause(context);
    }

    public static void onPageStart(String pageName) {
//        MobclickAgent.onPageStart(pageName); //统计页面，"MainScreen"为页面名称，可自定义
    }
    public static void onPageEnd(String pageName) {
//        MobclickAgent.onPageEnd(pageName);
    }

    /**
     * 统计商品的点击
     * @param context
     * @param name
     */
    public static void goodsAddClickEvent(Context context,String name){
//        HashMap<String,String> map = new HashMap<String,String>();
//        map.put("type",name);
//        MobclickAgent.onEvent(context, "shopgoods", map);
    }

    /**
     * 启用账号记录
     * @param userId
     */
    public static void loginEvent(String userId){
//        MobclickAgent.onProfileSignIn(userId);
    }
    /**
     * 关闭账号记录
     */
    public static void logoutEvent(){
//        MobclickAgent.onProfileSignOff();
    }

}
