package com.zjzy.morebit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.ImageInfo;
import com.zjzy.morebit.utils.StringsUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wuchaowen
 * @Description: 二级模块
 **/
public class SelectGoodsAdapter extends RecyclerView.Adapter<SelectGoodsAdapter.ViewHolder>{
    public static final int DISPLAY_HORIZONTAL = 0; //横向
    private LayoutInflater mInflater;
    private List<ImageInfo> mDatas = new ArrayList<>();
    private Context mContext;

    public SelectGoodsAdapter(Context context/*, List<ImageInfo> data*/) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
       // this.mDatas=data;
    }



    @Override
    public SelectGoodsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = null;

             view = mInflater.inflate(R.layout.item_selectgoods, viewGroup, false);


        SelectGoodsAdapter.ViewHolder viewHolder = new SelectGoodsAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SelectGoodsAdapter.ViewHolder holder, final int position) {

        StringsUtils.retractTitle(holder.tv_icon, holder.tv_title, "特仑苏牛奶大减价了 快来买啊实践活动放松放松房间里刷卡缴费卡洛斯");

    }

    @Override
    public int getItemCount() {
//        if (mDatas.size()<2){
//            return mDatas.size();
//        }else{
//            return 2;
//        }
        return 20;

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_icon,tv_title;
        private ImageView img;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_icon= itemView.findViewById(R.id.tv_icon);
            img= itemView.findViewById(R.id.img);
            tv_title= itemView.findViewById(R.id.tv_title);
        }
    }
}
