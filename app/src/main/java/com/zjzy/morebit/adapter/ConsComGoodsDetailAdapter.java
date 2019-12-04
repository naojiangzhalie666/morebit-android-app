
package com.zjzy.morebit.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.ConsComGoodsInfo;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.ViewShowUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 消费佣金订单明细
 */
public class ConsComGoodsDetailAdapter extends RecyclerView.Adapter {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<ConsComGoodsInfo> mDatas = new ArrayList<>();

    public ConsComGoodsDetailAdapter(Context context, List<ConsComGoodsInfo> datas) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        if (datas != null && datas.size() > 0) {
            mDatas.addAll(datas);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_conscomgoods_detail, parent, false));
    }

    public void setData(List<ConsComGoodsInfo> datas) {
        mDatas.clear();
        if (datas != null && datas.size() > 0) {
            mDatas.addAll(datas);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ConsComGoodsInfo info = mDatas.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;

        if (TextUtils.isEmpty(info.getItemImg())) {
            viewHolder.pic.setImageResource(R.drawable.icon_default);
        } else {
            LoadImgUtils.setImg(mContext, viewHolder.pic, info.getItemImg());
        }

        try {
            viewHolder.title.setText(info.getItemName());
            if (!TextUtils.isEmpty(info.getCreateTime()) && !"0".equals(info.getCreateTime())) {
                viewHolder.createDay.setText("创建: " + info.getCreateTime());
            } else {
                viewHolder.createDay.setText("");
            }
            viewHolder.xfyugu_tv.setText("¥" + info.getPaymentAmount());
            if (!TextUtils.isEmpty(info.getCommission())){
                viewHolder.yjyugu_tv.setVisibility(View.VISIBLE);
                viewHolder.yjyugu_tv.setText("赚佣" + info.getCommission() + "元");
            }else{
                viewHolder.yjyugu_tv.setVisibility(View.GONE);
            }

            if(!TextUtils.isEmpty(info.getSubsidy())){
                viewHolder.subsidiesPriceTv.setVisibility(View.VISIBLE);
                viewHolder.subsidiesPriceTv.setText(mContext.getString(R.string.subsidiesPrice, info.getSubsidy()));
            }else{
                viewHolder.subsidiesPriceTv.setVisibility(View.GONE);
                viewHolder.subsidiesPriceTv.setText("");
            }
            if ("4".equals(info.getStatus())) {//失效4
                viewHolder.jiesuanDay.setText("已失效");
                viewHolder.yjyugu_tv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
                viewHolder.jiesuanDay.setTextColor(ContextCompat.getColor(mContext, R.color.color_999999));
                viewHolder.jiesuanDay_date.setVisibility(View.GONE);
                viewHolder.yjyugu_tv.setBackgroundResource(R.drawable.bg_commission_n);
                viewHolder.yjyugu_tv.setTextColor(ContextCompat.getColor(mContext,R.color.color_666666));
            } else {

                if ("3".equals(info.getStatus())) {  //已结算
                    viewHolder.jiesuanDay_date.setVisibility(View.VISIBLE);
                    viewHolder.jiesuanDay.setText("已结算");
                    viewHolder.jiesuanDay.setTextColor(ContextCompat.getColor(mContext, R.color.color_4FCC84));
                    if (!TextUtils.isEmpty(info.getSettlementTime()) && !"0".equals(info.getSettlementTime())) {
                        viewHolder.jiesuanDay_date.setText("结算日期: " + info.getSettlementTime());
                    } else {
                        viewHolder.jiesuanDay.setText("");
                    }
                } else {
                    viewHolder.jiesuanDay.setText("已付款");
                    viewHolder.jiesuanDay.setTextColor(ContextCompat.getColor(mContext, R.color.color_4FCC84));
                    viewHolder.jiesuanDay_date.setVisibility(View.GONE);
                }
                viewHolder.yjyugu_tv.getPaint().setFlags(0); //还原
                viewHolder.yjyugu_tv.getPaint().setAntiAlias(true);//抗锯齿
                viewHolder.yjyugu_tv.invalidate();
                viewHolder.yjyugu_tv.setBackgroundResource(R.drawable.bg_commission);
                viewHolder.yjyugu_tv.setTextColor(ContextCompat.getColor(mContext,R.color.color_333333));
            }
            viewHolder.tv_order.setText("订单单号: " + info.getOrderSn());
            viewHolder.tv_copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppUtil.coayText((Activity) mContext,info.getOrderSn());
                    ViewShowUtils.showShortToast(mContext,mContext.getString(R.string.coayTextSucceed));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        viewHolder.iten_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 if(onAdapterClickListener!=null){
                     onAdapterClickListener.onItem(position);
                 }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }


    private class ViewHolder extends RecyclerView.ViewHolder {
        ImageView pic;
        TextView title, createDay, jiesuanDay, xfyugu_tv, yjyugu_tv, tv_order, jiesuanDay_date;
        View bottomLine;
        LinearLayout iten_rl;
        TextView subsidiesPriceTv,tv_copy;

        public ViewHolder(View itemView) {
            super(itemView);
            iten_rl = (LinearLayout) itemView.findViewById(R.id.iten_rl);
            pic = (ImageView) itemView.findViewById(R.id.pic);
            title = (TextView) itemView.findViewById(R.id.title);
            createDay = (TextView) itemView.findViewById(R.id.createDay);
            jiesuanDay = (TextView) itemView.findViewById(R.id.jiesuanDay);
            xfyugu_tv = (TextView) itemView.findViewById(R.id.xfyugu_tv);
            jiesuanDay_date = (TextView) itemView.findViewById(R.id.jiesuanDay_date);
            yjyugu_tv = (TextView) itemView.findViewById(R.id.yjyugu_tv);
            tv_order = (TextView) itemView.findViewById(R.id.tv_order);
            bottomLine = (View) itemView.findViewById(R.id.bottomLine);
            subsidiesPriceTv = itemView.findViewById(R.id.subsidiesPriceTv);
            tv_copy = itemView.findViewById(R.id.tv_copy);
        }
    }

    private OnAdapterClickListener onAdapterClickListener;

    public interface OnAdapterClickListener {
        public void onItem(int position);
    }

    public void setOnAdapterClickListener(OnAdapterClickListener listener) {
        onAdapterClickListener = listener;
    }

}