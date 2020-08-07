package com.zjzy.morebit.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjzy.morebit.R;
import com.zjzy.morebit.circle.ui.SearchArticleListResultFragment;
import com.zjzy.morebit.pojo.ConsComGoodsInfo;
import com.zjzy.morebit.utils.OpenFragmentUtils;

import java.util.ArrayList;
import java.util.List;

/*
 * 历史搜索列表
 * */
public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder> {
    private Context context;
    private List<String> mListData = new ArrayList<>();
    private int ordertype;

    public SearchHistoryAdapter(Context context) {
        this.context = context;


    }


    public void setListData(List listData) {
        mListData.clear();
        if (listData != null) {
            mListData.addAll(listData);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.itme_history, parent, false);
        ViewHolder holder = new ViewHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.tv.setText(mListData.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoResult(mListData.get(position));
            }
        });

    }


    @Override
    public int getItemCount() {

        return mListData == null ? 0 : mListData.size();


    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);

            tv = itemView.findViewById(R.id.tv);
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

    private void gotoResult(String keyword) {
        Bundle bundle = new Bundle();
        bundle.putString("keyword", keyword);
        OpenFragmentUtils.goToSimpleFragment(context, SearchArticleListResultFragment.class.getName(), bundle);
    }
}
