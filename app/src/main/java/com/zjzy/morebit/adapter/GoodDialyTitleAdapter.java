package com.zjzy.morebit.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjzy.morebit.Activity.CircleActivity;
import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.CategoryListChildDtos;
import com.zjzy.morebit.pojo.CategoryListDtos;
import com.zjzy.morebit.pojo.VipBean;
import com.zjzy.morebit.utils.C;

import java.util.ArrayList;
import java.util.List;

/*
 * 每日好货title
 * */
public class GoodDialyTitleAdapter extends RecyclerView.Adapter<GoodDialyTitleAdapter.ViewHolder> {
    private Context context;
    private List<CategoryListChildDtos> list = new ArrayList<>();
    private int oneLevelId;
    private int circletype;
    private int twoLevelId;

    public GoodDialyTitleAdapter(Context context, List<CategoryListChildDtos> mchild,int oneid,int circletype,int twoLevelId) {
        this.context = context;
        this.list=mchild;
        this.oneLevelId=oneid;
        this.circletype=circletype;
        this.twoLevelId=twoLevelId;


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
        final String categoryTitle = list.get(position).getTitle();
        if (!TextUtils.isEmpty(categoryTitle)){
            holder.tv_title.setText(categoryTitle);
        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CircleActivity.class);
                intent.putExtra(C.Circle.CIRCLE_ONEID,oneLevelId+"");
                intent.putExtra(C.Circle.CIRCLE_TYPE,list.get(position).getType()+"");
                intent.putExtra(C.Circle.CIRCLE_FUTITLE,categoryTitle+"");
                if (circletype==0){
                    intent.putExtra(C.Circle.CIRCLE_TWOID,list.get(position).getId()+"");
                }else{
                    intent.putExtra(C.Circle.CIRCLE_TWOID,twoLevelId+"");
                    intent.putExtra(C.Circle.CIRCLE_THREEID,list.get(position).getId()+"");
                }
                intent.putExtra(C.Circle.CIRCLEFRAGMENT,circletype+"");
                context.startActivity(intent);
            }
        });


        if (list.size()!=0 && list.size()-1==position){
            holder.tv.setVisibility(View.VISIBLE);
        }else {
            holder.tv.setVisibility(View.GONE);
        }



    }


    @Override
    public int getItemCount() {

        return list == null ? 0 : list.size();


    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView tv_title,tv;


        public ViewHolder(View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_title);//标题
            tv=itemView.findViewById(R.id.tv);


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
