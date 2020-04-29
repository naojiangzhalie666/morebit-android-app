package com.zjzy.morebit.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import com.kepler.jd.Listener.LoginListener;
import com.kepler.jd.Listener.OpenAppAction;
import com.kepler.jd.login.KeplerApiManager;
import com.kepler.jd.sdk.bean.KelperTask;
import com.kepler.jd.sdk.bean.KeplerAttachParameter;
import com.kepler.jd.sdk.exception.KeplerBufferOverflowException;

import org.json.JSONException;

public class KaipuleUtils {
    private static Context mContext;
    private static KaipuleUtils kaipuleUtils;
    KeplerAttachParameter mKeplerAttachParameter = new KeplerAttachParameter();
    KelperTask mKelperTask;
    private Handler mHandler;

    public static final int timeOut = 15;


    private KaipuleUtils() {
        mHandler = new Handler();
    }

    OpenAppAction mOpenAppAction = new OpenAppAction() {
        @Override
        public void onStatus(final int status) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (status == OpenAppAction.OpenAppAction_start) {//开始状态未必一定执行，
//                        dialogShow(); //YYF
                    } else {
                        mKelperTask = null;

                    }
                }
            });
        }
    };

    public static KaipuleUtils getInstance(Context context) {
        mContext = context;
        return kaipuleUtils == null ? kaipuleUtils = new KaipuleUtils() : kaipuleUtils;
    }

    public void openUrlToApp(String url) {
        try {
            mKelperTask = KeplerApiManager.getWebViewService().openJDUrlPage(url,
                    mKeplerAttachParameter, mContext, mOpenAppAction, timeOut);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void login() {
        KeplerApiManager.getWebViewService().login((Activity) mContext, new LoginListener() {
            @Override
            public void authSuccess() {

            }

            @Override
            public void authFailed(int i) {
            }
        });
    }


}
