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

public class HomeDouAdapter extends DelegateAdapter.Adapter<HomeDouAdapter.ViewHolder> {
    private Context context;
    private  LayoutInflater mInflater;
    private SingleLayoutHelper singleLayoutHelper;
    public HomeDouAdapter(Context context, SingleLayoutHelper singleLayoutHelper) {
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
    public HomeDouAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  inflate = mInflater.inflate(R.layout.item_home_dou, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeDouAdapter.ViewHolder holder, int position) {
//        GridLayoutManager manager = new GridLayoutManager(context, 2);
//        holder.dou_rcy.setLayoutManager(manager);
//        if (holder.dou_rcy.getItemDecorationCount()==0){//防止每一次刷新recyclerview都会使间隔增大一倍 重复调用addItemDecoration方法
//            holder.dou_rcy.addItemDecoration(new SpaceItemDecoration(DensityUtil.dip2px(context, 3)));
//        }
//        ActivityDouAdapter douAdapter = new ActivityDouAdapter(m);
//        holder.dou_rcy.setAdapter(douAdapter);

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView dou_rcy;
        public ViewHolder(View itemView) {
            super(itemView);
            dou_rcy=itemView.findViewById(R.id.dou_rcy);
        }
    }
}
