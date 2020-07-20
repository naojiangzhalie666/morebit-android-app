package com.zjzy.morebit.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.zjzy.morebit.Activity.NumberGoodsDetailsActivity;
import com.zjzy.morebit.Activity.ShowWebActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.circle.ui.VideoPlayerActivity;
import com.zjzy.morebit.pojo.Article;
import com.zjzy.morebit.pojo.number.NumberGoods;
import com.zjzy.morebit.utils.DateTimeUtils;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MathUtils;

import java.util.ArrayList;
import java.util.List;

import static com.blankj.utilcode.util.StringUtils.getString;

/*
 * 技能课堂adapter
 * */
public class SubNumberAdapter extends RecyclerView.Adapter<SubNumberAdapter.ViewHolder> {
    private Context mContext;
    private List<NumberGoods> list=new ArrayList<>();


    public SubNumberAdapter(Context context) {
        this.mContext = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_number_goods, parent, false);
        ViewHolder holder = new ViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final NumberGoods goods = list.get(position);

        String img = goods.getPicUrl();
        if (!TextUtils.isEmpty(img)) {
            LoadImgUtils.loadingCornerTop(mContext, holder.pic, img,4);
        }
        holder.desc.setText(goods.getName());
        String price = goods.getRetailPrice();



        if (TextUtils.isEmpty(price)){
            holder.tvPrice.setText("0");
        }else{
            holder.tvPrice.setText(MathUtils.getnum(price));
        }
        String moreCoin = MathUtils.getMorebitCorn(price);
        holder. morebitCorn.setText(mContext.getResources().getString(R.string.give_growth_value,moreCoin));
        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                NumberGoodsDetailsActivity.start((Activity) mContext,String.valueOf(goods.getId()));
            }
        });
    }


    @Override
    public int getItemCount() {

        return list.size();


    }

    public void setData(List<NumberGoods> data) {
        if (data != null) {
            list.addAll(data);
            notifyItemRangeChanged(0,data.size());
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RoundedImageView pic;
        private TextView desc,tvPrice,morebitCorn;

        public ViewHolder(View itemView) {
            super(itemView);
              pic = itemView.findViewById(R.id.number_goods_pic);
              desc =  itemView.findViewById(R.id.number_goods_desc);
              tvPrice =  itemView.findViewById(R.id.number_goods_price);
              morebitCorn =  itemView.findViewById(R.id.txt_morebit_corn);

        }
    }

    public static interface OnAddClickListener {

        public void onShareClick();
    }

    // add click callback
    OnAddClickListener onItemAddClick;

    public void setOnAddClickListener(OnAddClickListener onItemAddClick) {
        this.onItemAddClick = onItemAddClick;
    }
}
