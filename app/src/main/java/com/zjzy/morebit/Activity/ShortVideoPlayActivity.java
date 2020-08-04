package com.zjzy.morebit.Activity;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.contact.SdDirPath;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.DateTimeUtils;
import com.zjzy.morebit.utils.DownloadManage;
import com.zjzy.morebit.utils.MyLog;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadLargeFileListener;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;


/**
 * 短视频播放页
 */

public class ShortVideoPlayActivity extends BaseActivity implements View.OnClickListener{

    private VideoView videoView;
    private SeekBar seekBar;
    private TextView progress_text;
    @BindView(R.id.tv_save)
    TextView tv_save;
    private String url = "";
    private int mType;
    private LinearLayout ll;
    private  boolean open=false;
    private ImageView tv_bo;
    private  MediaPlayer mp;
    private TextView progress_text2;
    public static void start(Activity activity,int type,String videoUrl) {
        Intent intent = new Intent(activity, ShortVideoPlayActivity.class);
        intent.putExtra("type",type);
        intent.putExtra(C.Extras.ITEMVIDEOID,videoUrl);
        activity.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortvideo);
        initBundle();
        inview();
    }

    private void inview(){
        ActivityStyleUtil.initSystemBar(ShortVideoPlayActivity.this,R.color.color_000000); //设置标题栏颜色值

        findViewById(R.id.closs).setOnClickListener(this);
        findViewById(R.id.sp_close).setOnClickListener(this);
        findViewById(R.id.playorpause).setOnClickListener(this);
          ll = (LinearLayout) findViewById(R.id.ll);

        videoView = (VideoView)findViewById(R.id.videoView);
        seekBar = (SeekBar)findViewById(R.id.seekBar);
        progress_text = (TextView)findViewById(R.id.progress_text);
        progress_text2= (TextView) findViewById(R.id.progress_text2);
        tv_bo= (ImageView) findViewById(R.id.tv_bo);


        if(!TextUtils.isEmpty(url)){
            try {
                Uri uri = Uri.parse(url);
                videoView.setVideoURI(uri);
                videoView.requestFocus();
                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(final MediaPlayer mediaPlayer) {

                        mp=mediaPlayer;
                        //视频装载好
                        videoView.start();
                        findViewById(R.id.playorpause).setBackgroundResource(R.mipmap.comm_fang_bg);
                        //得到总时长
                        int duration = videoView.getDuration();
                        seekBar.setMax(duration);
                        //的到当前进度
                        int currentPosition = videoView.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                        //更新文字
                        String durTimeStr = DateTimeUtils.getVideoTime(duration);
                        String curTimeStr = DateTimeUtils.getVideoTime(currentPosition);
                        progress_text2.setText(curTimeStr+"");
                        progress_text.setText(durTimeStr+"");
                        startTime();


                    }
                });
                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
//                        seekBar.setMax(100);
//                        seekBar.setProgress(0);
//                        progress_text.setText("00:00" + "/" + "00:00");
                        findViewById(R.id.playorpause).setBackgroundResource(R.mipmap.comm_bo_bg);
                    }
                });
                videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                        ViewShowUtils.showShortToast(ShortVideoPlayActivity.this,"视频出错了");
                        findViewById(R.id.playorpause).setBackgroundResource(R.mipmap.comm_bo_bg);
                        return false;
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }
//        if(mType==0){
//            tv_save.setVisibility(View.VISIBLE);
//
//        } else {
//            tv_save.setVisibility(View.VISIBLE);
//
//        }
        tv_save.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 取得当前进度条的刻度
                int progress = seekBar.getProgress();
                if (videoView.isPlaying()) {
                    // 设置当前播放的位置
                    videoView.seekTo(progress);
                }
            }
        });



        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp!=null){
                    // 设置静音
                    if (!open){
                        mp.setVolume(0, 0);
                        tv_bo.setImageResource(R.mipmap.bo_jing);
                        open=true;
                    }else{
                        open=false;
                        mp.setVolume(1,1);
                        tv_bo.setImageResource(R.mipmap.bo_kai);
                    }
                }
            }
        });
    }

    private void initBundle(){
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            url = bundle.getString(C.Extras.ITEMVIDEOID);
            mType = bundle.getInt("type",0);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.closs://关闭
                finish();
                break;
            case R.id.sp_close://关闭
                finish();
                break;
            case R.id.playorpause: //播放或暂停
                if(videoView.isPlaying()){
                    videoView.pause();
                    findViewById(R.id.playorpause).setBackgroundResource(R.mipmap.comm_bo_bg);
                }else{
                    videoView.start();
                    findViewById(R.id.playorpause).setBackgroundResource(R.mipmap.comm_fang_bg);
                }
                break;
            case R.id.tv_save:
                saveVideo();
                break;
            default:
                break;
        }
    }

    private void saveVideo() {
        String fileName = url.substring(url.lastIndexOf("/")+1,url.lastIndexOf("."));
        String suffix = url.substring(url.lastIndexOf(".")+1,url.length());
        DownloadManage.getInstance().start(url, SdDirPath.IMAGE_CACHE_PATH + fileName + "." + suffix, new FileDownloadLargeFileListener() {
            @Override
            protected void pending(BaseDownloadTask baseDownloadTask, long l, long l1) {

            }

            @Override
            protected void progress(BaseDownloadTask baseDownloadTask, long l, long l1) {

            }

            @Override
            protected void paused(BaseDownloadTask baseDownloadTask, long l, long l1) {

            }

            @Override
            protected void completed(BaseDownloadTask baseDownloadTask) {
                MyLog.i("test","已保存到："+baseDownloadTask.getPath());
                DownloadManage.getInstance().refreshGallery(ShortVideoPlayActivity.this,baseDownloadTask.getPath());
               ViewShowUtils.showShortToast(ShortVideoPlayActivity.this,"已保存到："+baseDownloadTask.getPath());
            }

            @Override
            protected void error(BaseDownloadTask baseDownloadTask, Throwable throwable) {

            }

            @Override
            protected void warn(BaseDownloadTask baseDownloadTask) {

            }
        });
    }

    /**
     * 计时器
     */
    private Timer timer;
    private TimerTask timerTask;
    private void startTime() {
        if (timer != null){
            timer.cancel();
        }
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                ShortVideoPlayActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //设置进度条
                        try{
                            //得到总时长
                            int duration = videoView.getDuration();
                            seekBar.setMax(duration);
                            //的到当前进度
                            int currentPosition = videoView.getCurrentPosition();
                            seekBar.setProgress(currentPosition);
                            //更新文字
                            String durTimeStr = DateTimeUtils.getVideoTime(duration);
                            String curTimeStr = DateTimeUtils.getVideoTime(currentPosition);
                            progress_text2.setText(curTimeStr+"");
                            progress_text.setText(durTimeStr+"");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        timer.schedule(timerTask, 1000,1000);//1000ms执行一次
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
        if(timer != null){
            timer.cancel();
            timer= null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }
}
