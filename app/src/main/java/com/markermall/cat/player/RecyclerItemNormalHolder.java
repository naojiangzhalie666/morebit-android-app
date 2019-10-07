package com.markermall.cat.player;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.markermall.cat.LocalData.UserLocalData;
import com.markermall.cat.Module.common.Dialog.HintDialog;
import com.markermall.cat.R;
import com.markermall.cat.circle.ui.RecommendListActivity;
import com.markermall.cat.pojo.Article;
import com.markermall.cat.pojo.MessageEvent;
import com.markermall.cat.utils.C;
import com.markermall.cat.utils.MyLog;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by guoshuyu on 2017/1/9.
 */

public class RecyclerItemNormalHolder extends RecyclerItemBaseHolder {

    public final static String TAG = "RecyclerView2List";

    protected Context context = null;

    @BindView(R.id.video_item_player)
    CircleVideo gsyVideoPlayer;
    @BindView(R.id.fl_isplay)
    FrameLayout flIsplay;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_see_count)
    TextView tv_see_count;
    @BindView(R.id.ll_count_container)
    LinearLayout ll_count_container;
    @BindView(R.id.video_title)
    TextView video_title;
    @BindView(R.id.ll_share)
    LinearLayout ll_share;
    @BindView(R.id.iv_recommend)
    ImageView iv_recommend;
    private HintDialog mDialog;
    GSYVideoOptionBuilder gsyVideoOptionBuilder;
    private  OnItemClickListener onItemClickListener;

    public RecyclerItemNormalHolder(Context context, View v) {
        super(v);
        this.context = context;
        ButterKnife.bind(this, v);
//        imageView = new ImageView(context);
        gsyVideoOptionBuilder = new GSYVideoOptionBuilder();
    }

    public void onBind(final int position, final Article videoModel, final String action, final int type,boolean isHomeRecommend) {
        if (videoModel != null && videoModel.getAuthority() == 1) {
            flIsplay.setVisibility(View.GONE);
        } else {
            flIsplay.setVisibility(View.VISIBLE);
            flIsplay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener!=null){
                        onItemClickListener.onAuthority(v);
                    }
                }
            });
        }
        gsyVideoPlayer.setShrinkImageRes(R.drawable.ic_player_shrink);
        gsyVideoPlayer.setEnlargeImageRes(R.drawable.ic_player_enlarge);
        Map<String, String> header = new HashMap<>();
        header.put("ee", "33");
        MyLog.i("test", "videoModel.getVideoUrl(): " + videoModel.getVideoUrl());
        if(isHomeRecommend){
            if(position<=2){
                iv_recommend.setVisibility(View.VISIBLE);
            } else {
                iv_recommend.setVisibility(View.GONE);
            }
        } else {
            iv_recommend.setVisibility(View.GONE);
        }

        //防止错位，离开释放
        //gsyVideoPlayer.initUIState();
        gsyVideoOptionBuilder
                .setIsTouchWiget(false)
                .setUrl(videoModel.getVideoUrl())
                .setVideoTitle(videoModel.getTitle())
                .setCacheWithPlay(false)
                .setRotateViewAuto(true)
                .setLockLand(true)
                .setPlayTag(TAG)
                .setMapHeadData(header)
                .setShowFullAnimation(false)
                .setPlayPosition(position)
                .setNeedShowWifiTip(true)
                .setNeedLockFull(false)
                .setVideoAllCallBack(new GSYSampleCallBack() {
                    @Override
                    public void onPrepared(String url, Object... objects) {
                        super.onPrepared(url, objects);
                        if (!gsyVideoPlayer.isIfCurrentIsFullscreen()) {
//                            SensorsDataUtil.getInstance().mifenClickTrack("","","商学院",position,"","","","",videoModel.getUrl(),videoModel.getTitle(),"");
                            sendStatistics(videoModel, action);
                        }

                    }

                    @Override
                    public void onQuitFullscreen(String url, Object... objects) {
                        super.onQuitFullscreen(url, objects);

                    }

                    @Override
                    public void onPlayError(String url, Object... objects) {
                        super.onPlayError(url, objects);
                        MyLog.i("test", "url:" + url);
                    }

                    @Override
                    public void onStartPrepared(String url, Object... objects) {
                        super.onStartPrepared(url, objects);
                        ll_count_container.setVisibility(View.GONE);
                        MyLog.i("test", "onStartPrepared");
                    }

                    @Override
                    public void onAutoComplete(String url, Object... objects) {
                        super.onAutoComplete(url, objects);
                        MyLog.i("test", "onAutoComplete");
                        if(type != RecommendListActivity.ARTICLE_REVIEW){
                            ll_count_container.setVisibility(View.VISIBLE);
                        }
                        GSYVideoManager.releaseAllVideos();
                    }


                    @Override
                    public void onEnterFullscreen(String url, Object... objects) {
                        super.onEnterFullscreen(url, objects);
                        gsyVideoPlayer.getCurrentPlayer().getTitleTextView().setText((String) objects[0]);
                    }
                }).build(gsyVideoPlayer);

        ll_count_container.setVisibility(View.VISIBLE);
        video_title.setText(videoModel.getTitle());
        tv_time.setText(videoModel.getReleaseTime() + "");
        String finalViewCount = videoModel.getRealView() + videoModel.getVirtualView() + "";
        if (TextUtils.isEmpty(finalViewCount)) {
            tv_see_count.setText("");
        } else {
            tv_see_count.setText(videoModel.getRealView() + videoModel.getVirtualView() + "");
        }
        MyLog.i("test", "==getReleaseTime=="+videoModel.getReleaseTime() );
        //设置背景图
        gsyVideoPlayer.loadCoverImage(videoModel.getImage(), R.drawable.icon_default);
        //增加title
        gsyVideoPlayer.getTitleTextView().setVisibility(View.GONE);
        //设置返回键
        gsyVideoPlayer.getBackButton().setVisibility(View.GONE);
        //设置全屏按键功能
        gsyVideoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resolveFullBtn(gsyVideoPlayer);
            }
        });
        if(type == RecommendListActivity.ARTICLE_REVIEW){
            ll_share.setVisibility(View.GONE);
            ll_count_container.setVisibility(View.GONE);
        } else {
            ll_share.setVisibility(View.VISIBLE);
            ll_count_container.setVisibility(View.VISIBLE);
            ll_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyLog.i("test","b");
                    if(onItemClickListener!=null){
                        onItemClickListener.onShare(v);
                    }
                }
            });
        }

    }

    private void sendStatistics(Article article, String action) {
        MessageEvent messageEvent = new MessageEvent();
        messageEvent.setAction(action);
        messageEvent.setId(article.getId());
        messageEvent.setMsg(article.getTwoLevel() + "");
        EventBus.getDefault().post(messageEvent);
    }

    /**
     * 全屏幕按键处理
     */
    private void resolveFullBtn(final StandardGSYVideoPlayer standardGSYVideoPlayer) {
        standardGSYVideoPlayer.startWindowFullscreen(context, true, true);
    }


    private void openDialog() {
        if (mDialog == null) {
            // 0是消费者，1是代理商，2是运营专员
            String toastMsg = getHintString();
            mDialog = new HintDialog(context, R.style.dialog, "提示", toastMsg);
        }
        String toastMsg = getHintString();
        mDialog.setContentText(toastMsg);
        mDialog.show();
    }

    @NonNull
    private String getHintString() {
        String toastMsg = "";
        if (C.UserType.member.equals(UserLocalData.getUser(context).getPartner())) {
            toastMsg = context.getString(R.string.dialog_article_vip_hint);
        } else if (C.UserType.vipMember.equals(UserLocalData.getUser(context).getPartner())) {
            toastMsg = context.getString(R.string.dialog_article_operator_hint);
        } else {
            toastMsg = context.getString(R.string.dialog_article_drm_hint); // 默认提示
        }
        return toastMsg;
    }
    public interface OnItemClickListener{
        void onAuthority(View v);
        void onShare(View v);
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}