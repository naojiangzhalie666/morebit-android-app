package com.zjzy.morebit.home.adpater;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzy.morebit.Activity.GoodsDetailActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.R;
import com.zjzy.morebit.goodsvideo.VideoActivity;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.StringsUtils;

import java.util.List;

public class CommissionGoodsAdapter extends RecyclerView.Adapter<CommissionGoodsAdapter.ViewHolder> {
    private Context context;
    private List<ShopGoodInfo> list;

    public CommissionGoodsAdapter(Context context, List<ShopGoodInfo> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.itme_shake_goods, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final ShopGoodInfo videoBean = list.get(position);
        videoBean.setItemSourceId(list.get(position).getItemid());
        videoBean.setGoodsId(Long.valueOf(list.get(position).getItemid()));
        videoBean.setPrice(videoBean.getItemprice());
        videoBean.setVoucherPrice(videoBean.getItemendprice());
        videoBean.setCouponStartTime(videoBean.getCouponstarttime());
        videoBean.setCouponEndTime(videoBean.getCouponendtime());
        videoBean.setCouponUrl(videoBean.getCouponurl());
        videoBean.setCouponPrice(videoBean.getCouponmoney());
        videoBean.setCommission(videoBean.getTkmoney());
        videoBean.setSaleMonth(videoBean.getItemsale());

        holder.tv_title.setText(list.get(position).getItemtitle());
        holder.tv_num.setVisibility(View.GONE);
        LoadImgUtils.loadingCornerTop(context, holder.iv_head, list.get(position).getItempic(), 5);
        holder.commission.setText(MathUtils.getnum(list.get(position).getCouponmoney()) + "元劵");
        holder.tv_price.setText("¥" + list.get(position).getItemendprice());
        holder.img_bo.setVisibility(View.GONE);
        if (videoBean.getShoptype() == 1) {
            holder.good_mall_tag.setImageResource(R.mipmap.tb_icon);
        } else {
            holder.good_mall_tag.setImageResource(R.mipmap.tm_icon);
        }

        UserInfo userInfo1 = UserLocalData.getUser();
        if (userInfo1 == null || TextUtils.isEmpty(UserLocalData.getToken())) {
            holder.tv_coupul.setText("登录赚佣金");
        } else {
            if (C.UserType.operator.equals(UserLocalData.getUser(context).getPartner())
                    || C.UserType.vipMember.equals(UserLocalData.getUser(context).getPartner())) {
                holder.tv_coupul.setText("预估收益" + MathUtils.getMuRatioComPrice(UserLocalData.getUser(context).getCalculationRate(), list.get(position).getTkmoney() + "") + "元");
            }
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoodsDetailActivity.start(context, videoBean);
            }
        });


    }

    @Override
    public int getItemCount() {
        if (list.size() < 3) {
            return list.size();
        } else {
            return 3;
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_head, img_bo, good_mall_tag;
        private TextView tv_title, commission, tv_price, tv_coupul, tv_num;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_head = itemView.findViewById(R.id.iv_head);//主图
            tv_title = itemView.findViewById(R.id.tv_title);//标题
            commission = itemView.findViewById(R.id.commission);//优惠券
            tv_price = itemView.findViewById(R.id.tv_price);//价格
            tv_coupul = itemView.findViewById(R.id.tv_coupul);//预估收益
            tv_num = itemView.findViewById(R.id.tv_num);//点赞数
            img_bo = itemView.findViewById(R.id.img_bo);
            good_mall_tag = itemView.findViewById(R.id.good_mall_tag);

        }
    }
}
