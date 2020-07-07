package com.zjzy.morebit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.FloorBean2;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MathUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wuchaowen
 * @Description: 二级模块
 **/
public class FloorAdapter2 extends RecyclerView.Adapter<FloorAdapter2.ViewHolder>{
    public static final int DISPLAY_HORIZONTAL = 0; //横向
    private LayoutInflater mInflater;
    private List<FloorBean2.ListDataBean.ChildBean> mDatas = new ArrayList<>();
    private Context mContext;

    public FloorAdapter2(Context context,List<FloorBean2.ListDataBean.ChildBean> child) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
         this.mDatas=child;
    }



    @Override
    public FloorAdapter2.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = null;

             view = mInflater.inflate(R.layout.item_vipfloor_tou2, viewGroup, false);


        FloorAdapter2.ViewHolder viewHolder = new FloorAdapter2.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FloorAdapter2.ViewHolder holder, final int position) {

        LoadImgUtils.loadingCornerBitmap(mContext, holder.imageView,mDatas.get(position).getPicture(), 8);

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

        return mDatas.size();

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView= itemView.findViewById(R.id.image);
        }
    }
}
