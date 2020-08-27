package com.zjzy.morebit.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.ConsComGoodsInfo;
import com.zjzy.morebit.pojo.VipBean;

import java.util.ArrayList;
import java.util.List;

/*
 * 会员权益title
 * */
public class MembershipAdapter1 extends RecyclerView.Adapter<MembershipAdapter1.ViewHolder> {
    private Context context;
    private List<VipBean> list = new ArrayList<>();
    public int mCheckedPosition = 0;

    public MembershipAdapter1(Context context) {
        this.context = context;

    }
    public int getmPosition() {
        return mCheckedPosition;
    }

    public void setmPosition(int mPosition) {
        this.mCheckedPosition = mPosition;
        Log.e("kkkk",mCheckedPosition+"   sssssss");
    }


    public void setData(List<VipBean> data) {
        if (data != null) {
            list.clear();
            list.addAll(data);
            notifyDataSetChanged();
        }
    }


    public void addData(List<VipBean> data) {
        if (data != null) {
            list.addAll(data);
            notifyItemRangeChanged(0, data.size());
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.itme_membership1, parent, false);
        ViewHolder holder = new ViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        String categoryTitle = list.get(position).getCategoryTitle();
        if (!TextUtils.isEmpty(categoryTitle)){
            StringBuffer buffer=new StringBuffer(categoryTitle);
            buffer.insert(2,"\n");
            holder.title.setText(buffer.toString());
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onItemAddClick.onShareClick(position);
               notifyDataSetChanged();
            }
        });

        Log.e("kkkk",mCheckedPosition+"   sssssss");

        if (position== mCheckedPosition){
            Log.e("kkkk",mCheckedPosition+"   66999966"+position+"    8888");
            holder.title.setTextColor(Color.parseColor("#F05557"));
            holder.tv.setBackgroundColor( Color.parseColor("#FFFFFF"));//选中白色，不选择灰色
            holder.line.setVisibility(View.VISIBLE);
        }else{
            Log.e("kkkk",mCheckedPosition+"   6666"+position+"   7777");
            holder.tv.setBackgroundColor( Color.parseColor("#F8F8F8"));//选中灰色，不选择白色
            holder.line.setVisibility(View.GONE);
            holder.title.setTextColor(Color.parseColor("#333333"));
        }


    }


    @Override
    public int getItemCount() {

        return list == null ? 0 : list.size();


    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView title;
        private LinearLayout tv;
        private View line;

        public ViewHolder(View itemView) {
            super(itemView);
            tv=itemView.findViewById(R.id.tv);
            title = itemView.findViewById(R.id.title);//标题
            line=itemView.findViewById(R.id.line);

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
