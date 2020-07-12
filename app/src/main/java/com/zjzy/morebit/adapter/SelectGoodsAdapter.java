package com.zjzy.morebit.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzy.morebit.Activity.GoodsDetailActivity;
import com.zjzy.morebit.Activity.GoodsDetailForJdActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.StringsUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wuchaowen
 * @Description: 二级模块
 **/
public class SelectGoodsAdapter extends RecyclerView.Adapter {
    public static final int DISPLAY_HORIZONTAL = 0; //横向
    private LayoutInflater mInflater;
    private List<ShopGoodInfo> mDatas = new ArrayList<>();
    private Context mContext;
    private static final int FristType = 0;
    private static final int TwoType = 1;

    public SelectGoodsAdapter(Context context, List<ShopGoodInfo> goods) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mDatas = goods;
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
        view = mInflater.inflate(R.layout.item_selectgoods, parent, false);
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
        ViewHolder1 holder1 = (ViewHolder1) holder;

        Paint paint = new Paint();
        paint.setTextSize(holder1.tv_icon.getTextSize());
        float size = paint.measureText(holder1.tv_icon.getText().toString());
        StringsUtils.retractTitles(holder1.tv_title, info.getItemTitle(), (int) (size) + 30);
        LoadImgUtils.loadingCornerBitmap(mContext, holder1.img, MathUtils.getPicture(info), 8);
        holder1.tv_price.setText(MathUtils.getnum(info.getVoucherPrice()));
        holder1.volnum.setText("销量" + MathUtils.getSales(info.getSaleMonth()));
        LoadImgUtils.loadingCornerBitmap(mContext, holder1.img, info.getImageUrl());
        if (StringsUtils.isEmpty(info.getCouponPrice())) {
            holder1.coupon.setVisibility(View.GONE);
        } else {
            holder1.coupon.setVisibility(View.VISIBLE);
        }

        //判断是淘宝还是天猫的商品
        if (info.getShopType() == 2) {
            holder1.good_mall_tag.setImageResource(R.drawable.tm_list_icon);
        } else {
            holder1.good_mall_tag.setImageResource(R.drawable.tb_list_icon);
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


        if (!TextUtils.isEmpty(info.getShopName())) {
            holder1.shop_name.setText(info.getShopName() + "");
        }


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
        private TextView tv_icon, tv_title, tv_price, volnum, coupon, tv_zhuan, shop_name;
        private ImageView img, good_mall_tag;

        public ViewHolder1(View itemView) {
            super(itemView);
            tv_icon = itemView.findViewById(R.id.tv_icon);
            img = itemView.findViewById(R.id.img);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_price = itemView.findViewById(R.id.tv_price);
            volnum = itemView.findViewById(R.id.volnum);
            coupon = itemView.findViewById(R.id.coupon);
            tv_zhuan = itemView.findViewById(R.id.tv_zhuan);
            shop_name = itemView.findViewById(R.id.shop_name);
            good_mall_tag = itemView.findViewById(R.id.good_mall_tag);
        }
    }


    public class ViewHolder2 extends RecyclerView.ViewHolder {
        private ImageView img2;

        public ViewHolder2(View itemView) {
            super(itemView);

            img2 = itemView.findViewById(R.id.img2);

        }
    }
}
