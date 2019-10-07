package com.markermall.cat.main.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.markermall.cat.R;
import com.markermall.cat.pojo.ImageInfo;
import com.markermall.cat.utils.LoadImgUtils;
import com.markermall.cat.utils.UI.BannerInitiateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wuchaowen
 * @Description:
 **/
public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder>{
    private LayoutInflater mInflater;
    private List<ImageInfo> mDatas = new ArrayList<>();
    private Context mContext;

    public NoticeAdapter(Context mContext) {
        mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;

    }

    public void clearData() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    public void setData(List<ImageInfo> data) {
        if (data != null) {
            mDatas.clear();
            mDatas.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void addData(List<ImageInfo> data) {
        if (data != null) {
            mDatas.addAll(data);
            notifyDataSetChanged();
        }
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        NoticeAdapter.ViewHolder viewHolder = null;
        //open=1的是打开商品，其他都是文章显示
         if(viewType == 1){
            view = mInflater.inflate(R.layout.item_notice_good, parent, false);
        }else {
            view = mInflater.inflate(R.layout.item_notice_artice, parent, false);
        }
        if (null != view) {
            viewHolder = new NoticeAdapter.ViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ImageInfo item = mDatas.get(position);
        if(getItemViewType(position) == 1){
            //商品
            holder.subTitleTv.setText(item.getSubTitle());
        }else{
            //文章
        }

        holder.title.setText(item.getTitle());
        LoadImgUtils.setImg(mContext, holder.image, item.getPicture());
        holder.timeTv.setText(item.getUpdateTime());
        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BannerInitiateUtils.gotoAction((Activity) mContext,item);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return mDatas.get(position).getOpen();
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView title;
        private TextView timeTv;
        private TextView subTitleTv;
        private LinearLayout rootLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            timeTv = itemView.findViewById(R.id.timeTv);
            subTitleTv = itemView.findViewById(R.id.subTitleTv);
            rootLayout = itemView.findViewById(R.id.ll_root);
        }
    }
}
