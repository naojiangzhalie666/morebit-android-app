package com.zjzy.morebit.goodsvideo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;


import com.blankj.utilcode.util.ToastUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zjzy.morebit.Activity.ShareMoneyActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.goodsvideo.adapter.OnViewPagerListener;
import com.zjzy.morebit.goodsvideo.adapter.PagerLayoutManager;
import com.zjzy.morebit.goodsvideo.adapter.VideoDouAdapter;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.goods.TKLBean;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.GoodsUtil;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.TaobaoUtil;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.action.MyAction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
 * 视频播放页
 *
 * */
public class VideoActivity extends BaseActivity implements View.OnClickListener {

    private Bundle bundle;
    private VideoDouAdapter douAdapter;
    private RecyclerView rcy_video;
    private List<ShopGoodInfo> data = new ArrayList<>();
    private VideoView mVideoView;
    private int videoId;
    private int mCurPos;
    private ImageView closs, first_img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        initBundle();
        initView();
        initData();

    }


    private void initData() {


    }

    private void initView() {
        ActivityStyleUtil.initSystemBar(VideoActivity.this, R.color.black); //设置标题栏颜色值
        rcy_video = (RecyclerView) findViewById(R.id.rcy_video);
        closs = (ImageView) findViewById(R.id.closs);


        closs.setOnClickListener(this);
        douAdapter = new VideoDouAdapter(this, data);
        PagerLayoutManager mLayoutManager = new PagerLayoutManager(this, OrientationHelper.VERTICAL);
        rcy_video.setLayoutManager(mLayoutManager);
        rcy_video.setAdapter(douAdapter);
        mLayoutManager.setOnViewPagerListener(new OnViewPagerListener() {
            @Override
            public void onInitComplete(View view) {
                playVideo(videoId, view);
            }

            @Override
            public void onPageSelected(int position, boolean isBottom, View view) {
                if (mCurPos == position) return;
                playVideo(position, view);
            }

            @Override
            public void onPageRelease(boolean isNext, int position, View view) {
                int index = 0;
                if (isNext) {
                    index = 0;
                } else {
                    index = 1;
                }
                releaseVideo(view);
            }
        });

        rcy_video.scrollToPosition(videoId);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.closs:
                finish();
                break;


        }
    }


    /**
     * 播放视频
     */
    private void playVideo(int position, View view) {
        if (view != null) {
            mVideoView = view.findViewById(R.id.videoView);
            first_img = view.findViewById(R.id.first_img);
            Uri uri = Uri.parse(data.get(position).getItemVideo());
            mVideoView.setVideoURI(uri);
            mVideoView.requestFocus();
            mVideoView.start();
            mCurPos = position;


        }
    }

    /**
     * 停止播放
     */
    private void releaseVideo(View view) {
        if (view != null) {
            VideoView videoView = view.findViewById(R.id.videoView);
            videoView.stopPlayback();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mVideoView != null) {
            mVideoView.resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mVideoView != null) {
            mVideoView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVideoView != null) {
            mVideoView.stopPlayback();
            mVideoView = null;
        }
    }

    public static void start(Context context, List<ShopGoodInfo> info, int videoId) {
        Intent intent = new Intent((Activity) context, VideoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(C.Extras.GOODSBEAN, (Serializable) info);
        bundle.putSerializable(C.Extras.VIDEOBEAN, videoId);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private void initBundle() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            data = (List<ShopGoodInfo>) bundle.getSerializable(C.Extras.GOODSBEAN);
            videoId = (int) bundle.getSerializable(C.Extras.VIDEOBEAN);
        }
    }


}
