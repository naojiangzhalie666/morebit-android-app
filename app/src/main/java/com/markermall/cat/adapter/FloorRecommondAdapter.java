package com.markermall.cat.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.markermall.cat.R;
import com.markermall.cat.pojo.FloorChildInfo;
import com.markermall.cat.utils.DensityUtil;
import com.markermall.cat.utils.LoadImgUtils;
import com.markermall.cat.utils.MyGsonUtils;
import com.markermall.cat.utils.UI.BannerInitiateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wuchaowen
 * @Description:
 **/
public class FloorRecommondAdapter extends RecyclerView.Adapter<FloorRecommondAdapter.ViewHolder>{
    private LayoutInflater mInflater;
    private List<FloorChildInfo> mDatas = new ArrayList<>();
    private Context mContext;
    private FragmentManager fm;
    private int screenWidth = 0;
    private int viewType = 0;
    public FloorRecommondAdapter(Context mContext,int viewType) {
        mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.viewType = viewType;
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

        if(this.viewType == 3){
            view = mInflater.inflate(R.layout.item_floor_three_rv, viewGroup, false);
        }else if(this.viewType == 1){
            view = mInflater.inflate(R.layout.item_floor_one_rv, viewGroup, false);
        }else if(this.viewType == 2){
            view = mInflater.inflate(R.layout.item_floor_two_rv, viewGroup, false);
        } else {
            view = mInflater.inflate(R.layout.item_floor_four_rv, viewGroup, false);
        }

        if(null != view){
            viewHolder = new ViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final FloorChildInfo floorChildInfo = mDatas.get(position);
        if (!TextUtils.isEmpty(floorChildInfo.getMainTitle())) {
            holder.title.setText(floorChildInfo.getMainTitle());
        }
        final ViewGroup.LayoutParams imageLayoutParams =  holder.imageView.getLayoutParams();
        final ViewGroup.LayoutParams titleLayoutParams =  holder.title.getLayoutParams();
        int count = 0;
        if(this.viewType == 1){
            count = 1;
        }else if(this.viewType == 2){
            count = 2;
        }else if(this.viewType == 3){
            count = 3;
        }else{
            count = 4;
        }
        if(this.viewType != 1){
            int imageWidth = (screenWidth - 180 - DensityUtil.dip2px(mContext,12)*9) /count;
            imageLayoutParams.width = imageWidth;
            titleLayoutParams.width = imageWidth;
            if(viewType == 3 || viewType == 4){
                imageLayoutParams.height = imageWidth;
            }
            holder.imageView.setLayoutParams(imageLayoutParams);
            holder.title.setLayoutParams(titleLayoutParams);
        }


        if (!TextUtils.isEmpty(floorChildInfo.getPicture())) {
            LoadImgUtils.setImg(mContext, holder.imageView, floorChildInfo.getPicture());
        }

        holder.itemRootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BannerInitiateUtils.gotoAction((Activity) mContext,MyGsonUtils.toImageInfo(floorChildInfo));
        }
        });



    }



    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder{

         private ImageView imageView;
         private TextView title;
         private LinearLayout itemRootLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView)itemView.findViewById(R.id.title);
            itemRootLayout = (LinearLayout)itemView.findViewById(R.id.itemRootLayout);
        }
    }
}
