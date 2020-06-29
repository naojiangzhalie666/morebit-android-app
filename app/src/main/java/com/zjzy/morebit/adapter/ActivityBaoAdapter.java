package com.zjzy.morebit.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzy.morebit.Activity.GoodsDetailActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.goods.HandpickBean;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.StringsUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wuchaowen
 * @Description: 二级模块
 **/
public class ActivityBaoAdapter extends RecyclerView.Adapter<ActivityBaoAdapter.ViewHolder> {
    public static final int DISPLAY_HORIZONTAL = 0; //横向
    private LayoutInflater mInflater;
    private List<ShopGoodInfo> data;
    private Context mContext;

    public ActivityBaoAdapter(Context context,List<ShopGoodInfo> data) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.data=data;
    }


    @Override
    public ActivityBaoAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = null;

        view = mInflater.inflate(R.layout.item_recommend_acitivty_horizontal, viewGroup, false);
        ActivityBaoAdapter.ViewHolder viewHolder = new ActivityBaoAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ActivityBaoAdapter.ViewHolder holder, final int position) {
        final ShopGoodInfo item = data.get(position);

        if(!TextUtils.isEmpty(item.getItemLabeling())  ){
            holder.markTv.setVisibility(View.VISIBLE);
            holder.markTv.setText(item.getItemLabeling());
        }else{
            holder.markTv.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(item.getPicture())) {
            LoadImgUtils.loadingCornerTop(mContext,holder.img,item.getPicture(),8);
        }
        holder.title.setText(MathUtils.getTitle(item));


        holder.price.setText(MathUtils.getnum(item.getVoucherPrice()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoodsDetailActivity.start(mContext, item);
            }
        });
        if (StringsUtils.isEmpty(item.getCouponPrice())) {
            holder.coupon.setVisibility(View.INVISIBLE);
        } else {
            holder.coupon.setVisibility(View.VISIBLE);
            holder.coupon.setText(mContext.getString(R.string.yuan, MathUtils.getnum(item.getCouponPrice())));
        }



        if (C.UserType.operator.equals(UserLocalData.getUser(mContext).getPartner())
                || C.UserType.vipMember.equals(UserLocalData.getUser(mContext).getPartner())) {
            holder.tv_coupul.setText("赚 ¥ " + MathUtils.getMuRatioComPrice(UserLocalData.getUser(mContext).getCalculationRate(), item.getCommission() + "") + "元");
        } else {
            UserInfo userInfo1 = UserLocalData.getUser();
            if (userInfo1 == null || TextUtils.isEmpty(UserLocalData.getToken())) {
                holder.tv_coupul.setText("登录赚佣金");
            } else {
                holder.tv_coupul.setText("赚 ¥ " + MathUtils.getMuRatioComPrice(UserLocalData.getUser(mContext).getCalculationRate(), item.getCommission() + "") + "元");
            }


        }

    }

    @Override
    public int getItemCount() {
        if (data.size()!=0){
            return data.size();
        }else{
            return  0;
        }


    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView title,markTv,coupon,price,tv_coupul;

        public ViewHolder(View itemView) {
            super(itemView);

              img =itemView.findViewById(R.id.img);
              title = itemView.findViewById(R.id.title);
              markTv = itemView.findViewById(R.id.markTv);
              coupon = itemView.findViewById(R.id.coupon);
              price = itemView.findViewById(R.id.price);
              tv_coupul=itemView.findViewById(R.id.tv_coupul);
        }
    }
}
