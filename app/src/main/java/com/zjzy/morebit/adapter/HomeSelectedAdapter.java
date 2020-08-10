package com.zjzy.morebit.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.alibaba.android.vlayout.layout.StickyLayoutHelper;
import com.zjzy.morebit.R;
import com.zjzy.morebit.utils.Banner;

public class HomeSelectedAdapter extends DelegateAdapter.Adapter<HomeSelectedAdapter.ViewHolder> {
    private Context context;
    private  LayoutInflater mInflater;
    private StickyLayoutHelper stickyLayoutHelper;
    public HomeSelectedAdapter(Context context, StickyLayoutHelper stickyLayoutHelper) {
         mInflater = LayoutInflater.from(context);
        this.context=context;
        this.stickyLayoutHelper=stickyLayoutHelper;


    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return stickyLayoutHelper;
    }

    @NonNull
    @Override
    public HomeSelectedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  inflate = mInflater.inflate(R.layout.item_home_selectclass, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeSelectedAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);

        }
    }
}
