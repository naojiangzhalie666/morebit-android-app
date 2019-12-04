package com.zjzy.morebit.adapter;

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

import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.FloorChildInfo;
import com.zjzy.morebit.utils.LoadImgUtils;
import com.zjzy.morebit.utils.MyGsonUtils;
import com.zjzy.morebit.utils.SensorsDataUtil;
import com.zjzy.morebit.utils.UI.BannerInitiateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wuchaowen
 * @Description:
 **/
public class FloorFourAdapter extends RecyclerView.Adapter<FloorFourAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<FloorChildInfo> mDatas = new ArrayList<>();
    private Context mContext;
    private FragmentManager fm;
    private int screenWidth = 0;
    private int mPosition;
    public FloorFourAdapter(Context mContext) {
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
        view = mInflater.inflate(R.layout.item_floor_four, null, false);
        if (null != view) {
            viewHolder = new ViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final List<FloorChildInfo> floorChildInfos = mDatas.get(position).getChild();
        if (null != floorChildInfos && floorChildInfos.size() > 0) {
            String mainTitle = floorChildInfos.get(0).getMainTitle();
            String subTitle = floorChildInfos.get(0).getSubTitle();

            if (!TextUtils.isEmpty(mainTitle)) {
                holder.itemMainTitle.setText(mainTitle);
            }
            if (!TextUtils.isEmpty(subTitle)) {
                holder.itemSubTitle.setText(subTitle);
            }


            for (int i = 0; i < floorChildInfos.size(); i++) {
                final FloorChildInfo fcl = floorChildInfos.get(i);
                if (i == 0) {
                    setImageView(fcl, holder.imageViewLeft);
                    String labelleft = floorChildInfos.get(0).getLabel();
                    if (!TextUtils.isEmpty(labelleft)) {
                        holder.itemLabelLeft.setVisibility(View.VISIBLE);
                        holder.itemLabelLeft.setText(labelleft);
                    }else{
                        holder.itemLabelLeft.setVisibility(View.GONE);
                    }
                    holder.ll_left.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SensorsDataUtil.getInstance().advClickTrack(fcl.getId()+"",fcl.getOpen()+"", "楼层管理"+mPosition, position,fcl.getMainTitle(), fcl.getClassId()+"", fcl.getUrl(), fcl.getSubTitle());
                            BannerInitiateUtils.gotoAction((Activity) mContext,MyGsonUtils.toImageInfo(fcl));
                        }
                    });
                }
                if (i == 1) {
                    setImageView(fcl, holder.imageViewRight);
                    String labelright = floorChildInfos.get(1).getLabel();
                    if (!TextUtils.isEmpty(labelright)) {
                        holder.itemLabelRight.setVisibility(View.VISIBLE);
                        holder.itemLabelRight.setText(labelright);
                    }else{
                        holder.itemLabelRight.setVisibility(View.GONE);
                    }
                    holder.ll_right.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SensorsDataUtil.getInstance().advClickTrack(fcl.getId()+"",fcl.getOpen()+"", "楼层管理"+mPosition, position,fcl.getMainTitle(), fcl.getClassId()+"", fcl.getUrl(), fcl.getSubTitle());
                            BannerInitiateUtils.gotoAction((Activity) mContext,MyGsonUtils.toImageInfo(fcl));
                        }
                    });
                }
            }
        }

    }


    private void setImageView(final FloorChildInfo floorChildInfo, final ImageView imageView) {
        if (!TextUtils.isEmpty(floorChildInfo.getPicture())) {
            LoadImgUtils.setImg(mContext, imageView, floorChildInfo.getPicture());
        } else {
            imageView.setVisibility(View.GONE);
        }

//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SensorsDataUtil.getInstance().advClickTrack(floorChildInfo.getId()+"",floorChildInfo.getOpen()+"", "楼层管理"+mPosition, position,floorChildInfo.getMainTitle(), floorChildInfo.getClassId()+"", floorChildInfo.getUrl(), floorChildInfo.getSubTitle());
//                BannerInitiateUtils.gotoAction((Activity) mContext,MyGsonUtils.toImageInfo(floorChildInfo));
//            }
//        });
    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewLeft;
        private ImageView imageViewRight;
        private TextView itemMainTitle;
        private TextView itemSubTitle;
        private TextView itemLabelLeft;
        private TextView itemLabelRight;
        private LinearLayout ll_right;
        private LinearLayout ll_left;


        public ViewHolder(View itemView) {
            super(itemView);
            imageViewLeft = itemView.findViewById(R.id.imageLeft);
            imageViewRight = itemView.findViewById(R.id.imageRight);
            itemMainTitle = itemView.findViewById(R.id.itemMainTitle);
            itemSubTitle = itemView.findViewById(R.id.itemSubTitle);
            itemLabelLeft = itemView.findViewById(R.id.itemLabelLeft);
            itemLabelRight = itemView.findViewById(R.id.itemLabelRight);
            ll_right = itemView.findViewById(R.id.ll_right);
            ll_left = itemView.findViewById(R.id.ll_left);
        }
    }

    public int getmPosition() {
        return mPosition;
    }

    public void setmPosition(int mPosition) {
        this.mPosition = mPosition;
    }
}
