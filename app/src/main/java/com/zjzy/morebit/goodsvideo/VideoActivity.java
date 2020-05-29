package com.zjzy.morebit.goodsvideo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.zjzy.morebit.Activity.ShareMoneyActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.goodsvideo.adapter.VideoDouAdapter;
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

/*
 * 视频播放页
 *
 * */
public class VideoActivity extends BaseActivity implements View.OnClickListener {

    private TextView closs, tv_title, tv_price, tv_subsidy, tv_num, tv_coupon_price, tv_buy, tv_share;
    private VideoView videoView;
    private ImageView iv_head, img_stop;
    private RelativeLayout r1;
    private Bundle bundle;
    private ShopGoodInfo mGoodsInfo;
    private VideoDouAdapter douAdapter;
    private RecyclerView rcy_video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        initBundle();
        initView();
        initData();

    }


    private void initData() {
        String url = mGoodsInfo.getItemVideo();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);
            }
        });
        if (!TextUtils.isEmpty(url)) {
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
                        ViewShowUtils.showShortToast(VideoActivity.this, "视频出错了");
                        return false;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        LoadImgUtils.loadingCornerBitmap(this, iv_head, mGoodsInfo.getItemPic());
        tv_title.setText(mGoodsInfo.getItemTitle());
        tv_price.setText(mGoodsInfo.getCouponMoney() + "元劵");
        tv_num.setText("销量：" + mGoodsInfo.getItemSale());
        String itemPrice = mGoodsInfo.getItemPrice();
        tv_coupon_price.setText(mGoodsInfo.getItemPrice() + "");
        UserInfo userInfo1 = UserLocalData.getUser();
        if (userInfo1 == null || TextUtils.isEmpty(UserLocalData.getToken())) {
            tv_subsidy.setText("登录赚佣金");
        } else {
            if (C.UserType.operator.equals(UserLocalData.getUser(this).getPartner())
                    || C.UserType.vipMember.equals(UserLocalData.getUser(this).getPartner())) {
                tv_subsidy.setText("预估收益" + MathUtils.getMuRatioComPrice(UserLocalData.getUser(this).getCalculationRate(), mGoodsInfo.getTkMoney() + "") + "元");
            }
        }
    }

    private void initView() {
        ActivityStyleUtil.initSystemBar(VideoActivity.this, R.color.color_757575); //设置标题栏颜色值
        rcy_video= (RecyclerView) findViewById(R.id.rcy_video);
        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                    img_stop.setVisibility(View.VISIBLE);
                } else {
                    videoView.start();
                    img_stop.setVisibility(View.GONE);
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_share://进入淘宝详情
                if (TaobaoUtil.isAuth()) {//淘宝授权
                    TaobaoUtil.getAllianceAppKey((BaseActivity) this);
                } else {
//                        TaobaoUtil.showUrl(this, mGoodsInfo.getCouponUrl());
                    //         mPresenter.materialLinkList(this, mGoodsInfo.getItemSourceId(), mGoodsInfo.material);
                    //    GoodsDetailActivity.start(this, mGoodsInfo);

                    GoodsUtil.getCouponInfo(this, mGoodsInfo);
                }

                break;
            case R.id.videoView://点击视频播放暂停
                if (videoView.isPlaying()) {
                    videoView.pause();
                    img_stop.setVisibility(View.VISIBLE);
                } else {
                    videoView.start();
                    img_stop.setVisibility(View.GONE);
                }
                break;
        }
    }



    @Override
    protected void onPause() {
        super.onPause();
        if (null != videoView && videoView.isPlaying()) {
            videoView.stopPlayback();
            videoView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != videoView) {
            videoView.stopPlayback();
            videoView = null;
        }

    }


    public static void start(Context context, ShopGoodInfo info) {
        Intent intent = new Intent((Activity) context, VideoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(C.Extras.GOODSBEAN, info);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private void initBundle() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            mGoodsInfo = (ShopGoodInfo) bundle.getSerializable(C.Extras.GOODSBEAN);
        }
    }


}
