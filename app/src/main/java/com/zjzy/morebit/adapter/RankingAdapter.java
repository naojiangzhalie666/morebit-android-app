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

import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.pojo.RankTimeBean;
import com.zjzy.morebit.pojo.number.NumberGoods;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MathUtils;
import com.zjzy.morebit.utils.StringsUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *爆款排行
 **/
public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.ViewHolder1>{
    private LayoutInflater mInflater;
    private List<RankTimeBean.ItemListBean> mDatas = new ArrayList<>();
    private Context mContext;


    public RankingAdapter(Context context,List<RankTimeBean.ItemListBean> itemList) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mDatas=itemList;
    }

    public void setData(List<RankTimeBean.ItemListBean> data) {
        if (data != null) {
            mDatas.addAll(data);
            notifyItemRangeChanged(0,data.size());
        }
    }
    @NonNull
    @Override
    public ViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

              view = mInflater.inflate(R.layout.item_rankinggoods, parent, false);
             return new ViewHolder1(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder1 holder, int position) {

        RankTimeBean.ItemListBean listBean = mDatas.get(position);

        if (!TextUtils.isEmpty(listBean.getItemPicture())) {
            LoadImgUtils.loadingCornerBitmap(mContext, holder.img,listBean.getItemPicture(), 8);
        }

        if (!TextUtils.isEmpty(listBean.getItemTitle())){
            holder.tv_title.setText(listBean.getItemTitle()+"");
        }

        if (!TextUtils.isEmpty(listBean.getItemVoucherPrice())){
            holder.tv_price.setText(listBean.getItemVoucherPrice()+"");
        }

        if (!TextUtils.isEmpty(listBean.getItemPrice())){
            holder.tv_mprice.setText("¥ "+listBean.getItemPrice());
            holder.tv_mprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
        }

        if (!TextUtils.isEmpty(listBean.getCouponPrice())){
            holder.tv_coupul.setText(MathUtils.getnum(listBean.getCouponPrice())+"元劵");
        }

        if (!TextUtils.isEmpty(listBean.getCommission())){
            holder.tv_zhuan.setText("赚 ¥ " + MathUtils.getMuRatioComPrice(UserLocalData.getUser(mContext).getCalculationRate(), listBean.getCommission() + ""));

        }

        if (!TextUtils.isEmpty(listBean.getSaleMonth())){
            holder.tv_sale.setText("销量 "+MathUtils.getSales(listBean.getSaleMonth()));

        }
        if (position<99){
            holder.tv_num.setVisibility(View.VISIBLE);
            holder.tv_num.setText(position+1+"");
        }else{
            holder.tv_num.setVisibility(View.GONE);
        }

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
        private TextView tv_title,tv_num,tv_sale,tv_zhuan,tv_coupul,tv_mprice,tv_price;
        private ImageView img;
        private View view;
        public ViewHolder1(View itemView) {
            super(itemView);
            img= itemView.findViewById(R.id.img);
            tv_title= itemView.findViewById(R.id.tv_title);
            tv_num= itemView.findViewById(R.id.tv_num);
            tv_sale= itemView.findViewById(R.id.tv_sale);
            tv_zhuan= itemView.findViewById(R.id.tv_zhuan);
            tv_coupul= itemView.findViewById(R.id.tv_coupul);
            tv_mprice= itemView.findViewById(R.id.tv_mprice);
            tv_price= itemView.findViewById(R.id.tv_price);
            view=itemView.findViewById(R.id.view);
        }
    }


}
