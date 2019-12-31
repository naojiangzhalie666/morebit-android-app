package com.zjzy.morebit.network;


import com.zjzy.morebit.BuildConfig;
import com.zjzy.morebit.Module.service.CommonService;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.MyLog;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by liuhaiping on 2012/12/6 20:54
 */

public class RxFastJsonHttp {
    private final String BASE_URL = C.BASE_MOREBIT;
    private Map<String, Retrofit> mRetrofitMap = new HashMap<>();
    HttpLoggingInterceptor loggingInterceptor = null;
    private boolean mIsFast;

    private RxFastJsonHttp() {
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
    public static RxFastJsonHttp getInstance() {
        return RxHttpHolder.sInstance;
    }

    private static class RxHttpHolder {
        private final static RxFastJsonHttp sInstance = new RxFastJsonHttp();
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
        return getRetrofit(BASE_URL).create(CommonService.class);
    }

    public RxFastJsonHttp swicthFastFactory(boolean isFast) {
        this.mIsFast = isFast;
        return RxHttpHolder.sInstance;
    }

    /**
     * @param baseUrl
     * @return
     */
    private Retrofit createRetrofit(String baseUrl) {
        Converter.Factory factory = null;
        if (mIsFast) {
            factory = FastJsonConverterFactory.create();
        } else {
            factory = GsonConverterFactory.create();

        }
        OkHttpClient.Builder client = new OkHttpClient().newBuilder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(new CommonParamsInterceptor());
        if (BuildConfig.DEBUG && loggingInterceptor != null) {
            client.addInterceptor(loggingInterceptor);
        }
        OkHttpClient build = client.build();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(FastJsonConverterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create(new Gson()))
//                .addConverterFactory(factory)
//                .addConverterFactory(CustomGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(build)
                .build();
    }
}
