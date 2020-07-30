package com.zjzy.morebit.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzy.morebit.Activity.CircleActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.CategoryListChildDtos;
import com.zjzy.morebit.pojo.ConsComGoodsInfo;
import com.zjzy.morebit.pojo.MarkermallCircleInfo;
import com.zjzy.morebit.pojo.number.NumberGoods;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.DateTimeUtils;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.view.CopyTextView;

import java.util.ArrayList;
import java.util.List;

/*
 * 每日好货商品内容
 * */
public class GoodsDialyAdapter extends RecyclerView.Adapter<GoodsDialyAdapter.ViewHolder> {
    private Context context;
    private List<MarkermallCircleInfo> list = new ArrayList<>();
    private  GridLayoutManager manager;


    public GoodsDialyAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<MarkermallCircleInfo> data) {
        if (data != null) {
            list.clear();
            list.addAll(data);
            notifyDataSetChanged();
        }
    }


    public void addData(List<MarkermallCircleInfo> data) {
        if (data != null) {
            list.addAll(data);
            notifyItemRangeChanged(0, data.size());
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.itme_dialy_goods, parent, false);
        ViewHolder holder = new ViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        MarkermallCircleInfo circleInfo = list.get(position);
        if (!TextUtils.isEmpty(circleInfo.getIcon())) {
            LoadImgUtils.setImgCircle(context, holder.userIcon, circleInfo.getIcon());
        }
        if (!TextUtils.isEmpty(circleInfo.getContent())) {
            holder.content.setText(circleInfo.getContent());
        }
        holder.tv_shart.setText(circleInfo.getShareCount() + "");
        if (!TextUtils.isEmpty(circleInfo.getCreateTime())) {
            holder.tv_time.setText(DateTimeUtils.getShortTime2(circleInfo.getCreateTime()) + "");
        }
        if (!TextUtils.isEmpty(circleInfo.getComment())) {
            holder.tv_comment.setText(circleInfo.getComment());
        }
        if (!TextUtils.isEmpty(circleInfo.getName())){
            holder.title.setText(circleInfo.getName());
        }


        List<String> picture = circleInfo.getPicture();
        if (picture.size()==4){
              manager=new GridLayoutManager(context,2);
        }else{
              manager=new GridLayoutManager(context,3);
        }

        holder.rcy_photo.setLayoutManager(manager);





    }


    @Override
    public int getItemCount() {


        return list == null ? 0 : list.size();


    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private CopyTextView tv_shart, content, tv_time, tv_comment, tv_copy,title;
        private ImageView userIcon;
        private RecyclerView rcy_photo;


        public ViewHolder(View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.title);//名字
            userIcon = itemView.findViewById(R.id.userIcon);//头像
            tv_shart = itemView.findViewById(R.id.tv_shart);//分享
            content = itemView.findViewById(R.id.content);//内容
            rcy_photo = itemView.findViewById(R.id.rcy_photo);//图片列表
            tv_time = itemView.findViewById(R.id.tv_time);//时间
            tv_comment = itemView.findViewById(R.id.tv_comment);//复制内容
            tv_copy = itemView.findViewById(R.id.tv_copy);//复制


        }
    }

    public static interface OnAddClickListener {

        public void onShareClick(int postion);
    }

    // add click callback
    OnAddClickListener onItemAddClick;

    public void setOnAddClickListener(OnAddClickListener onItemAddClick) {
        this.onItemAddClick = onItemAddClick;
    }
}
