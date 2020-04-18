package com.zjzy.morebit.purchase.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zjzy.morebit.Activity.GoodsDetailActivity;
import com.zjzy.morebit.Module.common.Dialog.PurchaseDialog;
import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.purchase.PurchaseActivity;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.UI.GlideImageCircularLoader;

import java.util.List;

/*
 * 免单adapter
 * */
public class PurchsePosterAdapter extends RecyclerView.Adapter<PurchsePosterAdapter.ViewHolder> {
    private Context context;
    private List<ShopGoodInfo> list;
    public PurchsePosterAdapter(Context context, List<ShopGoodInfo> list) {
        this.context=context;
        this.list=list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = View.inflate(context, R.layout.itme_pruchase_poster_goods, null);
        ViewHolder holder = new ViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LoadImgUtils.setImg(context, holder.iv_head, list.get(position).getItemPicture());
        final ShopGoodInfo info = list.get(position);
        LoadImgUtils.loadingCornerBitmap(context, holder.iv_head,list.get(position).getItemPicture());
        holder.tv_title.setText(list.get(position).getItemTitle());
        holder.tv_price.setText(list.get(position).getCouponPrice()+"元劵");
        holder.tv_subsidy.setText("补贴"+list.get(position).getSubsidyPrice()+"元");


    }



    @Override
    public int getItemCount() {
        if (list.size()>3){
            return 3;
        }else{
            return list.size();

        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title,tv_price,tv_subsidy;
        private ImageView iv_head;
        public ViewHolder(View itemView) {
            super(itemView);
            iv_head=itemView.findViewById(R.id.iv_head);//主图
            tv_title=itemView.findViewById(R.id.tv_title);//标题
            tv_price=itemView.findViewById(R.id.tv_price);//优惠券
            tv_subsidy=itemView.findViewById(R.id.tv_subsidy);//预估收益

        }
    }
}
