package com.zjzy.morebit.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.zjzy.morebit.R;
import com.zjzy.morebit.goodsvideo.VideoClassActivity;
import com.zjzy.morebit.goodsvideo.VideoFragment;
import com.zjzy.morebit.home.fragment.IconFragment;
import com.zjzy.morebit.main.ui.myview.xtablayout.XTabLayout;
import com.zjzy.morebit.pojo.VideoClassBean;
import com.zjzy.morebit.utils.Banner;

import java.util.ArrayList;
import java.util.List;

public class HomeIconAdapter extends DelegateAdapter.Adapter<HomeIconAdapter.ViewHolder> {
    private Context context;
    private  LayoutInflater mInflater;
    private SingleLayoutHelper singleLayoutHelper;

    public HomeIconAdapter(Context context, SingleLayoutHelper singleLayoutHelper) {
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
    public HomeIconAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  inflate = mInflater.inflate(R.layout.item_home_icon, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeIconAdapter.ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);


        }
    }


}
