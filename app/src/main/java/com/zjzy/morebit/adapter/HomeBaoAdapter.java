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
import com.zjzy.morebit.R;
import com.zjzy.morebit.utils.Banner;

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

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private Banner banner;
        public ViewHolder(View itemView) {
            super(itemView);
            banner=itemView.findViewById(R.id.banner);
        }
    }
}
