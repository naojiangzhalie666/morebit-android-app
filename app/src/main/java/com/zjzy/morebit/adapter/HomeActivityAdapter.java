package com.zjzy.morebit.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.github.jdsjlzx.ItemDecoration.SpaceItemDecoration;
import com.zjzy.morebit.R;
import com.zjzy.morebit.utils.Banner;
import com.zjzy.morebit.utils.DensityUtil;
import com.zjzy.morebit.utils.UI.SpaceItemRightUtils;

public class HomeActivityAdapter extends DelegateAdapter.Adapter<HomeActivityAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater mInflater;
    private SingleLayoutHelper singleLayoutHelper;

    public HomeActivityAdapter(Context context, SingleLayoutHelper singleLayoutHelper) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.singleLayoutHelper = singleLayoutHelper;


    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return singleLayoutHelper;
    }

    @NonNull
    @Override
    public HomeActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = mInflater.inflate(R.layout.item_home_activity, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeActivityAdapter.ViewHolder holder, int position) {
        GridLayoutManager manager = new GridLayoutManager(context, 2);
        holder.activity_rcy1.setLayoutManager(manager);
        if (holder.activity_rcy1.getItemDecorationCount()==0){//防止每一次刷新recyclerview都会使间隔增大一倍 重复调用addItemDecoration方法
            holder.activity_rcy1.addItemDecoration(new SpaceItemDecoration(DensityUtil.dip2px(context, 3)));
        }
        ActivityFloorAdapter1 floorAdapter1 = new ActivityFloorAdapter1(context);
        holder.activity_rcy1.setAdapter(floorAdapter1);

        ActivityFloorAdapter2 floorAdapter2 = new ActivityFloorAdapter2(context);
        GridLayoutManager manager2 = new GridLayoutManager(context, 4);
        //设置图标的间距
        if (holder.activity_rcy2.getItemDecorationCount()==0){//防止每一次刷新recyclerview都会使间隔增大一倍 重复调用addItemDecoration方法
            SpaceItemRightUtils spaceItemDecorationUtils = new SpaceItemRightUtils(24, 5);
            holder.activity_rcy2.addItemDecoration(spaceItemDecorationUtils);
        }

        holder.activity_rcy2.setLayoutManager(manager2);
        holder.activity_rcy2.setAdapter(floorAdapter2);


    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView activity_img;
        private LinearLayout ll_bg;
        private RecyclerView activity_rcy1, activity_rcy2;

        public ViewHolder(View itemView) {
            super(itemView);
            activity_img = itemView.findViewById(R.id.activity_img);//头图
            ll_bg = itemView.findViewById(R.id.ll_bg);//下图背景
            activity_rcy1 = itemView.findViewById(R.id.activity_rcy1);//横版列表
            activity_rcy2 = itemView.findViewById(R.id.activity_rcy2);//竖版列表


        }
    }
}
