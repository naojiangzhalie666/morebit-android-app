package com.zjzy.morebit.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
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
import com.zjzy.morebit.pojo.RankTimeBean;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MathUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *9.9包邮
 **/
public class NineAdapter extends RecyclerView.Adapter<NineAdapter.ViewHolder1>{
    private LayoutInflater mInflater;
    private List<ShopGoodInfo> mDatas = new ArrayList<>();
    private Context mContext;


    public NineAdapter(Context context, List<ShopGoodInfo> data) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mDatas=data;
    }

    public void setData(List<ShopGoodInfo> data) {
        if (data != null) {
            mDatas.addAll(data);
            notifyItemRangeChanged(0,data.size());
        }
    }
    @NonNull
    @Override
    public ViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

              view = mInflater.inflate(R.layout.item_ninegoods, parent, false);
             return new ViewHolder1(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder1 holder, int position) {

        final ShopGoodInfo shopGoodInfo = mDatas.get(position);


        if (!TextUtils.isEmpty(shopGoodInfo.getItemPicture())) {
            LoadImgUtils.loadingCornerBitmap(mContext, holder.img,shopGoodInfo.getItemPicture(), 8);
        }

        if (!TextUtils.isEmpty(shopGoodInfo.getItemTitle())){
            holder.tv_title.setText(shopGoodInfo.getItemTitle()+"");
        }

        if (!TextUtils.isEmpty(shopGoodInfo.getItemVoucherPrice())){
            holder.tv_price.setText(shopGoodInfo.getItemVoucherPrice()+"");
        }

        if (!TextUtils.isEmpty(shopGoodInfo.getItemPrice())){
            holder.tv_mprice.setText("¥ "+shopGoodInfo.getItemPrice());
            holder.tv_mprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
        }

        if (!TextUtils.isEmpty(shopGoodInfo.getCouponPrice())){
            holder.tv_coupul.setText(MathUtils.getnum(shopGoodInfo.getCouponPrice())+"元劵");
        }

        if (!TextUtils.isEmpty(shopGoodInfo.getCommission())){
            holder.tv_zhuan.setText("赚 ¥ " + MathUtils.getMuRatioComPrice(UserLocalData.getUser(mContext).getCalculationRate(), shopGoodInfo.getCommission() + ""));

        }

        if (!TextUtils.isEmpty(shopGoodInfo.getSaleMonth())){
            holder.tv_sale.setText("销量 "+MathUtils.getSales(shopGoodInfo.getSaleMonth()));

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoodsDetailActivity.start(mContext, shopGoodInfo);
            }
        });


        if (mDatas.size()==position+1){
            holder.view.setVisibility(View.GONE);
        }else{
            holder.view.setVisibility(View.VISIBLE);
        }


    }








    @Override
    public int getItemCount() {

        return mDatas == null ? 0 : mDatas.size();

    }


    public class ViewHolder1 extends RecyclerView.ViewHolder {
        private TextView tv_title,tv_sale,tv_zhuan,tv_coupul,tv_mprice,tv_price;
        private ImageView img;
        private View view;
        public ViewHolder1(View itemView) {
            super(itemView);
            img= itemView.findViewById(R.id.img);
            tv_title= itemView.findViewById(R.id.tv_title);
            tv_sale= itemView.findViewById(R.id.tv_sale);
            tv_zhuan= itemView.findViewById(R.id.tv_zhuan);
            tv_coupul= itemView.findViewById(R.id.tv_coupul);
            tv_mprice= itemView.findViewById(R.id.tv_mprice);
            tv_price= itemView.findViewById(R.id.tv_price);
            view=itemView.findViewById(R.id.view);
        }
    }


}
