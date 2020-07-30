package com.zjzy.morebit.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.CategoryListChildDtos;
import com.zjzy.morebit.pojo.CategoryListDtos;
import com.zjzy.morebit.pojo.VipBean;

import java.util.ArrayList;
import java.util.List;

/*
 * 每日好货title
 * */
public class GoodDialyTitleAdapter extends RecyclerView.Adapter<GoodDialyTitleAdapter.ViewHolder> {
    private Context context;
    private List<CategoryListChildDtos> list = new ArrayList<>();
    private int oneLevelId;

    public GoodDialyTitleAdapter(Context context, List<CategoryListChildDtos> mchild,int oneid) {
        this.context = context;
        this.list=mchild;
        this.oneLevelId=oneid;

    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.itme_dialy_title, parent, false);
        ViewHolder holder = new ViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        String categoryTitle = list.get(position).getTitle();
        if (!TextUtils.isEmpty(categoryTitle)){
            holder.tv_title.setText(categoryTitle);
        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });





    }


    @Override
    public int getItemCount() {

        return list == null ? 0 : list.size();


    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView tv_title;


        public ViewHolder(View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_title);//标题


        }
    }

    public static interface OnAddClickListener {

        public void onShareClick(int postion);
    }

    // add click callback
    OnAddClickListener onItemAddClick;

    public void setOnAddClickListener(OnAddClickListener onItemAddClick) {
        this.onItemAddClick = onItemAddClick;
    }
}
