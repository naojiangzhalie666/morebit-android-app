package com.zjzy.morebit.goodsvideo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.zjzy.morebit.Activity.GoodsDetailActivity;
import com.zjzy.morebit.Activity.ShareMoneyActivity;
import com.zjzy.morebit.Activity.ShortVideoPlayActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.goods.shopping.contract.GoodsDetailContract;
import com.zjzy.morebit.goods.shopping.presenter.GoodsDetailPresenter;
import com.zjzy.morebit.mvp.base.base.BaseView;
import com.zjzy.morebit.mvp.base.frame.MvpActivity;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.ReleaseGoodsPermission;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.goods.TKLBean;
import com.zjzy.morebit.pojo.goods.VideoBean;
import com.zjzy.morebit.utils.ActivityStyleUtil;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.DateTimeUtils;
import com.zjzy.morebit.utils.GoodsUtil;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.TaobaoUtil;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.action.MyAction;

import java.util.List;

import static com.zjzy.morebit.utils.C.requestType.initData;

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
    private TKLBean mTKLBean;

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
        if (C.UserType.operator.equals(UserLocalData.getUser(this).getPartner())
                || C.UserType.vipMember.equals(UserLocalData.getUser(this).getPartner())) {
            tv_subsidy.setText("预估收益" + MathUtils.getMuRatioComPrice(UserLocalData.getUser(this).getCalculationRate(), mGoodsInfo.getTkMoney() + "") + "元");
        } else {
            UserInfo userInfo1 = UserLocalData.getUser();
            if (userInfo1 == null || TextUtils.isEmpty(UserLocalData.getToken())) {
                tv_subsidy.setText("预估收益" + MathUtils.getMuRatioComPrice(C.SysConfig.NUMBER_COMMISSION_PERCENT_VALUE, mGoodsInfo.getTkMoney() + "") + "元");
            } else {
                tv_subsidy.setText("预估收益" + MathUtils.getMuRatioComPrice(UserLocalData.getUser(this).getCalculationRate(), mGoodsInfo.getTkMoney() + "") + "元");
            }


        }
    }
        private void initView () {
            ActivityStyleUtil.initSystemBar(VideoActivity.this, R.color.color_757575); //设置标题栏颜色值
            closs = (TextView) findViewById(R.id.closs);
            closs.setOnClickListener(this);
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
            img_stop = (ImageView) findViewById(R.id.img_stop);
            r1 = (RelativeLayout) findViewById(R.id.r1);
            r1.setOnClickListener(this);
        }

        @Override
        public void onClick (View v){
            switch (v.getId()) {
                case R.id.closs://返回
                    finish();
                    break;
                case R.id.r1://返回
                    finish();
                    break;
                case R.id.tv_buy://跳转分享
                    if (TaobaoUtil.isAuth()) {//淘宝授权
                        TaobaoUtil.getAllianceAppKey((BaseActivity) this);
                    } else {
                        if (isGoodsLose()) return;

                        if (mTKLBean == null) {
                            LoadingView.showDialog(this, "");
                            GoodsUtil.getTaoKouLing(VideoActivity.this, mGoodsInfo, new MyAction.OnResult<TKLBean>() {
                                @Override
                                public void invoke(TKLBean arg) {
                                    mTKLBean = arg;
                                }

                                @Override
                                public void onError() {
                                }
                            });
                        } else {
                            ShareMoneyActivity.start(this, mGoodsInfo, mTKLBean);
                        }
                    }

                    break;
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
    /**
     * 商品是否过期
     *
     * @return
     */
    private boolean isGoodsLose() {
        if (!LoginUtil.checkIsLogin(VideoActivity.this)) {
            return true;
        }
        if (AppUtil.isFastClick(200)) {
            return true;
        }
        if (mGoodsInfo == null) {
            return true;
        }
        if (TextUtils.isEmpty(mGoodsInfo.getPrice())) {
            ViewShowUtils.showLongToast(this, "商品已经过期，请联系管理员哦");
            return true;
        }
        return false;
    }
        @Override
        protected void onPause () {
            super.onPause();
            if (null != videoView && videoView.isPlaying()) {
                videoView.stopPlayback();
                videoView.pause();
            }
        }

        @Override
        protected void onDestroy () {
            super.onDestroy();
            if (null != videoView) {
                videoView.stopPlayback();
                videoView = null;
            }

        }



    public static void start (Context context, ShopGoodInfo info){
            Intent intent = new Intent((Activity) context, VideoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(C.Extras.GOODSBEAN, info);
            intent.putExtras(bundle);
            context.startActivity(intent);
        }
        private void initBundle () {
            bundle = getIntent().getExtras();
            if (bundle != null) {
                mGoodsInfo = (ShopGoodInfo) bundle.getSerializable(C.Extras.GOODSBEAN);
            }
        }


}
