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
import com.markermall.cat.utils.LoadImgUtils;
import com.markermall.cat.utils.MyGsonUtils;
import com.markermall.cat.utils.SensorsDataUtil;
import com.markermall.cat.utils.UI.BannerInitiateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wuchaowen
 * @Description:
 **/
public class FloorTwoAdapter extends RecyclerView.Adapter<FloorTwoAdapter.ViewHolder>{
    private LayoutInflater mInflater;
    private List<FloorChildInfo> mDatas = new ArrayList<>();
    private Context mContext;
    private FragmentManager fm;
    private int screenWidth = 0;
    private int mPosition;
    public FloorTwoAdapter(Context mContext) {
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
        view = mInflater.inflate(R.layout.item_floor_two, null, false);
        if(null != view){
            viewHolder = new ViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
               final FloorChildInfo floorChildInfo = mDatas.get(position);
               if(!TextUtils.isEmpty(floorChildInfo.getMainTitle())){
                   holder.itemMainTitle.setText(floorChildInfo.getMainTitle());
               }
               if(!TextUtils.isEmpty(floorChildInfo.getSubTitle())){
                   holder.itemSubTitle.setText(floorChildInfo.getSubTitle());
               }
               if(!TextUtils.isEmpty(floorChildInfo.getLabel())){
                   holder.itemLabel.setVisibility(View.VISIBLE);
                   holder.itemLabel.setText(floorChildInfo.getLabel());
               }else{
                   holder.itemLabel.setVisibility(View.GONE);
               }
                if(!TextUtils.isEmpty(floorChildInfo.getPicture())){
                    LoadImgUtils.setImg(mContext, holder.imageView, floorChildInfo.getPicture());
        }

                holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SensorsDataUtil.getInstance().advClickTrack(floorChildInfo.getId()+"",floorChildInfo.getOpen()+"", "楼层管理"+mPosition, position,floorChildInfo.getMainTitle(), floorChildInfo.getClassId()+"", floorChildInfo.getUrl(), floorChildInfo.getSubTitle());
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
         private TextView itemMainTitle;
         private TextView itemSubTitle;
         private TextView itemLabel;
         private LinearLayout itemLayout;



        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            itemMainTitle = itemView.findViewById(R.id.itemMainTitle);
            itemSubTitle = itemView.findViewById(R.id.itemSubTitle);
            itemLabel = itemView.findViewById(R.id.itemLabel);
            itemLayout = itemView.findViewById(R.id.itemLayout);
        }
    }

    public void setmPosition(int mPosition) {
        this.mPosition = mPosition;
    }
}
