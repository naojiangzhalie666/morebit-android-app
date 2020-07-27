package com.zjzy.morebit.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.ConsComGoodsInfo;
import com.zjzy.morebit.utils.LoadImgUtils;

import java.util.ArrayList;
import java.util.List;

/*
 * 生活服务订单列表
 * */
public class LifeAdapter extends RecyclerView.Adapter<LifeAdapter.ViewHolder> {
    private Context context;
    private List<ConsComGoodsInfo> list = new ArrayList<>();
    private int ordertype;

    public LifeAdapter(Context context, int teamType) {
        this.context = context;
        this.ordertype = teamType;

    }

    public void setData(List<ConsComGoodsInfo> data) {
        if (data != null) {
            list.clear();
            list.addAll(data);
            notifyDataSetChanged();
        }
    }


    public void addData(List<ConsComGoodsInfo> data) {
        if (data != null) {
            list.addAll(data);
            notifyItemRangeChanged(0, data.size());
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.itme_life, parent, false);
        ViewHolder holder = new ViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ConsComGoodsInfo info = list.get(position);

       if (ordertype==7){
           holder.img_zhu.setImageResource(R.mipmap.ele_icon);
       }else{
           holder.img_zhu.setImageResource(R.mipmap.kb_icon);
       }

        if (!TextUtils.isEmpty(info.getCreateTime())) {
            holder.tv_time.setText("下单时间：" + info.getCreateTime());
        }

        if (!TextUtils.isEmpty(info.getPaymentAmount())) {
            holder.tv_fu.setText("¥" + info.getPaymentAmount() + "元");
        }
        if (!TextUtils.isEmpty(info.getOrderSn())) {
            holder.tv_order.setText("单号：" + info.getOrderSn());
            holder.tv_order.getPaint().setFakeBoldText(true);
        }

        if (!TextUtils.isEmpty(info.getCommission())) {
            holder.tv_fan.setText("预估赚\n ¥" + info.getCommission());

        }

        if (!TextUtils.isEmpty(info.getStatus())) {
            switch (Integer.valueOf(info.getStatus())) {
                case 4:
                    holder.tv_type.setText("已失效");
                    holder.tv_type.setTextColor(Color.parseColor("#999999"));
                    break;
                default:
                    holder.tv_type.setText("已付款");
                    holder.tv_type.setTextColor(Color.parseColor("#F05557"));
                    break;
            }
        }


    }


    @Override
    public int getItemCount() {

        return list == null ? 0 : list.size();


    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_zhu;
        private TextView tv_fan, tv_fu, tv_order, tv_time, tv_type, tv_content;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_fu = itemView.findViewById(R.id.tv_fu);//付款金额
            tv_order = itemView.findViewById(R.id.tv_order);//订单号
            tv_fan = itemView.findViewById(R.id.tv_fan);//常见问题
            tv_time = itemView.findViewById(R.id.tv_time);//下单时间
            tv_type = itemView.findViewById(R.id.tv_type);//失效
            img_zhu=itemView.findViewById(R.id.img_zhu);
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
