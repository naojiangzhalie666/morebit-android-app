package com.zjzy.morebit.goodsvideo.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.zjzy.morebit.Activity.NumberGoodsDetailsActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.R;
import com.zjzy.morebit.goodsvideo.ShopMallActivity;
import com.zjzy.morebit.network.observer.DataObserver;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.pojo.number.NumberGoods;
import com.zjzy.morebit.pojo.number.NumberGoodsList;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.StringsUtils;

import java.util.List;

public class ShopmallAdapter extends RecyclerView.Adapter<ShopmallAdapter.ViewHolder> {
    private Context context;
    private List<NumberGoods> list;

    public ShopmallAdapter(Context context, List<NumberGoods> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = View.inflate(context, R.layout.item_number_goods, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    //加载数据
    public void loadMore(List<NumberGoods> strings) {
        list.addAll(strings);
        notifyDataSetChanged();
    }

    //刷新数据
    public void refreshData(List<NumberGoods> strings) {
        list.addAll(0, strings);
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        LoadImgUtils.setImg(context, holder.pic, list.get(position).getPicUrl());
       holder.desc.setText(list.get(position).getName());
        String price = list.get(position).getRetailPrice();



        if (TextUtils.isEmpty(price)){
            holder.tvPrice.setText("0");
        }else {
            double pricedouble = Double.parseDouble(price);
            long pricelong = ((Number) pricedouble).longValue();
            if (pricelong == 0) {
                holder.tvPrice.setText(price);
            } else {
                holder.tvPrice.setText(String.valueOf(pricelong));
            }
        }

        String moreCoin = MathUtils.getMorebitCorn(price);
        holder.morebitCorn.setText(context.getResources().getString(R.string.give_growth_value,moreCoin));
        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                NumberGoodsDetailsActivity.start((Activity) context,String.valueOf(list.get(position).getId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPrice, desc, morebitCorn;
        private RoundedImageView pic;

        public ViewHolder(View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.number_goods_pic);//主图
            desc = itemView.findViewById(R.id.number_goods_desc);//标题
            tvPrice = itemView.findViewById(R.id.number_goods_price);//价格
            morebitCorn = itemView.findViewById(R.id.txt_morebit_corn);//预估收益


        }
    }
}
