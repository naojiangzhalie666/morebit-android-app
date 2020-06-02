package com.zjzy.morebit.goodsvideo.adapter;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.zjzy.morebit.Activity.ShareMoneyActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.goodsvideo.VideoActivity;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.goods.TKLBean;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.GoodsUtil;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.TaobaoUtil;
import com.zjzy.morebit.utils.ViewShowUtils;
import com.zjzy.morebit.utils.action.MyAction;

import java.util.List;

/*
*
* 抖货视频adapter
* */
public class VideoDouAdapter extends RecyclerView.Adapter<VideoDouAdapter.ViewHolder> {
    private Context context;
    private List<ShopGoodInfo> data;
    private TKLBean mTKLBean;
//    private final PlayerConfig playerConfig;



    public VideoDouAdapter(Context context,List<ShopGoodInfo> data) {
        this.context = context;
        this.data=data;
//        playerConfig = new PlayerConfig.Builder()
//                .enableCache()
//                .usingSurfaceView()
//                .savingProgress()
//                .disableAudioFocus()
//                .setLooping()
//                .addToPlayerManager()
//                .build();


    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View inflate = LayoutInflater.from(context).inflate(R.layout.item_videodou, parent, false);
        ViewHolder holder = new ViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final ShopGoodInfo mGoodsInfo=data.get(position);
        if (mGoodsInfo.getColler()==0){
            holder.img_collect.setImageResource(R.mipmap.video_unselect);
        }else{
            holder.img_collect.setImageResource(R.mipmap.video_select);
        }
        LoadImgUtils.loadingCornerTop(context, holder.first_img,mGoodsInfo.getItemVideoPic(),0);
        holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {//监听视频是否渲染
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START){
                            holder.first_img.setVisibility(View.GONE);
                        }

                        return true;
                    }
                });
            }
        });
                //监听视频播放完的代码
        holder.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mPlayer) {
                mPlayer.start();
                mPlayer.setLooping(true);
            }
        });
        LoadImgUtils.loadingCornerBitmap(context, holder.iv_head, mGoodsInfo.getItemPic());
        holder.tv_title.setText(mGoodsInfo.getItemTitle());
        holder.tv_price.setText(mGoodsInfo.getCouponMoney() + "元劵");
        holder.tv_num.setText("销量：" + mGoodsInfo.getItemSale());
        String itemPrice = mGoodsInfo.getItemPrice();
        holder.tv_coupon_price.setText(mGoodsInfo.getItemPrice() + "");
        UserInfo userInfo1 = UserLocalData.getUser();
        if (userInfo1 == null || TextUtils.isEmpty(UserLocalData.getToken())) {
            holder.tv_subsidy.setText("登录赚佣金");
        } else {
            if (C.UserType.operator.equals(UserLocalData.getUser(context).getPartner())
                    || C.UserType.vipMember.equals(UserLocalData.getUser(context).getPartner())) {
                holder.tv_subsidy.setText("预估收益" + MathUtils.getMuRatioComPrice(UserLocalData.getUser(context).getCalculationRate(), mGoodsInfo.getTkMoney() + "") + "元");
            }
        }


        holder.tv_buy.setOnClickListener(new View.OnClickListener() {//购买
            @Override
            public void onClick(View v) {
                if (TaobaoUtil.isAuth()) {//淘宝授权
                    TaobaoUtil.getAllianceAppKey((BaseActivity) context);
                } else {
                    if (isGoodsLose(mGoodsInfo)) return;

                    if (mTKLBean == null) {
                        LoadingView.showDialog(context, "");
                        GoodsUtil.getTaoKouLing((RxAppCompatActivity) context, mGoodsInfo, new MyAction.OnResult<TKLBean>() {
                            @Override
                            public void invoke(TKLBean arg) {
                                mTKLBean = arg;
                            }

                            @Override
                            public void onError() {
                            }
                        });
                    } else {
                        ShareMoneyActivity.start((Activity) context, mGoodsInfo, mTKLBean);
                    }
                }
            }
        });

        holder.tv_share.setOnClickListener(new View.OnClickListener() {//分享
            @Override
            public void onClick(View v) {
                if (TaobaoUtil.isAuth()) {//淘宝授权
                    TaobaoUtil.getAllianceAppKey((BaseActivity) context);
                } else {
                    GoodsUtil.getCouponInfo((RxAppCompatActivity) context, mGoodsInfo);
                }

            }
        });

    }
    /**
     * 商品是否过期
     *
     * @return
     * @param mGoodsInfo
     */
    private boolean isGoodsLose(ShopGoodInfo mGoodsInfo) {
        if (!LoginUtil.checkIsLogin((Activity) context)) {
            return true;
        }
        if (AppUtil.isFastClick(200)) {
            return true;
        }
        if (mGoodsInfo == null) {
            return true;
        }
        if (TextUtils.isEmpty(mGoodsInfo.getPrice())) {
            ViewShowUtils.showLongToast(context, "商品已经过期，请联系管理员哦");
            return true;
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView closs, tv_title, tv_price, tv_subsidy, tv_num, tv_coupon_price, tv_buy, tv_share;
        private VideoView videoView;
        private ImageView iv_head, img_stop,img_share,img_collect,img_xia,first_img;
        private RelativeLayout r1;
        public ViewHolder(View itemView) {
            super(itemView);
            closs = (TextView) itemView.findViewById(R.id.closs);
            videoView = (VideoView) itemView.findViewById(R.id.videoView);//视频
            iv_head = (ImageView) itemView.findViewById(R.id.iv_head);//主图
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);//标题
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);//优惠券
            tv_subsidy = (TextView) itemView.findViewById(R.id.tv_subsidy);//预估收益
            tv_num = (TextView) itemView.findViewById(R.id.tv_num);//销量
            tv_coupon_price = (TextView) itemView.findViewById(R.id.tv_coupon_price);//商品价格
            tv_buy = (TextView) itemView.findViewById(R.id.tv_buy);//立即购买
            tv_share = (TextView) itemView.findViewById(R.id.tv_share);//分享

            img_stop = (ImageView) itemView.findViewById(R.id.img_stop);
            r1 = (RelativeLayout) itemView.findViewById(R.id.r1);
            img_collect= itemView.findViewById(R.id.img_collect);//收藏
            img_share= itemView.findViewById(R.id.img_share);//分享
            img_xia= itemView.findViewById(R.id.img_xia);//下载
            first_img=itemView.findViewById(R.id.first_img);//视频第一帧图片
        }
    }


}
