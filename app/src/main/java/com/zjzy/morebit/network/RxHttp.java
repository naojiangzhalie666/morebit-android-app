package com.zjzy.morebit.network;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.zjzy.morebit.BuildConfig;
import com.zjzy.morebit.Module.service.CommonService;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.MyLog;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by liuhaiping on 2019/12/6 20:54
 */

public class RxHttp {
    private Map<String, Retrofit> mRetrofitMap = new HashMap<>();
    HttpLoggingInterceptor loggingInterceptor = null;
    private boolean mIsFast;

    private RxHttp() {
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
    public static RxHttp getInstance() {
        return RxHttpHolder.sInstance;
    }

    private static class RxHttpHolder {
        private final static RxHttp sInstance = new RxHttp();
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


    public CommonService getCommonService() {
        return getRetrofit(C.getInstance().getGoodsIp()).create(CommonService.class);
    }

    public CommonService getCommonService(String url) {
        return getRetrofit(url).create(CommonService.class);
    }

    public CommonService getUsersService() {
        return getRetrofit(C.getInstance().getGoodsIp()).create(CommonService.class);
    }

    public CommonService getGoodsService() {
        return getRetrofit(C.getInstance().getGoodsIp()).create(CommonService.class);
    }

    public CommonService getSysteService() {
        return getRetrofit(C.getInstance().getGoodsIp()).create(CommonService.class);
    }

    public CommonService getOrdersService() {
        return getRetrofit(C.getInstance().getGoodsIp()).create(CommonService.class);
    }



    /**
     *
     *
     * @param baseUrl
     * @return
     */
    private Retrofit createRetrofit(String baseUrl) {
        OkHttpClient.Builder client = new OkHttpClient().newBuilder()
                .readTimeout(3000, TimeUnit.SECONDS)
                .connectTimeout(3000, TimeUnit.SECONDS)
                .addInterceptor(new CommonParamsInterceptor());
        if (BuildConfig.DEBUG && loggingInterceptor != null) {
            client.addInterceptor(loggingInterceptor).addNetworkInterceptor(new StethoInterceptor());
        }
        OkHttpClient build = client.build();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
//                .addConverterFactory(FastJsonConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
//                .addConverterFactory(factory)
//                .addConverterFactory(CustomGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(build)
                .build();
    }
}
