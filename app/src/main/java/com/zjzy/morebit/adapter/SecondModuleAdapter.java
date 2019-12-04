package com.zjzy.morebit.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.SensorsDataUtil;
import com.zjzy.morebit.utils.UI.BannerInitiateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wuchaowen
 * @Description: 二级模块
 **/
public class SecondModuleAdapter extends RecyclerView.Adapter<SecondModuleAdapter.ViewHolder>{
    public static final int DISPLAY_HORIZONTAL = 0; //横向
    public static final int DISPLAY_VERTICAL = 1; //1/纵向
    private LayoutInflater mInflater;
    private List<ImageInfo> mDatas = new ArrayList<>();
    private Context mContext;

    public SecondModuleAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
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

    @Override
    public SecondModuleAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = null;
        if(viewType == DISPLAY_HORIZONTAL){
            //横
             view = mInflater.inflate(R.layout.item_second_horizontal, viewGroup, false);
        }else if(viewType == DISPLAY_VERTICAL){
            //竖
            view = mInflater.inflate(R.layout.item_second_vertical, viewGroup, false);
        }

        SecondModuleAdapter.ViewHolder viewHolder = new SecondModuleAdapter.ViewHolder(view);
        viewHolder.imageView = view.findViewById(R.id.image);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SecondModuleAdapter.ViewHolder holder, final int position) {
        final ImageInfo imageInfo = mDatas.get(position);
        SecondModuleAdapter.ViewHolder viewHolder = (SecondModuleAdapter.ViewHolder) holder;
        if (!TextUtils.isEmpty(imageInfo.getPicture()) && imageInfo.getId()!=-1) {
            viewHolder.imageView.setVisibility(View.VISIBLE);
            LoadImgUtils.setImg(mContext, viewHolder.imageView, imageInfo.getPicture());
        }else{
            viewHolder.imageView.setVisibility(View.GONE);
        }
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(-1 != imageInfo.getId()){
                    SensorsDataUtil.getInstance().advClickTrack(imageInfo.getId()+"",imageInfo.getOpen()+"", "二级分类", position,imageInfo.getTitle(), imageInfo.getClassId()+"", imageInfo.getUrl(), imageInfo.getSubTitle());
                    BannerInitiateUtils.gotoAction((Activity) mContext,imageInfo);
                }
            }
        });
    }


    @Override
    public int getItemViewType(int position) {
        ImageInfo data = mDatas.get(position);
        if(null != data && data.getDisplayStyle() == DISPLAY_HORIZONTAL){
            return DISPLAY_HORIZONTAL;
        }

        if(null != data && data.getDisplayStyle() == DISPLAY_VERTICAL){
            return DISPLAY_VERTICAL;
        }

        return DISPLAY_HORIZONTAL;
    }

    @Override
    public int getItemCount() {
          return mDatas.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }
        ImageView imageView;
    }
}
