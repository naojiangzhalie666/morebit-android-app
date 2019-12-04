package com.zjzy.morebit.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.StringsUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wuchaowen
 * @Description:
 **/
public class TodayGoodAdapter extends RecyclerView.Adapter<TodayGoodAdapter.ViewHolder>{
    private List<ShopGoodInfo> mDatas = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mInflater;

    public TodayGoodAdapter(Context mContext) {
        mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
    }


    public void clearData() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    public void setData(List<ShopGoodInfo> data) {

        if (data != null) {
            mDatas.clear();
            mDatas.addAll(data);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = null;
        TodayGoodAdapter.ViewHolder viewHolder = null;
        view = mInflater.inflate(R.layout.item_todaygood, viewGroup, false);
        if(null != view){
            viewHolder = new TodayGoodAdapter.ViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ShopGoodInfo info = mDatas.get(position);
        if (!StringsUtils.isEmpty(info.getCouponPrice())) {
            holder.couponRL.setVisibility(View.VISIBLE);
            holder.couponTv.setText(info.getCouponPrice());
        }else{
            holder.couponRL.setVisibility(View.GONE);
        }
        LoadImgUtils.setImg(mContext, holder.imageGoodPic, MathUtils.getPicture(info));
        holder.goodTitle.setText(MathUtils.getTitle(info));
        if (C.UserType.operator.equals(UserLocalData.getUser(mContext).getPartner()) || C.UserType.vipMember.equals(UserLocalData.getUser(mContext).getPartner())) {
            holder.incomeTv.setText(mContext.getString(R.string.commission, MathUtils.getMuRatioComPrice(UserLocalData.getUser(mContext).getCalculationRate(), info.getCommission())));
        } else {
            holder.incomeTv.setText(mContext.getString(R.string.upgrade_commission));
        }

        //原价
        if (!StringsUtils.isEmpty(info.getPrice())) {
            holder.text_two.setText(" ¥" + MathUtils.getPrice(info.getPrice()));
            holder.text_two.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
        }

        if (!StringsUtils.isEmpty(info.getVoucherPrice())) {
            holder.textview_original.setText("¥ " + MathUtils.getVoucherPrice(info.getVoucherPrice()));
        }

        //平台补贴
        if(LoginUtil.checkIsLogin((Activity) mContext, false) && !TextUtils.isEmpty(info.getSubsidiesPrice())){
                holder.subsidiesPriceTv.setVisibility(View.VISIBLE);
                String getRatioSubside = MathUtils.getMuRatioSubSidiesPrice(UserLocalData.getUser(mContext).getCalculationRate(),info.getSubsidiesPrice());
                holder.subsidiesPriceTv.setText(mContext.getString(R.string.subsidiesPrice, getRatioSubside));
        }else{
            holder.subsidiesPriceTv.setVisibility(View.GONE);
            holder.subsidiesPriceTv.setText("");
        }

        holder.todayRootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoodsDetailActivity.start(mContext, info);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageGoodPic;
        private TextView couponTv;
        private RelativeLayout couponRL;
        private TextView goodTitle;
        private TextView incomeTv;
        private TextView textview_original;
        private TextView text_two;
        private LinearLayout todayRootLayout;
        private TextView subsidiesPriceTv;


        public ViewHolder(View itemView) {
            super(itemView);
            imageGoodPic = itemView.findViewById(R.id.ImageGoodPic);
            couponTv = itemView.findViewById(R.id.couponTv);
            couponRL = itemView.findViewById(R.id.couponRL);
            goodTitle = itemView.findViewById(R.id.goodTitle);
            incomeTv = itemView.findViewById(R.id.incomeTv);
            textview_original = itemView.findViewById(R.id.textview_original);
            text_two = itemView.findViewById(R.id.text_two);
            todayRootLayout = itemView.findViewById(R.id.todayRootLayout);
            subsidiesPriceTv = itemView.findViewById(R.id.subsidiesPriceTv);
        }
    }
}
