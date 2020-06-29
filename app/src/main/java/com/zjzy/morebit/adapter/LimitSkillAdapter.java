package com.zjzy.morebit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.ImageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wuchaowen
 * @Description: 二级模块
 **/
public class LimitSkillAdapter extends RecyclerView.Adapter<LimitSkillAdapter.ViewHolder>{
    public static final int DISPLAY_HORIZONTAL = 0; //横向
    private LayoutInflater mInflater;
    private List<ImageInfo> mDatas = new ArrayList<>();
    private Context mContext;

    public LimitSkillAdapter(Context context/*, List<ImageInfo> data*/) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
       // this.mDatas=data;
    }



    @Override
    public LimitSkillAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = null;

             view = mInflater.inflate(R.layout.item_litmitskill, viewGroup, false);


        LimitSkillAdapter.ViewHolder viewHolder = new LimitSkillAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(LimitSkillAdapter.ViewHolder holder, final int position) {

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
        private ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView= itemView.findViewById(R.id.image);
        }
    }
}
