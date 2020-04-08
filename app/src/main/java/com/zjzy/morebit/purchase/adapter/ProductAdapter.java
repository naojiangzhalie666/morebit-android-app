package com.zjzy.morebit.purchase.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzy.morebit.Activity.SettingActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.Module.common.Utils.LoadingView;
import com.zjzy.morebit.R;
import com.zjzy.morebit.network.BaseResponse;
import com.zjzy.morebit.network.RxHttp;
import com.zjzy.morebit.network.RxUtils;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.purchase.PurchaseActivity;
import com.zjzy.morebit.utils.ViewShowUtils;

import io.reactivex.functions.Action;

/*
* 好货adapter
* */
public class ProductAdapter extends  RecyclerView.Adapter<ProductAdapter.ViewHolder>{
    private Context context;
    public ProductAdapter(Context context) {
        this.context=context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = View.inflate(context, R.layout.itme_product_goods, null);
        ViewHolder holder = new ViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tv_share.setOnClickListener(new View.OnClickListener() {//立即购买
            @Override
            public void onClick(View v) {

            }
        });
    }



    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_share,tv_title,tv_price,tv_subsidy,price_after,tv_coupon_price;
        private ImageView iv_head;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_share=itemView.findViewById(R.id.tv_share);//购买
            iv_head=itemView.findViewById(R.id.iv_head);//主图
            tv_title=itemView.findViewById(R.id.tv_title);//标题
            tv_price=itemView.findViewById(R.id.tv_price);//预估收益
            tv_subsidy=itemView.findViewById(R.id.tv_subsidy);//优惠券
            price_after=itemView.findViewById(R.id.price_after);//劵后价
            tv_coupon_price=itemView.findViewById(R.id.tv_coupon_price);//原价

        }
    }
}
