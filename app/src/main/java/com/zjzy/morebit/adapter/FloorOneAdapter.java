package com.zjzy.morebit.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.Space;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.FloorChildInfo;
import com.zjzy.morebit.utils.DensityUtil;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MyGsonUtils;
import com.zjzy.morebit.utils.SensorsDataUtil;
import com.zjzy.morebit.utils.UI.BannerInitiateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wuchaowen
 * @Description:
 * @Time: $date$ $time$
 **/
public class FloorOneAdapter extends RecyclerView.Adapter<FloorOneAdapter.ViewHolder>{
    private LayoutInflater mInflater;
    private List<FloorChildInfo> mDatas = new ArrayList<>();
    private Context mContext;
    private FragmentManager fm;
    private int screenWidth = 0;
    private int mPosition;
    public FloorOneAdapter(Context mContext) {
        mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
    }


    public void clearData() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    public void setData(List<FloorChildInfo> data) {

        if (data != null) {
            mDatas.clear();
            mDatas.addAll(data);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = null;
        ViewHolder viewHolder = null;
        view = mInflater.inflate(R.layout.item_floor_one, viewGroup, false);
        if(null != view){
            viewHolder = new ViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
               final FloorChildInfo floorChildInfo = mDatas.get(position);
            if(null != floorChildInfo && !TextUtils.isEmpty(floorChildInfo.getPicture())){
                ViewGroup.LayoutParams para =  holder.imageView.getLayoutParams();
                ViewGroup.LayoutParams paraSpace =  holder.mSpace.getLayoutParams();
                if(mDatas.size()>1){
                    para.width = screenWidth-DensityUtil.dip2px(mContext,81);
                }else{
                    if(1 == mDatas.size()){
                        para.width = screenWidth-DensityUtil.dip2px(mContext,54);
                    }else{
                        para.width = screenWidth-DensityUtil.dip2px(mContext,60);
                    }
                }
                para.height = (int) (para.width / 3.16);
                holder.imageView.setLayoutParams(para);

                if(position == mDatas.size()-1){
                    paraSpace.width = DensityUtil.dip2px(mContext,12);
                }else{
                    paraSpace.width = DensityUtil.dip2px(mContext,6);
                }
                holder.mSpace.setLayoutParams(paraSpace);
                if(!TextUtils.isEmpty(floorChildInfo.getPicture())){
                    LoadImgUtils.setImg(mContext, holder.imageView, floorChildInfo.getPicture());
                }

                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SensorsDataUtil.getInstance().advClickTrack(floorChildInfo.getId()+"",floorChildInfo.getOpen()+"", "楼层管理"+mPosition, position,floorChildInfo.getMainTitle(), floorChildInfo.getClassId()+"", floorChildInfo.getUrl(), floorChildInfo.getSubTitle());
                        BannerInitiateUtils.gotoAction((Activity) mContext,MyGsonUtils.toImageInfo(floorChildInfo));
                    }
                });
        }


    }



    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder{

         private ImageView imageView;
         private Space mSpace;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            mSpace = (Space)itemView.findViewById(R.id.space);
        }
    }

    public void setmPosition(int mPosition) {
        this.mPosition = mPosition;
    }
}
