package com.zjzy.morebit.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.FloorChildInfo;
import com.zjzy.morebit.pojo.FloorInfo;
import com.zjzy.morebit.utils.C;
import com.zjzy.morebit.utils.DensityUtil;
import com.zjzy.morebit.view.CustomRecyclerView;
import com.zjzy.morebit.view.GridItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wuchaowen
 * @Description:
 * @Time: $date$ $time$
 **/
public class FloorAdapter extends RecyclerView.Adapter<FloorAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<FloorInfo> mDatas = new ArrayList<>();
    private Context mContext;
    private FragmentManager fm;

    public FloorAdapter(Context mContext, FragmentManager fm) {
        mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.fm = fm;
    }


    public void clearData() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    public void setData(List<FloorInfo> data) {

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
        if (viewType == C.ViewType.FLOOR_ONE) {
            //横
            view = mInflater.inflate(R.layout.vlayout_floor_one, viewGroup, false);
        } else if (viewType == C.ViewType.FLOOR_TWO) {
            view = mInflater.inflate(R.layout.vlayout_floor_two, viewGroup, false);
        } else if (viewType == C.ViewType.FLOOR_THREE) {
            view = mInflater.inflate(R.layout.vlayout_floor_three, viewGroup, false);
        } else if (viewType == C.ViewType.FLOOR_FOUR) {
            view = mInflater.inflate(R.layout.vlayout_floor_four, viewGroup, false);
        } else if (viewType == C.ViewType.FLOOR_FIVE) {
        view = mInflater.inflate(R.layout.vlayout_floor_four, viewGroup, false);
    }else {
            view = mInflater.inflate(R.layout.view_empty, viewGroup, false);
        }
        if (null != view) {
            viewHolder = new ViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        FloorInfo floorInfo = mDatas.get(position);
        final List<FloorChildInfo> floorChildInfos = mDatas.get(position).getChild();
        FloorInfo childTwo = mDatas.get(position).getChildTwo();
        if (viewType == C.ViewType.FLOOR_ONE) {
            if (null != floorChildInfos && floorChildInfos.size() > 0) {
                setFloorTitle(holder, floorInfo);
                FloorOneAdapter floorHorizontalImageAdapter = new FloorOneAdapter(mContext);
                floorHorizontalImageAdapter.setmPosition(position+1);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                holder.mBannerRv.setLayoutManager(linearLayoutManager);
                holder.mBannerRv.setAdapter(floorHorizontalImageAdapter);
                floorHorizontalImageAdapter.setData(floorChildInfos);
            }else{
                    holder.floorOneLayout.removeAllViews();
            }

        } else if (viewType == C.ViewType.FLOOR_TWO) {
               setFloorTitle(holder, floorInfo);
            if (null != floorChildInfos && floorChildInfos.size() > 0) {
                int count = getRatio(floorChildInfos.size(),2);
                if (count >= 2) {
                    List<FloorChildInfo> spiltfloorChildInfos = floorChildInfos.subList(0, count);
                    FloorTwoAdapter floorTwoAdapter = new FloorTwoAdapter(mContext);
                    floorTwoAdapter.setmPosition(position+1);
                    GridLayoutManager floorTwoLayoutManager = new GridLayoutManager(mContext, 2);
                    holder.mTwoRv.setLayoutManager(floorTwoLayoutManager);
                    GridItemDecoration divider = new GridItemDecoration.Builder(mContext)
                            .setHorizontalSpan(R.dimen.grid_line)
                            .setVerticalSpan(R.dimen.grid_line)
                            .setColorResource(R.color.color_ECECEC)
                            .setShowLastLine(false)
                            .setHorizontalPadding(DensityUtil.dip2px(mContext,12),DensityUtil.dip2px(mContext,12))
                            .setVerticaPadding(DensityUtil.dip2px(mContext,26),DensityUtil.dip2px(mContext,3))
                            .build();

                    if(holder.mTwoRv.getItemDecorationCount() == 0){
                        holder.mTwoRv.addItemDecoration(divider);
                    }

                    holder.mTwoRv.setAdapter(floorTwoAdapter);
                    floorTwoAdapter.setData(spiltfloorChildInfos);
                }else{
                    holder.floorTwoLayout.removeAllViews();
                }
            }else{
                holder.floorTwoLayout.removeAllViews();
            }
        } else if (viewType == C.ViewType.FLOOR_THREE) {
            setFloorTitle(holder, floorInfo);
            if (null != floorChildInfos && floorChildInfos.size() > 0) {
                int count = getRatio(floorChildInfos.size(),3);
                if (count >= 3) {
                    List<FloorChildInfo> spiltfloorChildInfos = floorChildInfos.subList(0, count);
                    FloorThreeAdapter floorthreeAdapter = new FloorThreeAdapter(mContext);
                    floorthreeAdapter.setmPosition(position+1);
                    GridLayoutManager floorThreeLayoutManager = new GridLayoutManager(mContext, 3);
                    holder.mThreeRv.setLayoutManager(floorThreeLayoutManager);
                    GridItemDecoration divider = new GridItemDecoration.Builder(mContext)
                            .setHorizontalSpan(R.dimen.grid_line_floor_three)
                            .setVerticalSpan(R.dimen.grid_line_floor_three)
                            .setColorResource(R.color.color_F8F8F8)
                            .setShowLastLine(false)
                            .setHorizontalPadding(DensityUtil.dip2px(mContext,12),DensityUtil.dip2px(mContext,12))
                            .setVerticaPadding(DensityUtil.dip2px(mContext,12),DensityUtil.dip2px(mContext,12))
                            .build();
                    if(holder.mThreeRv.getItemDecorationCount() == 0){
                        holder.mThreeRv.addItemDecoration(divider);
                    }
                    holder.mThreeRv.setAdapter(floorthreeAdapter);
                    floorthreeAdapter.setData(spiltfloorChildInfos);
                }else{
                    holder.floorThreeLayout.removeAllViews();
                }
            }else{
                holder.floorThreeLayout.removeAllViews();
            }

        } else if (viewType == C.ViewType.FLOOR_FOUR) {
            setFloorTitle(holder, floorInfo);

            if (null != floorChildInfos && floorChildInfos.size() > 0) {
                int count = getRatio(floorChildInfos.size(),2);
                if (getRatio(floorChildInfos.size(),2) >= 2) {
                    List<FloorChildInfo> spiltfloorChildInfos = floorChildInfos.subList(0, count);
                    FloorFourAdapter floorFourAdapter = new FloorFourAdapter(mContext);
                    floorFourAdapter.setmPosition(position+1);
                    GridLayoutManager floorFourLayoutManager = new GridLayoutManager(mContext, 2);
                    holder.mfloorFourRv.setLayoutManager(floorFourLayoutManager);
                    GridItemDecoration divider = new GridItemDecoration.Builder(mContext)
                            .setHorizontalSpan(R.dimen.grid_line)
                            .setVerticalSpan(R.dimen.grid_line)
                            .setColorResource(R.color.color_ECECEC)
                            .setShowLastLine(false)
                            .setHorizontalPadding(DensityUtil.dip2px(mContext,12),DensityUtil.dip2px(mContext,12))
                            .setVerticaPadding(DensityUtil.dip2px(mContext,11),DensityUtil.dip2px(mContext,13))
                            .build();
                    if(holder.mfloorFourRv.getItemDecorationCount() == 0){
                        holder.mfloorFourRv.addItemDecoration(divider);
                    }

                    holder.mfloorFourRv.setAdapter(floorFourAdapter);
                    floorFourAdapter.setData(spiltfloorChildInfos);
                }else{
                    holder.floorFourLayout.removeAllViews();
                }
            }else{
                holder.floorFourLayout.removeAllViews();
            }

        }
        setRecommodRv(holder, childTwo);
    }

    private void setRecommodRv(@NonNull ViewHolder holder, FloorInfo childTwo) {
        if(null != childTwo){
            final List<FloorChildInfo> infosRecommonds = childTwo.getChild();
            String recommodMainTitle = childTwo.getMainTitle();
            String recommodSubTitle = childTwo.getSubTitle();
            if(null != infosRecommonds && infosRecommonds.size()>0){
                if(null != holder.recommod_external_layout){
                    holder.recommod_external_layout.setVisibility(View.VISIBLE);
                }
                holder.recommondLayout.setVisibility(View.VISIBLE);
                //为你推荐
                FloorRecommondAdapter recommondAdapter = new FloorRecommondAdapter(mContext,childTwo.getShowType());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                holder.recommondFourRv.setLayoutManager(linearLayoutManager);
                holder.recommondFourRv.setAdapter(recommondAdapter);
                recommondAdapter.setData(infosRecommonds);
                if(!TextUtils.isEmpty(recommodMainTitle)){
                    holder.recommondMainTitle.setText(recommodMainTitle);
                }
                if(!TextUtils.isEmpty(recommodSubTitle)){
                    holder.recommondSubTitle.setText(recommodSubTitle);
                }
            }else{
                //隐藏
                if(null != holder.recommod_external_layout){
                    holder.recommod_external_layout.setVisibility(View.GONE);
                }
                holder.recommondLayout.setVisibility(View.GONE);
            }
        }else{
            if(null != holder.recommod_external_layout){
                holder.recommod_external_layout.setVisibility(View.GONE);
            }
            holder.recommondLayout.setVisibility(View.GONE);
        }
    }



    private void setFloorTitle(@NonNull ViewHolder holder, FloorInfo floorInfo) {
        if (floorInfo.getMainTitleShow() == 1 && !TextUtils.isEmpty(floorInfo.getMainTitle())) {
            holder.mainTitle.setVisibility(View.VISIBLE);
            holder.mainTitle.setText(floorInfo.getMainTitle());
        } else {
            holder.mainTitle.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(floorInfo.getSubTitle())) {
            holder.subTitle.setVisibility(View.VISIBLE);
            holder.subTitle.setText(floorInfo.getSubTitle());
        } else {
            holder.subTitle.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = this.mDatas.get(position).getShowType();
        switch (viewType) {
            case C.ViewType.FLOOR_ONE:
                viewType = C.ViewType.FLOOR_ONE;
                break;
            case C.ViewType.FLOOR_TWO:
                viewType = C.ViewType.FLOOR_TWO;
                break;
            case C.ViewType.FLOOR_THREE:
                viewType = C.ViewType.FLOOR_THREE;
                break;
            case C.ViewType.FLOOR_FOUR:
                viewType = C.ViewType.FLOOR_FOUR;
                break;
        }
        return viewType;
    }


    /**
     * 获取倍数
     * @return
     */
    private int getRatio(int size,int ratio){
        int count = 0;
        for (int i = 0; i < size; i++) {
            if ((i+1) % ratio == 0) {
                count = i+1;
            }
        }

        return count;
    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private CustomRecyclerView mBannerRv;
        private TextView mainTitle;
        private TextView subTitle;
        private RecyclerView mTwoRv;
        private RecyclerView mThreeRv;
        private RecyclerView mfloorFourRv;
        private LinearLayout floorTwoLayout;
        private LinearLayout floorThreeLayout;
        private LinearLayout floorFourLayout;
        private LinearLayout floorOneLayout;
        private RecyclerView recommondFourRv;
        private TextView recommondMainTitle;
        private TextView recommondSubTitle;
        private LinearLayout recommondLayout;
        private LinearLayout recommod_external_layout;


        public ViewHolder(View itemView) {
            super(itemView);
            mBannerRv = (CustomRecyclerView) itemView.findViewById(R.id.bannerRv);
            mainTitle = itemView.findViewById(R.id.mainTitle);
            subTitle = itemView.findViewById(R.id.subTitle);
            mTwoRv = itemView.findViewById(R.id.floorTwoRv);
            mThreeRv = itemView.findViewById(R.id.floorThreeRv);
            mfloorFourRv = itemView.findViewById(R.id.floorFourRv);
            floorTwoLayout = itemView.findViewById(R.id.floorTwoLayout);
            floorThreeLayout = itemView.findViewById(R.id.floorThreeLayout);
            floorFourLayout = itemView.findViewById(R.id.floorFourLayout);
            floorOneLayout = itemView.findViewById(R.id.floorOneLayout);
            recommondFourRv = itemView.findViewById(R.id.recommondRv);
            recommondMainTitle = itemView.findViewById(R.id.recommondMainTitle);
            recommondSubTitle = itemView.findViewById(R.id.recommondSubTitle);
            recommondLayout = itemView.findViewById(R.id.recommondLayout);
            recommod_external_layout = itemView.findViewById(R.id.recommod_layout);
        }
    }
}
