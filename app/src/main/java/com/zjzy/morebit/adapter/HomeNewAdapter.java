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
import com.zjzy.morebit.R;
import com.zjzy.morebit.utils.Banner;
import com.zjzy.morebit.utils.SpaceItemDecorationUtils;
import com.zjzy.morebit.utils.UI.SpaceItemRightUtils;

public class HomeNewAdapter extends DelegateAdapter.Adapter<HomeNewAdapter.ViewHolder> {
    private Context context;
    private  LayoutInflater mInflater;
    private SingleLayoutHelper singleLayoutHelper;
    public HomeNewAdapter(Context context, SingleLayoutHelper singleLayoutHelper) {
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
    public HomeNewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  inflate = mInflater.inflate(R.layout.item_home_newpeolpe, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeNewAdapter.ViewHolder holder, int position) {

//        NewItemAdapter newItemAdapter= new NewItemAdapter(context);
//        GridLayoutManager manager = new GridLayoutManager(context, 3);
//        //设置图标的间距
//        if (holder.new_rcy.getItemDecorationCount()==0){//防止每一次刷新recyclerview都会使间隔增大一倍 重复调用addItemDecoration方法
//            SpaceItemRightUtils spaceItemDecorationUtils = new SpaceItemRightUtils(16, 3);
//            holder.new_rcy.addItemDecoration(spaceItemDecorationUtils);
//        }
//
//        holder.new_rcy.setLayoutManager(manager);
//        holder.new_rcy.setAdapter(newItemAdapter);

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView new_rcy;
        public ViewHolder(View itemView) {
            super(itemView);
            new_rcy=itemView.findViewById(R.id.new_rcy);

        }
    }
}
