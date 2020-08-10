package com.zjzy.morebit.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzy.morebit.Activity.CommissionClassActivity;
import com.zjzy.morebit.Activity.GoodsDetailActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.R;
import com.zjzy.morebit.goodsvideo.VideoActivity;
import com.zjzy.morebit.goodsvideo.VideoClassActivity;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.QueryDhAndGyBean;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MathUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wuchaowen
 * @Description: 二级模块
 **/
public class ActivityDouAdapter extends RecyclerView.Adapter<ActivityDouAdapter.ViewHolder> {
    public static final int DISPLAY_HORIZONTAL = 0; //横向
    private LayoutInflater mInflater;
    private List<ImageInfo> mDatas = new ArrayList<>();
    private Context mContext;
    private QueryDhAndGyBean data;

    public ActivityDouAdapter(Context context, QueryDhAndGyBean data) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.data = data;
    }


    @Override
    public ActivityDouAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = null;

        view = mInflater.inflate(R.layout.item_douding, viewGroup, false);
        ActivityDouAdapter.ViewHolder viewHolder = new ActivityDouAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ActivityDouAdapter.ViewHolder holder, final int position) {
        List<QueryDhAndGyBean.DhListBean> dhList = data.getDhList();//抖货
        List<QueryDhAndGyBean.GyListBean> gyList = data.getGyList();//高佣


        final List<ShopGoodInfo> list=new ArrayList<>();

        for (int i=0;i<dhList.size();i++){
           ShopGoodInfo dhBean = new ShopGoodInfo();
            dhBean.setItemTitle(dhList.get(i).getItemTitle());
            dhBean.setItemSourceId(dhList.get(i).getItemId()+"");
            dhBean.setGoodsId(Long.valueOf(dhList.get(i).getItemId()));
            dhBean.setItemPic(dhList.get(i).getItemPic());
            dhBean.setCouponMoney(dhList.get(i).getCouponMoney()+"");
            dhBean.setGoodsId(Long.valueOf(gyList.get(i).getItemid()));
            dhBean.setPrice(dhList.get(i).getItemPrice());
            dhBean.setItemVideo(dhList.get(i).getItemVideo());
            dhBean.setCouponUrl(dhList.get(i).getCouponUrl());
            dhBean.setCouponMoney(dhList.get(i).getCouponMoney());
            dhBean.setTkMoney(dhList.get(i).getTkMoney());
            dhBean.setItemSale(dhList.get(i).getItemSale());
            dhBean.setItemVideoPic(dhList.get(i).getItemVideoPic());
            list.add(dhBean);

        }
        Log.e("kkkk",list.size()+"");
        if (position == 0) {
            final ShopGoodInfo gyBean = new ShopGoodInfo();
            gyBean.setItemSourceId(gyList.get(0).getItemid()+"");
            gyBean.setGoodsId(Long.valueOf(gyList.get(0).getItemid()));
            gyBean.setPrice(gyList.get(0).getItemprice());
            gyBean.setVoucherPrice(gyList.get(0).getItemendprice());
            gyBean.setCouponStartTime(gyList.get(0).getCouponstarttime());
            gyBean.setCouponEndTime(gyList.get(0).getCouponendtime());
            gyBean.setCouponUrl(gyList.get(0).getCouponurl());
            gyBean.setCouponPrice(gyList.get(0).getCouponmoney());
            gyBean.setCommission(gyList.get(0).getTkmoney());
            gyBean.setSaleMonth(gyList.get(0).getItemsale());

            holder.title1.setText("定向高佣");
            holder.title1.setTextColor(Color.parseColor("#F05557"));
            if (gyList.size() == 0) return;
            if (!TextUtils.isEmpty(gyList.get(0).getItempic())) {
                LoadImgUtils.loadingCornerBitmap(mContext, holder.img_zhu, gyList.get(0).getItempic(), 8);
            }
            if (!TextUtils.isEmpty(gyList.get(0).getItemtitle())) {
                holder.title2.setText(gyList.get(0).getItemtitle() + "");
            }

            if (!TextUtils.isEmpty(gyList.get(0).getItemendprice())) {
                holder.price.setText(MathUtils.getnum(gyList.get(0).getItemendprice()) + "");
            }
            if (!TextUtils.isEmpty(gyList.get(0).getCouponmoney())) {
                holder.coupon.setText(MathUtils.getnum(gyList.get(0).getCouponmoney())+"元劵");
            }
            if (!TextUtils.isEmpty(gyList.get(0).getTkmoney())) {
                holder.tv_coupon.setText("赚 ¥ " + MathUtils.getMuRatioComPrice(UserLocalData.getUser(mContext).getCalculationRate(), gyList.get(0).getTkmoney() + ""));
            }

            holder.tv_jump.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, CommissionClassActivity.class));
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GoodsDetailActivity.start(mContext, gyBean);
                }
            });
        } else {
            holder.title1.setText("抖货爆款");
            holder.title1.setTextColor(Color.parseColor("#CC2498"));
            if (dhList.size() == 0) return;
            if (!TextUtils.isEmpty(dhList.get(0).getItemPic())) {
                LoadImgUtils.loadingCornerBitmap(mContext, holder.img_zhu, dhList.get(0).getItemPic(), 8);
            }
            if (!TextUtils.isEmpty(dhList.get(0).getItemTitle())) {
                holder.title2.setText(dhList.get(0).getItemTitle() + "");
            }

            if (!TextUtils.isEmpty(dhList.get(0).getItemPrice())) {
                holder.price.setText(MathUtils.getnum(dhList.get(0).getItemPrice()) + "");
            }
            if (!TextUtils.isEmpty(dhList.get(0).getCouponMoney())) {
                holder.coupon.setText(MathUtils.getnum(dhList.get(0).getCouponMoney())+"元劵");
            }
            if (!TextUtils.isEmpty(dhList.get(0).getTkMoney())) {
                holder.tv_coupon.setText("赚 ¥ " + MathUtils.getMuRatioComPrice(UserLocalData.getUser(mContext).getCalculationRate(), dhList.get(0).getTkMoney() + ""));
            }
            holder.tv_jump.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, VideoClassActivity.class));
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (LoginUtil.checkIsLogin((Activity) mContext)) {
                        Log.e("sfsdfsd",list.size()+"");
                        VideoActivity.start(mContext,(List<ShopGoodInfo>)list,position,"0",1);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_zhu;
        private TextView title1, tv_coupon, coupon, title2, price, tv_jump;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_jump = itemView.findViewById(R.id.tv_jump);//立即抢购
            title1 = itemView.findViewById(R.id.title1);//头部标题
            img_zhu = itemView.findViewById(R.id.img_zhu);//主图
            title2 = itemView.findViewById(R.id.title2);//标题
            price = itemView.findViewById(R.id.price);//劵后价
            coupon = itemView.findViewById(R.id.coupon);//优惠券
            tv_coupon = itemView.findViewById(R.id.tv_coupon);//预估赚
        }
    }
}
