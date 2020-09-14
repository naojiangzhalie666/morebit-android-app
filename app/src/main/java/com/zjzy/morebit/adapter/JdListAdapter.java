package com.zjzy.morebit.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzy.morebit.Activity.GoodsDetailForJdActivity;
import com.zjzy.morebit.Activity.ShowWebActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.CommInterface;
import com.zjzy.morebit.utils.KaipuleUtils;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.ShareUtil;
import com.zjzy.morebit.utils.StringsUtils;
import com.zjzy.morebit.utils.VerticalImageSpan;
import com.zjzy.morebit.view.CommNewShareDialog;

import java.util.List;

import io.reactivex.functions.Action;


/**
 * 京东adapter
 */
public class JdListAdapter extends RecyclerView.Adapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<ShopGoodInfo> mDatas;

    private final int mBottomPadding;
    private    CommNewShareDialog shareDialog;

    public JdListAdapter(Context context, List<ShopGoodInfo> data) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        mBottomPadding = mContext.getResources().getDimensionPixelSize(R.dimen.ranking_adapter_bottom_padding);
        this.mDatas = data;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_jd_shopping, parent, false));
    }


    public void setData(List<ShopGoodInfo> data) {
        if (data != null) {
            mDatas.addAll(data);
            notifyItemRangeChanged(0, data.size());
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ShopGoodInfo info = mDatas.get(position);
        if (info == null) return;

        final ViewHolder viewHolder = (ViewHolder) holder;


        LoadImgUtils.loadingCornerBitmap(mContext, viewHolder.iv_icon, MathUtils.getPicture(info), 8);
        viewHolder.textview_original.setText(MathUtils.getnum(info.getVoucherPriceForPdd()));
        viewHolder.textvihew_Preco.setText("¥ " + MathUtils.getnum(info.getPriceForPdd()));
        viewHolder.textvihew_Preco.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

        LoadImgUtils.loadingCornerBitmap(mContext, viewHolder.iv_icon, info.getImageUrl());
        try {


            if (StringsUtils.isEmpty(info.getCouponPrice())) {
                viewHolder.coupon.setVisibility(View.GONE);
            } else {
                viewHolder.coupon.setVisibility(View.VISIBLE);
            }

            viewHolder.coupon.setText(mContext.getString(R.string.yuan, MathUtils.getnum((info.getCouponPrice()))));
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GoodsDetailForJdActivity.start(mContext, info);
                }
            });

            if (!TextUtils.isEmpty(info.getCommission())) {
                viewHolder.commission.setText(mContext.getString(R.string.mcommission, MathUtils.getMuRatioComPrice(UserLocalData.getUser(mContext).getCalculationRate(), info.getCommission())));

            }
            SpannableString spannableString = new SpannableString("   " + info.getItemTitle());
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.jd_list_icon);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            spannableString.setSpan(new VerticalImageSpan(drawable), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.title.setText(spannableString);


            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {//点击购买
                @Override
                public void onClick(View v) {
                    CommInterface.generatePromotionUrlForJd((BaseActivity) mContext, info.getGoodsId(), info.getCouponUrl())
                            .doFinally(new Action() {
                                @Override
                                public void run() throws Exception {
                                }
                            })
                            .subscribe(new DataObserver<String>() {
                                @Override
                                protected void onSuccess(final String data) {
                                    if (LoginUtil.checkIsLogin((Activity) mContext)) {

                                        if (data != null) {

                                            if (isHasInstalledjd()){
                                                KaipuleUtils.getInstance(mContext).openUrlToApp(data);
                                            }else{
                                                ShowWebActivity.start((Activity) mContext,data,"");
                                            }


                                        }


                                    }
                                }
                            });
                }
            });


            viewHolder.tv_share.setOnClickListener(new View.OnClickListener() {//分享链接
                @Override
                public void onClick(View v) {
                    if (!LoginUtil.checkIsLogin((Activity) mContext)) {
                        return;
                    }
                    CommInterface.generatePromotionUrlForJd((BaseActivity) mContext, info.getGoodsId(), info.getCouponUrl())
                            .doFinally(new Action() {
                                @Override
                                public void run() throws Exception {
                                }
                            })
                            .subscribe(new DataObserver<String>() {
                                @Override
                                protected void onSuccess(final String data) {
                                    if (LoginUtil.checkIsLogin((Activity) mContext)) {

                                        if (data != null) {
                                            mShare(info,data);
                                        }


                                    }
                                }
                            });



                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void mShare(final ShopGoodInfo info, final String data) {
          shareDialog = new CommNewShareDialog(mContext, new View.OnClickListener() {//分享
            @Override
            public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.weixinFriend: //分享到好友
                            ShareUtil.App.toWechatFriend((Activity) mContext, info.getItemTitle(), info.getShopName(),info.getImageUrl(),data,null);
                            break;
                        case R.id.weixinCircle: //分享到朋友圈
                            ShareUtil.App.toWechatMoments((Activity) mContext, info.getItemTitle(), info.getShopName(),info.getImageUrl(),data,null);
                            break;
                        case R.id.qqFriend: //分享到QQ
                            ShareUtil.App.toQQFriend((Activity) mContext, info.getItemTitle(), info.getShopName(),info.getImageUrl(),data,null);
                            break;
                        case R.id.qqRoom: //分享到QQ空间
                            ShareUtil.App.toQQRoom((Activity) mContext, info.getItemTitle(), info.getShopName(),info.getImageUrl(),data,null);
                            break;
                        default:
                            break;
                    }

                shareDialog.dismiss();
            }
        });

        if (!shareDialog.isShowing()) {
            shareDialog.show();
        }
    }


    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        TextView textview_original, textvihew_Preco, coupon, commission, tv_shop_name;
        ImageView iv_icon, good_mall_tag;

        TextView title, tv_share;


        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            tv_shop_name = (TextView) itemView.findViewById(R.id.tv_shop_name);
            textview_original = (TextView) itemView.findViewById(R.id.discount_price);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            good_mall_tag = (ImageView) itemView.findViewById(R.id.good_mall_tag);
            textvihew_Preco = (TextView) itemView.findViewById(R.id.price);
            coupon = (TextView) itemView.findViewById(R.id.coupon);
            commission = (TextView) itemView.findViewById(R.id.commission);
            tv_share = itemView.findViewById(R.id.tv_share);

        }
    }

    /**
     * 判断是否安装jd
     *
     * @return
     */
    private boolean isHasInstalledjd() {
        return AppUtil.checkHasInstalledApp(mContext, "com.jingdong.app.mall");
    }

}