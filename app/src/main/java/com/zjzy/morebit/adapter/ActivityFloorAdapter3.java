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
public class ActivityFloorAdapter3 extends RecyclerView.Adapter<ActivityFloorAdapter3.ViewHolder>{
    public static final int DISPLAY_HORIZONTAL = 0; //横向
    private LayoutInflater mInflater;
    private List<ImageInfo> mDatas = new ArrayList<>();
    private Context mContext;

    public ActivityFloorAdapter3(Context context, List<ImageInfo> data) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
       this.mDatas=data;
    }



    @Override
    public ActivityFloorAdapter3.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = null;

             view = mInflater.inflate(R.layout.item_vipfloor_horizontal, viewGroup, false);


        ActivityFloorAdapter3.ViewHolder viewHolder = new ActivityFloorAdapter3.ViewHolder(view);
        viewHolder.imageView = view.findViewById(R.id.image);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ActivityFloorAdapter3.ViewHolder holder, final int position) {
        final ImageInfo imageInfo = mDatas.get(position);

        if (!TextUtils.isEmpty(imageInfo.getPicture()) && imageInfo.getId()!=-1) {
            holder.imageView.setVisibility(View.VISIBLE);
            LoadImgUtils.loadingCornerBitmap(mContext, holder.imageView, imageInfo.getPicture(),8);
        }else{
            holder.imageView.setVisibility(View.GONE);
        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(-1 != imageInfo.getId()){
                    SensorsDataUtil.getInstance().advClickTrack(imageInfo.getId()+"",imageInfo.getOpen()+"", "", position,imageInfo.getTitle(), imageInfo.getClassId()+"", imageInfo.getUrl(), imageInfo.getSubTitle());
                    BannerInitiateUtils.gotoAction((Activity) mContext,imageInfo);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDatas.size()<2){
            return mDatas.size();
        }else{
            return 2;
        }

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView= itemView.findViewById(R.id.image);
        }
    }
}
