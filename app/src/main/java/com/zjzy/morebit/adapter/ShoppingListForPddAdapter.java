package com.zjzy.morebit.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
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

import com.zjzy.morebit.Activity.GoodsDetailActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.R;
import com.zjzy.morebit.goods.shopping.ui.view.ForeshowItemTiemView;
import com.zjzy.morebit.goods.shopping.ui.view.RecommendIndexView;
import com.zjzy.morebit.pojo.SelectFlag;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.StringsUtils;
import com.zjzy.morebit.utils.ViewShowUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 列表新版
 */
public class ShoppingListForPddAdapter extends RecyclerView.Adapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<ShopGoodInfo> mDatas = new ArrayList<>();



    public ShoppingListForPddAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (mDatas == null || mDatas.size() == 0) {
            return 0;
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mInflater.inflate(R.layout.item_shoppingaole2_pdd, parent, false));

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
        LoadImgUtils.loadingCornerBitmap(mContext, viewHolder.iv_icon, MathUtils.getImageUrl(info), 9);
        viewHolder.textview_original.setText(mContext.getString(R.string.coupon, MathUtils.getVoucherPrice(info.getVoucherPriceForPdd())));
        viewHolder.textvihew_Preco.setText("" + MathUtils.getPrice(info.getPriceForPdd()));
        viewHolder.textvihew_Preco.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

        try {
            if (C.UserType.member.equals(UserLocalData.getUser((Activity) mContext).getPartner()) ) {
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

            if (C.UserType.vipMember.equals(UserLocalData.getUser((Activity) mContext).getPartner())
                        || C.UserType.operator.equals(UserLocalData.getUser((Activity) mContext).getPartner())) {
                viewHolder.commission.setText(mContext.getString(R.string.commission,
                        MathUtils.getMuRatioComPrice(UserLocalData.getUser((Activity) mContext).getCalculationRate(),
                                info.getCommission())));
            } else {
                UserInfo userInfo1 =UserLocalData.getUser();
                if (userInfo1 == null || TextUtils.isEmpty(UserLocalData.getToken())) {
                    viewHolder.commission.setText(mContext.getString(R.string.commission,
                            MathUtils.getMuRatioComPrice(C.SysConfig.NUMBER_COMMISSION_PERCENT_VALUE, info.getCommission())));
                }else{
                    viewHolder.commission.setText(mContext.getString(R.string.commission,
                            MathUtils.getMuRatioComPrice(UserLocalData.getUser(mContext).getCalculationRate(), info.getCommission())));
                }
            }

            viewHolder.momVolume.setText("销量：" + MathUtils.getSales(info.getSaleMonth()));

            StringsUtils.retractTitle(viewHolder.good_pdd_tag,viewHolder.textview,MathUtils.getTitle(info));

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }



    private class ViewHolder extends RecyclerView.ViewHolder {
        ForeshowItemTiemView mForeshowItemTiemView;
        RecommendIndexView mRecommendIndexView;
        TextView textview, textview_original, textvihew_Preco, momVolume, coupon, commission, tv_shop_name,good_pdd_tag;
        ImageView iv_icon;
        LinearLayout return_cash;
        RelativeLayout toDetail, img_rl;

        View iv_icon_bg, ll_prise;




        public ViewHolder(View itemView) {
            super(itemView);

            textview = (TextView) itemView.findViewById(R.id.title);
            tv_shop_name = (TextView) itemView.findViewById(R.id.tv_shop_name);
            textview_original = (TextView) itemView.findViewById(R.id.discount_price);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
            textvihew_Preco = (TextView) itemView.findViewById(R.id.price);
            momVolume = (TextView) itemView.findViewById(R.id.sales);
            coupon = (TextView) itemView.findViewById(R.id.coupon);
            toDetail = (RelativeLayout) itemView.findViewById(R.id.toDetail);
            img_rl = (RelativeLayout) itemView.findViewById(R.id.img_rl);

            commission = (TextView) itemView.findViewById(R.id.commission);

            good_pdd_tag = (TextView)itemView.findViewById(R.id.good_pdd_tag);
            iv_icon_bg = (View) itemView.findViewById(R.id.iv_icon_bg);
            ll_prise = (View) itemView.findViewById(R.id.ll_return_cash);


            return_cash = (LinearLayout) itemView.findViewById(R.id.ll_return_cash);
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

}