package com.markermall.cat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeInitCallback;
import com.facebook.stetho.Stetho;
import com.markermall.cat.LocalData.UserLocalData;
import com.markermall.cat.adapter.MarkermallCircleAdapter;
import com.markermall.cat.network.BaseResponse;
import com.markermall.cat.network.RxHttp;
import com.markermall.cat.network.RxUtils;
import com.markermall.cat.pojo.event.CirleUpdataShareCountEvent;
import com.markermall.cat.pojo.request.RequestCircleShareCountBean;
import com.markermall.cat.utils.AppUtil;
import com.markermall.cat.utils.C;
import com.markermall.cat.utils.LogUtils;
import com.markermall.cat.utils.action.ACache;
import com.markermall.cat.utils.encrypt.EncryptUtlis;
import com.markermall.cat.utils.fire.BuglyUtils;
import com.markermall.cat.utils.fire.DeviceIDUtils;
import com.mob.tools.proguard.ProtectedMemberKeeper;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.player.SystemPlayerManager;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.interfaces.BetaPatchListener;
import com.tencent.bugly.crashreport.CrashReport;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;


public class App extends Application implements ProtectedMemberKeeper {

    private static Context context;
    private static ACache mACache;
    private static Random random = null;
    private static boolean mIsForeground;
    public static Handler mHandler;
  //  public static boolean mIsHotAppUpdataFailure = false;
//    public static RefWatcher mRefWatcher;

    public static Context getAppContext() {
        return context;
    }

    public void onCreate() {
        super.onCreate();

        try {
            mHandler = new Handler();
            registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
            context = getApplicationContext();
            mACache = ACache.get(context);  // 本地缓存 data目录下

            if (BuildConfig.DEBUG) {
                //用于调试
                Stetho.initializeWithDefaults(this);
            }
            InitializeService.start(this); //初始化第三方工具和相关
            initBuglyHotFix();
            initBaiDuStat();
//            initJAnalyStat();
            // 网络请求最终错误回调
            RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
                @Override
                public void accept(@NonNull Throwable throwable) throws Exception {
                    BuglyUtils.putErrorToBugly(throwable);
                    Log.i("test", "test: " + throwable.toString());
                }
            });


            ZXingLibrary.initDisplayOpinion(this);

            String userAgent = System.getProperty("http.agent");
            if (!TextUtils.isEmpty(userAgent)) {
                C.Setting.user_agent = userAgent;
            }
            String deviceId = DeviceIDUtils.getDeviceId(App.getAppContext());
            if (!TextUtils.isEmpty(deviceId)) {
                C.Setting.device_id = deviceId;
            }
            String versionCode = AppUtil.getVersionCode(App.getAppContext());
            if (!TextUtils.isEmpty(versionCode)) {
                C.Setting.app_version = versionCode;
            }
            LogUtils.Log("AppTag", "userAgent  " + userAgent);
            LogUtils.Log("AppTag", "deviceId  " + deviceId);
            LogUtils.Log("AppTag", "app_version  " + versionCode);

//            setupLeakCanary();
        } catch (Exception e) {
            e.printStackTrace();
        }
        PlayerFactory.setPlayManager(SystemPlayerManager.class);//系统模式


        //电商SDK初始化
        AlibcTradeSDK.asyncInit((Application) this.getApplicationContext(), new AlibcTradeInitCallback() {
            @Override
            public void onSuccess() {
            }
            @Override
            public void onFailure(int code, String msg) {
            }
        });
       // initBidDataAPI();

    }

    private void initBaiDuStat() {

//        // 打开调试开关，可以查看logcat日志。版本发布前，为避免影响性能，移除此代码
//        // 查看方法：adb logcat -s sdkstat
//        StatService.setDebugOn(false);
//
//        // 开启自动埋点统计，为保证所有页面都能准确统计，建议在Application中调用。
//        // 第三个参数：autoTrackWebview：
//        // 如果设置为true，则自动track所有webview；如果设置为false，则不自动track webview，
//        // 如需对webview进行统计，需要对特定webview调用trackWebView() 即可。
//        // 重要：如果有对webview设置过webchromeclient，则需要调用trackWebView() 接口将WebChromeClient对象传入，
//        // 否则开发者自定义的回调无法收到。
//        StatService.autoTrace(this, true, true);

    }


    public static ACache getACache() {
        if (mACache == null) {
            mACache = ACache.get(context);
        }
        return mACache;
    }


    public App() {
    }


    /**
     * 初始化热更新配置
     */
    private void initBuglyHotFix() {
        //	setStrictMode();
        // 设置是否开启热更新能力，默认为true
        Beta.enableHotfix = true;
        // 设置是否自动下载补丁
        Beta.canAutoDownloadPatch = true;
        // 设置是否提示用户重启
        Beta.canNotifyUserRestart = false;
        // 设置是否自动合成补丁
        Beta.canAutoPatch = true;

        /**
         *  全量升级状态回调
         */
//		Beta.upgradeStateListener = new UpgradeStateListener() {
//			@Override
//			public void onUpgradeFailed(boolean b) {
//
//			}
//
//			@Override
//			public void onUpgradeSuccess(boolean b) {
//
//			}
//
//			@Override
//			public void onUpgradeNoVersion(boolean b) {
//				Toast.makeText(getApplicationContext(), "最新版本", Toast.LENGTH_SHORT).show();
//			}
//
//			@Override
//			public void onUpgrading(boolean b) {
//				Toast.makeText(getApplicationContext(), "onUpgrading", Toast.LENGTH_SHORT).show();
//			}
//
//			@Override
//			public void onDownloadCompleted(boolean b) {
//
//			}
//		};

        /**
         * 补丁回调接口，可以监听补丁接收、下载、合成的回调
         */
        Beta.betaPatchListener = new BetaPatchListener() {
            @Override
            public void onPatchReceived(String patchFileUrl) {
                Log.i("test", "onPatchReceived: " + patchFileUrl);
                //Toast.makeText(getApplicationContext(), patchFileUrl, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDownloadReceived(long savedLength, long totalLength) {
                Log.i("test", "onDownloadReceived: " + savedLength);
//				Toast.makeText(getApplicationContext(), String.format(Locale.getDefault(),
//						"%s %d%%",
//						Beta.strNotificationDownloading,
//						(int) (totalLength == 0 ? 0 : savedLength * 100 / totalLength)), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDownloadSuccess(String patchFilePath) {
                Log.i("test", "onDownloadSuccess: " + patchFilePath);
                //Toast.makeText(getApplicationContext(), patchFilePath, Toast.LENGTH_SHORT).show();
//                Beta.applyDownloadedPatch();
                //mIsHotAppUpdataFailure = true;
            }

            @Override
            public void onDownloadFailure(String msg) {
                Log.i("test", "onDownloadFailure: " + msg);
                //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onApplySuccess(String msg) {
              //  mIsHotAppUpdataFailure = false;
                Log.i("test", "onApplyFailure: " + msg);
                // LogUtils.Log("test", "onApplySuccess: " + msg);
                //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onApplyFailure(String msg) {
               // mIsHotAppUpdataFailure = true;
                Log.i("test", "onApplyFailure: " + msg);
                //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPatchRollback() {
                Log.i("test", "onPatchRollback");
                //Toast.makeText(getApplicationContext(), "onPatchRollback", Toast.LENGTH_SHORT).show();
            }
        };

        long start = System.currentTimeMillis();
//        // 这里实现SDK初始化，appId替换成您的在Bugly平台申请的appId,调试时将第三个参数设置为true
//        String channelName = getChannelName();
//        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(App.getAppContext());
//        String versionName = AppUtil.getVersionName(App.getAppContext());
////...在`这里设置strategy的属性，在bugly初始化时传入
//        strategy.setAppChannel(channelName);  //设置渠道
//
//        strategy.setAppVersion(TextUtils.isEmpty(versionName) ? App.getAppContext().getString(R.string.app_name) : versionName);      //App的版本
//        ToastUtil.show(channelName);

        Bugly.init(this, "3040a7dbe1", BuildConfig.DEBUG);
        CrashReport.setIsDevelopmentDevice(this,BuildConfig.DEBUG);
        //自定义用户的数据
        try {
            if(null != UserLocalData.getUser() && !TextUtils.isEmpty(UserLocalData.getUser().getPhone())){
                CrashReport.setUserId(UserLocalData.getUser().getPhone());
            }
        } catch (Exception e){

        }
        long end = System.currentTimeMillis();
        LogUtils.Log("init time--->", end - start + "ms");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);

        // 安装tinker
        Beta.installTinker();

        fixOppoAssetManager();
    }


    /**
     * Activity 生命周期监听，用于监控app前后台状态切换
     */
    ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacks() {


        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(Activity activity) {
            if (activityAount == 0) {
                //app回到前台
                mIsForeground = true;
            }

            activityAount++;
        }

        @Override
        public void onActivityResumed(Activity activity) {
            LogUtils.Log("CircleDayHotFrgment", "onActivityResumed");
        }

        @Override
        public void onActivityPaused(Activity activity) {
            LogUtils.Log("CircleDayHotFrgment", "onActivityPaused");
        }

        @Override
        public void onActivityStopped(Activity activity) {
            activityAount--;
            if (activityAount == 0) {
                mIsForeground = false;
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (MarkermallCircleAdapter.mShareCountId != 0) {
                            int shareCountId = MarkermallCircleAdapter.mShareCountId;
                            shareCount(shareCountId);
                            MarkermallCircleAdapter.mShareCountId = 0;
                        }
                        LogUtils.Log("CircleDayHotFrgment", "onActivityStopped");
                    }
                }, 500);

            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
        }
    };
    /**
     * 当前Acitity个数
     */
    private int activityAount = 0;


    /**
     * 分享增加后台分享数
     *
     * @param id
     */
    @SuppressLint("CheckResult")
    public void shareCount(int id) {
        try {

            RequestCircleShareCountBean requestBean = new RequestCircleShareCountBean();
            requestBean.setId(id+"");
            String sign = EncryptUtlis.getSign2(requestBean);
            requestBean.setSign(sign);

            RxHttp.getInstance().getOrdersService()
                    .getMarkermallShareCount(requestBean)
                    .compose(RxUtils.<BaseResponse>switchSchedulers())
                    .subscribe(new Consumer<BaseResponse>() {
                        @Override
                        public void accept(BaseResponse response) throws Exception {
                            EventBus.getDefault().post(new CirleUpdataShareCountEvent());
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static synchronized Random getRandom() {
        if (random == null) {
            random = new Random();
        }
        return random;
    }

    /**
     * 埋点sdk初始化
     */
    private void initBidDataAPI() {
        SensorsDataAPI.sharedInstance(this, "");
        List<SensorsDataAPI.AutoTrackEventType> eventTypeList = new ArrayList<>();
        eventTypeList.add(SensorsDataAPI.AutoTrackEventType.APP_START);
        eventTypeList.add(SensorsDataAPI.AutoTrackEventType.APP_END);
        eventTypeList.add(SensorsDataAPI.AutoTrackEventType.APP_VIEW_SCREEN);
        eventTypeList.add(SensorsDataAPI.AutoTrackEventType.APP_CLICK);
        SensorsDataAPI.sharedInstance(this).enableAutoTrack(eventTypeList);
        SensorsDataAPI.sharedInstance(App.getAppContext()).enableLog(BuildConfig.DEBUG == true? true:false);
    }


    /**
     * 关掉这个负责计时的,此问题只有R9和A5出现
     */
    private void fixOppoAssetManager() {
        try {
            String device = AppUtil.getSystemModel();
            if (!TextUtils.isEmpty(device)) {
                if (device.contains("OPPO R9") || device.contains("OPPO A5")) {

                        // 关闭掉FinalizerWatchdogDaemon
                        Class clazz = Class.forName("java.lang.Daemons$FinalizerWatchdogDaemon");
                        Method method = clazz.getSuperclass().getDeclaredMethod("stop");
                        method.setAccessible(true);
                        Field field = clazz.getDeclaredField("INSTANCE");
                        field.setAccessible(true);
                        method.invoke(field.get(null));

                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
