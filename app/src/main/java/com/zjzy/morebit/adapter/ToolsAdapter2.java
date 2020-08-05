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

import com.zjzy.morebit.Module.common.Activity.BaseActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.ConsComGoodsInfo;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.LoginUtil;
import com.zjzy.morebit.utils.UI.BannerInitiateUtils;

import java.util.ArrayList;
import java.util.List;

/*
 * 必备工具列表
 * */
public class ToolsAdapter2 extends RecyclerView.Adapter<ToolsAdapter2.ViewHolder> {
    private Context context;
    private List<ImageInfo> list = new ArrayList<>();


    public ToolsAdapter2(Context context) {
        this.context = context;


    }

    public void setData(List<ImageInfo> data) {
        if (data != null) {
            list.clear();
            list.addAll(data);
            notifyDataSetChanged();
        }
    }





    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.itme_tools, parent, false);
        ViewHolder holder = new ViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ImageInfo imageInfo = list.get(position);
        if (!TextUtils.isEmpty(imageInfo.getTitle())){
            holder.tv.setText(imageInfo.getTitle());
        }

        if (!TextUtils.isEmpty(imageInfo.getPicture())){
            LoadImgUtils.setImg(context,holder.img,imageInfo.getPicture());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BannerInitiateUtils.gotoAction((Activity) context, imageInfo);
                BannerInitiateUtils.statisticsStartAdOnclick((BaseActivity) context, imageInfo.getId() + "", 1);
            }
        });

    }


    @Override
    public int getItemCount() {

        return list == null ? 0 : list.size();


    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView img;
        private TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);

            tv= itemView.findViewById(R.id.tv);
            img=itemView.findViewById(R.id.img);
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
