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
import com.zjzy.morebit.pojo.SelectFlag;
import com.zjzy.morebit.pojo.ShopGoodInfo;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MathUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wuchaowen
 * @Description: 二级模块
 **/
public class LimitSkillAdapter extends RecyclerView.Adapter<LimitSkillAdapter.ViewHolder>{
    public static final int DISPLAY_HORIZONTAL = 0; //横向
    private LayoutInflater mInflater;
    private List<ShopGoodInfo> mDatas = new ArrayList<>();
    private Context mContext;
    private String time,title;

    public LimitSkillAdapter(Context context,String time,String title) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.time=time;
        this.title=title;

    }
    public void setData(List<ShopGoodInfo> data) {
        if (data != null) {
            mDatas.clear();
            mDatas.addAll(data);
        }
    }


    @Override
    public LimitSkillAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = null;

             view = mInflater.inflate(R.layout.item_litmitskill, viewGroup, false);


        LimitSkillAdapter.ViewHolder viewHolder = new LimitSkillAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(LimitSkillAdapter.ViewHolder holder, final int position) {
        final ShopGoodInfo goodInfo = mDatas.get(position);
        if (!TextUtils.isEmpty(goodInfo.getItemPicture())) {
            LoadImgUtils.loadingCornerBitmap(mContext, holder.img,goodInfo.getItemPicture(), 8);
        }

        if (!TextUtils.isEmpty(goodInfo.getItemTitle())){
            holder.tv_title.setText(goodInfo.getItemTitle()+"");
        }

        if (!TextUtils.isEmpty(goodInfo.getItemVoucherPrice())){
            holder.tv_price.setText(goodInfo.getItemVoucherPrice()+"");
        }

        if (!TextUtils.isEmpty(goodInfo.getItemPrice())){
            holder.tv_jprice.setText("¥ "+goodInfo.getItemPrice());
            holder.tv_jprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
        }

        if (!TextUtils.isEmpty(goodInfo.getCouponPrice())){
            holder.tv_coupul.setText(MathUtils.getnum(goodInfo.getCouponPrice())+"元劵");
        }

        if (!TextUtils.isEmpty(goodInfo.getCommission())){
            holder.tv_zhuan.setText("赚 ¥ " + MathUtils.getMuRatioComPrice(UserLocalData.getUser(mContext).getCalculationRate(), goodInfo.getCommission() + ""));

        }

        if ("已开抢".equals(title)||"抢购中".equals(title)||"昨日秒杀".equals(title)){
            holder.tv_qiang.setVisibility(View.VISIBLE);
            holder.tv_qiang.getPaint().setFakeBoldText(true);
            holder.tv_qiang2.setVisibility(View.GONE);
        }else{
            holder.tv_qiang.setVisibility(View.GONE);
            holder.tv_qiang2.setVisibility(View.VISIBLE);
            holder.tv_qiang2.setText(time+"点\n开抢");
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoodsDetailActivity.start(mContext, goodInfo);
            }
        });



    }

    @Override
    public int getItemCount() {
        return mDatas.size();

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView tv_title,tv_price,tv_jprice,tv_coupul,tv_zhuan,tv_qiang,tv_qiang2;
        public ViewHolder(View itemView) {
            super(itemView);
            img= itemView.findViewById(R.id.img);
            tv_title= itemView.findViewById(R.id.tv_title);
            tv_price= itemView.findViewById(R.id.tv_price);
            tv_jprice= itemView.findViewById(R.id.tv_jprice);
            tv_coupul= itemView.findViewById(R.id.tv_coupul);
            tv_zhuan= itemView.findViewById(R.id.tv_zhuan);
            tv_qiang=itemView.findViewById(R.id.tv_qiang);
            tv_qiang2=itemView.findViewById(R.id.tv_qiang2);

        }
    }
}
