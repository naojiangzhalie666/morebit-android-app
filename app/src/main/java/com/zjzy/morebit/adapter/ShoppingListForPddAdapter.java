package com.zjzy.morebit.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
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

import com.lovejjfg.powertext.LabelTextView;
import com.zjzy.morebit.Activity.GoodsDetailActivity;
import com.zjzy.morebit.Activity.GoodsDetailForPddActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.R;
import com.zjzy.morebit.adapter.holder.SimpleViewHolder;
import com.zjzy.morebit.goods.shopping.ui.view.ForeshowItemTiemView;
import com.zjzy.morebit.goods.shopping.ui.view.RecommendIndexView;
import com.zjzy.morebit.pojo.SelectFlag;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.StringUtils;
import com.zjzy.morebit.utils.StringsUtils;
import com.zjzy.morebit.utils.ViewShowUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 列表新版
 */
public class ShoppingListForPddAdapter extends SimpleAdapter<ShopGoodInfo, SimpleViewHolder> {
    private Context mContext;
//    private List<ShopGoodInfo> mDatas = new ArrayList<>();

    public ShoppingListForPddAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            return new ViewHolder(mInflater.inflate(R.layout.item_shoppingaole2_pdd, parent, false));
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_jd_goodsearch, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull SimpleViewHolder holder, int position) {
        final ShopGoodInfo info = getItem(position);
        if (info == null) return;
        final ViewHolder viewHolder = (ViewHolder) holder;
        LoadImgUtils.loadingCornerBitmap(mContext, viewHolder.iv_icon, MathUtils.getPicture(info), 8);
        viewHolder.textview_original.setText(MathUtils.getnum(info.getVoucherPriceForPdd()));
        viewHolder.textvihew_Preco.setText("销量" + MathUtils.getSales(info.getSaleMonth()));
        viewHolder.good_mall_tag.setImageResource(R.mipmap.pdd_list_icon);

        LoadImgUtils.loadingCornerBitmap(mContext, viewHolder.iv_icon, info.getImageUrl());
        try {


            if (StringsUtils.isEmpty(info.getCouponPrice())) {
                viewHolder.coupon.setVisibility(View.GONE);
            } else {
                viewHolder.coupon.setVisibility(View.VISIBLE);
            }

            viewHolder.coupon.setText(mContext.getString(R.string.yuan, MathUtils.getnum((info.getCouponPrice()))));


            if (!TextUtils.isEmpty(info.getCommission())) {
                viewHolder.commission.setText(mContext.getString(R.string.mcommission, MathUtils.getMuRatioComPrice(UserLocalData.getUser(mContext).getCalculationRate(), info.getCommission())));

            }
            if (!TextUtils.isEmpty(info.getItemTitle())){
                viewHolder.title.setText(info.getItemTitle()+"");
            }



            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {//点击跳详情
                @Override
                public void onClick(View v) {

                    GoodsDetailForPddActivity.start(mContext, info);
                }
            });

            if (!TextUtils.isEmpty(info.getShopName())){
                viewHolder.shop_name.setText(info.getShopName()+"");
            }






        } catch (Exception e) {
            e.printStackTrace();
        }


    }






    private class ViewHolder  extends SimpleViewHolder {

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
            good_mall_tag=itemView.findViewById(R.id.good_mall_tag);
            shop_name=itemView.findViewById(R.id.shop_name);

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