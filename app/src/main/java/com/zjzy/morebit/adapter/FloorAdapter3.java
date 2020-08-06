package com.zjzy.morebit.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.FloorBean2;
import com.zjzy.morebit.pojo.FloorChildInfo;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MyGsonUtils;
import com.zjzy.morebit.utils.SensorsDataUtil;
import com.zjzy.morebit.utils.UI.BannerInitiateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wuchaowen
 * @Description: 二级模块
 **/
public class FloorAdapter3 extends RecyclerView.Adapter<FloorAdapter3.ViewHolder>{
    public static final int DISPLAY_HORIZONTAL = 0; //横向
    private LayoutInflater mInflater;
    private List<FloorBean2.ListDataBean.ChildBean> mDatas = new ArrayList<>();
    private Context mContext;

    public FloorAdapter3(Context context, List<FloorBean2.ListDataBean.ChildBean> child) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
         this.mDatas=child;
    }



    @Override
    public FloorAdapter3.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = null;

             view = mInflater.inflate(R.layout.item_vipfloor_su, viewGroup, false);


        FloorAdapter3.ViewHolder viewHolder = new FloorAdapter3.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FloorAdapter3.ViewHolder holder, final int position) {
        final FloorChildInfo floorChildInfo=new FloorChildInfo();
        floorChildInfo.setId(mDatas.get(position).getId());
        floorChildInfo.setOpen(mDatas.get(position).getOpen());
        floorChildInfo.setMainTitle(mDatas.get(position).getMainTitle());
        floorChildInfo.setClassId(mDatas.get(position).getClassId());
        floorChildInfo.setUrl(mDatas.get(position).getUrl());
        floorChildInfo.setSubTitle(mDatas.get(position).getSubTitle());
        LoadImgUtils.loadingCornerBitmap(mContext, holder.imageView,mDatas.get(position).getPicture(), 8);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SensorsDataUtil.getInstance().advClickTrack(floorChildInfo.getId()+"",floorChildInfo.getOpen()+"", "楼层管理"+position, position,floorChildInfo.getMainTitle(), floorChildInfo.getClassId()+"", floorChildInfo.getUrl(), floorChildInfo.getSubTitle());
                BannerInitiateUtils.gotoAction((Activity) mContext, MyGsonUtils.toImageInfo(floorChildInfo));
            }
        });
//        final ImageInfo imageInfo = mDatas.get(position);
//        ActivityFloorAdapter1.ViewHolder viewHolder = (ActivityFloorAdapter1.ViewHolder) holder;
//        if (!TextUtils.isEmpty(imageInfo.getPicture()) && imageInfo.getId()!=-1) {
//            viewHolder.imageView.setVisibility(View.VISIBLE);
//            LoadImgUtils.loadingCornerBitmap(mContext, viewHolder.imageView, imageInfo.getPicture(),8);
//        }else{
//            viewHolder.imageView.setVisibility(View.GONE);
//        }
//        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(-1 != imageInfo.getId()){
//                    SensorsDataUtil.getInstance().advClickTrack(imageInfo.getId()+"",imageInfo.getOpen()+"", "", position,imageInfo.getTitle(), imageInfo.getClassId()+"", imageInfo.getUrl(), imageInfo.getSubTitle());
//                    BannerInitiateUtils.gotoAction((Activity) mContext,imageInfo);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        if (mDatas.size()>4){
            return 4;
        }else{
            return mDatas ==null ? 0 : mDatas.size();
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
