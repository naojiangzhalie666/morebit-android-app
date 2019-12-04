package com.zjzy.morebit.network.Exception;

import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.zjzy.morebit.App;
import com.zjzy.morebit.BuildConfig;
import com.zjzy.morebit.utils.MyLog;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.IOException;
import java.io.NotSerializableException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import retrofit2.HttpException;

/**
 * Created by YangBoTian on 2018/7/3.
 * 异常类型
 */

public class FxEception extends Exception {

    private final int code;
    private String message;

    public FxEception(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
        this.message = throwable.getMessage();
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public static FxEception handleException(Throwable e, boolean isShowError) {

//        CrashReport.postCatchedException(e);  // bugly会将这个throwable上报
        FxEception ex = null;
        if (e instanceof HttpException) {
            if (BuildConfig.DEBUG) {
                HttpException httpException = (HttpException) e;
                ex = new FxEception(httpException, httpException.code());
                ex.message = "网络错误";

                try {
                    ex.message = httpException.response().errorBody().string();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    ex.message = e1.getMessage();
                }
            }

        } else if (e instanceof SocketTimeoutException) {
            ex = new FxEception(e, ERROR.TIMEOUT_ERROR);
            ex.message = "网络连接超时，请检查您的网络状态，稍后重试！";

        } else if (e instanceof ConnectException) {
            ex = new FxEception(e, ERROR.TIMEOUT_ERROR);
            ex.message = "网络连接异常，请检查您的网络状态，稍后重试！";

        } else if (e instanceof ConnectTimeoutException) {
            ex = new FxEception(e, ERROR.TIMEOUT_ERROR);
            ex.message = "网络连接超时，请检查您的网络状态，稍后重试！";

        } else if (e instanceof UnknownHostException) {
            ex = new FxEception(e, ERROR.TIMEOUT_ERROR);
            ex.message = "网络连接异常，请检查您的网络状态，稍后重试！";

        } else if (e instanceof NullPointerException) {
            if (BuildConfig.DEBUG) {
                ex = new FxEception(e, ERROR.NULL_POINTER_EXCEPTION);
                ex.message = "空指针异常";
            }
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            if (BuildConfig.DEBUG) {
                ex = new FxEception(e, ERROR.SSL_ERROR);
                ex.message = "证书验证失败";
            }
        } else if (e instanceof ClassCastException) {
            ex = new FxEception(e, ERROR.CAST_ERROR);
            ex.message = "类型转换错误";

        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof JsonSyntaxException
                || e instanceof JsonSerializer
                || e instanceof NotSerializableException
                || e instanceof ParseException) {
            ex = new FxEception(e, ERROR.PARSE_ERROR);
            MyLog.i("test","json: " +e.getMessage());
            ex.message = "解析错误";

        } else if (e instanceof IllegalStateException) {
            ex = new FxEception(e, ERROR.ILLEGAL_STATE_ERROR);
            ex.message = e.getMessage();

        } else {
            if (BuildConfig.DEBUG) {
                ex = new FxEception(e, ERROR.UNKNOWN);
                ex.message = "未知错误";
            }
        }
        if (ex != null) {
            ex.message = " 网络不给力,请检查您的网络 " + ex.code;
            if (isShowError) {
                Toast.makeText(App.getAppContext(), ex.message, Toast.LENGTH_SHORT).show();
            }
            return ex;
        } else {
            return new FxEception(new Exception(""), ERROR.UNKNOWN);
        }

    }

    /**
     * 约定异常
     */
    public static class ERROR {
        /**
         * 未知错误
         */
        public static final int UNKNOWN = 1000;
        /**
         * 连接超时
         */
        public static final int TIMEOUT_ERROR = 1001;
        /**
         * 空指针错误
         */
        public static final int NULL_POINTER_EXCEPTION = 1002;

        /**
         * 证书出错
         */
        public static final int SSL_ERROR = 1003;

        /**
         * 类转换错误
         */
        public static final int CAST_ERROR = 1004;

        /**
         * 解析错误
         */
        public static final int PARSE_ERROR = 1005;

        /**
         * 非法数据异常
         */
        public static final int ILLEGAL_STATE_ERROR = 1006;


    }
}
