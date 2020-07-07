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



        }
    }
}
