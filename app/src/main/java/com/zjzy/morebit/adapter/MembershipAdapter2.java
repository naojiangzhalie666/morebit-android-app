package com.zjzy.morebit.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjzy.morebit.R;
import com.zjzy.morebit.pojo.VipBean;

import java.util.ArrayList;
import java.util.List;

/*
 * 会员权益title
 * */
public class MembershipAdapter2 extends RecyclerView.Adapter<MembershipAdapter2.ViewHolder> {
    private Context context;
    private List<VipBean> list = new ArrayList<>();


    public MembershipAdapter2(Context context) {
        this.context = context;

    }

    public void setData(List<VipBean> data) {
        Log.e("sssss", "捡来的7" + data);
        if (data != null) {
            list.clear();
            list.addAll(data);
            Log.e("sssss", "捡来的6");
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
        View inflate = LayoutInflater.from(context).inflate(R.layout.itme_membership2, parent, false);
        ViewHolder holder = new ViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //  List<VipBean.VoBean> info = list.get(position).getCategoryTitle();
        String categoryTitle = list.get(position).getCategoryTitle();

        if (!TextUtils.isEmpty(categoryTitle)) {
            holder.title.setText(categoryTitle);
            holder.title.getPaint().setFakeBoldText(true);
        }

        MembershipAdapter3 adapter3 = new MembershipAdapter3(context);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        holder.recycleview.setLayoutManager(gridLayoutManager);
        holder.recycleview.setAdapter(adapter3);
        holder.recycleview.setNestedScrollingEnabled(false);
        adapter3.setData(list.get(position).getVo());
        if (position == 0) {
            holder.ll.setVisibility(View.VISIBLE);
        }else{
            holder.ll.setVisibility(View.GONE);
        }
        holder.title2.getPaint().setFakeBoldText(true);


    }


    @Override
    public int getItemCount() {

        return list == null ? 0 : list.size();


    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView title, title2;
        private RecyclerView recycleview;
        private LinearLayout ll;

        public ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);//标题
            title2 = itemView.findViewById(R.id.title2);//标题
            recycleview = itemView.findViewById(R.id.recycleview);//icon
            ll=itemView.findViewById(R.id.ll);//

        }
    }

    public static interface OnAddClickListener {

        public void onShareClick();
    }

    // add click callback
    OnAddClickListener onItemAddClick;

    public void setOnAddClickListener(OnAddClickListener onItemAddClick) {
        this.onItemAddClick = onItemAddClick;
    }
}
