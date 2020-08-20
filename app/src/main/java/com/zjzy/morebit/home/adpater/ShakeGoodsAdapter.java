package com.zjzy.morebit.home.adpater;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.R;
import com.zjzy.morebit.goodsvideo.VideoActivity;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.goods.VideoBean;

import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.StringsUtils;

import java.util.ArrayList;
import java.util.List;

public class ShakeGoodsAdapter extends RecyclerView.Adapter<ShakeGoodsAdapter.ViewHolder> {
    private Context context;
    private List<ShopGoodInfo> list;
    public ShakeGoodsAdapter(Context context, List<ShopGoodInfo> list) {
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       // View view= LayoutInflater.from(context).inflate(R.layout.itme_shake_goods,parent,false);
         View view = View.inflate(context, R.layout.itme_shake_goods, null);
//        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
//        int widthPixels = metrics.widthPixels;
//        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
//        layoutParams.width=widthPixels/3;
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final ShopGoodInfo videoBean = list.get(position);
        videoBean.setItemSourceId(list.get(position).getItemId());

        holder.tv_title.setText(list.get(position).getItemTitle());
        holder.tv_num.setText(MathUtils.getSale(videoBean.getDyLikeCount()));
        //LoadImgUtils.setImg(context, holder.iv_head, list.get(position).getItemPic());
        LoadImgUtils.loadingCornerTop(context, holder.iv_head,list.get(position).getItemPic(),5);
        holder.commission.setText(list.get(position).getCouponMoney()+"元劵");
        holder.tv_price.setText("¥"+list.get(position).getItemPrice());

        UserInfo userInfo1 =UserLocalData.getUser();
        if (userInfo1 == null || TextUtils.isEmpty(UserLocalData.getToken())) {
            holder.tv_coupul.setText("登录赚佣金");
        }else{
            if (C.UserType.operator.equals(UserLocalData.getUser(context).getPartner())
                    || C.UserType.vipMember.equals(UserLocalData.getUser(context).getPartner())) {
//            commission.setVisibility(View.VISIBLE);
                holder.tv_coupul.setText("预估收益"+ MathUtils.getMuRatioComPrice(UserLocalData.getUser(context).getCalculationRate(), list.get(position).getTkMoney()+"")+"元");
            }
        }

      //  holder.tv_coupul.setText("预估收益"+list.get(position).getTkMoney()+"元");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginUtil.checkIsLogin((Activity) context)) {
                    VideoActivity.start(context,(List<ShopGoodInfo>)list,position,"0",1);
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        if (list.size()<3){
            return list.size();
        }else{
            return 3;
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_head;
        private TextView tv_title,commission,tv_price,tv_coupul,tv_num;
        public ViewHolder(View itemView) {
            super(itemView);
            iv_head=itemView.findViewById(R.id.iv_head);//主图
            tv_title=itemView.findViewById(R.id.tv_title);//标题
            commission=itemView.findViewById(R.id.commission);//优惠券
            tv_price=itemView.findViewById(R.id.tv_price);//价格
            tv_coupul=itemView.findViewById(R.id.tv_coupul);//预估收益
            tv_num=itemView.findViewById(R.id.tv_num);//点赞数

        }
    }
}
