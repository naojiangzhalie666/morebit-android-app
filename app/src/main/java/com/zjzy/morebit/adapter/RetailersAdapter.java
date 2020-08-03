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

import com.zjzy.morebit.Activity.ShowWebActivity;
import com.zjzy.morebit.LocalData.UserLocalData;
import com.zjzy.morebit.R;
import com.zjzy.morebit.circle.ui.VideoPlayerActivity;
import com.zjzy.morebit.pojo.Article;
import com.zjzy.morebit.pojo.ConsComGoodsInfo;
import com.zjzy.morebit.pojo.UserInfo;
import com.zjzy.morebit.utils.AppUtil;
import com.zjzy.morebit.utils.DateTimeUtils;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.ViewShowUtils;

import java.util.ArrayList;
import java.util.List;

/*
 * 订单列表
 * */
public class RetailersAdapter extends RecyclerView.Adapter<RetailersAdapter.ViewHolder> {
    private Context context;
    private List<ConsComGoodsInfo> list = new ArrayList<>();
    private int ordertype;
    public RetailersAdapter(Context context, int order_type) {
        this.context = context;
       this.ordertype=order_type;

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
        View inflate = LayoutInflater.from(context).inflate(R.layout.itme_retailers, parent, false);
        ViewHolder holder = new ViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ConsComGoodsInfo info = list.get(position);

        if (!TextUtils.isEmpty(info.getItemImg())) {
            LoadImgUtils.loadingCornerBitmap(context, holder.img_zhu,info.getItemImg());
        }
        int orderType =info.getType();
        switch (orderType){
            case 1:
                holder.tv_type.setText("淘宝");
                break;
            case 2:
                holder.tv_type.setText("京东");
                break;
            case 4:
                holder.tv_type.setText("拼多多");
                break;
            case 5:
                holder.tv_type.setText("考拉");
                break;
            case 6:
                holder.tv_type.setText("唯品会");
                break;
        }
        if (!TextUtils.isEmpty(info.getCreateTime())){
            holder.tv_time.setText("下单时间："+info.getCreateTime());
        }

        if (!TextUtils.isEmpty(info.getPaymentAmount())){
            holder.tv_fu.setText("付款 "+info.getPaymentAmount()+"元");
        }
        if(!TextUtils.isEmpty(info.getOrderSn())){
            holder.tv_order.setText("订单号："+info.getOrderSn());
            holder.tv_order.getPaint().setFakeBoldText(true);
        }

        if (!TextUtils.isEmpty(info.getCommission())){
            holder.tv_fanyong.setText("返还 "+info.getCommission()+"元");
            holder.tv_fanyong.getPaint().setFakeBoldText(true);
        }

        if (ordertype == 3) {
            holder.tv_content.setText("已到账");
        }else{
            holder.tv_content.setText("收货后次月结算");
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtil.coayText((Activity) context, info.getOrderSn());
                ViewShowUtils.showShortToast(context, "已复制订单号，点击粘贴文案");
            }
        });

        holder.img_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfo usInfo = UserLocalData.getUser(context);
                if (!TextUtils.isEmpty(usInfo.getProblemUrl())){
                    ShowWebActivity.start((Activity) context,usInfo.getProblemUrl(),"常见问题");
                }
            }
        });

    }


    @Override
    public int getItemCount() {

        return list == null ? 0 : list.size();


    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_zhu, img_question;
        private TextView tv_fanyong, tv_fu, tv_order, tv_time, tv_type,tv_content;

        public ViewHolder(View itemView) {
            super(itemView);
            img_zhu = itemView.findViewById(R.id.img_zhu);//主图
            tv_fanyong = itemView.findViewById(R.id.tv_fanyong);//返佣
            tv_fu = itemView.findViewById(R.id.tv_fu);//付款金额
            tv_order = itemView.findViewById(R.id.tv_order);//订单号
            img_question = itemView.findViewById(R.id.img_question);//常见问题
            tv_time = itemView.findViewById(R.id.tv_time);//下单时间
            tv_type = itemView.findViewById(R.id.tv_type);//平台类型
            tv_content=itemView.findViewById(R.id.tv_content);//内容
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
