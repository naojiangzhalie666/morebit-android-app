package com.zjzy.morebit.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.github.jdsjlzx.ItemDecoration.SpaceItemDecoration;
import com.zjzy.morebit.R;
import com.zjzy.morebit.utils.Banner;
import com.zjzy.morebit.utils.DensityUtil;

public class HomeHaoAdapter extends DelegateAdapter.Adapter<HomeHaoAdapter.ViewHolder> {
    private Context context;
    private  LayoutInflater mInflater;
    private SingleLayoutHelper singleLayoutHelper;
    public HomeHaoAdapter(Context context, SingleLayoutHelper singleLayoutHelper) {
         mInflater = LayoutInflater.from(context);
        this.context=context;
        this.singleLayoutHelper=singleLayoutHelper;


    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return singleLayoutHelper;
    }

    @NonNull
    @Override
    public HomeHaoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  inflate = mInflater.inflate(R.layout.item_home_hao, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeHaoAdapter.ViewHolder holder, int position) {
//        GridLayoutManager manager = new GridLayoutManager(context, 2);
//        holder.activity_hao.setLayoutManager(manager);
//        if (holder.activity_hao.getItemDecorationCount()==0){//防止每一次刷新recyclerview都会使间隔增大一倍 重复调用addItemDecoration方法
//            holder.activity_hao.addItemDecoration(new SpaceItemDecoration(DensityUtil.dip2px(context, 3)));
//        }
//        ActivityFloorAdapter3 floorAdapter = new ActivityFloorAdapter3(context);
//        holder.activity_hao.setAdapter(floorAdapter);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView activity_hao;
        public ViewHolder(View itemView) {
            super(itemView);
            activity_hao=itemView.findViewById(R.id.activity_hao);
        }
    }
}
