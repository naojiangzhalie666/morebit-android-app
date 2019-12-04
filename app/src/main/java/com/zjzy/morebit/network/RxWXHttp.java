package com.zjzy.morebit.network;


import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.zjzy.morebit.BuildConfig;
import com.zjzy.morebit.Module.service.WXService;
import com.zjzy.morebit.utils.MyLog;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;


/**
 * Created by YangBoTian on 2018/6/6 20:54
 */

public class RxWXHttp {
    private Map<String, Retrofit> mRetrofitMap = new HashMap<>();
    HttpLoggingInterceptor loggingInterceptor = null;
    private RxWXHttp() {
        if (BuildConfig.DEBUG) {
            loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    //打印retrofit日志
                    MyLog.i("RetrofitLog", "retrofitBack = " + message);
                }
            });
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }
    }

    /**
     * 单例模式
     *
     * @return
     */
    public static RxWXHttp getInstance() {
        return RxHttpHolder.sInstance;
    }

    private static class RxHttpHolder {
        private final static RxWXHttp sInstance = new RxWXHttp();
    }

    public Retrofit getRetrofit(String serverUrl) {
        Retrofit retrofit;
        if (mRetrofitMap.containsKey(serverUrl)) {
            retrofit = mRetrofitMap.get(serverUrl);
        } else {
            retrofit = createRetrofit(serverUrl);
            mRetrofitMap.put(serverUrl, retrofit);
        }
        return retrofit;
    }

    public WXService getService(String baseUrl) {
        return getRetrofit(baseUrl).create(WXService.class);
    }

    /**
     * @param baseUrl
     * @return
     */
    private Retrofit createRetrofit(String baseUrl) {

        OkHttpClient.Builder client = new OkHttpClient().newBuilder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS);
//                .addInterceptor(new CommonParamsInterceptor());
        if (BuildConfig.DEBUG && loggingInterceptor != null) {
            client.addInterceptor(loggingInterceptor).addNetworkInterceptor(new StethoInterceptor());
        }
        OkHttpClient build = client.build();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(FastJsonConverterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create(new Gson()))
//                .addConverterFactory(CustomGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(build)
                .build();
    }

//
//        OkHttpClient client = new OkHttpClient().newBuilder()
//                .readTimeout(30, TimeUnit.SECONDS)
//                .connectTimeout(30, TimeUnit.SECONDS)
//                .addInterceptor(new CommonParamsInterceptor());
//        if (BuildConfig.DEBUG && loggingInterceptor != null) {
//            client.addInterceptor(loggingInterceptor);
//        }
//        OkHttpClient build = client.build();
//
//        return new Retrofit.Builder()
//                .baseUrl(baseUrl)
//                .addConverterFactory(FastJsonConverterFactory.create())
////                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .client(client)
//                .build();
//    }
}
