package com.zjzy.morebit.purchase.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzy.morebit.R;
import com.zjzy.morebit.purchase.PurchaseActivity;
/*
 * 免单adapter
 * */
public class PurchseAdapter extends RecyclerView.Adapter<PurchseAdapter.ViewHolder> {
    private Context context;
    public PurchseAdapter(Context context) {
        this.context=context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = View.inflate(context, R.layout.itme_pruchase_goods, null);
        ViewHolder holder = new ViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }



    @Override
    public int getItemCount() {
        return 8;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_share,tv_title,tv_price,tv_subsidy,tv_coupon_price;
        private ImageView iv_head;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_share=itemView.findViewById(R.id.tv_share);//购买
            iv_head=itemView.findViewById(R.id.iv_head);//主图
            tv_title=itemView.findViewById(R.id.tv_title);//标题
            tv_price=itemView.findViewById(R.id.tv_price);//优惠券
            tv_subsidy=itemView.findViewById(R.id.tv_subsidy);//预估收益
            tv_coupon_price=itemView.findViewById(R.id.tv_coupon_price);//劵后价
        }
    }
}
