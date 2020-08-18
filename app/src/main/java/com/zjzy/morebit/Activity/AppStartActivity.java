package com.zjzy.morebit.Activity;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.gyf.barlibrary.ImmersionBar;
import com.zjzy.morebit.App;
import com.zjzy.morebit.MainActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.network.CallBackObserver;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.DateTimeUtils;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.SharedPreferencesUtils;
import com.zjzy.morebit.utils.helper.ActivityLifeHelper;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zjzy.morebit.view.AspectRatioView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;


/**
 * App启动页-逻辑跳转用  111
 * Created by Administrator on 2017/9/11 0011.
 */

public class AppStartActivity extends RxAppCompatActivity {

    private ImageView mBgAd;

    TextView mSkip;
    private Disposable mdDisposable;

    VideoView mVideoView;
    private Uri h5Uri;
    private AspectRatioView ar_title_banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppStart);
      //设置用户信息，作为展示的 name
//        OneApmConfig.setLoginUserName(UserLocalData.getUser().getPhone());
        // 避免从桌面启动程序后，会重新实例化入口类的activity
        try {
            h5Uri = getIntent().getData();
            if (h5Uri != null) {
                if (ActivityLifeHelper.getInstance().isContainsActivity(MainActivity.class)) {
                    MyLog.d("AppStartActivityHelper ", "    sendWebOpenApp  ");
                    AppUtil.sendWebOpenApp(h5Uri, this);
                    finish();
                    return;
                }

            }
//            if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            if (ActivityLifeHelper.getInstance().isContainsActivity(MainActivity.class)) {
                MyLog.d("AppStartActivityHelper ", "    finish  ");
                finish();
                return;
            }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
                .init();
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        setContentView(R.layout.activity_appstart);
        try {
            initRun();//初始化
        } catch (Exception e) {
            Intent intent = new Intent(AppStartActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    private void initViews() {
        String isFirstStart = (String) SharedPreferencesUtils.get(AppStartActivity.this, "isFirstStart", "");
        if (TextUtils.isEmpty(isFirstStart)) {
            Intent intent = new Intent(AppStartActivity.this, FirstStartActivity.class);
            startActivity(intent);
            finish();
        } else {
            mBgAd = (ImageView) findViewById(R.id.imgBg);
            mSkip = (TextView) findViewById(R.id.skip);
            ar_title_banner=findViewById(R.id.ar_title_banner);
            mSkip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    statrMain(null);
                }
            });
            showBanner();
        }
    }


    /**
     * 显示广告
     */
    private void showBanner() {
        List<ImageInfo> imageInfoList = (ArrayList) App.getACache().getAsObject(C.sp.appStartImg);
        if (imageInfoList != null && imageInfoList.size() > 0) {
            MyLog.i("test", "size: " + imageInfoList.size());
            Collections.shuffle(imageInfoList);
            MyLog.i("test", "title: " + imageInfoList.get(0).getTitle());
            final ImageInfo imageInfo = imageInfoList.get(0);
            boolean IsToday = false;
            if (imageInfo.getPopupType() == 1) {
                long popupTypeTime = App.getACache().getAsLong(C.sp.APPSTART_POPUPTYPE + imageInfo.getId());
                if (popupTypeTime != 0) {
                    IsToday = DateTimeUtils.getPastDate(popupTypeTime);
                    MyLog.i("test", "isShowAdv: " + IsToday);
                }
            }
            if (!IsToday) {
                App.getACache().put(C.sp.APPSTART_POPUPTYPE + imageInfo.getId(), System.currentTimeMillis() + "");
                if (imageInfo.getMediaType() == 1) {
                    ar_title_banner.setVisibility(View.GONE);
                    playerVideo(imageInfo);
                } else {
                    ar_title_banner.setVisibility(View.VISIBLE);
                    showLocalPicture(imageInfo);
                }
            } else {
                statrMain(null);
            }
        } else {
            statrMain(null);
        }
    }

    /**
     * 播放视频
     *
     * @param imageInfo
     */
    private void playerVideo(ImageInfo imageInfo) {
        String videoPath = imageInfo.videoPath;
        if (TextUtils.isEmpty(videoPath) ) {
            statrMain(null);
            return;
        }



        mVideoView = (VideoView) findViewById(R.id.videoView);
        RelativeLayout rl_videoView = (RelativeLayout) findViewById(R.id.rl_videoView);
        rl_videoView.setVisibility(View.VISIBLE);




        try {
            Uri uri = Uri.parse(videoPath);


            //设置视频路径
            mVideoView.setVideoURI(uri);
            //开始播放视频
            mVideoView.start();

            countDown();
        } catch (Exception e) {
            e.printStackTrace();
            statrMain(null);
        }
    }

    private void countDown() {
        MyLog.i("test", "读秒");
        mSkip.setVisibility(View.VISIBLE);
        //从0开始发射61个数字为：0-60依次输出，延时0s执行，每1s发射一次。
        mdDisposable = Flowable.intervalRange(0, 3, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        int seconds = (int) (3 - aLong);
                        mSkip.setText(getString(R.string.start_skip, " " + seconds));
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        statrMain(null);
                    }
                })
                .subscribe();
    }


    private void initRun() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initViews();
            }
        }, 300);
    }

    /**
     * 加载广告
     *
     * @param imageInfo
     */
    private void showLocalPicture(final ImageInfo imageInfo) {
        if (imageInfo != null && !TextUtils.isEmpty(imageInfo.getPicture())) {
            MyLog.i("test", "asString: " + imageInfo.getPicture());
           // mBgAd.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                LoadImgUtils.setImg(this, mBgAd, imageInfo.getPicture(), false);
            if(imageInfo.getPicture().startsWith("http")){
                LoadImgUtils.getImgToFileObservable(this, imageInfo.getPicture())
                        .subscribe(new CallBackObserver<File>() {
                            @Override
                            public void onNext(File file) {
                                imageInfo.setPicture(file.getAbsolutePath());
                                showLocalPicture(imageInfo);

                            }

                            @Override
                            public void onError(Throwable e) {
                                statrMain(null);

                            }
                        });
            }else{
                try {
                    LoadImgUtils.setImg(this, mBgAd, imageInfo.getPicture() , false, new LoadImgUtils.GlideLoadListener() {
                        @Override
                        public void loadError() {
                            mBgAd.setVisibility(View.GONE);
                            statrMain(null);
                        }

                        @Override
                        public void loadSuccess() {

                            ar_title_banner.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    statrMain(imageInfo);
                                }
                            });
                            countDown();
                        }
                    });
                }catch (Exception e) {
                    statrMain(null);
                } catch (OutOfMemoryError outOfMemoryError) {
                    statrMain(null);
                }

            }

        } else {
            statrMain(null);
        }
    }


    public void changeIcon(String activityPath) {
        PackageManager pm = App.getAppContext().getPackageManager();
        pm.setComponentEnabledSetting(getComponentName(),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(new ComponentName(this, activityPath),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        //重启桌面 加速显示
//        restartSystemLauncher(pm);
        //Intent 重启 Launcher 应用
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        List<ResolveInfo> resolves = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo res : resolves) {
            if (res.activityInfo != null) {
                ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                am.killBackgroundProcesses(res.activityInfo.packageName);
            }
        }
    }

    private void switchIcon(int useCode) {
        try {
            //要跟manifest的activity-alias 的name保持一致
            String icon_tag = "com.zjzy.morebit.default";
            String icon_tag_1212 = "com.zjzy.morebit.login1";

            if (useCode != 3) {
                PackageManager pm = getPackageManager();
                ComponentName normalComponentName = new ComponentName(getBaseContext(), icon_tag);
                //正常图标新状态
                int normalNewState = useCode == 2 ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                        : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
                if (pm.getComponentEnabledSetting(normalComponentName) != normalNewState) {//新状态跟当前状态不一样才执行
                    pm.setComponentEnabledSetting(
                            normalComponentName,
                            normalNewState,
                            PackageManager.DONT_KILL_APP);
                }

                ComponentName actComponentName = new ComponentName(getBaseContext(), icon_tag_1212);
                //正常图标新状态
                int actNewState = useCode == 1 ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                        : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
                if (pm.getComponentEnabledSetting(actComponentName) != actNewState) {//新状态跟当前状态不一样才执行
                    pm.setComponentEnabledSetting(
                            actComponentName,
                            actNewState,
                            PackageManager.DONT_KILL_APP);
                }
//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.addCategory(Intent.CATEGORY_HOME);
//                intent.addCategory(Intent.CATEGORY_DEFAULT);
//                List<ResolveInfo> resolves = pm.queryIntentActivities(intent, 0);
//                for (ResolveInfo res : resolves) {
//                    if (res.activityInfo != null) {
//                        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//                        am.killBackgroundProcesses(res.activityInfo.packageName);
//                    }
//                }
            }
        } catch (Exception e) {
        }
    }


    private void statrMain(ImageInfo imageInfo) {
        Log.e("hhhh","1");
        Intent intent = new Intent(AppStartActivity.this, MainActivity.class);
        if (mdDisposable != null) {
            mdDisposable.dispose();
        }
        Log.e("hhhh","2"+mdDisposable);
        if (imageInfo != null) {
            intent.putExtra("imageInfo", imageInfo);
        }
        Log.e("hhhh","3"+imageInfo);
        if (null != h5Uri) {
            intent.setData(h5Uri);
        }
        Log.e("hhhh","4"+h5Uri);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
        if (mdDisposable != null) {
            mdDisposable.dispose();
        }
        if (mVideoView != null) {
            mVideoView.stopPlayback();
            mVideoView.setOnCompletionListener(null);
            mVideoView.setOnPreparedListener(null);
            mVideoView = null;
        }
    }

}
