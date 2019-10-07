package com.markermall.cat.utils.netWork;

import android.text.TextUtils;

import com.markermall.cat.utils.C;

/**
 * Created by fengrs on 2018/12/17.
 * 备注:
 */

public class ErrorCodeUtlis {

    /**
     * B10010  成功
     *
     * @return
     */
    public static boolean isSucceed(String errorCode) {
        if (TextUtils.isEmpty(errorCode)) return false;
        return C.requestCode.SUCCESS.equals(errorCode);
    }

    /**
     * B10010 注册手机号已存在,跳转登录界面
     *
     * @return
     */
    public static boolean isRegister(String errorCode) {
        if (TextUtils.isEmpty(errorCode)) return false;
        return C.requestCode.B10010.equals(errorCode)||C.requestCode.B10005.equals(errorCode);
    }

    /**
     *户未注册，请先注册
     *
     * @return
     */
    public static boolean isLogin(String errorCode) {
        if (TextUtils.isEmpty(errorCode)) return false;
        return C.requestCode.B10031.equals(errorCode);
    }


    /**
     * B10011 该微信未注册,跳转到注册界面
     *
     * @return
     */
    public static boolean isNuRegister(String errorCode) {
        if (TextUtils.isEmpty(errorCode)) return false;
        return C.requestCode.B10040.equals(errorCode) ;
    }
}
