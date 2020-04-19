package com.zjzy.morebit.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.LeadingMarginSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjzy.morebit.Activity.GoodsDetailActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.StringsUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 列表新版
 */
public class RankingListAdapter extends RecyclerView.Adapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<ShopGoodInfo> mDatas = new ArrayList<>();
    private boolean isEditor;//收藏列表是否是编辑状态
    private final int mBottomPadding;

    public RankingListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        mBottomPadding = mContext.getResources().getDimensionPixelSize(R.dimen.ranking_adapter_bottom_padding);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_ranking_shopping, parent, false));
    }


    public void setData(List<ShopGoodInfo> data) {
        if (data != null) {
            mDatas.clear();
            mDatas.addAll(data);

        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ShopGoodInfo info = mDatas.get(position);
        if (info == null) return;

        final ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.img_shop_icon.setVisibility(View.GONE);
        viewHolder.ll_shop_name.setVisibility(View.GONE);
        viewHolder.ll_bottom.setPadding(0, mBottomPadding, 0, 0);

        LoadImgUtils.loadingCornerBitmap(mContext, viewHolder.iv_icon,  MathUtils.getPicture(info), 9);
        //viewHolder.textview.setText(StringsUtils.retractTitle(MathUtils.getTitle(info)));
        viewHolder.textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        viewHolder.textview_original.setText("¥" + MathUtils.getVoucherPrice(info.getVoucherPrice()));
        viewHolder.textvihew_Preco.setText("¥" + MathUtils.getPrice(info.getPrice()));
        viewHolder.textvihew_Preco.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        if(!TextUtils.isEmpty(info.getItemLabeling())  ){
            viewHolder. markTv.setVisibility(View.VISIBLE);
            viewHolder.markTv.setText(info.getItemLabeling());
        }else{
            viewHolder.markTv.setVisibility(View.GONE);
        }


        try {
            if (C.UserType.member.equals(UserLocalData.getUser((Activity) mContext).getPartner()) || info.isCollect) {
                viewHolder.ll_prise.setVisibility(View.GONE);
            } else {
                if (StringsUtils.isEmpty(info.getCouponPrice())) {
                    viewHolder.ll_prise.setVisibility(View.GONE);
                } else {
                    viewHolder.ll_prise.setVisibility(View.VISIBLE);
                }
            }

            if (StringsUtils.isEmpty(info.getCouponPrice())) {
                viewHolder.return_cash.setVisibility(View.GONE);
            } else {
                viewHolder.return_cash.setVisibility(View.VISIBLE);
            }
            //店铺名称
            if (!TextUtils.isEmpty(info.getShopName())) {
                viewHolder.tv_shop_name.setText(info.getShopName());
            }

            viewHolder.coupon.setText(mContext.getString(R.string.yuan, MathUtils.getCouponPrice(info.getCouponPrice())));
            viewHolder.toDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GoodsDetailActivity.start(mContext, info);
                }
            });
            if (C.UserType.vipMember.equals(UserLocalData.getUser((Activity) mContext).getPartner()) || C.UserType.operator.equals(UserLocalData.getUser((Activity) mContext).getPartner())) {
                viewHolder.commission.setText(mContext.getString(R.string.commission, MathUtils.getMuRatioComPrice(UserLocalData.getUser((Activity) mContext).getCalculationRate(), info.getCommission())));
            } else {
                UserInfo userInfo1 =UserLocalData.getUser();
                if (userInfo1 == null || TextUtils.isEmpty(UserLocalData.getToken())) {
                    viewHolder.commission.setText(mContext.getString(R.string.commission, MathUtils.getMuRatioComPrice(C.SysConfig.NUMBER_COMMISSION_PERCENT_VALUE, info.getCommission())));
                }else{
                    viewHolder.commission.setText(mContext.getString(R.string.commission, MathUtils.getMuRatioComPrice(UserLocalData.getUser(mContext).getCalculationRate(), info.getCommission())));
                }
//                viewHolder.commission.setText(mContext.getString(R.string.upgrade_commission));
            }

            viewHolder.momVolume.setText("销量：" + MathUtils.getSales(info.getSaleMonth()));

            //判断是淘宝还是天猫的商品
            if (info.getShopType() == 2) {
                viewHolder.good_mall_tag.setImageResource(R.drawable.tianmao);
            } else {
                viewHolder.good_mall_tag.setImageResource(R.drawable.taobao);
            }
            StringsUtils.retractTitle(viewHolder.good_mall_tag,viewHolder.textview,MathUtils.getTitle(info));

            if (TextUtils.isEmpty(info.getVideoid()) || "0".equals(info.getVideoid())) {
                viewHolder.video_play.setVisibility(View.GONE);
            } else {
                viewHolder.video_play.setVisibility(View.VISIBLE);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        TextView textview, textview_original, textvihew_Preco, momVolume, coupon, commission, tv_shop_name,markTv;
        ImageView iv_icon;
        LinearLayout return_cash;
        RelativeLayout toDetail, img_rl;
        ImageView select_tag, video_play;
        View ll_prise;
        ImageView good_mall_tag,img_shop_icon;
        LinearLayout ll_shop_name, ll_bottom;


        public ViewHolder(View itemView) {
            super(itemView);
            textview = (TextView) itemView.findViewById(R.id.title);
            markTv = (TextView) itemView.findViewById(R.id.markTv);
            tv_shop_name = (TextView) itemView.findViewById(R.id.tv_shop_name);
            textview_original = (TextView) itemView.findViewById(R.id.discount_price);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            textvihew_Preco = (TextView) itemView.findViewById(R.id.price);
            momVolume = (TextView) itemView.findViewById(R.id.sales);
            coupon = (TextView) itemView.findViewById(R.id.coupon);
            toDetail = (RelativeLayout) itemView.findViewById(R.id.toDetail);
            img_rl = (RelativeLayout) itemView.findViewById(R.id.img_rl);
            good_mall_tag = (ImageView) itemView.findViewById(R.id.good_mall_tag);
            commission = (TextView) itemView.findViewById(R.id.commission);
            select_tag = (ImageView) itemView.findViewById(R.id.select_tag);
            video_play = (ImageView) itemView.findViewById(R.id.video_play);
            ll_shop_name = (LinearLayout) itemView.findViewById(R.id.ll_shop_name);
            ll_prise = (View) itemView.findViewById(R.id.ll_return_cash);

            ll_bottom = (LinearLayout) itemView.findViewById(R.id.ll_bottom);
            return_cash = (LinearLayout) itemView.findViewById(R.id.ll_return_cash);
            img_shop_icon = (ImageView)itemView.findViewById(R.id.img_shop_icon);

        }
    }


    private OnAdapterClickListener onAdapterClickListener;

    public interface OnAdapterClickListener {
        void onItem(int position);

        void onLongItem(int position);
    }

    public void setOnAdapterClickListener(OnAdapterClickListener listener) {
        onAdapterClickListener = listener;
    }

    private OnSelectTagListener onSelectTagListener;

    public interface OnSelectTagListener {
        public void onSelect(int position);
    }

    public void setOnSelectTagListener(OnSelectTagListener listener) {
        onSelectTagListener = listener;
    }


    public boolean isEditor() {
        return isEditor;
    }

    public void setEditor(boolean editor) {
        isEditor = editor;
    }

//
//    tvDes.setText(getSpannableString(label, description));
//      tvLabel.setText(label);

    /**
     * 首行缩进的SpannableString
     *
     * @param description 描述信息
     */
    private SpannableString getSpannableString(String description) {
        SpannableString spannableString = new SpannableString(description);
        int dimension = (int) mContext.getResources().getDimension(R.dimen.good_mall_wide);
        int padding = (int) mContext.getResources().getDimension(R.dimen.good_mall_wide_padding);
        LeadingMarginSpan leadingMarginSpan = new LeadingMarginSpan.Standard(dimension + padding, 0);//仅首行缩进
        spannableString.setSpan(leadingMarginSpan, 0, description.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }



}