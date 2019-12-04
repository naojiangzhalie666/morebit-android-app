package com.zjzy.morebit;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.pojo.SystemConfigBean;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.ConfigListUtlis;
import com.zjzy.morebit.utils.SensorsDataUtil;
import com.zjzy.morebit.utils.action.MyAction;
import com.mob.MobSDK;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * APP启动初始化第三方sdk
 */

public class InitializeService extends IntentService {

    private static final String ACTION_INIT_WHEN_APP_CREATE = "com.markermalll.cat.service.action.INIT";

    public InitializeService() {
        super("InitializeService");
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, InitializeService.class);
        intent.setAction(ACTION_INIT_WHEN_APP_CREATE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
           if (ACTION_INIT_WHEN_APP_CREATE.equals(action)) {
                toInIt();
            }
        }
    }

    private void toInIt() {
        initImageLoader(getApplicationContext());
//        MobSDK.init(this.getApplicationContext(), "23e1e9104c75c", "a35df204d7e2a887f752c730a6eda761"); //sharesdk注册
        MobSDK.init(this.getApplicationContext(), "23e1e9104c75c", "a35df204d7e2a887f752c730a6eda761"); //sharesdk注册
		JPushInterface.setDebugMode(!BuildConfig.DEBUG); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this.getApplicationContext());            // 初始化 JPush
//		AlibcTradeCommon.turnOnDebug();
//		AlibcTradeBiz.turnOnDebug();


        //初始化友盟统计
//		MobclickAgent.setDebugMode(true);
//		MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
//友盟统计
        String channel = AppUtil.getAppMetaData(this.getApplicationContext(), "UMENG_CHANNEL");
//			MobclickAgent.setDebugMode(true);
        MobclickAgent.setDebugMode(!BuildConfig.DEBUG);
        MobclickAgent.setCatchUncaughtExceptions(!BuildConfig.DEBUG); //是否需要错误统计功能
        MobclickAgent.startWithConfigure(
                new MobclickAgent.UMAnalyticsConfig(this.getApplicationContext(), "5b37a472b27b0a5eb100003a", channel,
                        MobclickAgent.EScenarioType.E_UM_NORMAL));

//		try {
        //获取渠道信息
//			String channel = ChannelReaderUtil.getChannel(getApplicationContext());
        //友盟统计
//			String channel = AppUtil.getAppMetaData(this,"UMENG_CHANNEL");
//			MobclickAgent.setDebugMode(true);
//			MobclickAgent.startWithConfigure(
//					 new MobclickAgent.UMAnalyticsConfig(this, "5aab990ca40fa314b60002ac", channel,
//					 MobclickAgent.EScenarioType.E_UM_NORMAL));

        //bugly错误监控
//			if (!TextUtils.isEmpty(channel)) {
//				Log.e("channelinfo", channel);
//                CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
//				strategy.setAppChannel(channel);
//                try {
//                    strategy.setAppVersion(AppUtil.getVersionName(getApplicationContext()));
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//                strategy.setAppPackageName("com.loveyou.aole");
//				CrashReport.initCrashReport(getApplicationContext(), "ae5e0fb76b", false,strategy);
//			}
//		}catch (Exception e){
//			e.printStackTrace();
//		}


        ConfigListUtlis.getConfigList(ConfigListUtlis.getConfigAllKey(), new MyAction.One<List<SystemConfigBean>>() {
            @Override
            public void invoke(List<SystemConfigBean> data) {
                if(null == data){
                    //获取失败。默认开
                    //初始化大数据库
                    initBiaData();
                }else{
                    SystemConfigBean todayBean = ConfigListUtlis.getSystemConfigBean(C.ConfigKey.ORDER_BIG_DATA_PUSH_SYSTEM);
                    if(null != todayBean && !TextUtils.isEmpty(todayBean.getSysValue()) && todayBean.getSysValue().equals("1")){
                        //初始化大数据库
                        initBiaData();
                    }
                }

            }
        });
    }

    public static void initImageLoader(Context context) {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory().cacheOnDisc().build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).defaultDisplayImageOptions(defaultOptions)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }

    public static void initBiaData(){
        SensorsDataUtil.initBidDataAPI();
        if(!TextUtils.isEmpty(UserLocalData.getToken())){
            SensorsDataUtil.getInstance().setUserInfo();
        }
    }
}
