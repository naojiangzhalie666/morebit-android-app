package com.zjzy.morebit.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.zjzy.morebit.R;
import com.zjzy.morebit.home.adpater.ActivityAdapter;
import com.zjzy.morebit.utils.Banner;
import com.zjzy.morebit.utils.UI.SpaceItemRightUtils;

public class HomeBaoAdapter extends DelegateAdapter.Adapter<HomeBaoAdapter.ViewHolder> {
    private Context context;
    private  LayoutInflater mInflater;
    private SingleLayoutHelper singleLayoutHelper;
    public HomeBaoAdapter(Context context, SingleLayoutHelper singleLayoutHelper) {
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
    public HomeBaoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  inflate = mInflater.inflate(R.layout.item_home_bao, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeBaoAdapter.ViewHolder holder, int position) {
//        LinearLayoutManager manager = new LinearLayoutManager(context);
//        manager.setOrientation(LinearLayout.HORIZONTAL);
//        //设置图标的间距
//        if (holder.activity_rcy.getItemDecorationCount()==0){//防止每一次刷新recyclerview都会使间隔增大一倍 重复调用addItemDecoration方法
//            SpaceItemRightUtils spaceItemDecorationUtils = new SpaceItemRightUtils(20, 3);
//            holder.activity_rcy.addItemDecoration(spaceItemDecorationUtils);
//        }
//        holder.activity_rcy.setLayoutManager(manager);
//        ActivityBaoAdapter activityBaoAdapter = new ActivityBaoAdapter(context);
//        holder.activity_rcy.setAdapter(activityBaoAdapter);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView activity_rcy;
        public ViewHolder(View itemView) {
            super(itemView);
            activity_rcy=itemView.findViewById(R.id.activity_rcy);
        }
    }
}
