package com.zjzy.morebit.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzy.morebit.Activity.GoodsDetailActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.StringsUtils;
import com.zjzy.morebit.utils.VerticalImageSpan;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wuchaowen
 * @Description: 二级模块
 **/
public class SelectGoodsAdapter2 extends RecyclerView.Adapter{
    public static final int DISPLAY_HORIZONTAL = 0; //横向
    private LayoutInflater mInflater;
    private List<ShopGoodInfo> mDatas = new ArrayList<>();
    private Context mContext;
    private static final int FristType = 0;
    private static final int TwoType = 1;
    private Drawable drawable;

    public SelectGoodsAdapter2(Context context) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    public void setData(List<ShopGoodInfo> data) {
        if (data != null) {
            mDatas.addAll(data);
            notifyItemRangeChanged(0, data.size());
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
//        if (viewType == FristType) {
              view = mInflater.inflate(R.layout.item_selectgoods3, parent, false);
             return new ViewHolder1(view);
//        } else if (viewType==TwoType){
//              view = mInflater.inflate(R.layout.item_selectgoods2, parent, false);
//            return new ViewHolder2(view);
//        }
//        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        if (holder instanceof ViewHolder1){
        final ShopGoodInfo info = mDatas.get(position);
        ViewHolder1 holder1= (ViewHolder1) holder;
        StringsUtils.retractTitle(holder1.tv_icon, holder1.tv_title, info.getItemTitle());
        LoadImgUtils.loadingCornerBitmap(mContext, holder1.img, MathUtils.getPicture(info), 4);
        holder1.tv_price.setText(MathUtils.getnum(info.getVoucherPrice()));
        holder1.volnum.setText("月销" + MathUtils.getSales(info.getSaleMonth()));
        LoadImgUtils.loadingCornerBitmap(mContext, holder1.img, info.getImageUrl());
        if (StringsUtils.isEmpty(info.getCouponPrice())) {
            holder1.coupon.setVisibility(View.GONE);
        } else {
            holder1.coupon.setVisibility(View.VISIBLE);
        }



        holder1.coupon.setText(mContext.getString(R.string.yuan, MathUtils.getnum((info.getCouponPrice()))));
        holder1.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoodsDetailActivity.start(mContext, info);
            }
        });

        if (!TextUtils.isEmpty(info.getCommission())) {
            holder1.tv_zhuan.setText(mContext.getString(R.string.mcommission, MathUtils.getMuRatioComPrice(UserLocalData.getUser(mContext).getCalculationRate(), info.getCommission())));

        }
        if (!TextUtils.isEmpty(info.getItemPrice())) {
            holder1.price.setText("¥ "+info.getItemPrice());
            ((ViewHolder1) holder).price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

        }

        SpannableString spannableString = new SpannableString("  " + info.getItemTitle());
        //判断是淘宝还是天猫的商品
        if (info.getShopType() == 2) {
              drawable = mContext.getResources().getDrawable(R.drawable.tm_list_icon);
        } else {
              drawable = mContext.getResources().getDrawable(R.drawable.tb_list_icon);
        }

        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        spannableString.setSpan(new VerticalImageSpan(drawable), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder1.tv_title.setText(spannableString);




//        }else{
//            ViewHolder2 holder2= (ViewHolder2) holder;
//        }
    }

//    @Override
//    public int getItemViewType(int position) {
//
//        if("0".equals(FristType)){
//            return FristType;
//        }else {
//            return FristType;
//        }
//    }





    @Override
    public int getItemCount() {
//        if (mDatas.size()<2){
//            return mDatas.size();
//        }else{
//            return 2;
//        }
        return mDatas.size();

    }


    public class ViewHolder1 extends RecyclerView.ViewHolder {
        private TextView tv_title,tv_price,volnum,coupon,tv_zhuan,price;
        private ImageView img,tv_icon;
        public ViewHolder1(View itemView) {
            super(itemView);
            tv_icon= itemView.findViewById(R.id.tv_icon);
            img= itemView.findViewById(R.id.img);
            tv_title= itemView.findViewById(R.id.tv_title);
            tv_price=itemView.findViewById(R.id.tv_price);
            volnum=itemView.findViewById(R.id.volnum);
            coupon=itemView.findViewById(R.id.coupon);
            tv_zhuan=itemView.findViewById(R.id.tv_zhuan);
            price=itemView.findViewById(R.id.price);

        }
    }



    public class ViewHolder2 extends RecyclerView.ViewHolder {
        private ImageView img2;
        public ViewHolder2(View itemView) {
            super(itemView);

            img2= itemView.findViewById(R.id.img2);

        }
    }
}
