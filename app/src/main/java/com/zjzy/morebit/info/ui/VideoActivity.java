package com.zjzy.morebit.info.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.gyf.barlibrary.ImmersionBar;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.player.CircleVideo;
import com.zjzy.morebit.utils.MyLog;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;

import butterknife.BindView;

/**
 * @Description:
 * @Author: liys
 * @CreateDate: 2019/3/22 18:35
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/3/22 18:35
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class VideoActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.video_item_player)
    CircleVideo gsyVideoPlayer;

    GSYVideoOptionBuilder gsyVideoOptionBuilder = new GSYVideoOptionBuilder();

    public static void start(Activity activity, String url, String title) {
        Intent intent = new Intent(activity, VideoActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        init();
    }

    public void init() {
        gsyVideoPlayer.findViewById(R.id.back).setOnClickListener(this);
        gsyVideoPlayer.findViewById(R.id.fullscreen).setOnClickListener(this);

        ImmersionBar.with(this)
                .statusBarDarkFont(true, 0.2f)
                .statusBarColor(R.color.transparent)
//                .fitsSystemWindows(true)
                .init();
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        String title = intent.getStringExtra("title");

        gsyVideoOptionBuilder
                .setIsTouchWiget(false)
                .setUrl(url)
                .setVideoTitle(title)
                .setCacheWithPlay(false)
                .setRotateViewAuto(true)
                .setLockLand(true)
//                .setPlayTag(TAG)
//                .setMapHeadData(header)
                .setShowFullAnimation(true)
                .setNeedLockFull(true)
//                .setPlayPosition(position)
                .setNeedShowWifiTip(true)
                .setVideoAllCallBack(new GSYSampleCallBack() {
                    @Override
                    public void onPrepared(String url, Object... objects) {
                        super.onPrepared(url, objects);
                        if (!gsyVideoPlayer.isIfCurrentIsFullscreen()) {
                            //静音
                            GSYVideoManager.instance().setNeedMute(true);
//                            tv_time.setVisibility(View.GONE);
//                            ll_count_container.setVisibility(View.GONE);
//                            sendStatistics(videoModel,action);
                        }

                    }

                    @Override
                    public void onQuitFullscreen(String url, Object... objects) {
                        super.onQuitFullscreen(url, objects);
                        //全屏不静音
                        GSYVideoManager.instance().setNeedMute(true);
                    }

                    @Override
                    public void onPlayError(String url, Object... objects) {
                        super.onPlayError(url, objects);
                        MyLog.i("test", "url:" + url);
                    }

                    @Override
                    public void onStartPrepared(String url, Object... objects) {
                        super.onStartPrepared(url, objects);
                        MyLog.i("test", "onStartPrepared");
                    }

                    @Override
                    public void onAutoComplete(String url, Object... objects) {
                        super.onAutoComplete(url, objects);
                        MyLog.i("test", "onAutoComplete");
//                        tv_time.setVisibility(View.VISIBLE);
//                        ll_count_container.setVisibility(View.VISIBLE);
                        GSYVideoManager.releaseAllVideos();
                    }


                    @Override
                    public void onEnterFullscreen(String url, Object... objects) {
                        super.onEnterFullscreen(url, objects);
                        GSYVideoManager.instance().setNeedMute(false);
                        gsyVideoPlayer.getCurrentPlayer().getTitleTextView().setText((String) objects[0]);
                    }
                }).build(gsyVideoPlayer);


    }

    @Override
    protected void onPause() {
        super.onPause();
        GSYVideoManager.releaseAllVideos();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
    }

    @Override
    public void onClick(View v) {
        Configuration mConfiguration = this.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向
        switch (v.getId()) {
            case R.id.back:
                if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制为竖屏
                } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {//竖屏
                    finish();
                }
                break;
            case R.id.fullscreen:
                if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {//横屏
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制为竖屏
                } else if (ori == mConfiguration.ORIENTATION_PORTRAIT) {//竖屏
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
                }
                break;
        }
    }


}
