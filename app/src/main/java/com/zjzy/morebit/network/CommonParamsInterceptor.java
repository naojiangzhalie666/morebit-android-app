package com.zjzy.morebit.network;


import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.zjzy.morebit.App;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.NetDoorUtils;
import com.zjzy.morebit.utils.encrypt.AESOperator;
import com.zjzy.morebit.utils.encrypt.EncryptUtlis;
import com.zjzy.morebit.utils.encrypt.MD5Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by YangBoTian on 2018/6/7 11:35
 */

public class CommonParamsInterceptor implements Interceptor {


    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        String getSysTime = System.currentTimeMillis() + "";
        String getNonceStr = NetDoorUtils.getNonceStr();

        if (request.method().equals("GET")) {
            HttpUrl httpUrl = request.url().newBuilder()
//                    .addQueryParameter("token", NetDoorUtils.getToken())
//                    .addQueryParameter("noncestr", getNonceStr)
//                    .addQueryParameter("timestamp", getSysTime)
//                    .addQueryParameter("sign", NetDoorUtils.getSha1Str(getSysTime, getNonceStr))
                    .build();

            try {
                String nonce = getNonce(getSysTime);
                Request.Builder builder = request.newBuilder()
                        .addHeader("version", C.Setting.app_version)
                        .addHeader("userAgent", C.Setting.user_agent)
                        .addHeader("deviceId", C.Setting.device_id)
                        .addHeader("os", C.Setting.os+"")
                        .addHeader("timestamp", getSysTime)
                        .addHeader("nonce", nonce);
                String encryptToken = getEncryptToken(getSysTime, nonce);
                if (TextUtils.isEmpty(encryptToken)) {
                    builder.addHeader("token",encryptToken);
                }
                MyLog.i("CommonParamsInterceptor", "head  timestamp= " + getSysTime);
                MyLog.i("CommonParamsInterceptor", "head  nonce= " + nonce);
                MyLog.i("CommonParamsInterceptor", "head  token= " + encryptToken);
                request = builder.url(httpUrl).build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (request.method().equals("POST")) {
            Request.Builder builder = request.newBuilder();
            String nonce = getNonce(getSysTime);
            if (request.body() instanceof FormBody) {
                try {

                    // 添加请求内容
                    JSONObject requestData = addRequestBody(request);
                    String requestDataStr = requestData.toString();
                    MyLog.i("CommonParamsInterceptor", "requestDataStr= " + requestDataStr);
                    request = getRequestBody(request, getSysTime, builder, nonce, requestDataStr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } /*else if (request.body() instanceof RequestBody) {
                String requestDataString = getRequestJSONObject(request);
                request = getRequestBody(request, getSysTime, builder, nonce, requestDataString);
            } */else {
                addRequestHead(getSysTime, nonce, builder);
                request = builder.post(request.body()).build();
            }
        }
        return chain.proceed(request);

    }

    /**
     * 获取body
     *
     * @param request
     * @param getSysTime
     * @param builder
     * @param nonce
     * @param requestDataStr
     * @return
     */
    private Request getRequestBody(Request request, String getSysTime, Request.Builder builder, String nonce, String requestDataStr) {
        if (!TextUtils.isEmpty(requestDataStr)) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestDataStr);
            addRequestHead(getSysTime, nonce, builder);
            request = builder.post(requestBody).build();
        }
        return request;
    }

    /**
     * @param request
     * @return
     */
    @NonNull
    private String getRequestJSONObject(Request request) {
        String requestDataString = "";
        JSONObject requestData = new JSONObject();
        try {
            String s = request.url().url().toString();
            Map<String, String> stringStringMap = AppUtil.URLRequest(s);
            for (Map.Entry<String, String> entry : stringStringMap.entrySet()) {
                String key = entry.getKey().toString();
                String value = entry.getValue().toString();
                requestData.put(key, value);
            }
            requestDataString = requestData.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestDataString;
    }

    /**
     * 添加请求头
     *
     * @param getSysTime
     * @param nonce
     * @param builder
     */
    private void addRequestHead(String getSysTime, String nonce, Request.Builder builder) {
        builder.addHeader("version", C.Setting.app_version)
                .addHeader("userAgent", C.Setting.user_agent)
                .addHeader("deviceId", C.Setting.device_id)
                .addHeader("os", C.Setting.os+"")
                .addHeader("timestamp", getSysTime)
                .addHeader("nonce", nonce);
        String encryptToken = getEncryptToken(getSysTime, nonce);
        if (!TextUtils.isEmpty(encryptToken)) {
            builder.addHeader("token",encryptToken);
        }
        MyLog.i("CommonParamsInterceptor", "head  version= " + C.Setting.app_version);
        MyLog.i("CommonParamsInterceptor", "head  userAgent= " + C.Setting.user_agent);
        MyLog.i("CommonParamsInterceptor", "head  timestamp= " + getSysTime);
        MyLog.i("CommonParamsInterceptor", "head  nonce= " + nonce);
        MyLog.i("CommonParamsInterceptor", "head  token= " + encryptToken);
    }

    /**
     * 添加请求体
     *
     * @param request
     * @return
     */
    @NonNull
    private JSONObject addRequestBody(Request request) {
        FormBody formBody = (FormBody) request.body();
        boolean isSign = false;
        JSONObject requestData = new JSONObject();
        try {
            for (int i = 0; i < formBody.size(); i++) {
                if ("sign".equals(formBody.encodedName(i))) {
                    isSign = true;
                }
                requestData.put(formBody.encodedName(i), AppUtil.StringDecoder(formBody.encodedValue(i)));
            }

            if (isSign) {
                TreeMap<Object, Object> map = new TreeMap();
                for (int i = 0; i < formBody.size(); i++) {
                    if (!"sign".equals(formBody.encodedName(i))) {
                        map.put(formBody.encodedName(i), AppUtil.StringDecoder(formBody.encodedValue(i)));
                    }
                }
                String sign = EncryptUtlis.getSign(map);
                requestData.put("sign", sign);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestData;
    }


    /**
     * 获取 nonce
     *
     * @param getSysTime
     * @return
     */
    private String getNonce(String getSysTime) {
        String random = getRandom(0, 1000);
        String s = getSysTime + random;
        String s1 = MD5Utils.MD5Encode(s, "UTF-8");
        return s1 == null ? "" : s1;
    }

    /**
     * 获取 随机数
     *
     * @param min
     * @param max
     * @return
     */
    public String getRandom(int min, int max) {
        Random random = App.getRandom();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return String.valueOf(s);
    }

    /**
     * 获取 token
     *
     * @param sysTime
     * @param nonce
     * @return
     */
    public static String getEncryptToken(String sysTime, String nonce) {
        String key = sysTime + nonce;
        MyLog.i("CommonParamsInterceptor", "head  key= " + key);
        String md5Key = MD5Utils.MD5Encode(key, "UTF-8");
        MyLog.i("CommonParamsInterceptor", "head  md5Key= " + md5Key);

        String token = "";
        String nativeToken = UserLocalData.getToken();
        MyLog.i("CommonParamsInterceptor", "head  nativeToken= " + nativeToken);
        if (!TextUtils.isEmpty(nativeToken)) {
//            String encrypt = AESUtil.encrypt(nativeToken, md5Key);
            if (!TextUtils.isEmpty(md5Key) && md5Key.length() >= 16) {
                String substring = md5Key.substring(0, 16);
                MyLog.i("CommonParamsInterceptor", "head  substring= " + substring);
                String encrypt = AESOperator.getInstance().encrypt(nativeToken, substring);
                token = encrypt == null ? "" : encrypt;
                MyLog.i("CommonParamsInterceptor", "head  encrypt= " + encrypt);
            }
        }
        return token;
    }
}
