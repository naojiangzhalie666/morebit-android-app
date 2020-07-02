package com.zjzy.morebit.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.LeadingMarginSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zjzy.morebit.Activity.GoodsDetailForJdActivity;
import com.zjzy.morebit.Activity.GoodsDetailForWphActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.StringsUtils;
import com.zjzy.morebit.utils.VerticalImageSpan;

import java.util.List;


/**
 * 列表新版
 */
public class WphListAdapter extends RecyclerView.Adapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<ShopGoodInfo> mDatas;

    private final int mBottomPadding;

    public WphListAdapter(Context context, List<ShopGoodInfo> data) {
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


        // LoadImgUtils.loadingCornerBitmap(mContext, viewHolder.iv_icon, info.getGoodsMainPicture(), 9);
        viewHolder.textview_original.setText(MathUtils.getnum(info.getVipPrice()));
        viewHolder.textvihew_Preco.setText("¥" + MathUtils.getnum(info.getMarketPrice()));
        viewHolder.textvihew_Preco.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

        LoadImgUtils.loadingCornerBitmap(mContext, viewHolder.iv_icon, info.getGoodsMainPicture());
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
            SpannableString spannableString = new SpannableString("  " + info.getItemTitle());
            Drawable drawable = mContext.getResources().getDrawable(R.mipmap.pdd_list_icon);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            spannableString.setSpan(new VerticalImageSpan(drawable), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.title.setText(spannableString);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        TextView textview_original, textvihew_Preco, coupon, commission, tv_shop_name;
        ImageView iv_icon, good_mall_tag;
        TextView title;


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


        }
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