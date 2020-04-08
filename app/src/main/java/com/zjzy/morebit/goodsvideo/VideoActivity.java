package com.zjzy.morebit.goodsvideo;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.zjzy.morebit.Activity.ShareMoneyActivity;
import com.zjzy.morebit.Activity.ShortVideoPlayActivity;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.utils.DateTimeUtils;
import com.zjzy.morebit.utils.ViewShowUtils;

import static com.zjzy.morebit.utils.C.requestType.initData;

/*
 * 视频播放页
 *
 * */
public class VideoActivity extends BaseActivity implements View.OnClickListener {

    private TextView closs, tv_title, tv_price, tv_subsidy, tv_num, tv_coupon_price, tv_buy, tv_share;
    private VideoView videoView;
    private ImageView iv_head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");//获取视频链接
        if(!TextUtils.isEmpty(url)){
            try {
                Uri uri = Uri.parse(url);
                videoView.setVideoURI(uri);
                videoView.requestFocus();
                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        //视频装载好
                        videoView.start();

                    }
                });
                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {

                    }
                });
                videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                        ViewShowUtils.showShortToast(VideoActivity.this,"视频出错了");
                        return false;
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void initView() {
        closs = (TextView) findViewById(R.id.closs);
        videoView = (VideoView) findViewById(R.id.videoView);//视频
        iv_head = (ImageView) findViewById(R.id.iv_head);//主图
        tv_title = (TextView) findViewById(R.id.tv_title);//标题
        tv_price = (TextView) findViewById(R.id.tv_price);//优惠券
        tv_subsidy = (TextView) findViewById(R.id.tv_subsidy);//预估收益
        tv_num = (TextView) findViewById(R.id.tv_num);//销量
        tv_coupon_price = (TextView) findViewById(R.id.tv_coupon_price);//商品价格
        tv_buy = (TextView) findViewById(R.id.tv_buy);//立即购买
        tv_share = (TextView) findViewById(R.id.tv_share);//分享
        tv_buy.setOnClickListener(this);
        tv_share.setOnClickListener(this);
        videoView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_buy://跳转商品详情
                break;
            case R.id.tv_share://进入分享页
                Intent intent=new Intent(this, ShareMoneyActivity.class);
                startActivity(intent);
                break;
            case R.id.videoView://点击视频播放暂停
                if(videoView.isPlaying()){
                    videoView.pause();
                }else{
                    videoView.start();
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(null != videoView && videoView.isPlaying()){
            videoView.stopPlayback();
            videoView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != videoView){
            videoView.stopPlayback();
            videoView = null;
        }

    }
}
