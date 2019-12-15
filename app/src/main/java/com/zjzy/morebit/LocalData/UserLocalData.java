package com.zjzy.morebit.LocalData;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.zjzy.morebit.App;
import com.zjzy.morebit.R;
import com.zjzy.morebit.network.CallBackObserver;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MyGsonUtils;
import com.zjzy.morebit.utils.SensorsDataUtil;
import com.zjzy.morebit.utils.SharedPreferencesUtils;

/**
 * Created by Administrator on 2017/10/30.
 */

public class UserLocalData {
    public static boolean isCollect;// 是否有收藏的商品然后到了首页刷新
    public static boolean isPartner;// 是否有更新个人页面刷新个人页面
    public static UserInfo sUserInfo;
    public static String mUserhead = "";
    public static Bitmap mMyHeadBitmap;
    public static String mPhone = "";
    public static String sToken = "";
    public static boolean isUpdateAllRegion;

    /**
     * 获取User信息
     *
     * @return
     */
    public static UserInfo getUser() {
        return getUser(App.getAppContext());
    }

    public static UserInfo getUser(Context activity) {
        if (sUserInfo != null) {
            return sUserInfo;
        }
        try {
            String msgJson = (String) SharedPreferencesUtils.get(activity, C.sp.userInfo, "");
            if (msgJson != null && !"".equals(msgJson)) {
                UserInfo userInfo = MyGsonUtils.jsonToBean(msgJson, UserInfo.class);
                if (userInfo == null) {
                    sUserInfo = (UserInfo) App.getACache().getAsObject(C.sp.userInfo);
                } else {
                    sUserInfo = userInfo;
                }
            } else {
                sUserInfo = (UserInfo) App.getACache().getAsObject(C.sp.userInfo);
            }
            if (sUserInfo == null) {
                sUserInfo = new UserInfo();
            }
            return sUserInfo;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return new UserInfo();
    }

    public static void cloerUser() {
        mMyHeadBitmap = null;
        mUserhead = "";
        App.getACache().put(C.sp.token, "");
        App.getACache().put(C.sp.userInfo, "");
        SharedPreferencesUtils.put(App.getAppContext(), C.sp.token, "");
        SharedPreferencesUtils.put(App.getAppContext(), C.sp.userInfo, "");
        sToken = "";
        sUserInfo = null;
    }

    /**
     * 获取token
     */
    public static String getToken() {
        if (!TextUtils.isEmpty(sToken)) {
            return sToken;
        } else {
            String spToken = (String) SharedPreferencesUtils.get(App.getAppContext(), C.sp.token, "");
                if (TextUtils.isEmpty(spToken)) {
                sToken = App.getACache().getAsString(C.sp.token);
            } else {
                sToken = spToken;
            }
            return sToken;

        }
    }

    /**
     * settoken
     */
    public static void setToken(String token) {
        if (TextUtils.isEmpty(token)) return;
        sToken = token;
        App.getACache().put(C.sp.token, token);
        SharedPreferencesUtils.put(App.getAppContext(), C.sp.token, token);

    }

    /**
     * 保存user信息
     *
     * @param activity
     * @param userInfo
     */
    public static void setUser(final Activity activity, UserInfo userInfo) {
        if (userInfo == null) {
            mMyHeadBitmap = null;
            mUserhead = "";
            return;
        }
        SensorsDataUtil.getInstance().setUserInfo();
        try {
            String jsonInfo = MyGsonUtils.beanToJson(userInfo);
            if (!TextUtils.isEmpty(jsonInfo)) {
                SharedPreferencesUtils.put(activity, C.sp.userInfo, jsonInfo);
            }
            App.getACache().put(C.sp.userInfo, userInfo);
            sUserInfo = userInfo;

            mPhone = userInfo.getPhone();
            if (mMyHeadBitmap == null || !mUserhead.equals(userInfo.getHeadImg())) {
                mUserhead = userInfo.getHeadImg();
                final Bitmap bgBitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.image_erweimakuang);
                LoadImgUtils.getImgCircleToBitmapObservable(App.getAppContext(), mUserhead)
                        .subscribe(new CallBackObserver<Bitmap>() {
                            @Override
                            public void onNext(Bitmap bitmap) {
                                super.onNext(bitmap);
                                mMyHeadBitmap = LoadImgUtils.getHeadBitmap(bitmap, bgBitmap);
                            }

                            @Override
                            public void onError(Throwable e) {
                                final Bitmap bitmap = BitmapFactory.decodeResource(App.getAppContext().getResources(), R.drawable.head_icon);
                                mMyHeadBitmap = LoadImgUtils.getHeadBitmap(bitmap, bgBitmap);

                            }
                        });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Bitmap getmMyHeadBitmap() {
        if (mMyHeadBitmap == null) {
            return BitmapFactory.decodeResource(App.getAppContext().getResources(), R.drawable.head_icon);
        } else {
            return mMyHeadBitmap;
        }

    }

    public static boolean isShowGuide(){
        boolean isshow = (boolean) SharedPreferencesUtils.get(App.getAppContext(), C.sp.isShowGuide, false);
        return isshow;
    }

    public static boolean isShowGuideCircle(){
        boolean isshow = (boolean) SharedPreferencesUtils.get(App.getAppContext(), C.sp.isShowGuideCircle, false);
        return isshow;
    }
    public static boolean isShowGuideMine(){
        boolean isshow = (boolean) SharedPreferencesUtils.get(App.getAppContext(), C.sp.isShowGuideMine, false);
        return isshow;
    }

    /**
     * 获取savedFlag
     */
    public static boolean getSavedRegionFlag() {
            boolean savedFlag = (boolean) SharedPreferencesUtils.get(App.getAppContext(), C.sp.SAVED_DB_FOR_ADDRESS, false);
            return savedFlag;


    }

    /**
     * savedFlag
     */
    public static void setSavedRegionFlag(boolean savedFlag) {
        SharedPreferencesUtils.put(App.getAppContext(), C.sp.SAVED_DB_FOR_ADDRESS, savedFlag);

    }
}
