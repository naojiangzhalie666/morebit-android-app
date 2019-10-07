package com.jf.my.circle.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import com.jf.my.Module.common.Activity.BaseActivity;
import com.jf.my.Module.common.Utils.LoadingView;
import com.jf.my.R;
import com.jf.my.circle.adapter.ReviewArticleAdapter;
import com.jf.my.player.CircleVideo;
import com.jf.my.pojo.Article;
import com.jf.my.utils.AppUtil;
import com.jf.my.utils.MyLog;
import com.jf.my.utils.ShareUtil;
import com.jf.my.utils.ViewShowUtils;
import com.jf.my.view.CommercialShareDialog;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;

import java.util.HashMap;

import javax.net.ssl.SSLHandshakeException;

import butterknife.BindView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qq.QQClientNotExistException;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.utils.WechatClientNotExistException;
import cn.sharesdk.wechat.utils.WechatFavoriteNotSupportedException;
import cn.sharesdk.wechat.utils.WechatTimelineNotSupportedException;

/**
 * @author YangBoTian
 * @date 2019/9/5
 * @des
 */
public class VideoPlayerActivity extends BaseActivity {
    @BindView(R.id.video_item_player)
    CircleVideo mVideoPlayer;
    OrientationUtils orientationUtils;
    GSYVideoOptionBuilder gsyVideoOptionBuilder;
    CommercialShareDialog mShareDialog;
    //    @BindView(R.id.status_bar)
//    View status_bar;
    ReviewArticleAdapter.MyPlatformActionListener mMyPlatformActionListener;
    private Article mArticle;

    public static void start(Context context, Article article) {
        Intent intent = new Intent(context, VideoPlayerActivity.class);
        intent.putExtra("article", article);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
//        ImmersionBar.with(this)
//                .statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
//                .init();
        initView();
    }


    private void initView() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            //处理全屏显示
//            ViewGroup.LayoutParams viewParams = status_bar.getLayoutParams();
//            viewParams.height = ActivityStyleUtil.getStatusBarHeight(this);
//            status_bar.setLayoutParams(viewParams);
//
//        }


        mArticle = (Article) getIntent().getSerializableExtra("article");
        if (mArticle == null) {
            return;
        }

        mVideoPlayer.setShrinkImageRes(R.drawable.ic_player_shrink);
        mVideoPlayer.setEnlargeImageRes(R.drawable.ic_player_enlarge);
        mVideoPlayer.setUp(mArticle.getVideoUrl(), true, mArticle.getTitle());

        //增加封面
//        ImageView imageView = new ImageView(this);
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView.setImageResource(R.mipmap.xxx1);
//        mVideoPlayer.setThumbImageView(imageView);
        //增加title

        mVideoPlayer.getTitleTextView().setVisibility(View.VISIBLE);
        //设置返回键
        mVideoPlayer.getBackButton().setVisibility(View.VISIBLE);
        mVideoPlayer.findViewById(R.id.iv_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openShareDialog(mArticle.getTitle(), mArticle.getShareContent(), mArticle.getImage(), mArticle.getShareUrl());
            }
        });
        //设置旋转
        orientationUtils = new OrientationUtils(this, mVideoPlayer);
        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        mVideoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orientationUtils.resolveByClick();
            }
        });
        orientationUtils.setRotateWithSystem(false);
        orientationUtils.setEnable(false);

        //是否可以滑动调整
        mVideoPlayer.setIsTouchWiget(true);
        //设置返回按键功能
        mVideoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mVideoPlayer.startPlayLogic();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoPlayer.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVideoPlayer.onVideoResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }

    @Override
    public void onBackPressed() {
        //先返回正常状态
        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            mVideoPlayer.getFullscreenButton().performClick();
            return;
        }
        //释放所有
        mVideoPlayer.setVideoAllCallBack(null);
        super.onBackPressed();
    }


    private void openShareDialog(final String title, final String text, final String imageUrl, final String shareUrl) {

        if(mShareDialog==null){
            mShareDialog = new CommercialShareDialog(VideoPlayerActivity.this, CommercialShareDialog.BLACK,new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.weixinFriend: //分享到好友
                            if (!AppUtil.isWeixinAvilible(VideoPlayerActivity.this)) {
                                ViewShowUtils.showShortToast(VideoPlayerActivity.this, "请先安装微信客户端");
                                return;
                            }
                            LoadingView.showDialog(VideoPlayerActivity.this);
                            toShareFriends(1, title, text, imageUrl, shareUrl);
                            break;
                        case R.id.weixinCircle: //分享到朋友圈
                            if (!AppUtil.isWeixinAvilible(VideoPlayerActivity.this)) {
                                ViewShowUtils.showShortToast(VideoPlayerActivity.this, "请先安装微信客户端");
                                return;
                            }
                            LoadingView.showDialog(VideoPlayerActivity.this);
                            toShareFriends(2, title, text, imageUrl, shareUrl);
                            break;
                        case R.id.qqFriend: //分享到QQ
                            LoadingView.showDialog(VideoPlayerActivity.this);
                            toShareFriends(3, title, text, imageUrl, shareUrl);
                            break;
                        case R.id.qqRoom: //分享到QQ空间
                            LoadingView.showDialog(VideoPlayerActivity.this);
                            toShareFriends(4, title, text, imageUrl, shareUrl);
                            break;
                        case R.id.sinaWeibo: //分享到新浪微博
                            LoadingView.showDialog(VideoPlayerActivity.this);
                            toShareFriends(5, title, text, imageUrl, shareUrl);
                            break;
                        default:
                            break;
                    }
                }
            });
        }


        if (!mShareDialog.isShowing()) {
            mShareDialog.show();
        }
    }


    /**
     * 海报分享
     *
     * @param type
     */
    private void toShareFriends(int type, String title, String text, String imageUrl, String shareUrl) {

        if (mMyPlatformActionListener == null) {
            mMyPlatformActionListener = new ReviewArticleAdapter.MyPlatformActionListener(VideoPlayerActivity.this);
        }
        if (type == 1) {    //微信好友
            ShareUtil.App.toWechatFriend(VideoPlayerActivity.this, title, text, imageUrl, shareUrl, mMyPlatformActionListener);
        } else if (type == 2) {  //分享到朋友圈
            ShareUtil.App.toWechatMoments(VideoPlayerActivity.this, title, text, imageUrl, shareUrl, mMyPlatformActionListener);
        } else if (type == 3) {  //分享到qq好友
            ShareUtil.App.toQQFriend(VideoPlayerActivity.this, title, text, imageUrl, shareUrl, mMyPlatformActionListener);
        } else if (type == 4) {  //qq空间

            ShareUtil.App.toQQRoom(VideoPlayerActivity.this, title, text, imageUrl, shareUrl, mMyPlatformActionListener);
        } else if (type == 5) {  //新浪微博
            ShareUtil.App.toSinaWeibo(VideoPlayerActivity.this, title, text, imageUrl, shareUrl, mMyPlatformActionListener);
        }
        LoadingView.dismissDialog();
    }

    public static class MyPlatformActionListener implements PlatformActionListener {

        Context mContext;

        public MyPlatformActionListener(Context context) {
            mContext = context;
        }

        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            ViewShowUtils.showShortToast(com.jf.my.App.getAppContext(), "分享成功");
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            MyLog.i("test", "throwable: " + throwable.toString());
            String msg = "分享失败，请稍后再试";
            String type = "";
            if (platform.getName().equalsIgnoreCase(Wechat.NAME)) {
                type = "微信";
            } else if (platform.getName().equalsIgnoreCase(SinaWeibo.NAME)) {
                type = "微博";
            } else if (platform.getName().equalsIgnoreCase(QQ.NAME)) {
                type = "QQ";
            } else if (platform.getName().equalsIgnoreCase(QZone.NAME)) {
                type = "QQ空间";
            }
            if (throwable instanceof SSLHandshakeException) {
                msg = type + "分享失败，请检查您的网络状态";
            } else if (throwable instanceof WechatClientNotExistException
                    || throwable instanceof WechatFavoriteNotSupportedException
                    || throwable instanceof WechatTimelineNotSupportedException) {
                msg = "请先安装微信客户端";
            } else if (throwable instanceof QQClientNotExistException) {
                msg = "请先安装" + type + "客户端";
            } else if (throwable instanceof cn.sharesdk.tencent.qzone.QQClientNotExistException) {
                msg = "请先安装" + type + "客户端";
            }

            if (mContext != null && mContext instanceof Activity) {
                final Activity activity = (Activity) mContext;
                if (!activity.isFinishing()) {
                    final String finalMsg = msg;
                    activity.runOnUiThread(new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ViewShowUtils.showShortToast(activity, finalMsg);
                        }
                    }));
                }
            }
        }

        @Override
        public void onCancel(Platform platform, int i) {
            ViewShowUtils.showShortToast(com.jf.my.App.getAppContext(), "分享取消");
        }
    }

}
