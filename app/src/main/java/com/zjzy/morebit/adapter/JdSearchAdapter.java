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
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.request.RequestPromotionUrlBean;
import com.zjzy.morebit.utils.KaipuleUtils;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.ShareUtil;
import com.zjzy.morebit.utils.StringsUtils;
import com.zjzy.morebit.utils.VerticalImageSpan;
import com.zjzy.morebit.view.CommNewShareDialog;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;


/**
 * 京东adapter
 */
public class JdSearchAdapter extends RecyclerView.Adapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<ShopGoodInfo> mDatas;

    private final int mBottomPadding;
    private    CommNewShareDialog shareDialog;

    public JdSearchAdapter(Context context, List<ShopGoodInfo> data) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        mBottomPadding = mContext.getResources().getDimensionPixelSize(R.dimen.ranking_adapter_bottom_padding);
        this.mDatas = data;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_jd_goodsearch, parent, false));
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
        viewHolder.textvihew_Preco.setText("销量" + MathUtils.getSales(info.getSaleMonth()));


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
            if (!TextUtils.isEmpty(info.getItemTitle())){
                viewHolder.title.setText(info.getItemTitle()+"");
            }



            if (!TextUtils.isEmpty(info.getShopName())){
                viewHolder.shop_name.setText(info.getShopName()+"");
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

        TextView textview_original, textvihew_Preco, coupon, commission, shop_name;
        ImageView iv_icon, good_mall_tag;

        TextView title, tv_share;


        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            textview_original = (TextView) itemView.findViewById(R.id.discount_price);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            textvihew_Preco = (TextView) itemView.findViewById(R.id.volume);
            coupon = (TextView) itemView.findViewById(R.id.coupon);
            commission = (TextView) itemView.findViewById(R.id.commission);
            shop_name=itemView.findViewById(R.id.shop_name);

        }
    }




}